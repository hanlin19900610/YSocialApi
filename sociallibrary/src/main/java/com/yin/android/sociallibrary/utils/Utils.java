package com.yin.android.sociallibrary.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;

public final class Utils {

  @SuppressLint("StaticFieldLeak")
  private static Application sApplication;

  private Utils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Init utils.
   * <p>Init it in the class of Application.</p>
   *
   * @param app application
   */
  public static void init(@NonNull final Application app) {
    if (sApplication == null) {
      Utils.sApplication = app;
    }
  }

  /**
   * Return the context of Application object.
   *
   * @return the context of Application object
   */
  public static Application getApp() {
    if (sApplication != null) return sApplication;
    try {
      @SuppressLint("PrivateApi")
      Class<?> activityThread = Class.forName("android.app.ActivityThread");
      Object at = activityThread.getMethod("currentActivityThread").invoke(null);
      Object app = activityThread.getMethod("getApplication").invoke(at);
      if (app == null) {
        throw new NullPointerException("u should init first");
      }
      init((Application) app);
      return sApplication;
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    throw new NullPointerException("u should init first");
  }
}
