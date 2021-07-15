package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class MineVideoBean(
    var id: Int? = 0,
    var designation: String? =null,
    var title: String? =null,
    var cover0: String? =null,
    var cover1: String? =null,
    var tags: String? =null,
    var hash: String? =null,
    var duration: String? =null,
    var create_time: String? =null,
    var update_time: String? =null,
    var cate_id: Int? = 0,
    var special_id: Int? = 0,
    var cast_ids: String? = null,
    var like: Int? = 0,
    var play: Int? = 0,
    var status: Int? = 0,
    var pivot:PivotBean?=null,
    var itemViewType :Int =0
    ):Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
