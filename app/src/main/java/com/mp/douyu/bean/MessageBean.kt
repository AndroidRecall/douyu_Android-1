package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class MessageBean(
    var type:String?=null,
    var msg:String?=null,
    var num:String?=null
):Parcelable {

}