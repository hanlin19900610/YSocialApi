package com.yin.android.sociallibrary.share_media;

import android.graphics.Bitmap;

/**
 * description:  视频分享 实体类
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public class ShareVideoMedia implements IShareMedia {
  private String mVideoUrl;       //视频url
  private String mTitle;          //标题
  private String mDescription;    //描述
  private Bitmap mThumb;          //缩略图
  private String mUrl;          //网页

  public String getUrl() {
    return mUrl;
  }

  public void setUrl(String url) {
    mUrl = url;
  }

  public String getVideoUrl() {
    return mVideoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    mVideoUrl = videoUrl;
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
}
