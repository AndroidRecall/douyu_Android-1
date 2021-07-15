package com.swbg.mlivestreaming.utils

import android.content.Context
import com.swbg.mlivestreaming.MApplication.Companion.getApplicationInstances

object DisplayUtils {

    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
    @JvmStatic
    fun dp2px(dipValue: Float): Int {
        val scale: Float = getApplicationInstances.getResources().getDisplayMetrics().density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}