package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class LiveStreamingBean(
    var id: Int? = 0,
    var avatar: String? =null,
    var user_name: String? =null,
    var comment: String? =null,
    var fans_count: Int? = 0,
    var cate_id: Int? = 0,
    var gift_count: Int? = 0,
    var level_id: Int? = 0,
    var status: Int? = 0,
    var user_id: Int? = 0,
    var hot: Int? = 0,
    var gold: Int? = 0,
    var name: String? = null,
    var title: String? = null,
    var update_time: String? = null,
    var create_time: String? = null,
    var image: String? = null,
    var push_url: String? = null,
    var GroupId: String? = null,
    var user: CommonUserBean? = null
    ):Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
