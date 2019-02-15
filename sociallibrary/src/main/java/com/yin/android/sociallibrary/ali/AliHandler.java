package com.yin.android.sociallibrary.ali;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.yin.android.sociallibrary.PayBean;
import com.yin.android.sociallibrary.PlatformConfig;
import com.yin.android.sociallibrary.SSOHandler;
import com.yin.android.sociallibrary.listener.AuthListener;
import com.yin.android.sociallibrary.listener.PayListener;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public class AliHandler extends SSOHandler {
  private static final int SDK_PAY_FLAG = 11;
  private static final int SDK_AUTH_FLAG = 2;
  /**
   * 支付宝支付成功
   */
  public static final String ALI_9000 = "9000";
  /**
   * 支付宝支付失败
   */
  public static final String ALI_4000 = "4000";
  /**
   * 支付宝支付取消
   */
  public static final String ALI_6001 = "6001";

  public static final String STRING_ONE = "0";

  /**
   * type
   */
  private PlatformConfig.Ali mConfig;
  /**
   * 支付宝登录
   */
  private AuthListener mAuthListener;

  /**
   * 支付宝支付
   */
  private PayListener mPayListener;
  /**
   * 传入token
   */
  private String authToken;
  private Handler mHandler;

  public AliHandler(String token) {
    authToken = token;
  }

  @Override public void onCreate(Context context, PlatformConfig.Platform config) {
    super.onCreate(context, config);
    this.mConfig = (PlatformConfig.Ali) config;
  }

  private void setHandle(Activity mAce) {
    mHandler = new Handler(mAce.getMainLooper()) {
      @Override public void handleMessage(Message msg) {
        switch (msg.what) {
          case SDK_PAY_FLAG: {
            String resultStatus = ((Map<String, String>) msg.obj).get("resultStatus");
            if (ALI_9000.equals(resultStatus)) {
              mPayListener.onComplete(mConfig.getName());
            } else if (ALI_6001.equals(resultStatus)) {
              mPayListener.onCancel(mConfig.getName());
            } else {
              mPayListener.onError(mConfig.getName(), "");
            }
            break;
          }
          case SDK_AUTH_FLAG: {
            String resultStatus = ((Map<String, String>) msg.obj).get("resultStatus");
            if (ALI_9000.equals(resultStatus)) {
              String result = ((Map<String, String>) msg.obj).get("result");
              Map<String, String> aliToken = getAliToken(result);
              Map<String, String> map = new HashMap<>(16);
              if (aliToken != null) {
                map.put("auth_code", aliToken.get("auth_code"));
              }
              if (aliToken != null) {
                map.put("user_id", aliToken.get("user_id"));
              }
              mAuthListener.onComplete(mConfig.getName(), map);
            } else if (ALI_6001.equals(resultStatus)) {
              mAuthListener.onCancel(mConfig.getName());
            } else {
              mAuthListener.onError(mConfig.getName(), "");
            }
            break;
          }
          default:
            break;
        }
        remove();
      }
    };
  }

  private void remove() {
    if (mHandler != null) {
      mHandler.removeCallbacksAndMessages(null);
    }
  }

  @Override public void authorize(Activity activity, AuthListener authListener) {
    if (TextUtils.isEmpty(authToken)) {
      Log.e("aliToken", "no authToken");
      return;
    }
    setHandle(activity);
    this.mAuthListener = authListener;
    auth(authToken, activity);
  }

  /**
   * 调用该方法进行支付宝sdk调用
   */
  private void auth(final String payInfo, Activity activity) {
    // 必须异步调用
    new Thread(() -> {
      // 构造AuthTask 对象
      // 调用授权接口，获取授权结果
      Map<String, String> result = new AuthTask(activity).authV2(authToken, true);
      Message msg = new Message();
      msg.what = SDK_AUTH_FLAG;
      msg.obj = result;
      if (mHandler != null) {
        mHandler.sendMessage(msg);
      }
    }).start();
  }

  private static Map<String, String> getAliToken(String str) {
    if (TextUtils.isEmpty(str)) return null;
    Map<String, String> map = new HashMap<>();
    String[] udderCode = str.split("&");
    for (String s : udderCode) {
      String value;
      String[] split = s.split("=");
      try {
        if (split.length > 1) {
          value = split[1];
        } else {
          value = "";
        }
      } catch (Exception e) {
        e.getMessage();
        value = "";
      }
      String key = split[0];
      if ("auth_code".equals(key)) {
        map.put("auth_code", value);
      } else if ("user_id".equals(key)) {
        map.put("user_id", value);
      } else if ("success".equals(key)) {
        map.put("success", value);
      } else if ("result_code".equals(key)) {
        map.put("result_code", value);
      }
    }
    return map;
  }

  @Override public boolean isInstall() {
    return super.isInstall();
  }

  @Override public void pay(Activity activity, PayBean payBean, PayListener payListener) {
    mPayListener = payListener;
    if (payBean == null) {
      mPayListener.onError(mConfig.getName(), "");
      return;
    }
    setHandle(activity);
    pay(payBean.getParams(), activity);
  }

  /**
   * 调用该方法进行支付宝sdk调用
   */
  private void pay(final String payInfo, Activity activity) {
    // 必须异步调用
    new Thread(() -> {
      // 构造AuthTask 对象
      // 调用授权接口，获取授权结果
      Map<String, String> result = new PayTask(activity).payV2(payInfo.replace("&amp", "&"), true);
      Message msg = new Message();
      msg.what = SDK_PAY_FLAG;
      msg.obj = result;
      if (mHandler != null) {
        mHandler.sendMessage(msg);
      }
    }).start();
  }
}
