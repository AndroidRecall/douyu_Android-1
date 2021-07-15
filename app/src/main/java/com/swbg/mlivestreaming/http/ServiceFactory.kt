package com.swbg.mlivestreaming.http

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 设置retrofit配置参数，创建service的工厂类
 */

class ServiceFactory private constructor() {

    init {
        throw UnsupportedOperationException("new ServiceFactory " + "instance.")
    }

    companion object {


        private val TAG = "okHttp"


        /**
         * 复用retrofit
         */
        @Synchronized
        fun <T> create(clazz: Class<T>): T {
            return retrofit.create(clazz)
        }

        /**
         * 重新创建retrofit对象
         */
        @Synchronized
        fun <T> createA(clazz: Class<T>): T {
            return newRetrofit().create(clazz)
        }

        /**
         * 主网络请求线程池
         */
        private val RETROFIT by lazy {
            Retrofit.Builder().client(httpClient).baseUrl(
                    BuildConfig.SERVER_URL).addConverterFactory(
                    NobodyConverterFactory.create()).addConverterFactory(
                    GsonConverterFactory.create(
                            GsonBuilder().addSerializationExclusionStrategy(
                                    SerializationIgnoreStrategy()).registerTypeAdapter(
                                    object :
                                            TypeToken<HashMap<String, Any>>() {
                                    }.type,
                                    MapTypeAdapter()).create())).addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()).build()
        }

        private val retrofit: Retrofit
            @Synchronized get() {
                return RETROFIT
            }

        /**
         * 为了不影响正常业务请求，避开主网络请求线程池，主要用于上传业务。
         */
        @Synchronized
        private fun newRetrofit(): Retrofit {
            return Retrofit.Builder().client(httpClient).baseUrl(
                    BuildConfig.SERVER_URL).addConverterFactory(
                    NobodyConverterFactory.create()).addConverterFactory(
                    GsonConverterFactory.create(GsonBuilder().registerTypeAdapter(
                            object : TypeToken<HashMap<String, Any>>() {

                            }.type, MapTypeAdapter()).create())).addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create()).build()
        }

        val httpClient: OkHttpClient
            get() = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15,
                    TimeUnit.SECONDS).writeTimeout(
                    15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(AccessTokenInterceptor.instance)
                    .addInterceptor(RefreshTokenInterceptor())
                    .addInterceptor(ErrorPreviewInterceptor())
                    .addInterceptor(HttpLoggingInterceptor(
                            HttpLoggingInterceptor.Logger { message ->
                                LogUtils.i(TAG, message)
                            }).setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    }

}
