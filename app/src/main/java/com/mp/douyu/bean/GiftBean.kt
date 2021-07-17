package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class GiftBean(
    var id:Int?=0,
    var image:String?=null,
    var animation:String?=null,
    var animation_type:Int?=0,
    var price:Int?=0,
    var type:Int? = 0,
    var duration:Long? = 0,
    var sort:Int? = 0,
    var title:String?=null,
    var comment:String?=null,
    var create_time:String?=null,
    var update_time:String?=null,
    var isSelect:Boolean = false
):Parcelable {

}