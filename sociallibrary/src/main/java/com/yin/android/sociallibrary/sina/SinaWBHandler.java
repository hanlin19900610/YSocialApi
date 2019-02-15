package com.yin.android.sociallibrary.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.yin.android.sociallibrary.PlatformConfig;
import com.yin.android.sociallibrary.SSOHandler;
import com.yin.android.sociallibrary.listener.AuthListener;
import com.yin.android.sociallibrary.listener.ShareListener;
import com.yin.android.sociallibrary.share_media.IShareMedia;
import com.yin.android.sociallibrary.share_media.ShareImageMedia;
import com.yin.android.sociallibrary.share_media.ShareTextImageMedia;
import com.yin.android.sociallibrary.share_media.ShareTextMedia;
import java.util.HashMap;
import java.util.Map;

/**
 * description:  新浪微博 第三方Hnadler
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public class SinaWBHandler extends SSOHandler {

  private SsoHandler mSsoHandler;
  private WbShareCallback wbShareCallback;

  public PlatformConfig.SinaWB getConfig() {
    return mConfig;
  }

  private PlatformConfig.SinaWB mConfig;
  private AuthListener mAuthListener;

  public ShareListener getShareListener() {
    return mShareListener;
  }

  private ShareListener mShareListener;

  private WbShareHandler shareHandler = null;
  public static final String SCOPE =
      "email,direct_messages_read,direct_messages_write,"
          + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
          + "follow_app_official_microblog," + "invitation_write";

  @Override
  public void onCreate(Context context, PlatformConfig.Platform config) {
    this.mConfig = (PlatformConfig.SinaWB) config;
    if (null == mConfig || null == mConfig.appKey || null == mConfig.callBackUrl) {
      Log.e("SinaWBHandler", "sina key");
      return;
    }
    // 初始化微博服务
    WbSdk.install(context,
        new AuthInfo(context, mConfig.appKey,
            mConfig.callBackUrl,
            SCOPE));
    initShareLister();
  }

  @Override
  public void authorize(Activity activity, AuthListener authListener) {
    this.mAuthListener = authListener;

    this.mSsoHandler = new SsoHandler(activity);

    mSsoHandler.authorize(new WbAuthListener() {
      @Override public void onSuccess(Oauth2AccessToken accessToken) {
        // 从 Bundle 中解析 Token
        if (accessToken.isSessionValid()) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("uid", accessToken.getUid());
          map.put("access_token", accessToken.getToken());
          map.put("refresh_token", accessToken.getRefreshToken());
          map.put("expire_time", "" + accessToken.getExpiresTime());
          mAuthListener.onComplete(mConfig.getName(), map);
          release();
        } else {
          String errmsg = "errmsg=accessToken is not SessionValid";
          mAuthListener.onError(mConfig.getName(), errmsg);
          release();
        }
      }

      @Override public void cancel() {
        mAuthListener.onCancel(mConfig.getName());
      }

      @Override public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
        String errmsg = "errmsg=" + wbConnectErrorMessage.getErrorMessage();
        mAuthListener.onError(mConfig.getName(), errmsg);
        release();
      }
    });
  }

  @Override
  public void share(Activity activity, IShareMedia shareMedia, ShareListener shareListener) {
    this.mShareListener = shareListener;
    shareHandler = new WbShareHandler(activity);
    shareHandler.registerApp();
    //}
    this.mSsoHandler = new SsoHandler(activity);

    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
    //文字分享
    if (shareMedia instanceof ShareTextMedia) {
      ShareTextMedia shareTextMedia = (ShareTextMedia) shareMedia;
      String atUser =
          TextUtils.isEmpty(shareTextMedia.getAtUser()) ? "" : shareTextMedia.getAtUser();
      TextObject textObject = new TextObject();
      if (!TextUtils.isEmpty(shareTextMedia.getUrl())) {
        textObject.text =
            shareTextMedia.getDescription() + shareTextMedia.getUrl() + atUser;
      } else {
        textObject.text =
            shareTextMedia.getDescription() + atUser;
      }
      weiboMessage.textObject = textObject;
      //图片分享
    } else if (shareMedia instanceof ShareImageMedia) {
      ShareImageMedia shareImageMedia = (ShareImageMedia) shareMedia;
      ImageObject imageObject = new ImageObject();
      imageObject.setImageObject(shareImageMedia.getImage());
      weiboMessage.imageObject = imageObject;
    } else if (shareMedia instanceof ShareTextImageMedia) {
      ShareTextImageMedia shareTextImageMedia = (ShareTextImageMedia) shareMedia;
      String atUser =
          TextUtils.isEmpty(shareTextImageMedia.getAtUser()) ? "" : shareTextImageMedia.getAtUser();
      TextObject textObject = new TextObject();
      if (!TextUtils.isEmpty(shareTextImageMedia.getUrl())) {
        textObject.text =
            shareTextImageMedia.getDescription() + shareTextImageMedia.getUrl() + atUser;
      } else {
        textObject.text =
            shareTextImageMedia.getDescription() + atUser;
      }
      weiboMessage.textObject = textObject;
      ImageObject imageObject = new ImageObject();
      imageObject.setImageObject(shareTextImageMedia.getThumb());
      weiboMessage.imageObject = imageObject;
    } else {
      if (this.mShareListener != null) {
        this.mShareListener.onError(this.mConfig.getName(), "weibo is not support this shareMedia");
      }
      return;
    }
    if (shareHandler != null) {
      shareHandler.shareMessage(weiboMessage, false);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (mSsoHandler != null) {
      mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
    }
  }

  private void initShareLister() {
    wbShareCallback = new WbShareCallback() {
      @Override
      public void onWbShareSuccess() {
        mShareListener.onComplete(mConfig.getName());
        release();
      }

      @Override
      public void onWbShareCancel() {
        mShareListener.onCancel(mConfig.getName());
        release();
      }

      @Override
      public void onWbShareFail() {
        mShareListener.onError(mConfig.getName(), "");
        release();
      }
    };
  }

  public void onNewIntent(Intent intent) {
    if (shareHandler != null) {
      shareHandler.doResultIntent(intent, wbShareCallback);
    }
  }

  private void release() {
    mShareListener = null;
    wbShareCallback = null;
    shareHandler = null;
    mSsoHandler = null;
  }
}
