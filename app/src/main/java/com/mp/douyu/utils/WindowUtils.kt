package com.mp.douyu.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import com.mp.douyu.MApplication.Companion.getApplicationInstances

object WindowUtils {
    fun dip2Px(context: Context, dip: Float): Int {
        return (context.resources.displayMetrics.density * dip + 0.5f).toInt()
    }

    fun getDisplayWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }


    fun dip2Px(dip: Float): Int {
        return (getApplicationInstances.getResources().getDisplayMetrics().density * dip + 0.5f).toInt()
    }


    fun isInputVisible(targetView: View): Boolean {
        val outRect = Rect()
        targetView.getWindowVisibleDisplayFrame(outRect)
        val context = targetView.context
        if (context is Activity) {
            val decorView = context.window.decorView
            return outRect.bottom < decorView.height * 2 / 3
        }
        return false
    }
}