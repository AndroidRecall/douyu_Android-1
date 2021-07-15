package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class PivotBean(
    var id: Int?,
    var uid: Int?,
    var video_id: Int?,
    var post_id: Int?,
    var user_id: Int?,
    var anchor_id: Int?,
    var create_time:String?=null,
    var update_time:String?=null
) : Parcelable
