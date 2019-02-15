package com.yin.android.sociallibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.tencent.connect.auth.AuthAgent;
import com.tencent.connect.common.UIListenerManager;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.yin.android.sociallibrary.ali.AliHandler;
import com.yin.android.sociallibrary.listener.AuthListener;
import com.yin.android.sociallibrary.listener.PayListener;
import com.yin.android.sociallibrary.listener.ShareListener;
import com.yin.android.sociallibrary.qq.QQHandler;
import com.yin.android.sociallibrary.share_media.IShareMedia;
import com.yin.android.sociallibrary.sina.SinaWBHandler;
import com.yin.android.sociallibrary.utils.Utils;
import com.yin.android.sociallibrary.weixin.WXHandler;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * description: api调用统一入口
 *
 * @author yinzeyu
 * @date 2018/6/19 16:50
 */
public class SocialApi {
  private static SocialApi sSocialApiManager;
  private SinaWBHandler sinaWBHandler;
  private IWXAPI mWeChat;
  private Tencent mTenCent;

  public static SocialApi getSocialApi() {
    if (sSocialApiManager == null) {
      synchronized (SocialApi.class) {
        if (sSocialApiManager == null) {
          sSocialApiManager = new SocialApi();
        }
      }
    }
    return sSocialApiManager;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  /**
   * 初始化支付宝sdk
   *
   * @param aliKey 支付宝key
   */
  public void initAliPAy(String aliKey) {
    if (!TextUtils.isEmpty(aliKey)) {
      PlatformConfig.setAli(aliKey);
    }
  }

  /**
   * 初始化微博sdk
   *
   * @param sInaWBKEy 微博key
   * @param callBackUrl 微博回调地址
   */
  public void initWBKey(String sInaWBKEy, String callBackUrl) {
    if (!TextUtils.isEmpty(sInaWBKEy)) {
      PlatformConfig.setSinaWB(sInaWBKEy, callBackUrl);
    }
  }

  /**
   * 初始化微信key
   *
   * @param application 上下文
   * @param weChatKey 微信key
   */
  public void initWeChatKey(Application application, String weChatKey) {
    if (!TextUtils.isEmpty(weChatKey)) {
      PlatformConfig.setWeixin(weChatKey);
      mWeChat = WXAPIFactory.createWXAPI(application, weChatKey, true);
      mWeChat.registerApp(weChatKey);
    }
  }

  /**
   * 初始化微信key
   *
   * @param application 上下文
   * @param qqKey qqKey
   */
  public void initQQ(Application application, String qqKey) {
    if (!TextUtils.isEmpty(qqKey)) {
      PlatformConfig.setQQ(qqKey);
      // 必须 写此方法 否则会导致使用崩溃
      mTenCent = Tencent.createInstance(qqKey, application.getApplicationContext());
    }
  }

  /**
   * 获取微信 api
   */
  public static IWXAPI getWeChat() {
    return getSocialApi().mWeChat;
  }

  public static Tencent getTenCent() {
    return getSocialApi().mTenCent;
  }

  /**
   * 传入token
   */
  private String authToken;

  private final HashMap<PlatformType, SSOHandler> mMapSSOHandler = new HashMap<>();

  private SocialApi() {
  }

  public SSOHandler getSSOHandler(PlatformType platformType) {
    if (mMapSSOHandler.get(platformType) == null) {
      switch (platformType) {
        case WEIXIN:
          mMapSSOHandler.put(platformType, new WXHandler());
          break;

        case WEIXIN_CIRCLE:
          mMapSSOHandler.put(platformType, new WXHandler());
          break;

        case QQ:
          mMapSSOHandler.put(platformType, new QQHandler());
          break;

        case QZONE:
          mMapSSOHandler.put(platformType, new QQHandler());
          break;

        case SINA_WB:
          sinaWBHandler = new SinaWBHandler();
          mMapSSOHandler.put(platformType, sinaWBHandler);
          break;
        case ALI:
          mMapSSOHandler.put(platformType, new AliHandler(authToken));
          break;
        default:
          break;
      }
    }

    return mMapSSOHandler.get(platformType);
  }

  /**
   * 第三方登录授权
   *
   * @param platformType 第三方平台
   * @param authListener 授权回调
   */
  public void doOauthVerify(Activity activity, PlatformType platformType,
      AuthListener authListener) {
    SSOHandler ssoHandler = getSSOHandler(platformType);
    ssoHandler.onCreate(Utils.getApp(), PlatformConfig.getPlatformConfig(platformType));
    ssoHandler.authorize(activity, authListener);
  }

  /**
   * 分享
   */
  public void doShare(Activity activity, PlatformType platformType, IShareMedia shareMedia,
      ShareListener shareListener) {
    SSOHandler ssoHandler = getSSOHandler(platformType);
    ssoHandler.onCreate(Utils.getApp(), PlatformConfig.getPlatformConfig(platformType));
    ssoHandler.share(activity, shareMedia, shareListener);
  }

  /**
   * 支付
   */
  public void doPayment(Activity activity, PlatformType platformType, PayBean shareMedia,
      PayListener payListener) {
    if (platformType != PlatformType.WEIXIN || platformType != PlatformType.ALI) {
      Log.e("socialApi", "支付方式只支持微信和支付宝");
      return;
    }
    SSOHandler ssoHandler = getSSOHandler(platformType);
    ssoHandler.onCreate(Utils.getApp(), PlatformConfig.getPlatformConfig(platformType));
    ssoHandler.pay(activity, shareMedia, payListener);
  }

  /**
   * actvitiy result
   */
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    for (Map.Entry<PlatformType, SSOHandler> entry : mMapSSOHandler.entrySet()) {
      entry.getValue().onActivityResult(requestCode, resultCode, data);
    }
  }

  /**
   * 微博分享
   *
   * @param intent intent
   */
  public void doNewIntent(Intent intent) {
    if (sinaWBHandler != null) {
      sinaWBHandler.onNewIntent(intent);
    }
  }

  /**
   * 优化内存泄漏 onDestroy 调用
   */
  public void release() {
    if (mMapSSOHandler.size() > 0) {
      mMapSSOHandler.clear();
    }
    sinaWBHandler = null;
    if (mTenCent != null) {
      //因为腾讯没有提供get方法，无法释放资源，所以暂时使用反射
      try {
        //一次反射获取c对象
        Field tencentField = mTenCent.getClass().getDeclaredField("a");
        tencentField.setAccessible(true);
        com.tencent.connect.auth.c c = (com.tencent.connect.auth.c) tencentField.get(mTenCent);
        //二次返获取AuthAgent对象
        Field authAgentField = c.getClass().getDeclaredField("a");
        authAgentField.setAccessible(true);
        AuthAgent authAgent = (AuthAgent) authAgentField.get(c);
        authAgent.releaseResource();
      } catch (Exception e) {
        e.printStackTrace();
      }
      mTenCent.releaseResource();
      //mTenCent = null;
    }
    UIListenerManager.getInstance().getListnerWithAction("action_login");
  }
}
