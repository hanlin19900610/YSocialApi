package com.yin.android.sociallibrary.utils;

import com.yin.android.sociallibrary.SocialApi;

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/14 9:42
 */
public class WechatUtils {
  public static boolean isInstallWechat() {
    if (!SocialApi.getWeChat().isWXAppInstalled()) {
      //ToastUtils.showShort(R.string.no_install_wechat);
      return false;
    }
    return true;
  }
}
