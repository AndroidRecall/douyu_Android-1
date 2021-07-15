package com.swbg.mlivestreaming.dialog

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.core.CenterPopupView
import github.leavesc.reactivehttp.viewmodel.IUIActionEventObserver
import kotlinx.coroutines.CoroutineScope

open class BaseCenterPopupView(context: Context) : CenterPopupView(context) , LifecycleOwner, IUIActionEventObserver {
    private lateinit var lifecycleRegistry: LifecycleRegistry
    override fun showLoadingView(show: Boolean) {

    }

    override fun showError(t: Throwable?) {
    }

    override fun dismissLoading() {
    }

    override fun showToast(msg: String) {
    }

    override fun finishView() {
    }

    override fun onCreate() {
        super.onCreate()
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }
    override val lContext: Context?
        get() = context
    override val lLifecycleOwner: LifecycleOwner
        get() = this
    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleScope

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}