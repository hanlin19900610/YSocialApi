package com.yin.android.sociallibrary.utils;

import android.content.Context;
import android.os.storage.StorageManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/25 15:13
 */
public class SDCardUtils {
  private SDCardUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }


  /**
   * Return whether sdcard is enabled.
   *
   * @return true : enabled<br>false : disabled
   */
  public static boolean isSDCardEnable() {
    return !getSDCardPaths().isEmpty();
  }

  /**
   * Return the paths of sdcard.
   *
   * @return the paths of sdcard
   */
  public static List<String> getSDCardPaths() {
    StorageManager storageManager = (StorageManager) Utils.getApp()
        .getSystemService(Context.STORAGE_SERVICE);
    List<String> paths = new ArrayList<>();
    try {
      Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
      getVolumePathsMethod.setAccessible(true);
      Object invoke = getVolumePathsMethod.invoke(storageManager);
      paths = Arrays.asList((String[]) invoke);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return paths;
  }
}
