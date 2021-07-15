package com.swbg.mlivestreaming.bean

import java.util.concurrent.atomic.AtomicLong

data class PageBaseBean<T>(
    val current_page: Int? = 0,
    val `data`: ArrayList<T>? = arrayListOf(),
    val last_page: Int? = 0,
    val per_page: Int? = 0,
    val total: Int? = 0
)