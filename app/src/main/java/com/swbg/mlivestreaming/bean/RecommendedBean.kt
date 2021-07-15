package com.swbg.mlivestreaming.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class RecommendedBean(var banner: List<BannerImageBean>? = listOf(), var videoList: List<VideoBean>? = listOf(), val cate: List<RecommendCateBean>? = listOf(), val headlines: String? = "") :
    Parcelable
data class RecommendedTestBean(
    @SerializedName("banner") val banner: List<BannerImageBean>? = listOf(),
    val cate: List<RecommendCateBean>? = listOf(),
    val headlines: String? = ""

)
@Keep
@Parcelize
data class RecommendCateBean(val id: Int? = 0, val title: String? = "", val videos: List<VideoBean>? = listOf()) :
    Parcelable
//
//data class RecommendedTestBean(
//    val banner: List<Banner>? = listOf(),
//    val cate: List<Cate>? = listOf(),
//    val headlines: String? = ""
//)
//
//data class Banner(
//    val cover: String? = "",
//    val id: Int? = 0,
//    val url: String? = ""
//)
//
//data class Cate(
//    val id: Int? = 0,
//    val title: String? = "",
//    val videos: List<Video>? = listOf()
//)
//
//data class Video(
//    val cover0: String? = "",
//    val cover1: String? = "",
//    val duration: String? = "",
//    val id: Int? = 0,
//    val title: String? = ""
//)