package com.mp.douyu.base


interface BaseViewInterface {
    fun showLoadingView(show: Boolean)
    fun showError(t: Throwable?)
}
