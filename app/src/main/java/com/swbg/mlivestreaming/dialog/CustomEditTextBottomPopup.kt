package com.swbg.mlivestreaming.dialog

import android.content.Context
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import com.lxj.xpopup.core.BottomPopupView
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.http.DisposableSubscriberAdapter
import com.swbg.mlivestreaming.ui.video.comment.CommentViewModel
import com.swbg.mlivestreaming.utils.ToastUtils
import kotlinx.android.synthetic.main.custom_edittext_bottom_popup.view.*
import kotlinx.android.synthetic.main.fragment_video_comment.*

/**
 * Description: 自定义带有输入框的Bottom弹窗
 * Create by dance, at 2019/2/27
 */
class CustomEditTextBottomPopup(context: Context, var videoId: Int? = 0) :
    BaseBottomPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.custom_edittext_bottom_popup
    }

    override fun onCreate() {
        super.onCreate()
        et_content.apply {
            hint = "我来说几句~"
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    text.toString().let {
                        if (text.isNotEmpty()) {
                            if (text.isNotEmpty()) {
                                commentViewModel.commentShortVideo2(hashMapOf("video_id" to "$videoId",
                                    "content" to "$text"),object : DisposableSubscriberAdapter<String?>() {
                                    override fun onNext(t: String?) {
                                        ToastUtils.showToast("评论成功，等待审核~")
                                        dismiss()
                                    }
                                    override fun onError(t: Throwable?) {
                                        super.onError(t)
                                        ToastUtils.showToast("${t?.localizedMessage}")
                                    }
                                })
                            }
                        }
                    }
                    true
                }
                false
            }
        }
    }

    override fun onShow() {
        super.onShow()
    }

    override fun onDismiss() {
        super.onDismiss()
        //        Log.e("tag", "CustomEditTextBottomPopup  onDismiss");
    }

    //    @Override
    val comment: String
        get() = et_content!!.text.toString()
    //    protected int getMaxHeight() {
    //        return (int) (XPopupUtils.getWindowHeight(getContext())*0.75);
    //    }

    private val commentViewModel by getViewModel(CommentViewModel::class.java) {
        _commentShortVideoResult.observe(it, Observer {
            it?.let {
                //评论成功
                dismiss()
//                currentPage =1
//                loadData()
            }
        })
    }
}