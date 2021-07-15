package com.swbg.mlivestreaming.http

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class SerializationIgnore(val value: Boolean = true)