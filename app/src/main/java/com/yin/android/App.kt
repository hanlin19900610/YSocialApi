package com.yin.android

import android.app.Application
import com.yin.sociallibrary.config.PlatformType
import com.yin.sociallibrary.entity.platform.CommPlatConfigBean
import com.yin.sociallibrary.entity.platform.SinaPlatConfigBean
import com.yin.sociallibrary.Social

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/30 18:00
 */
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    initSocial()
  }

  private fun initSocial() {
    Social.init(
      applicationContext,
      CommPlatConfigBean(PlatformType.WEIXIN, ""),  // 微信key
      CommPlatConfigBean(PlatformType.QQ, appkey = ""), // qqkey
      SinaPlatConfigBean(
        PlatformType.SINA_WEIBO,
        appkey = "1472835731",
        redirectUrl = "http://www.meda.cc",
        scope = (
          "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write")
      )
    )
  }
}
