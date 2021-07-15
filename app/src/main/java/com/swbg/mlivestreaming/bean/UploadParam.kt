package com.swbg.mlivestreaming.bean

import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.ArrayList

data class UploadParam(
    val parts: MutableList<MultipartBody.Part> = arrayListOf(),
    val bodyMap: MutableMap<String, RequestBody> = hashMapOf()
) {}