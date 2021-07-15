package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class HookRecordBean(
    var id:Int?=0,
    var nickname:String,
    var issue:Int?=0,
    var hook_id:Int = 0,
    var create_time:String?=null,
    var update_time:String?=null,
    var luck_number:Int?=0,
    var number:Int?=0,
    var user_id:Int?=0,
    var user:CommonUserBean?=null,
    var hook:HookBean?=null

):Parcelable {

}
@Parcelize
data class HookBean(
    var id:Int?=0,
    var image:String?=null,
    var title:String?=null,
    var comment:String?=null,
    var description:String?=null,
    var start_time:String?=null,
    var create_time:String?=null,
    var update_time:String?=null,
    var luck_number:String?=null,
    var n_content:String?=null,
    var n_title:String?=null,
    var step:String?=null,
    var status:Int?=0,
    var price:Int?=0,
    var total:Int?=0,
    var rest:Int?=0,
    var issue:Int?=0,
    var sort:Int?=0,
    var is_last_show:Int?=0
) : Parcelable