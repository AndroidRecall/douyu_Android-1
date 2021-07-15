package com.mp.douyu.dialog

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import cn.hadcn.keyboard.ChatKeyboardLayout
import cn.hadcn.keyboard.ChatKeyboardLayout.SimpleOnChatKeyboardListener
import com.mp.douyu.LoadingFragment
import com.mp.douyu.R
import com.mp.douyu.ui.video.comment.CommentViewModel
import github.leavesc.reactivehttp.viewmodel.IUIActionEventObserver
import kotlinx.coroutines.CoroutineScope

class ShortVideoCommentDialog(context: Context, listener: OnCommendListener?) :
    AppCompatDialogFragment(), IUIActionEventObserver {
    private var dialogView: View? = null
    private var videoId: Int = 0
    private val mChatKeyboard by lazy {
        dialogView?.findViewById<ChatKeyboardLayout>(R.id.chat_key_board)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.dialog_input_text_msg, container, false)

        dialog?.window?.apply {

        setWindowAnimations(R.style.main_menu_animstyle)
        }
        init(dialogView)
        return dialogView
    }

    private fun init(dialogView: View?) {
        initKeyboard()
    }
    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val params = window.attributes
            params.gravity = Gravity.BOTTOM
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.addFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            )
            window.attributes = params
            window.setLayout(params.width, params.height)
        }
    }

    private fun initKeyboard() {
        mChatKeyboard?.setKeyboardStyle(ChatKeyboardLayout.Style.TEXT_EMOTICON)
        mChatKeyboard?.setOnChatKeyBoardListener(object : SimpleOnChatKeyboardListener() {
            override fun onSendButtonClicked(text: String) {
                super.onSendButtonClicked(text)
                text?.let {
                    if (text.isNotEmpty()) {
                        commentViewModel.commentShortVideo(hashMapOf("video_id" to "$videoId",
                            "content" to "$text"))
                    }
                }
            }

            override fun onBackPressed() {
                super.onBackPressed()
            }

            override fun onSoftKeyboardClosed() {
                super.onSoftKeyboardClosed()
            }
        })
    }

    private fun setLayout() {}

    private val commentViewModel by getViewModel(CommentViewModel::class.java) {

        _commentShortVideoResult.observe(it, Observer {

            it?.let {
                //评论成功
                mChatKeyboard?.clearInputContent()
                mChatKeyboard?.hideKeyboard()

            }
        })
    }
    override val lContext: Context?
        get() = context
    override val lLifecycleOwner: LifecycleOwner
        get() = this

    protected var isLoading = false
    protected val loading by lazy {
        LoadingFragment()
    }

    override fun showLoadingView(show: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showError(t: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun dismissLoading() {
        TODO("Not yet implemented")
    }

    override fun showToast(msg: String) {
        TODO("Not yet implemented")
    }

    override fun finishView() {
        TODO("Not yet implemented")
    }

    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleSupportedScope

    interface OnCommendListener {
        fun onSendSuccess()
    }
}