package com.mp.douyu.http

import com.mp.douyu.http.exception.Http400Exception
import com.mp.douyu.http.exception.Http401Exception
import com.mp.douyu.http.exception.HttpResultException
import com.mp.douyu.http.exception.MException
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

/**
 * 网络请求统一包装异常并重新抛出
 */
internal class ErrorPreviewInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())
        val code = response.code
        if (code < 200 || code > 300) {
            val responseBody = response.body
            if (responseBody != null) {
                val body = responseBody.string()
                val error = try {
                    val jsonObject = JSONObject(body)
                    jsonObject.getInt("error")
                } catch (e: JSONException) {
                    MException.CODE_NONE
                }

                val message = try {
                    val jsonObject = JSONObject(body)
                    if (jsonObject.has("message") && jsonObject.getString("message").isNotEmpty()) {
                        jsonObject.getString("message")
                    } else if (jsonObject.has("error_desc") && jsonObject.getString("error_desc").isNotEmpty()) {
                        jsonObject.getString("error_desc")
                    } else {
                        jsonObject.getString("error_description")
                    }
                } catch (e: JSONException) {
                    ""
                }


                when (code) {
                    400 -> throw Http400Exception(error, message, body)
                    401-> throw Http401Exception(error, message, body)
                    in 402 .. 500 -> throw Http400Exception(error, message, body)
                    /*401 -> when (error) {
                        //Token失效，需要刷新，不能抛出异常
                        90002 -> {
                            return response.newBuilder().message("90002").build()
                        }
                        else -> throw Http401Exception(error, message, body)
                    }*/
                    else -> throw HttpResultException(code, message)
                }
            }
        }
        return response
    }
}
