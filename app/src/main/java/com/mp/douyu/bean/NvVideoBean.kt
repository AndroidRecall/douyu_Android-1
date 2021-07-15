package com.mp.douyu.bean

data class NvVideoBean(
    val current_page: Int? = 0,
    val `data`: List<VideoBean>? = listOf(),
    val last_page: Int? = 0,
    val per_page: Int? = 0,
    val total: Int? = 0
)