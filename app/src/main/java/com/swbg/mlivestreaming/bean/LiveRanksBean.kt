package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class LiveRanksBean(
    var day:RankListBean?=null,
    var week:RankListBean?=null,
    var month:RankListBean?=null
):Parcelable {

}