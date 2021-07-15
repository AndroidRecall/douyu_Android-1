package com.mp.douyu.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NvDetailBean(
    val cover: String? = "",
    val cup: String? = "",
    val height: Int? = 0,
    val id: Int? = 0,
    val initial: String? = "",
    val measurements: String? = "",
    var isHeader : Boolean = false,
    val name: String? = ""
) : Parcelable