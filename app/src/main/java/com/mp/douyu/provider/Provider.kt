package com.mp.douyu.provider

interface Provider {
    fun update(field: String, any: Any)
    fun clear()
}