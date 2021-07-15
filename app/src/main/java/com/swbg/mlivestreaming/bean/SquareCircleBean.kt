package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class SquareCircleBean(
    var id: Int? = 0,
    var title: String? = null,
    var url: String? = null,
    var banner: String? = null,
    var create_time: String? = null,
    var update_time: String? = null,
    var explain: String? = null,
    var user_count: Int? = 0,
    var post_count: Int? = 0,
    var description: String? = null,
    var avatar: String? = null,
    var adv_img: String? = null,
    var adv_url: String? = null,
    var adv_title: String? = null,
    var adv_status: Int? = 0,
    var is_join: Int? = 0,
    var isHorizontal: Boolean? = true
    ):Parcelable {

}