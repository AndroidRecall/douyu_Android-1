package com.swbg.mlivestreaming.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyVipBean(
    val cover: String = "",
    val danmaku_level: Int = 0,
    val download: Int = 0,
    val horn: Int = 0,
    val id: Int = 0,
    val mount_icon: String? = "",
    val mount_text: String? = "",
    val play: Int = 0,
    val points: Int = 0,
    val title: String = "",
    val create_time: String = "",
    val update_time: String = "",
    val localImage: Int = 0
) : Parcelable