package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class HookDescBean(
    var id:Int?=0,
    var nickname:String?=null,
    var tip:String?=null,
    var issue:Int?=0,
    var hook_id:Int = 0,
    var create_time:String?=null,
    var update_time:String?=null,
    var luck_number:Int?=0,
    var number:Int?=0,
    var user_id:Int?=0,
    var user:CommonUserBean?=null,
    var itemViewType: Int = 0
):Parcelable {

}