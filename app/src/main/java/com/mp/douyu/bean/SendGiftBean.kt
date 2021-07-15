package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class SendGiftBean(
    var num:String?=null,
    var giftBean: GiftBean? =null
):Parcelable {

}