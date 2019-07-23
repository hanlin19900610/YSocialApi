package com.yin.sociallibrary.entity

import android.content.Context
import com.yin.sociallibrary.callback.OperationCallback
import com.yin.sociallibrary.config.OperationType
import com.yin.sociallibrary.config.PlatformType
import com.yin.sociallibrary.entity.content.OperationContent

/**
 * description: 第三方平台操作的实体
 *@date 2019/7/15
 *@author: yzy.
 */
data class OperationBean(
  var operationContext: Context,        // 操作上下文
  var operationPlat: PlatformType,      // 平台类型
  var operationType: OperationType,     // 操作类型
  var operationCallback: OperationCallback,   // 回调
  var operationContent: OperationContent? = null  // 平台内容
)