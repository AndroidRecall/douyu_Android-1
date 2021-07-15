package com.mp.douyu.base

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mp.douyu.http.exception.ExceptionHandler
import com.mp.douyu.utils.ToastUtils
import github.leavesc.reactivehttp.viewmodel.IUIActionEventObserver
import kotlinx.coroutines.CoroutineScope

abstract class MBaseFragment :BaseFragment(), IUIActionEventObserver {

    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleScope

    override val lContext: Context?
        get() = context

    override val lLifecycleOwner: LifecycleOwner
        get() = this

//    private var loadDialog: ProgressDialog? = null


    override fun showError(t: Throwable?) {
        val activity = activity
        if (t != null && activity != null) {
            activity.runOnUiThread {
                ExceptionHandler.handle(activity, t)
            }
        }
    }

    @Synchronized
    override fun showLoadingView(show: Boolean) {
        if (isLoading != show) {
            isLoading = show
            activity?.runOnUiThread {
                when {
                    show -> when {
                        loading.isAdded ->
                            loading.show()
                        !activity?.isDestroyed!! && !activity?.isFinishing!! ->
                            activity?.window?.decorView?.postDelayed({ showLoadingView(isLoading) }, 250)
                    }
                    else -> loading.dismiss()
                }
            }
        }
      /*  isLoading = show
        when {
            isHidden -> {
                LogUtils.e(this::class.java.simpleName, "($loading) is hidden")
            }
            show -> {
                when {
                    loading.isAdded -> {
                        loading.show()
                    }
                    isAdded && !isDetached -> view?.postDelayed(
                        {
//                            showLoadingView(isLoading)
                        }, 50)
                }
            }
            else -> {
                loading.dismiss()
            }
        }*/
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
        activity?.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }


}