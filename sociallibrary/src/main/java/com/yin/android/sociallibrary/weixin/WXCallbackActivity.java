package com.yin.android.sociallibrary.weixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import com.yin.android.sociallibrary.PlatformConfig;
import com.yin.android.sociallibrary.PlatformType;
import com.yin.android.sociallibrary.SocialApi;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
/**
 * description:  微信登陆分享 activity
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public class WXCallbackActivity extends Activity implements IWXAPIEventHandler {

  protected WXHandler mWXHandler = null;
  protected WXHandler mWXCircleHandler = null;

  public WXCallbackActivity() {

  }

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    this.mWXHandler = (WXHandler) SocialApi.getSocialApi().getSSOHandler(PlatformType.WEIXIN);
    this.mWXHandler.onCreate(this.getApplicationContext(),
        PlatformConfig.getPlatformConfig(PlatformType.WEIXIN));

    this.mWXCircleHandler =
        (WXHandler) SocialApi.getSocialApi().getSSOHandler(PlatformType.WEIXIN_CIRCLE);
    this.mWXCircleHandler.onCreate(this.getApplicationContext(),
        PlatformConfig.getPlatformConfig(PlatformType.WEIXIN_CIRCLE));

    this.mWXHandler.getWXApi().handleIntent(this.getIntent(), this);
  }

  protected final void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    this.mWXHandler = (WXHandler) SocialApi.getSocialApi().getSSOHandler(PlatformType.WEIXIN);
    this.mWXHandler.onCreate(this.getApplicationContext(),
        PlatformConfig.getPlatformConfig(PlatformType.WEIXIN));

    this.mWXCircleHandler =
        (WXHandler) SocialApi.getSocialApi().getSSOHandler(PlatformType.WEIXIN_CIRCLE);
    this.mWXCircleHandler.onCreate(this.getApplicationContext(),
        PlatformConfig.getPlatformConfig(PlatformType.WEIXIN_CIRCLE));

    this.mWXHandler.getWXApi().handleIntent(this.getIntent(), this);
  }

  public void onResp(BaseResp resp) {
    if (this.mWXHandler != null && resp != null) {
      try {
        this.mWXHandler.getWXEventHandler().onResp(resp);
      } catch (Exception var3) {
        ;
      }
    }

    if (this.mWXCircleHandler != null && resp != null) {
      try {
        this.mWXCircleHandler.getWXEventHandler().onResp(resp);
      } catch (Exception var3) {
        ;
      }
    }

    this.finish();
    overridePendingTransition(0 ,0);
  }

  public void onReq(BaseReq req) {
    if (this.mWXHandler != null) {
      this.mWXHandler.getWXEventHandler().onReq(req);
    }

    this.finish();
    overridePendingTransition(0 ,0);
  }
}