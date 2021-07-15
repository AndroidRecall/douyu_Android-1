package com.mp.douyu.bean

import com.google.gson.annotations.SerializedName

class VideoPlayBean(var videoUrl: String? = "",var is_star: String? = "0",var is_like: String? = "0",var create_time: String? = "", val cate_id: Int? = 0, @SerializedName("cover1") val cover0: String? = "",
//    val cover1: String? = "",
    val designation: String? = "",    val limit: List<LimitVideoClear>? = listOf(),
    val id: Int? = 0, val like: Int? = 0, val play: Int? = 0, val summary: String? = "", val tags: String? = "", val title: String? = "")

data class LimitVideoClear(
    val id: Int? = 0,
    val play_times: Int? = 0,
    val title: String? = ""
)

data class VideoPlayLinkBean(
    val id: Int? = 0,
    val title: String? = "",
    val url: String? = ""
)

data class VideoDoenloadLink(
    val cover0: String? = "",
    val cover1: String? = "",
    val id: String? = "",
    val title: String? = "",
    val url: String? = ""
)