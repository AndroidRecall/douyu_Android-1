package com.swbg.mlivestreaming.base

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.swbg.mlivestreaming.http.exception.ExceptionHandler
import com.swbg.mlivestreaming.utils.LogUtils
import com.swbg.mlivestreaming.utils.ToastUtils
import github.leavesc.reactivehttp.viewmodel.IUIActionEventObserver
import kotlinx.coroutines.CoroutineScope

abstract class MBaseActivity :BaseActivity(), IUIActionEventObserver {

    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleScope

    override val lContext: Context?
        get() = this

    override val lLifecycleOwner: LifecycleOwner
        get() = this



    //loading
    override fun showLoadingView(show: Boolean) {
        if (isLoading != show) {
            isLoading = show
            runOnUiThread {
                when {
                    show -> when {
                        loading.isAdded -> loading.show()
                        !isDestroyed && !isFinishing -> window.decorView.postDelayed({ showLoadingView(isLoading) }, 250)
                    }
                    else -> loading.dismiss()
                }
            }
        }
    }

    override fun dismissLoading() {
        showLoadingView(false)
    }

    override fun showToast(msg: String) {
        if (msg.isNotBlank()) {
            ToastUtils.showToast(msg)
        }
    }

    override fun finishView() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }
    override fun showError(t: Throwable?) {
        if (t != null) {
            runOnUiThread {
                ExceptionHandler.handle(this, t)
            }
        }
    }
}