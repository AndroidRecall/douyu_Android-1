package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class AnchorRuleBean (
    var id: Int?=0,
    var level:String?=null,
    var time:String?=null,
    var name:String?=null,
    var rate:String?=null,
    var rmb_rate:String?=null,
    var experience: Int?=0,
    var create_time:String?=null,
    var update_time:String?=null
) : Parcelable