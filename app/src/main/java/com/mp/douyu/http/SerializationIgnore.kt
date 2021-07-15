package com.mp.douyu.http

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class SerializationIgnore(val value: Boolean = true)