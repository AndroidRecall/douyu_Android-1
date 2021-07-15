package com.mp.douyu.dialog

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.util.XPopupUtils
import com.mp.douyu.R
import com.mp.douyu.adapter.AccountAdapter
import com.mp.douyu.bean.AccountRes
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.dialog_withdraw_account.view.*

class SelectAccountDialog(context: Context, var list: List<AccountRes> = arrayListOf(),var listener:OnAccountListener) : BaseBottomPopupView(context) {
    private val pageSize = 10
    private var currentPage = 1
    val mAdapter by lazy {
        AccountAdapter({}, context).apply {
            setListener(object : AccountAdapter.OnAccountListener {

                override fun onSelectClick(position: Int) {
                   list.forEachIndexed { index, accountRes ->
                       if (position == index) {
                           accountRes.isSelect = true
                       } else {
                           accountRes.isSelect = false
                       }
                   }
                    notifyDataSetChanged()
                    listener?.selectPos(position)
                    dismiss()
                }
            })
        }
    }

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    private fun initView() {

        recycler_view?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.refresh(list,null)
        tv_more.singleClick {
        listener?.addAccount()
            dismiss()
        }
        if (list.size > 2) {
            tv_more.visibility = View.GONE
        }
        initKeyBoard()
        loadData()
    }

    private fun loadData() {

    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_withdraw_account
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
    interface OnAccountListener {
        fun selectPos(position:Int)
        fun addAccount()
    }
}