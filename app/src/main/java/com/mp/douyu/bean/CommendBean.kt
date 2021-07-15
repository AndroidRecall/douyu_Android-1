package com.mp.douyu.bean

data class CommendBean(
    val current_page: Int? = 0,
    val `data`: List<CommendData>? = listOf(),
    val last_page: Int? = 0,
    val per_page: Int? = 0,
    val total: Int? = 0
)

data class CommendData(
    val content: String? = "",
    val id: Int? = 0,
    val like: Int? = 0,
    val pid: Int? = 0,
    val reply: Int? = 0,
    val user: UserCommend? = UserCommend(),
    val user_id: Int? = 0,
    val video_id: Int? = 0,
    val data_i:MutableList<CommendData> = arrayListOf()
)

data class UserCommend(
    val avatar: String? = "",
    val id: Int? = 0,
    val nickname: String? = ""
)