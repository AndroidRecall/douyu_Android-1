package com.swbg.mlivestreaming.bean

data class PayBean(
    val amount_list: List<String>? = listOf(),
    val icon: String? = "",
    val id: Int? = 0,
    var isSelect :Boolean = false,
    val title: String? = ""
)

data class PayListBean(
    val pay_list: List<PayBean>? = listOf(),
    val tutorial_text: String? = "",
    val tutorial_video: String? = ""
)