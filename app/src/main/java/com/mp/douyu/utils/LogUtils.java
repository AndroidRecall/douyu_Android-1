package com.mp.douyu.utils;

import android.util.Log;

import com.mp.douyu.BuildConfig;



public class LogUtils {

    private static boolean LOG_ENABLE = BuildConfig.LOG_ENABLE;


    public static void e(String tag, String message) {
        if (LOG_ENABLE && tag != null && message != null) {
            Log.e(tag, message);
        }
    }
    public static void e(String message) {
        if (LOG_ENABLE  && message != null) {
            Log.e("live==", message);
        }
    }



    public static void i(String tag, String message) {
        if (LOG_ENABLE && tag != null && message != null) {
//            Log.i(tag, message);
            //  把4*1024的MAX字节打印长度改为2001字符数
            int max_str_length = 2001 - tag.length();
            //大于4000时
            while (message.length() > max_str_length) {
                Log.i(tag, message.substring(0, max_str_length));
                message = message.substring(max_str_length);
            }
            //剩余部分
            Log.i(tag, message);
        }
    }

    public static void t(Throwable t) {
        if (LOG_ENABLE && t != null) {
            t.printStackTrace();
        }
    }
}
