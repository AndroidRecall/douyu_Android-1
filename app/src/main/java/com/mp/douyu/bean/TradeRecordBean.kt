package com.mp.douyu.bean

data class TradeRecordBean(
    val current_page: Int? = 0,
    val `data`: List<TradeRecordItemBean>? = listOf(),
    val last_page: Int? = 0,
    val per_page: Int? = 0,
    val total: Int? = 0
)

data class TradeRecordItemBean(
    val amount: String? = "",
    val create_time: String? = "",
    val id: String? = "",
    val remarks: String? = "",
    val type: Int? = 0
)