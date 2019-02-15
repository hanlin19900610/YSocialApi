package com.yin.android.sociallibrary.utils;

import android.content.Context;
import java.io.File;

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/25 15:13
 */
public class FilePathUtils {

  /**
   * 图片
   */
  public static final String IMAGES = "images";

  /**
   * 获取自定义的app的主目录
   */
  public static String getAppPath(Context context) {
    return (SDCardUtils.isSDCardEnable() ? (SDCardUtils.getSDCardPaths().get(0) + File.separator)
        : context.getCacheDir().getPath() + File.separator);
  }
}
