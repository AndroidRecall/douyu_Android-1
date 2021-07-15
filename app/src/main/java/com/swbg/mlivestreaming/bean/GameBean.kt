package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class GameBean(
    var id: Int? = 0,
    var iconId: Int? = 0,
    var itemViewType: Int = 0
) : Parcelable {

}