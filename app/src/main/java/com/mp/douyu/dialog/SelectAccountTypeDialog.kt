package com.mp.douyu.dialog

import android.content.Context
import com.lxj.xpopup.util.XPopupUtils
import com.mp.douyu.R
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.dialog_withdraw_account_type.view.*

class SelectAccountTypeDialog(context: Context,var listener:OnAccountTypeListener) : BaseBottomPopupView(context) {

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    private fun initView() {
        tv_bank.singleClick {
            listener.selectPos(0)
            dismiss()
        }
        tv_alipay.singleClick {
            listener.selectPos(1)
            dismiss()
        }
    }

    private fun loadData() {

    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_withdraw_account_type
    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * 0.35f).toInt()
    }

    private fun initKeyBoard() {
//        keyboard?.run {
//            setKeyboardStyle(ChatKeyboardLayout.Style.TEXT_EMOTICON)
//            setOnChatKeyBoardListener(object : ChatKeyboardLayout.SimpleOnChatKeyboardListener() {
//                override fun onSendButtonClicked(text: String?) {
//                    super.onSendButtonClicked(text)
//                    text?.let {
//                        if (text.isNotEmpty()) {
//                            commentViewModel.commentShortVideo(hashMapOf("video_id" to "$videoId",
//                                "content" to "$text"))
//                        }
//                    }
//                }
//
//                override fun onBackPressed() {
//                    super.onBackPressed()
//                    visibility = View.GONE
//                    hideLayout()
//                }
//
//                override fun onSoftKeyboardClosed() {
//                    super.onSoftKeyboardClosed()
//                    visibility = View.GONE
//                    hideLayout()
//                }
//            })
//        }
//        keyboard.showLayout()
    }
    interface OnAccountTypeListener {
        fun selectPos(type:Int)
    }
}