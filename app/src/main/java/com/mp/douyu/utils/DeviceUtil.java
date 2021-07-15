package com.mp.douyu.utils;
 
import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.Locale;

/**
 * 设备信息的工具类
 */
public class DeviceUtil {
 
    /**
     * 获取设备宽度（px）
     *
     * @param context
     * @return
     */
    public static int deviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
 
    /**
     * 获取设备高度（px）
     *
     * @param context
     * @return
     */
    public static int deviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取CPU架构
     */
    public static String getCpuApi(){
        String[] abis;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI,Build.CPU_ABI2};
        }
        StringBuilder abiStr = new StringBuilder();
        for(String abi:abis)
        {
            abiStr.append(abi);
            abiStr.append('/');
        }
        return abiStr.toString();
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有权限则返回""
            return "";
        } else {
            String deviceId = tm.getDeviceId();
            if (deviceId == null) {
                return "";
            } else {
                return deviceId;
            }
        }
    }
 
    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }
 
    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }
 
    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return Build.VERSION.SDK_INT;
    }
 
    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return Build.VERSION.RELEASE;
    }
 

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * 设置到剪切板
     */
    public void setClipboard(String text, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }

    /**
     * 获取剪切板上的内容
     */
    @Nullable
    public static String getClipboard(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            ClipData data = cm.getPrimaryClip();
            if (data != null && data.getItemCount() > 0) {
                ClipData.Item item = data.getItemAt(0);
                if (item != null) {
                    CharSequence sequence = item.coerceToText(context);
                    if (sequence != null) {
                        return sequence.toString();
                    }
                }
            }
        }
        return null;
    }

}
 
 