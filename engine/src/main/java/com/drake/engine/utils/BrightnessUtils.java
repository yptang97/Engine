/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.drake.engine.base.Library;


public final class BrightnessUtils {

  private BrightnessUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * 判断是否开启自动调节亮度
   *
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isAutoBrightnessEnabled() {
    try {
      int mode = Settings.System.getInt(
              Library.INSTANCE.getApp()
                      .getContentResolver(),
              Settings.System.SCREEN_BRIGHTNESS_MODE
      );
      return mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
    } catch (Settings.SettingNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 设置是否开启自动调节亮度
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
   * 并得到授权
   *
   * @param enabled {@code true}: 打开<br>{@code false}: 关闭
   * @return {@code true}: 成功<br>{@code false}: 失败
   */
  public static boolean setAutoBrightnessEnabled(final boolean enabled) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.System.canWrite(Library.INSTANCE.getApp())) {
      Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
      intent.setData(Uri.parse("package:" + Library.INSTANCE.getApp()
              .getPackageName()));
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Library.INSTANCE.getApp()
              .startActivity(intent);
      return false;
    }
    return Settings.System.putInt(
            Library.INSTANCE.getApp()
                    .getContentResolver(),
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            enabled ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
                    : Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
    );
  }

  /**
   * 获取屏幕亮度
   *
   * @return 屏幕亮度 0-255
   */
  public static int getBrightness() {
    try {
      return Settings.System.getInt(
              Library.INSTANCE.getApp()
                      .getContentResolver(),
              Settings.System.SCREEN_BRIGHTNESS
      );
    } catch (Settings.SettingNotFoundException e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 设置屏幕亮度
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_SETTINGS" />}</p>
   * 并得到授权
   *
   * @param brightness 亮度值
   */
  public static boolean setBrightness(@IntRange(from = 0, to = 255) final int brightness) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.System.canWrite(Library.INSTANCE.getApp())) {
      Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
      intent.setData(Uri.parse("package:" + Library.INSTANCE.getApp()
              .getPackageName()));
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      Library.INSTANCE.getApp()
              .startActivity(intent);
      return false;
    }
    ContentResolver resolver = Library.INSTANCE.getApp()
            .getContentResolver();
    boolean b = Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
    resolver.notifyChange(Settings.System.getUriFor("screen_brightness"), null);
    return b;
  }

  /**
   * 设置窗口亮度
   *
   * @param window     窗口
   * @param brightness 亮度值
   */
  public static void setWindowBrightness(@NonNull final Window window,
                                         @IntRange(from = 0, to = 255) final int brightness) {
    WindowManager.LayoutParams lp = window.getAttributes();
    lp.screenBrightness = brightness / 255f;
    window.setAttributes(lp);
  }

  /**
   * 获取窗口亮度
   *
   * @param window 窗口
   * @return 屏幕亮度 0-255
   */
  public static int getWindowBrightness(final Window window) {
    WindowManager.LayoutParams lp = window.getAttributes();
    float brightness = lp.screenBrightness;
    if (brightness < 0) {
      return getBrightness();
    }
    return (int) (brightness * 255);
  }
}