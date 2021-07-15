package com.swbg.mlivestreaming.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class VideoBean(@SerializedName("title") var videoTitle: String? = "", @SerializedName("video") var videoUrl: String? = "", @SerializedName(
    "cover1") var faceImageUrl: String? = "", val duration: String? = "",
//    val cover1: String? = "",
    val id: Int? = 0, val play: Int? = 0, val summary: String? = "", val tags: String? = "", val designation: String? = "",
    var isSelect: Boolean? = false,var btnShow: Boolean? = false) :
    Parcelable {}
