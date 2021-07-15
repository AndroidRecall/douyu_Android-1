package com.mp.douyu.bean

data class InviteFriendsBean(
    val code: String? = "",
    val qrcode: String? = "",
    val url: String? = "",
    val users: List<UserList>? = listOf()
)

data class UserList(
    val create_time: String? = "",
    val phone: String? = "",
    val username: String? = ""
)