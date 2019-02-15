package com.yin.android.sociallibrary.share_media;

/**
 * description:  文字分享 实体类
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public class ShareTextMedia implements IShareMedia {
  private String mDescription;    //描述
  private String mUrl;    // 连接
  private String atUser;

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    mDescription = description;
  }

  public String getUrl() {
    return mUrl;
  }

  public void setUrl(String url) {
    mUrl = url;
  }

  public String getAtUser() {
    return atUser;
  }

  public void setAtUser(String atUser) {
    this.atUser = atUser;
  }
}
