package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PublishItemBean(
    var id: Int? = 0,
    var title: String? = null,
    var text: String? = null,
    var itemType:Int=0,
    var localMedia:LocalMedia?=null
    ): Parcelable {
    companion object{
        const val PUBLISH_TYPE_IMG = 1
        const val PUBLISH_TYPE_TXT = 2
    }
}