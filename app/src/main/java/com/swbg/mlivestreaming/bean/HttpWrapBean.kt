package com.swbg.mlivestreaming.bean

import com.google.gson.annotations.SerializedName
import github.leavesc.reactivehttp.bean.IHttpWrapBean

/**
 * @Author: leavesC
 * @Date: 2020/4/30 15:22
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
class HttpWrapBean<T>(
        @SerializedName("status") var code: Int = 0,
        @SerializedName("message") var message: String? = null,
        @SerializedName("data", alternate = ["forecasts"]) var data: T) : IHttpWrapBean<T> {

    companion object {

        fun <T> success(data: T): HttpWrapBean<T> {
            return HttpWrapBean(200, "success", data)
        }

        fun <T> failed(data: T): HttpWrapBean<T> {
            return HttpWrapBean(-200, "服务器停止维护了~~", data)
        }

    }

    override val httpCode: Int
        get() = code

    override val httpMsg: String
        get() = message ?: ""

    override val httpData: T
        get() = data

    override val httpIsSuccess: Boolean
        get() = code == 200 || message == "OK"

    override fun toString(): String {
        return "HttpResBean(code=$code, message=$message, data=$data)"
    }

}