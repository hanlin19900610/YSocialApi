package com.yin.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yin.android.sociallibrary.PayBean;
import com.yin.android.sociallibrary.PlatformType;
import com.yin.android.sociallibrary.SocialApi;
import com.yin.android.sociallibrary.listener.AuthListener;
import com.yin.android.sociallibrary.listener.PayListener;
import com.yin.android.sociallibrary.listener.ShareListener;
import com.yin.android.sociallibrary.share_media.IShareMedia;
import com.yin.android.sociallibrary.share_media.ShareImageMedia;
import com.yin.android.sociallibrary.share_media.ShareMusicMedia;
import com.yin.android.sociallibrary.share_media.ShareTextImageMedia;
import com.yin.android.sociallibrary.share_media.ShareTextMedia;
import com.yin.android.sociallibrary.share_media.ShareVideoMedia;
import com.yin.android.sociallibrary.share_media.ShareWebMedia;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  @BindView(R.id.radioGSharePlatform) RadioGroup radioGSharePlatform;
  @BindView(R.id.radioGShareMedia) RadioGroup radioGShareMedia;
  Bitmap mBitmap;
  IShareMedia shareMedia = null;
  PlatformType mPlatformType;

  //微博分享需要重写此方法
  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    SocialApi.getSocialApi().doNewIntent(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    radioGSharePlatform.setOnCheckedChangeListener(
        (group, checkedId) -> {
          switch (radioGSharePlatform.getCheckedRadioButtonId()) {
            case R.id.radioShareWX:
              mPlatformType = PlatformType.WEIXIN;

              break;

            case R.id.radioShareWXCircle:
              mPlatformType = PlatformType.WEIXIN_CIRCLE;
              break;

            case R.id.radioShareQQ:
              mPlatformType = PlatformType.QQ;
              break;

            case R.id.radioShareQZone:
              mPlatformType = PlatformType.QZONE;
              break;

            case R.id.radioShareSinaWB:
              mPlatformType = PlatformType.SINA_WB;
              break;

            default:
              return;
          }
        });
    radioGShareMedia.setOnCheckedChangeListener((group, checkedId) -> {
      switch (radioGShareMedia.getCheckedRadioButtonId()) {
        case R.id.radioShareText:
          shareMedia = new ShareTextMedia();
          ((ShareTextMedia) shareMedia).setDescription("分享文字测试111");
          break;

        case R.id.radioShareImage:
          shareMedia = new ShareImageMedia();
          ((ShareImageMedia) shareMedia).setImage(
              mBitmap);
          break;
        case R.id.radioShareTextImage:
          shareMedia = new ShareTextImageMedia();
          ((ShareTextImageMedia) shareMedia).setTitle("分享文字测试");
          ((ShareTextImageMedia) shareMedia).setDescription("分享文字测试111");
          ((ShareTextImageMedia) shareMedia).setUrl(
              "http://dev.aimymusic.com/h5/#/share/play?worksId=5109");
          ((ShareTextImageMedia) shareMedia).setThumb(
              mBitmap);
          break;

        case R.id.radioShareMusic:
          shareMedia = new ShareMusicMedia();
          ((ShareMusicMedia) shareMedia).setTitle("分享音乐测试");
          ((ShareMusicMedia) shareMedia).setDescription("分享音乐测试");
          ((ShareMusicMedia) shareMedia).setAacUrl(
              "http://ossaudio.singworld.cn/9/50/83215_LOW_20180424122703_0.aac");
          ((ShareMusicMedia) shareMedia).setUrl(
              "http://dev.aimymusic.com/h5/#/share/play?worksId=5109");
          ((ShareMusicMedia) shareMedia).setThumb(
              mBitmap);
          break;

        case R.id.radioShareVideo:
          shareMedia = new ShareVideoMedia();
          ((ShareVideoMedia) shareMedia).setTitle("分享视频测试");
          ((ShareVideoMedia) shareMedia).setDescription("分享视频测试");
          ((ShareVideoMedia) shareMedia).setUrl(
              "http://dev.aimymusic.com/h5/#/share/play?worksId=5521");
          ((ShareVideoMedia) shareMedia).setVideoUrl(
              "http://dev.aimymusic.com/h5/#/share/play?worksId=5521");
          ((ShareVideoMedia) shareMedia).setThumb(
              mBitmap);
          break;

        case R.id.radioShareWeb:
          shareMedia = new ShareWebMedia();
          ((ShareWebMedia) shareMedia).setTitle("分享网页测试");
          ((ShareWebMedia) shareMedia).setDescription("分享网页测试111");
          ((ShareWebMedia) shareMedia).setWebPageUrl("http://www.baidu.com");
          ((ShareWebMedia) shareMedia).setThumb(
              mBitmap);
          break;

        default:
          return;
      }
    });
  }

  private PayListener getPayListener = new PayListener() {
    @Override public void onComplete(PlatformType platform_type) {

    }

    @Override public void onError(PlatformType platform_type, String err_msg) {

    }

    @Override public void onCancel(PlatformType platform_type) {

    }
  };

  AuthListener mAuthListener = new AuthListener() {

    @Override public void onComplete(PlatformType platformType, Map<String, String> map) {
      if (platformType == PlatformType.ALI) {
      } else if (platformType == PlatformType.QQ) {

      } else if (platformType == PlatformType.SINA_WB) {

      } else if (platformType == PlatformType.WEIXIN) {

      }
    }

    @Override public void onError(PlatformType platform_type, String err_msg) {
      if (platform_type == PlatformType.WEIXIN && "wx not install".equals(err_msg)) {
      }
    }

    @Override public void onCancel(PlatformType platform_type) {

    }
  };

  @OnClick({
      R.id.tv_main_test2, R.id.tv_main_test3,
      R.id.btnShare, R.id.tv_main_login
  })
  public void onClick(View view) {
    PayBean payBean = new PayBean();
    payBean.setAppid("wx6cfb5146066fbc02");
    payBean.setPackageX("Sign=WXPay");
    payBean.setPartnerid("1269456201");
    payBean.setPrepayid("wx19200551720615107b65b4dd2835845632");
    payBean.setSign("F43EA7E2C3B9E5B604DEE38541AC5E59");
    payBean.setTimestamp("1529409951");
    payBean.setNoncestr("9lm7F6");
    payBean.setOut_trade_no("1806194720128169618469354176");
    payBean.setOrderNumber("1806196752128226380787280320");
    payBean.setParams(
        "app_id=2017091408723747&biz_content=%7B%22subject%22%3A%2215%E5%88%86%E9%92%9F%E6%AC%A2%E5%94%B1%E5%A5%97%E9%A4%90%22%2C%22body%22%3A%2215%E5%88%86%E9%92%9F%E6%AC%A2%E5%94%B1%E5%A5%97%E9%A4%90%22%2C%22out_trade_no%22%3A%221806196752128228370872465024%22%2C%22total_amount%22%3A%220.50%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=api.aimymusic.com%2Fapi%2Fpay%2FreceiveAliNotify&sign_type=RSA2&timestamp=2018-06-19+20%3A13%3A46&version=1.0&sign=UsSHCTjpX5s6XsFK8XzEFMGihWmYNGL8g7lrryPZVyAHpCO%2F4zJ%2Fs%2Bl1YO2zBetArr3aGdKnzrBiBl8e9%2FvhU01LZr4VByFMC9PmrapgXF0DTgq%2FGT43%2B%2FeKEwZG1%2Fyx04b48hZLVACQsHdSjmBB6NkXtl0tx96v7qUFgK61DuJHF3PDI3rJhsl%2BiKHcWqoS%2BvmFapt29DTZ9tIcP2ziGrR1kdMN%2B3JJ9sYt7i8NWJf8%2BiiKcvZXOSAN6N7SCMU0RvcGD3tSyv8NUejqU8rxxMAl4%2BCXrxCiwBSPK4wbw4f2TFyrT2I1zs25YVQrc1MX0n07xx8zqTuVFHNkG73EdA%3D%3D");
    switch (view.getId()) {
      case R.id.tv_main_test2:
        SocialApi.getSocialApi().doPayment(this, PlatformType.WEIXIN, payBean, getPayListener);
        break;
      case R.id.tv_main_test3:
        SocialApi.getSocialApi().doPayment(this, PlatformType.ALI, payBean, getPayListener);
        break;
      case R.id.btnShare:
        if (mPlatformType == null) return;
        SocialApi.getSocialApi()
            .doShare(this, mPlatformType, shareMedia, new MyShareListener());
        break;
      case R.id.tv_main_login:
        //SocialApi.getSocialApi().doOauthVerify(this, PlatformType.SINA_WB, mAuthListener);
        //if (WechatUtils.isInstallWechat()) {
        //  SocialApi.getSocialApi().doOauthVerify(this, PlatformType.WEIXIN, mAuthListener);
        //}
        //支付宝要传入凭证
        //SocialApi.getSocialApi().setAuthToken(alipayLoginBean.getParams());
        //SocialApi.getSocialApi().doOauthVerify(mActivity, PlatformType.ALI, mAuthListener);
        SocialApi.getSocialApi().doOauthVerify(this, PlatformType.QQ, mAuthListener);
        break;
      default:
        break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    SocialApi.getSocialApi().onActivityResult(requestCode, resultCode, data);
  }

  public class MyShareListener implements ShareListener {

    @Override
    public void onComplete(PlatformType platform_type) {
    }

    @Override
    public void onError(PlatformType platform_type, String err_msg) {
    }

    @Override
    public void onCancel(PlatformType platform_type) {
    }
  }
}
