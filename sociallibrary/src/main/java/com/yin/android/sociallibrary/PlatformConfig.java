package com.yin.android.sociallibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * description:第三方平台配置信息存储
 *
 * @author yinzeyu
 * @date 2018/6/19 16:50
 */
public class PlatformConfig {

  public static Map<PlatformType, Platform> configs = new HashMap();

  static {
    configs.put(PlatformType.WEIXIN, new Weixin(PlatformType.WEIXIN));
    configs.put(PlatformType.WEIXIN_CIRCLE, new Weixin(PlatformType.WEIXIN_CIRCLE));
    configs.put(PlatformType.QQ, new QQ(PlatformType.QQ));
    configs.put(PlatformType.QZONE, new QQ(PlatformType.QZONE));
    configs.put(PlatformType.SINA_WB, new SinaWB(PlatformType.SINA_WB));
    configs.put(PlatformType.ALI, new Ali(PlatformType.ALI));
  }

  public interface Platform {
    PlatformType getName();
  }

  //微信
  public static class Weixin implements Platform {
    private final PlatformType media;
    String appId = null;

    @Override public PlatformType getName() {
      return this.media;
    }

    public Weixin(PlatformType type) {
      this.media = type;
    }
  }

  /**
   * 设置微信配置信息
   */
  public static void setWeixin(String appId) {
    Weixin weixin = (Weixin) configs.get(PlatformType.WEIXIN);
    weixin.appId = appId;

    Weixin weiringCircle = (Weixin) configs.get(PlatformType.WEIXIN_CIRCLE);
    weiringCircle.appId = appId;
  }

  //微信
  public static class Ali implements Platform {
    private final PlatformType media;
    String appId = null;

    @Override public PlatformType getName() {
      return this.media;
    }

    Ali(PlatformType type) {
      this.media = type;
    }
  }

  /**
   * 设置微信配置信息
   */
  public static void setAli(String appId) {
    Ali ali = (Ali) configs.get(PlatformType.ALI);
    ali.appId = appId;
  }

  //qq
  public static class QQ implements Platform {
    private final PlatformType media;
    public String appId = null;

    @Override public PlatformType getName() {
      return this.media;
    }

    public QQ(PlatformType type) {
      this.media = type;
    }
  }

  /**
   * 设置qq配置信息
   */
  public static void setQQ(String appId) {
    QQ qq = (QQ) configs.get(PlatformType.QQ);
    qq.appId = appId;

    QQ qzone = (QQ) configs.get(PlatformType.QZONE);
    qzone.appId = appId;
  }

  /**
   * qq
   */
  public static class SinaWB implements Platform {
    private final PlatformType media;
    public String appKey = null;

    @Override public PlatformType getName() {
      return this.media;
    }

    public SinaWB(PlatformType type) {
      this.media = type;
    }
  }

  /**
   * 设置新浪微博配置信息
   */
  public static void setSinaWB(String appKey) {
    SinaWB sinaWB = (SinaWB) configs.get(PlatformType.SINA_WB);
    sinaWB.appKey = appKey;
  }

  public static Platform getPlatformConfig(PlatformType platformType) {
    return configs.get(platformType);
  }
}
