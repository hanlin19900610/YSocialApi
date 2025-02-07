package com.yin.sociallibrary.handler

import com.yin.sociallibrary.callback.OperationCallback
import com.yin.sociallibrary.config.PlatformType
import com.yin.sociallibrary.entity.content.AuthContent
import com.yin.sociallibrary.entity.content.OperationContent
import com.yin.sociallibrary.entity.content.PayContent
import com.yin.sociallibrary.entity.content.ShareContent

/**
 * description: handler的抽象类
 * @date 2019/7/15
 * @author: yzy.
 */
abstract class SSOHandler {

  /**
   * 判断是否安装平台
   */
  open val isInstalled: Boolean
    get() = true

  /**
   * 重写onActivityResult
   */
  open fun onActivityResult(content: OperationContent) {

  }

  /**
   *  支付
   */
  open fun pay(type: PlatformType, content: PayContent, callback: OperationCallback) {

  }

  /**
   * 分享
   */
  open fun share(type: PlatformType, content: ShareContent, callback: OperationCallback) {
  }

  /**
   * 授权
   */
  open fun authorize(type: PlatformType, callback: OperationCallback, content: AuthContent? = null) {
  }

  /**
   * 资源释放
   */
  open fun release() {
  }
}