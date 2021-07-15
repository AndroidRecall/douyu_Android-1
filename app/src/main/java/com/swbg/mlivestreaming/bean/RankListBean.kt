package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class RankListBean(
    var total:Int?=0,
    var per_page:Int?=0,
    var current_page:Int?=0,
    var last_page:Int?=0,
    var data:MutableList<RankBean>? =null
):Parcelable {

}