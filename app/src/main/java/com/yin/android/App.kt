package com.yin.android

import android.app.Application
import com.yin.sociallibrary.Social
import com.yin.sociallibrary.config.PlatformType
import com.yin.sociallibrary.entity.platform.CommPlatConfigBean
import com.yin.sociallibrary.entity.platform.SinaPlatConfigBean

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
      CommPlatConfigBean(PlatformType.WEIXIN, "wxe83b3af2bb8b0dee"),  // 微信key
      CommPlatConfigBean(PlatformType.QQ, appkey = "1109696928"), // qqkey
      SinaPlatConfigBean(
        PlatformType.SINA_WEIBO,
        appkey = "2822560449",
        redirectUrl = "https://api.weibo.com/oauth2/default.html",
        scope = (
          "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write")
      )
    )
  }
}
