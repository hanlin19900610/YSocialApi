package com.yin.android.sociallibrary.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import com.yin.android.sociallibrary.PlatformConfig;
import com.yin.android.sociallibrary.PlatformType;
import com.yin.android.sociallibrary.SSOHandler;
import com.yin.android.sociallibrary.SocialApi;
import com.yin.android.sociallibrary.listener.AuthListener;
import com.yin.android.sociallibrary.listener.ShareListener;
import com.yin.android.sociallibrary.share_media.IShareMedia;
import com.yin.android.sociallibrary.share_media.ShareImageMedia;
import com.yin.android.sociallibrary.share_media.ShareMusicMedia;
import com.yin.android.sociallibrary.share_media.ShareTextImageMedia;
import com.yin.android.sociallibrary.share_media.ShareVideoMedia;
import com.yin.android.sociallibrary.share_media.ShareWebMedia;
import com.yin.android.sociallibrary.utils.BitmapUtils;
import com.yin.android.sociallibrary.utils.FilePathUtils;
import com.yin.android.sociallibrary.utils.FileUtils;
import com.yin.android.sociallibrary.utils.Utils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

/**
 * description: QQ 第三方 Handler
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
public class QQHandler extends SSOHandler {

  private Tencent mTencent;

  private PlatformConfig.QQ mConfig;
  private AuthListener mAuthListener;
  private ShareListener mShareListener;
  private String imagePath;

  public QQHandler() {

  }

  @Override
  public void onCreate(Context context, PlatformConfig.Platform config) {
    this.mConfig = (PlatformConfig.QQ) config;
    this.mTencent = SocialApi.getTenCent();
  }

  @Override
  public void authorize(Activity activity, AuthListener authListener) {
    this.mAuthListener = authListener;

    this.mTencent.login(activity, "all", new IUiListener() {
      @Override
      public void onComplete(Object o) {
        if (null == o) {
          Log.e("qq", "onComplete response=null");
          mAuthListener.onError(mConfig.getName(), "onComplete response=null");
          return;
        }
        JSONObject response = (JSONObject) o;
        initOpenidAndToken(response);
        mAuthListener.onComplete(mConfig.getName(), jsonToMap(response));

        //mTencent.logout(mActivity);
      }

      @Override
      public void onError(UiError uiError) {
        String errmsg = "errcode="
            + uiError.errorCode
            + " errmsg="
            + uiError.errorMessage
            + " errdetail="
            + uiError.errorDetail;
        Log.e("qq", errmsg);
        mAuthListener.onError(mConfig.getName(), errmsg);
      }

      @Override
      public void onCancel() {
        mAuthListener.onCancel(mConfig.getName());
      }
    });
  }

  private File file;

  @Override
  public void share(Activity activity, IShareMedia shareMedia, ShareListener shareListener) {
    this.mShareListener = shareListener;
    imagePath = FilePathUtils.getAppPath(Utils.getApp())
        + FilePathUtils.IMAGES
        + File.separator
        + "/socail_qq_img_tmp" + System.currentTimeMillis()
        + ".png";

    file = new File(imagePath);
    fileDelete();
    Bundle params = new Bundle();
    int shareType = -1;
    String title = "";
    String description = "";
    String url = "";
    Bitmap bitmap = null;
    //网页分享
    if (shareMedia instanceof ShareWebMedia) {
      ShareWebMedia shareWebMedia = (ShareWebMedia) shareMedia;
      //图片保存本地
      bitmap = shareWebMedia.getThumb();
      shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
      title = shareWebMedia.getTitle();
      description = shareWebMedia.getDescription();
      url = shareWebMedia.getWebPageUrl();
      //图片分享
    } else if (shareMedia instanceof ShareImageMedia) {
      ShareImageMedia shareImageMedia = (ShareImageMedia) shareMedia;
      //图片保存本地
      bitmap = shareImageMedia.getImage();
      shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE;
      //音乐分享
    } else if (shareMedia instanceof ShareMusicMedia) {
      ShareMusicMedia shareMusicMedia = (ShareMusicMedia) shareMedia;
      //图片保存本地
      bitmap = shareMusicMedia.getThumb();
      shareType = QQShare.SHARE_TO_QQ_TYPE_AUDIO;
      title = shareMusicMedia.getTitle();
      description = shareMusicMedia.getDescription();
      url = shareMusicMedia.getUrl();
      params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, shareMusicMedia.getAacUrl());
      //视频分享
    } else if (shareMedia instanceof ShareVideoMedia) {
      ShareVideoMedia shareVideoMedia = (ShareVideoMedia) shareMedia;
      //图片保存本地
      bitmap = shareVideoMedia.getThumb();
      shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
      title = shareVideoMedia.getTitle();
      description = shareVideoMedia.getDescription();
      url = shareVideoMedia.getVideoUrl();
    } else if (shareMedia instanceof ShareTextImageMedia) {
      ShareTextImageMedia shareTextImageMedia = (ShareTextImageMedia) shareMedia;
      shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
      bitmap = shareTextImageMedia.getThumb();
      shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
      title = shareTextImageMedia.getTitle();
      description = shareTextImageMedia.getDescription();
      url = shareTextImageMedia.getUrl();
    } else {
      if (this.mShareListener != null) {
        this.mShareListener.onError(this.mConfig.getName(), "QQ is not support this shareMedia");
      }
      return;
    }
    BitmapUtils.saveBitmapFile(bitmap, imagePath);
    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
    if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
      params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
      params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
      params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
    }
    if (this.mConfig.getName() == PlatformType.QZONE) {
      ArrayList<String> path_arr = new ArrayList<>();
      path_arr.add(imagePath);
      //!这里是大坑 不能用SHARE_TO_QQ_IMAGE_LOCAL_URL
      params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
          path_arr);
      params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
      this.mTencent.shareToQzone(activity, params, mIUiListener);
    } else {
      params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
      mTencent.shareToQQ(activity, params, mIUiListener);
    }
  }

  private IUiListener mIUiListener = new IUiListener() {
    @Override
    public void onComplete(Object o) {
      mShareListener.onComplete(mConfig.getName());
      fileDelete();
    }

    @Override
    public void onError(UiError uiError) {
      String errmsg = "errcode="
          + uiError.errorCode
          + " errmsg="
          + uiError.errorMessage
          + " errdetail="
          + uiError.errorDetail;
      Log.e("qq", errmsg);
      mShareListener.onError(mConfig.getName(), errmsg);

      fileDelete();
    }

    @Override
    public void onCancel() {
      mShareListener.onCancel(mConfig.getName());
      fileDelete();
    }
  };

  private void fileDelete() {
    FileUtils.deleteFile(file);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Tencent.onActivityResultData(requestCode, resultCode, data, null);
  }

  //要初始化open_id和token
  private void initOpenidAndToken(JSONObject jsonObject) {
    try {
      String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
      String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
      String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);

      mTencent.setAccessToken(token, expires);
      mTencent.setOpenId(openId);
    } catch (Exception e) {
      e.getMessage();
    }
  }

  private static Map<String, String> jsonToMap(JSONObject val) {
    HashMap<String, String> map = new HashMap<String, String>();

    Iterator<String> iterator = val.keys();

    while (iterator.hasNext()) {
      String var4 = iterator.next();
      map.put(var4, val.opt(var4) + "");
    }
    return map;
  }
}
