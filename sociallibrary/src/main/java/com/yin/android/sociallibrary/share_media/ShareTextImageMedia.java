package com.yin.android.sociallibrary.share_media;

import android.graphics.Bitmap;

/**
 * description:  文字图片分享 实体类
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public class ShareTextImageMedia implements IShareMedia {
  private String mUrl;       //音乐url
  private String mTitle;          //标题
  private String mDescription;    //描述
  private Bitmap mThumb;          //缩略图
  private String atUser;

  public String getUrl() {
    return mUrl;
  }

  public void setUrl(String url) {
    mUrl = url;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String title) {
    mTitle = title;
  }

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    mDescription = description;
  }

  public Bitmap getThumb() {
    return mThumb;
  }

  public void setThumb(Bitmap thumb) {
    mThumb = thumb;
  }

  public String getAtUser() {
    return atUser;
  }

  public void setAtUser(String atUser) {
    this.atUser = atUser;
  }
}
