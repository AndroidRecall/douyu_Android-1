package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class EnterLiveRes(var id: Int? = 0, var anchor_id: Int? = 0, var gold: Int? = 0, var cate_id: Int? = 0, var duration: Int? = 0, var direction: Int? = 0, var avatar: String? = null, var title: String? = null, var image: String? = null, var pull_url: String? = null, var push_url: String? = null, var status: Int? = 0, var create_time: String? = null, var update_time: String? = null, var end_time: String? = null, var user_id: Int? = 0, var anchor: AnchorBean? = null) :
    Parcelable {

}