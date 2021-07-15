package com.swbg.mlivestreaming.utils

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object DateUtil {
    /**
     * 将当前日期转换为一定格式的字符串
     */
    fun formatDate(time: Date?, format: String?): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            formatDate(Calendar.getInstance(Locale.CHINA).time, format)
        } else {
           Random(10000).nextLong().toString()
        }
    }
}