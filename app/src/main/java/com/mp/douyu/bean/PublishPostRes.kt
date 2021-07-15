package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PublishPostRes(
    var uid: Int? = 0,
    var content: String? = null,
    var url: String? = null,
    var create_time: String? = null,
    var update_time: String? = null,
    var id:String?=null
) : Parcelable {}