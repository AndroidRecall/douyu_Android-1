package com.swbg.mlivestreaming.bean

import com.google.gson.annotations.SerializedName

data class HomeDialogBean(
    val id: Int? = 0,
    val title: String? = "",
    @SerializedName("val") val content : String? = ""
)