package com.yin.android.sociallibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.yin.android.sociallibrary.listener.AuthListener;
import com.yin.android.sociallibrary.listener.PayListener;
import com.yin.android.sociallibrary.listener.ShareListener;
import com.yin.android.sociallibrary.share_media.IShareMedia;

/**
 * Created by tsy on 16/8/4.
 */
public abstract class SSOHandler {

  /**
   * 初始化
   *
   * @param config 配置信息
   */
  public void onCreate(Context context, PlatformConfig.Platform config) {

  }

  /**
   * 登录授权
   *
   * @param authListener 授权回调
   */
  public void authorize(Activity activity, AuthListener authListener) {

  }

  /**
   * 分享
   *
   * @param shareMedia 分享内容
   * @param shareListener 分享回调
   */
  public void share(Activity activity, IShareMedia shareMedia, ShareListener shareListener) {

  }

  /**
   * 分享
   *
   * @param payBean 支付 bean
   * @param shareListener 分享回调
   */
  public void pay(Activity activity, PayBean payBean, PayListener shareListener) {

  }

  /**
   * 重写onActivityResult
   */
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

  }

  /**
   * 是否安装
   *
   * @return 检查是否安装
   */
  public boolean isInstall() {
    return true;
  }
}
