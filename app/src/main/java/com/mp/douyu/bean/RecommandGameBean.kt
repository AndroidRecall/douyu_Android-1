package com.mp.douyu.bean

data class RecommandGameBean(
    val games: List<RecommendedGameDeatailBean>? = listOf(),
    val cover: String? = "",
    val id: Int? = 0,
    val title: String? = ""
)
data class RecommendedGameDeatailBean(
    val cover: String? = "",
    val id: Int? = 0,
    val title: String? = "",
    val url: String? = ""
)