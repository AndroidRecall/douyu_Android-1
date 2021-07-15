package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class InviteCityBean(
    var content:String,
    var id:Int?=0,
    var image:String?=null,
    var title:String?=null,
    var comment:String?=null,
    var step:Int?=0,
    var price:Int?=0,
    var description:String?=null,
    var total:Int?=0,
    var luck_number:Int?=0,
    var rest:Int?=0,
    var issue:Int?=0,
    var is_last_show:Int?=0,
    var n_title:String?=null,
    var n_content:String?=null,
    var create_time:String?=null,
    var update_time:String?=null
):Parcelable {

}