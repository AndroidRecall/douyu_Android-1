package com.mp.douyu.utils

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.mp.douyu.MApplication.Companion.getApplicationInstances
import com.mp.douyu.R

object ToastUtils {


    private val bottomDelegateImpl by lazy {
        BottomDelegateImpl()
    }

    private val centerDelegateImpl by lazy {
        CenterDelegateImpl()
    }


    fun showToast(message: String) {
        showToast(message, false)
    }

    fun showToastL(message: String) {
        showToast(message = message, success = false, duration = Toast.LENGTH_LONG)
    }

    @SuppressLint("ResourceType")
    fun showToast(message: String, success: Boolean, duration: Int = Toast.LENGTH_SHORT) {
        showToast(message, if (success) R.mipmap.icon_success else R.mipmap.icon_failure, duration)
    }

    @SuppressLint("ResourceType")
    fun showToast(message: String, success: Boolean) {
        showToast(message, if (success) R.mipmap.icon_success else R.mipmap.icon_failure)
    }

    /**
     * 显示Toast
     */
    @SuppressLint("SetTextI18n")
    fun showToast(message: String, @StringRes strRes: Int, duration: Int = Toast.LENGTH_SHORT) {
        when {
            message.isEmpty() -> Unit
            message.length in 1..6 -> showToast(centerDelegateImpl, message, strRes, duration)
            else -> showToast(centerDelegateImpl, message, strRes, duration)/*showToast(bottomDelegateImpl, message, strRes, duration)*/
        }
    }

    private fun showToast(delegate: ToastDelegate, message: String, @StringRes strRes: Int, duration: Int) {
        delegate.show(message, strRes, duration)
    }
}

internal interface ToastDelegate {
    fun show(message: String, @StringRes strRes: Int, duration: Int)
}

@SuppressLint("InflateParams")
internal class BottomDelegateImpl : ToastDelegate {

    private var preMessage = ""
    private var preTime: Long = 0
    private val bottomToast by lazy {
        Toast(getApplicationInstances.applicationContext).apply {
            view = LayoutInflater.from(getApplicationInstances).inflate(R.layout.view_toast, null)
                .apply {
//                background = ContextCompat.getDrawable(context, R.drawable.bottom_toast_background)
                }
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, WindowUtils.dip2Px(228f))
            duration = Toast.LENGTH_SHORT
        }
    }

    @SuppressLint("ResourceType")
    override fun show(message: String, strRes: Int, duration: Int) {
        if (preMessage == message) {
            if (System.currentTimeMillis() - preTime > 1000) {
                bottomToast.duration = duration
                bottomToast.show()
            }
        } else {
            preMessage = message
            val toastView = bottomToast.view.findViewById<TextView>(R.id.txtToast)
            val ivImage = bottomToast.view.findViewById<ImageView>(R.id.iv_image)
            Glide.with(ivImage.context).load(ContextCompat.getDrawable(ivImage.context, strRes))
                .into(ivImage)
            toastView.text = message
            bottomToast.show()
        }
        preTime = System.currentTimeMillis()
    }
}

@SuppressLint("InflateParams")
internal class CenterDelegateImpl : ToastDelegate {
    private var preMessage = ""
    private var preTime: Long = 0

    private val centerToast by lazy {
        Toast(getApplicationInstances).apply {
            view = LayoutInflater.from(getApplicationInstances).inflate(R.layout.view_toast, null)
                .apply {
//                background = ContextCompat.getDrawable(context, R.drawable.center_toast_background)
                }
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_SHORT
        }
    }


    @SuppressLint("SetTextI18n", "ResourceType")
    override fun show(message: String, strRes: Int, duration: Int) {
        if (preMessage == message) {
            if (System.currentTimeMillis() - preTime > 1000) {
                centerToast.duration = duration
                centerToast.show()
            }
        } else {
            preMessage = message
            val toastView = centerToast.view.findViewById<TextView>(R.id.txtToast)
            val ivImage = centerToast.view.findViewById<ImageView>(R.id.iv_image)
            Glide.with(ivImage.context).load(ContextCompat.getDrawable(ivImage.context, strRes))
                .into(ivImage)
            toastView.apply {
//                text = "\n\n$message"
                text = message
//                setIcon(0, 32, strRes)
//                background = ContextCompat.getDrawable(getApplicationInstances,strRes)
            }
            centerToast.show()
        }
        preTime = System.currentTimeMillis()
    }
}
