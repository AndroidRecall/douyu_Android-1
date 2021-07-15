package com.mp.douyu.http.exception

import android.app.Activity
import com.mp.douyu.utils.ToastUtils

internal class ProjectDeleteExceptionHandler : IExceptionHandler {
    override fun handle(activity: Activity, t: Throwable): Boolean {
        return when (t) {
            is Http400Exception -> {
                ToastUtils.showToast(t.errorMessage, false)
                true
            }
            else -> false
        }
    }
}
