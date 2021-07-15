package com.swbg.mlivestreaming.bean

data class GetGameLinkBean(
    val alert: Alert? = null,
    val url: String? = ""
)

data class Alert(
    val balance: String? = "0",
    val title: String? = "",
    val game_balance: String? = "0"
)