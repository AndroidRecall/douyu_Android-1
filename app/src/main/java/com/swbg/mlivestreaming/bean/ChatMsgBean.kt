package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ChatMsgBean(
    var id:Int?=0,
    var nickname:String?=null,
    var content:String?=null,
    var create_time:String?=null,
    var update_time:String?=null,
    var luck_number:Int?=0,
    var number:Int?=0,
    var user_id:Int?=0,
    var user:CommonUserBean?=null,
    var itemViewType: Int = 0
):Parcelable {

}