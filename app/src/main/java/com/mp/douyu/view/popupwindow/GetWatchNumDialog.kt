package com.mp.douyu.view.popupwindow

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.mp.douyu.R
import java.util.*

class GetWatchNumDialog : DialogFragment() {
    private var onClickListener: DialogInterface.OnClickListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = arguments
        val title = arguments?.getString("title")
        val message = arguments?.getString("message")
        val positive = arguments?.getString("positive")
        val negative = arguments?.getString("negative")
        return createDialog(this!!.requireActivity(),
            title,
            message,
            positive,
            negative,
            onClickListener)
    }

    fun setOnClickListener(onClickListener: DialogInterface.OnClickListener?): GetWatchNumDialog {
        this.onClickListener = onClickListener
        return this
    }

    fun createDialog(context: Context, title: String?, message: String?, positive: String?, negative: String?, onClickListener: DialogInterface.OnClickListener?): AlertDialog {
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.layout_dialog_get_watch_num, null)
        view.apply {
            try {
                findViewById<View>(R.id.v_click_btn_1).setOnClickListener {
                    dismiss()
                    onClickListener?.onClick(dialog, 1)
                }
                findViewById<View>(R.id.v_click_btn_2).setOnClickListener {
                    dismiss()
                    onClickListener?.onClick(dialog, 2)
                }
                findViewById<View>(R.id.v_click_btn_3).setOnClickListener {
                    dismiss()
                    onClickListener?.onClick(dialog, 3)
                }
                findViewById<View>(R.id.v_click_btn_4).setOnClickListener {
                    dismiss()
                    onClickListener?.onClick(dialog, 4)
                }
                findViewById<View>(R.id.v_click_close).setOnClickListener {
//                    ToastUtils.showToast("你点击了v_click_close我")
                    dismiss()
                    onClickListener?.onClick(dialog, Dialog.BUTTON_NEGATIVE)
                }
    /*            findViewById<View>(R.id.iv_cancel_some).setOnClickListener {
                    ToastUtils.showToast("你点击了iv_cancel_some我")

//                    dismiss()
//                    onClickListener?.onClick(dialog, Dialog.BUTTON_NEGATIVE)
                }*/
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val txtTitle = view.findViewById<TextView>(R.id.tv_title)
        if (TextUtils.isEmpty(title)) {
            txtTitle.visibility = View.GONE
        } else {
            txtTitle.text = title
        }
        val alertDialog =
            AlertDialog.Builder(context, R.style.Dialog_Global_TRANSPORT).setView(view).create()
        alertDialog.setOnShowListener { dialog: DialogInterface? ->
            if (dialog is AlertDialog) {
                val window = Objects.requireNonNull(dialog.window)
                val attributes = window?.attributes
                val p = Point()
                window?.windowManager?.defaultDisplay?.getSize(p)
                attributes?.width = (p.x * 0.85f).toInt()
                window?.attributes = attributes
            }
        }
    /*    alertDialog.window?.findViewById<AppCompatImageButton>(R.id.iv_cancel_some)?.setOnClickListener {
            ToastUtils.showToast("你点击了iv_cancel_some")

        //                    dismiss()
        //                    onClickListener?.onClick(dialog, Dialog.BUTTON_NEGATIVE)
        }*/
        return alertDialog
    }

    companion object {
        fun newInstance(title: String? = "", message: String? = "", positive: String? = "", negative: String? = ""): GetWatchNumDialog {
            val dialog = GetWatchNumDialog()
            val arguments = Bundle()
            arguments.putString("title", title)
            arguments.putString("message", message)
            arguments.putString("positive", positive)
            arguments.putString("negative", negative)
            dialog.arguments = arguments
            return dialog
        }
    }
}