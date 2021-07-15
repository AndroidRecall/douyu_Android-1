package com.swbg.mlivestreaming.view.popupwindow

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
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import kotlinx.android.synthetic.main.layout_dialog_charge_alarm.view.*
import java.util.*

class ChargeAlarmDialog : DialogFragment() {
    private var onClickListener: DialogInterface.OnClickListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = arguments
        val title = arguments?.getString("title")
        val message = arguments?.getString("message")
        val positive = arguments?.getString("positive")
        val negative = arguments?.getString("negative")
        return createDialog(this!!.requireActivity(), title, message, positive, negative, onClickListener)
    }

    fun setOnClickListener(onClickListener: DialogInterface.OnClickListener?): ChargeAlarmDialog {
        this.onClickListener = onClickListener
        return this
    }

    fun createDialog(context: Context, title: String?, message: String?, positive: String?, negative: String?, onClickListener: DialogInterface.OnClickListener?): AlertDialog {
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.layout_dialog_charge_alarm, null)
        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtPositive = view.findViewById<TextView>(R.id.txt_positive)
        val txtNegative = view.findViewById<TextView>(R.id.txt_negative)
        val txtMessage = view.findViewById<TextView>(R.id.txt_message)
        if (TextUtils.isEmpty(title)) {
            txtTitle.visibility = View.GONE
        } else {
            txtTitle.text = title
            if (title.equals("${BuildConfig.APP_NAME_}钱包充值成功")) {
                Glide.with(context).load(R.mipmap.charge_success_alarm_top).into(view.iv1)
            }
        }
        if (!TextUtils.isEmpty(positive)) {
            txtPositive.text = positive
        } else {
            txtPositive.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(message)) {
            txtMessage.text = message
        } else {
            txtMessage.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(negative)) {
            txtNegative.text = negative
        } else {
            txtNegative.visibility = View.GONE
        }
        txtPositive.setOnClickListener { v: View? ->
            dismiss()
            onClickListener?.onClick(dialog, Dialog.BUTTON_POSITIVE)
        }
        txtNegative.setOnClickListener { v: View? ->
            dismiss()
            onClickListener?.onClick(dialog, Dialog.BUTTON_NEGATIVE)
        }
        val alertDialog =
            AlertDialog.Builder(context, R.style.Dialog_Global).setView(view).create()
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
        return alertDialog
    }

    companion object {
        fun newInstance(title: String? = "", message: String?= "", positive: String?= "", negative: String?= ""): ChargeAlarmDialog {
            val dialog = ChargeAlarmDialog()
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