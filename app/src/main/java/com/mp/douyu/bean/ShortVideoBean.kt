package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ShortVideoBean(
    var id: Int? = 0,
    var uid: Int? = 0,
    var status: Int? = 0,
    var title: String? = null,
    var url: String? = null,
    var image: String? = null,
    var update_time: String? = null,
    var hash: String? = null,
    var comment_count: Int? = 0,
    var share_count: Int? = 0,
    var like_count: Int? = 0,
    var is_follow: Int? = 0,
    var is_recommend: Int? = 0,
    var publish: Int? = 0,
    var is_like_short_video: Int? = 0,
    var user: CommonUserBean? = null
) : Parcelable

