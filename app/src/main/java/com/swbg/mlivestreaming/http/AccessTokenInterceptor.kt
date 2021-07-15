package com.swbg.mlivestreaming.http
import com.swbg.mlivestreaming.provider.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

internal class AccessTokenInterceptor : Interceptor {
    /**
     * 不需要Token的接口
     */
    private val excludeUrls = HashSet<String>().apply {
        add("/user/perfection_user")
    }

    /**
     * 添加Token
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url
        val path = url.toUrl().path
        if (!excludeUrls.contains(path)) {
            val tokenProvider = TokenProvider.get()
            request = request.newBuilder()
                    .header("token", tokenProvider.accessToken)
                    .build()
        }
        return chain.proceed(request)
    }

    companion object {
        val instance = AccessTokenInterceptor()
    }
}