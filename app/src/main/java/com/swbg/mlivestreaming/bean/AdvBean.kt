package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class AdvBean(
    var id:Int?=0,
    var type:Int?=0,
    var title:String?=null,
    var cover:String?=null,
    var url:String?=null,
    var expire:String?=null,
    var tag:String?=null,
    var html:String?=null,
    var create_time:String?=null,
    var update_time:String?=null
):Parcelable {

}