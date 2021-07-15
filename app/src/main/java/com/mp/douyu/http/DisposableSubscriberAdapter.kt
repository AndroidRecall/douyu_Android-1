package com.mp.douyu.http

import com.mp.douyu.utils.LogUtils
import github.leavesc.reactivehttp.viewmodel.IUIActionEvent
import io.reactivex.subscribers.DisposableSubscriber

abstract class DisposableSubscriberAdapter<T>(val iActionEvent: IUIActionEvent? = null,private val showLoading: Boolean = true) :
        DisposableSubscriber<T>() {
    private var hashCode: Int = 0

    init {
        val stackTrace = Thread.currentThread().stackTrace
        for (element in stackTrace) {
            if (element.className.startsWith("com.swbg.mlivestreaming") && element.className.contains(
                            "ViewModel")) {
                hashCode = element.hashCode()
                break
            }
        }
        LogUtils.e("init", "$hashCode")
    }

    override fun onStart() {
        super.onStart()
        iActionEvent?.showLoadingView(showLoading)
//        view.takeIf { showLoading }?.showLoadingView(true)
    }


    override fun onError(t: Throwable?) {
        iActionEvent?.let {
            it.showLoadingView(false)
            it.showError(t)
        }

//        view?.showLoadingView(false)
//        view?.showError(t)
    }

    override fun onComplete() {
        iActionEvent?.showLoadingView(false)
//        view?.showLoadingView(false)
    }

    

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as? DisposableSubscriberAdapter<*>
        return hashCode == that?.hashCode
    }

    override fun hashCode(): Int {
        return hashCode
    }
}
