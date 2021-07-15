package com.mp.douyu.bean

data class PageBaseBean<T>(
    val current_page: Int? = 0,
    val `data`: ArrayList<T>? = arrayListOf(),
    val last_page: Int? = 0,
    val per_page: Int? = 0,
    val total: Int? = 0
)