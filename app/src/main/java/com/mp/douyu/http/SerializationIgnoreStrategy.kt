package com.mp.douyu.http

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class SerializationIgnoreStrategy: ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?) = false

    override fun shouldSkipField(f: FieldAttributes): Boolean {
        val annotation = f.getAnnotation(SerializationIgnore::class.java)
        return annotation != null && annotation.value
    }
}