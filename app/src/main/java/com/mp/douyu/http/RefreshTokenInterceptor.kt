package com.mp.douyu.http

import okhttp3.*

/**
 * 刷新Token拦截器
 * Created by HT on 2017/7/14.
 */

internal class RefreshTokenInterceptor : Interceptor {
    private val lock = Any()
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        if (response.code == 401 && response.message == "90002") {
        }
        return response
    }

}
