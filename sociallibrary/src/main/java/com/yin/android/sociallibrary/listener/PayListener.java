package com.yin.android.sociallibrary.listener;

import com.yin.android.sociallibrary.PlatformType;


/**
 * description: 支付回调监听
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public interface PayListener {
  void onComplete(PlatformType platform_type);

  void onError(PlatformType platform_type, String err_msg);

  void onCancel(PlatformType platform_type);
}
