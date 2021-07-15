package com.swbg.mlivestreaming.base

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.swbg.mlivestreaming.LoadingFragment
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.http.exception.ExceptionHandler
import com.swbg.mlivestreaming.utils.ToastUtils
import github.leavesc.reactivehttp.viewmodel.IUIActionEventObserver
import kotlinx.coroutines.CoroutineScope

open class BaseFullBottomSheetFragment : BottomSheetDialogFragment(), IUIActionEventObserver {
    protected var isLoading = false
    protected val loading by lazy {
        LoadingFragment()
    }

    /**
     * 顶部向下偏移量
     */
    var topOffset = 0
    var behavior: BottomSheetBehavior<FrameLayout>? = null
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return if (context == null) {
            super.onCreateDialog(savedInstanceState)
        } else BottomSheetDialog(context!!, R.style.TransparentBottomSheetStyle)
    }

    override fun onStart() {
        super.onStart()
        // 设置软键盘不自动弹出
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val dialog = dialog as BottomSheetDialog?
        val bottomSheet = dialog!!.delegate.findViewById<FrameLayout>(R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val layoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            behavior = BottomSheetBehavior.from(bottomSheet)
            // 初始为展开状态
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,height)
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
    }// 使用Point已经减去了状态栏高度

    /**
     * 获取屏幕高度
     *
     * @return height
     */
    open val height by lazy {
            var height = 1920
        context?.let {
            val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            if (wm != null) {
                // 使用Point已经减去了状态栏高度
                wm.defaultDisplay.getSize(point)
                height = point.y - topOffset
            }
        }

        height

    }


    override val lContext: Context?
        get() = context
    override val lLifecycleOwner: LifecycleOwner
        get() = this

    override fun showLoadingView(show: Boolean) {
        if (isLoading != show) {
            isLoading = show
            activity?.runOnUiThread {
                when {
                    show -> when {
                        loading.isAdded -> loading.show()
                        !activity?.isDestroyed!! && !activity?.isFinishing!! -> activity?.window?.decorView?.postDelayed(
                            { showLoadingView(isLoading) },
                            250)
                    }
                    else -> loading.dismiss()
                }
            }
        }
    }

    override fun showError(t: Throwable?) {
        val activity = activity
        if (t != null && activity != null) {
            activity.runOnUiThread {
                ExceptionHandler.handle(activity, t)
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
        activity?.finish()
    }

    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleSupportedScope

}