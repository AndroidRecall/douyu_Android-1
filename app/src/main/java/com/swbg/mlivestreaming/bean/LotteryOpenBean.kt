package com.swbg.mlivestreaming.bean

data class LotteryOpenBean(
    val countdown: Int? = 0,
    val number: String? = "",
    val result: String? = "",
    val game_id: String? = "",
    val title: String? = ""
)