package com.yin.android;

import android.app.Application;
import com.yin.android.sociallibrary.SocialApi;
import com.yin.android.sociallibrary.utils.Utils;

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/30 18:00
 */
public class App extends Application {
  @Override public void onCreate() {
    super.onCreate();
    Utils.init(this);
    SocialApi.getSocialApi().initAliPAy(null);
    SocialApi.getSocialApi().initQQ(this, "1106524213");
    SocialApi.getSocialApi().initWBKey(null, null);
    SocialApi.getSocialApi().initWeChatKey(this, null);
  }
}
