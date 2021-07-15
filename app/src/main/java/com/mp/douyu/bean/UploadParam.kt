package com.mp.douyu.bean

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UploadParam(
    val parts: MutableList<MultipartBody.Part> = arrayListOf(),
    val bodyMap: MutableMap<String, RequestBody> = hashMapOf()
) {}