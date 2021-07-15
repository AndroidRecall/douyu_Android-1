package com.swbg.mlivestreaming.dialog

import android.content.Context
import com.lxj.xpopup.core.CenterPopupView
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.live_dialog_transfer.view.*

/**
 * 转账
 */
class TransferDialog(context: Context) : CenterPopupView(context) {
    override fun onCreate() {
        super.onCreate()
        iv_dismiss.singleClick {
            dismiss()
        }

    }
    override fun getImplLayoutId(): Int {
        return R.layout.live_dialog_transfer
    }

}