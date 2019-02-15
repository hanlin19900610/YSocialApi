package com.yin.android.sociallibrary.weixin;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.yin.android.sociallibrary.PayBean;
import com.yin.android.sociallibrary.PlatformConfig;
import com.yin.android.sociallibrary.PlatformType;
import com.yin.android.sociallibrary.SSOHandler;
import com.yin.android.sociallibrary.SocialApi;
import com.yin.android.sociallibrary.listener.AuthListener;
import com.yin.android.sociallibrary.listener.PayListener;
import com.yin.android.sociallibrary.listener.ShareListener;
import com.yin.android.sociallibrary.share_media.IShareMedia;
import com.yin.android.sociallibrary.share_media.ShareImageMedia;
import com.yin.android.sociallibrary.share_media.ShareMusicMedia;
import com.yin.android.sociallibrary.share_media.ShareTextMedia;
import com.yin.android.sociallibrary.share_media.ShareVideoMedia;
import com.yin.android.sociallibrary.share_media.ShareWebMedia;
import com.yin.android.sociallibrary.utils.BitmapUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * description:  微信处理 Handler
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public class WXHandler extends SSOHandler {

  private IWXAPI mWXApi;
  /**
   * 默认scope 和 state
   */
  private static String sScope = "snsapi_userinfo";
  private static String sState = "wechat_sdk_demo_test";

  private IWXAPIEventHandler mEventHandler;

  private PlatformConfig.Weixin mConfig;
  private AuthListener mAuthListener;
  private ShareListener mShareListener;

  public WXHandler() {
    this.mEventHandler = new IWXAPIEventHandler() {
      @Override public void onResp(BaseResp resp) {
        int type = resp.getType();
        switch (type) {
          //授权返回
          case ConstantsAPI.COMMAND_SENDAUTH:
            WXHandler.this.onAuthCallback((SendAuth.Resp) resp);
            break;

          //分享返回
          case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
            WXHandler.this.onShareCallback((SendMessageToWX.Resp) resp);
            break;

          //支付返回
          case ConstantsAPI.COMMAND_PAY_BY_WX:
            WXHandler.this.onPayCallback((PayResp) resp);
            break;
          default:
            break;
        }
      }

      @Override public void onReq(BaseReq req) {
      }
    };
  }

  /**
   * 设置scope和state
   */
  public static void setScopeState(String scope, String state) {
    sScope = scope;
    sState = state;
  }

  @Override
  public void onCreate(Context context, PlatformConfig.Platform config) {
    this.mConfig = (PlatformConfig.Weixin) config;
    this.mWXApi = SocialApi.getWeChat();
  }

  @Override
  public boolean isInstall() {
    return this.mWXApi.isWXAppInstalled();
  }

  @Override
  public void authorize(Activity activity, AuthListener authListener) {
    if (!isInstall()) {
      authListener.onError(this.mConfig.getName(), "wx not install");
      release();
      return;
    }
    this.mAuthListener = authListener;

    SendAuth.Req req1 = new SendAuth.Req();
    req1.scope = sScope;
    req1.state = sState;
    req1.transaction = buildTransaction("authorize");

    if (!this.mWXApi.sendReq(req1)) {
      this.mAuthListener.onError(this.mConfig.getName(), "sendReq fail");
      release();
    }
  }

  /**
   * 验证回调
   */
  private void onAuthCallback(SendAuth.Resp resp) {
    switch (resp.errCode) {
      //授权成功
      case BaseResp.ErrCode.ERR_OK:
        Map<String, String> data = new HashMap<String, String>();
        data.put("code", resp.code);
        this.mAuthListener.onComplete(PlatformType.WEIXIN, data);
        release();
        break;

      //授权取消
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        if (this.mAuthListener != null) {
          this.mAuthListener.onCancel(PlatformType.WEIXIN);
        }
        release();
        break;

      //授权失败
      default:
        CharSequence err = TextUtils.concat("weixin auth error (", String.valueOf(resp.errCode),
            "):", resp.errStr);
        if (mAuthListener != null) {
          mAuthListener.onError(PlatformType.WEIXIN, err.toString());
        }
        release();
        break;
    }
  }

  @Override
  public void share(Activity activity, IShareMedia shareMedia, ShareListener shareListener) {
    if (!isInstall()) {
      shareListener.onError(this.mConfig.getName(), "wx not install");
      release();
      return;
    }
    this.mShareListener = shareListener;

    WXMediaMessage msg = new WXMediaMessage();
    String type = "";

    //网页分享
    if (shareMedia instanceof ShareWebMedia) {
      ShareWebMedia shareWebMedia = (ShareWebMedia) shareMedia;
      type = "webpage";
      //web object
      WXWebpageObject webpageObject = new WXWebpageObject();
      webpageObject.webpageUrl = shareWebMedia.getWebPageUrl();
      msg.mediaObject = webpageObject;
      msg.title = shareWebMedia.getTitle();
      msg.description = shareWebMedia.getDescription();
      if (shareWebMedia.getThumb() != null && shareWebMedia.getThumb().isRecycled()) {
        mShareListener.onError(this.mConfig.getName(), "bitmap Recycled");
        return;
      }
      msg.thumbData = BitmapUtils.bmpToByteArray(shareWebMedia.getThumb(), false);
      //文字分享
    } else if (shareMedia instanceof ShareTextMedia) {
      ShareTextMedia shareTextMedia = (ShareTextMedia) shareMedia;
      type = "text";
      //text object
      WXTextObject textObject = new WXTextObject();
      textObject.text = shareTextMedia.getDescription();
      msg.mediaObject = textObject;
      msg.description = shareTextMedia.getDescription();
      //图片分享
    } else if (shareMedia instanceof ShareImageMedia) {
      ShareImageMedia shareImageMedia = (ShareImageMedia) shareMedia;
      type = "image";
      msg.mediaObject = new WXImageObject(shareImageMedia.getImage());
      if (shareImageMedia.getImage() != null && shareImageMedia.getImage().isRecycled()) {
        mShareListener.onError(this.mConfig.getName(), "bitmap Recycled");
        return;
      }
      msg.thumbData = BitmapUtils.bmpToByteArray(shareImageMedia.getImage(), false);
      //音乐分享
    } else if (shareMedia instanceof ShareMusicMedia) {
      ShareMusicMedia shareMusicMedia = (ShareMusicMedia) shareMedia;
      type = "music";
      WXMusicObject musicObject = new WXMusicObject();
      musicObject.musicUrl = shareMusicMedia.getUrl();
      musicObject.musicDataUrl = shareMusicMedia.getAacUrl();
      msg.mediaObject = musicObject;
      msg.title = shareMusicMedia.getTitle();
      msg.description = shareMusicMedia.getDescription();
      if (shareMusicMedia.getThumb() != null && shareMusicMedia.getThumb().isRecycled()) {
        mShareListener.onError(this.mConfig.getName(), "bitmap Recycled");
        return;
      }
      msg.thumbData = BitmapUtils.bmpToByteArray(shareMusicMedia.getThumb(), false);
      //视频分享
    } else if (shareMedia instanceof ShareVideoMedia) {
      ShareVideoMedia shareVideoMedia = (ShareVideoMedia) shareMedia;
      type = "video";

      WXVideoObject videoObject = new WXVideoObject();
      videoObject.videoUrl = shareVideoMedia.getVideoUrl();
      msg.mediaObject = videoObject;
      msg.title = shareVideoMedia.getTitle();
      msg.description = shareVideoMedia.getDescription();
      if (shareVideoMedia.getThumb() != null && shareVideoMedia.getThumb().isRecycled()) {
        mShareListener.onError(this.mConfig.getName(), "bitmap Recycled");
        return;
      }
      msg.thumbData = BitmapUtils.bmpToByteArray(shareVideoMedia.getThumb(), false);
    } else {
      if (this.mShareListener != null) {
        this.mShareListener.onError(this.mConfig.getName(),
            "weixin is not support this shareMedia");
      }
      return;
    }

    //发起request
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.message = msg;
    req.transaction = buildTransaction(type);

    //分享好友
    if (this.mConfig.getName() == PlatformType.WEIXIN) {
      req.scene = SendMessageToWX.Req.WXSceneSession;
      //分享朋友圈
    } else if (this.mConfig.getName() == PlatformType.WEIXIN_CIRCLE) {
      req.scene = SendMessageToWX.Req.WXSceneTimeline;
    }

    if (!this.mWXApi.sendReq(req)) {
      if (this.mShareListener != null) {
        this.mShareListener.onError(this.mConfig.getName(), "sendReq fail");
      }
      release();
    }
  }

  private void onShareCallback(SendMessageToWX.Resp resp) {
    switch (resp.errCode) {
      //分享成功
      case BaseResp.ErrCode.ERR_OK:
        if (this.mShareListener != null) {
          this.mShareListener.onComplete(this.mConfig.getName());
        }
        release();
        break;

      //分享取消
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        if (this.mShareListener != null) {
          this.mShareListener.onCancel(this.mConfig.getName());
        }
        release();
        break;

      default:    //分享失败
        CharSequence err = TextUtils.concat("weixin share error (", String.valueOf(resp.errCode),
            "):", resp.errStr);
        if (mShareListener != null) {
          mShareListener.onError(this.mConfig.getName(), err.toString());
        }
        release();
        break;
    }
  }

  private String buildTransaction(String type) {
    return type == null ? String.valueOf(System.currentTimeMillis())
        : type + System.currentTimeMillis();
  }

  public IWXAPI getWXApi() {
    return this.mWXApi;
  }

  public IWXAPIEventHandler getWXEventHandler() {
    return this.mEventHandler;
  }

  private PayListener mPayListener;

  @Override public void pay(Activity activity, PayBean payBean, PayListener payListener) {
    mPayListener = payListener;
    if (!isInstall()) {
      mPayListener.onError(this.mConfig.getName(), "wx not install");
      release();
      return;
    }
    String appId = payBean.getAppid();
    PayReq request = new PayReq();
    request.appId = appId;
    request.partnerId = payBean.getPartnerid();
    request.prepayId = payBean.getPrepayid();
    request.packageValue = payBean.getPackageX();
    request.nonceStr = payBean.getNoncestr();
    request.timeStamp = payBean.getTimestamp();
    request.sign = payBean.getSign();
    mWXApi.sendReq(request);
  }

  private void onPayCallback(PayResp resp) {
    if (!isInstall()) {
      mPayListener.onError(this.mConfig.getName(), "wx not install");
      release();
      Log.e("weiXinApi", "wx not install");
      return;
    }
    switch (resp.errCode) {
      //分享成功
      case BaseResp.ErrCode.ERR_OK:
        if (this.mPayListener != null) {
          this.mPayListener.onComplete(this.mConfig.getName());
        }
        release();
        break;
      //分享取消
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        if (this.mPayListener != null) {
          this.mPayListener.onCancel(this.mConfig.getName());
        }
        release();
        break;

      default:    //分享失败
        CharSequence err = TextUtils.concat("weixin share error (", String.valueOf(resp.errCode),
            "):", resp.errStr);
        if (mPayListener != null) {
          mPayListener.onError(this.mConfig.getName(), err.toString());
        }
        release();
        break;
    }
  }

  private void release() {
    mShareListener = null;
    mPayListener = null;
    mEventHandler = null;
    mAuthListener = null;
    mWXApi = null;
  }
}
