package com.mp.douyu.bean

import com.google.gson.annotations.SerializedName

class SpecialTopicBean(var title : String? = "",var imageUrl : String? = "",var videoNum : String? = "",var videoUrl : String? = "",
    var videoDetail : String? = "",@SerializedName("banner") var bannersList : List<BannerImageBean>? = null
    ,    val special: List<SpecialBean>? = listOf())
