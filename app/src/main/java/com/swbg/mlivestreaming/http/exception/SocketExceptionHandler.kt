package com.swbg.mlivestreaming.http.exception

import android.app.Activity
import com.swbg.mlivestreaming.utils.ToastUtils
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import java.net.UnknownServiceException

internal class SocketExceptionHandler : IExceptionHandler {
    override fun handle(activity: Activity?, t: Throwable): Boolean {
        if (t is SocketException || t is HttpException || t is InterruptedIOException || t is UnknownHostException || t is UnknownServiceException) {
            ToastUtils.showToast("网络异常，请检查网络连接", false)
            return true
        }
        return false
    }
}
