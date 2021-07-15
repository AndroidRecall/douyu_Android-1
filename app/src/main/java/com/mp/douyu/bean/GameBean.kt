package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class GameBean(
    var id: Int? = 0,
    var iconId: Int? = 0,
    var itemViewType: Int = 0
) : Parcelable {

}