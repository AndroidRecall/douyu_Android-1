package com.swbg.mlivestreaming.base


interface BaseViewInterface {
    fun showLoadingView(show: Boolean)
    fun showError(t: Throwable?)
}
