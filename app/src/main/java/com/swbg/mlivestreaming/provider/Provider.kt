package com.swbg.mlivestreaming.provider

interface Provider {
    fun update(field: String, any: Any)
    fun clear()
}