package com.swbg.mlivestreaming.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageBean(
    var id: Int = 0,
    var post_id: Int = 0,
    var url: String? = null,
    var width: Int = 0,
    var height: Int = 0
) : Parcelable {


}