package com.swbg.mlivestreaming.dialog

import android.content.Context
import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.lxj.xpopup.util.XPopupUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.CommentVideoAdapter
import com.swbg.mlivestreaming.bean.CommentBean
import com.swbg.mlivestreaming.bean.HttpDataListBean
import com.swbg.mlivestreaming.closeInputMethodAndClearFocus
import com.swbg.mlivestreaming.http.DisposableSubscriberAdapter
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.square.comment.DynamicCommentDetailActivity
import com.swbg.mlivestreaming.ui.video.comment.CommentViewModel
import kotlinx.android.synthetic.main.fragment_video_comment2.view.*

class CommentBottomDialog(context: Context, var videoId: Int? = 0) : BaseBottomPopupView(context) {
    private val pageSize = 10
    private var currentPage = 1
    val mAdapter by lazy {
        CommentVideoAdapter({}, context).apply {
            setListener(object : CommentVideoAdapter.OnCommentVideoListener {
                override fun onLikeClick() {

                }

                override fun onCommentClick(position: Int) {
                    context.startActivity(DynamicCommentDetailActivity.open(context, get(position))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            })
        }
    }

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    private fun initView() {
        cl_bottom.singleClick {

            //弹出新的弹窗用来输入
            val textBottomPopup = CustomEditTextBottomPopup(context, videoId)
            XPopup.Builder(context)
                .autoOpenSoftInput(true) //                        .hasShadowBg(false)
                .setPopupCallback(object : SimpleCallback() {
                    override fun onShow(popupView: BasePopupView) {}
                    override fun onDismiss(popupView: BasePopupView) {
                        val comment: String = textBottomPopup.comment
//                            if (!comment.isEmpty()) {
//                                data.add(0, comment)
//                                commonAdapter.notifyDataSetChanged()
//                            }
                    }
                }).asCustom(textBottomPopup).show()
        }
        et_content.apply {
//            setOnEditorActionListener { v, actionId, event ->
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//                    text.toString().let {
//                        if (text.isNotEmpty()) {
//                            commentViewModel.commentShortVideo(hashMapOf("video_id" to "$videoId",
//                                "content" to "$text"))
//                        }
//                    }
//                    true
//                }
//                false
//            }
        }
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        refreshLayout?.apply {
            setOnRefreshListener {
                currentPage = 1
                loadData()
            }
            setRefreshFooter(ClassicsFooter(context))
            setOnLoadMoreListener { refreshlayout ->
                currentPage++
                loadData()
            }
        }
        initKeyBoard()
        loadData()
    }

    private fun loadData() {
        commentViewModel.getCommentListData2(hashMapOf("video_id" to "${videoId}",
            "list_rows" to "${pageSize}",
            "page" to "${currentPage}"),
            object : DisposableSubscriberAdapter<HttpDataListBean<CommentBean>?>() {
                override fun onNext(t: HttpDataListBean<CommentBean>?) {
                    refreshLayout?.finishLoadMore()
                    t?.let {
                        tv_comment_num.text = "全部${it.total}条评论"
                        it.data.let { it1 ->
                            if (it1.size < pageSize) refreshLayout?.finishLoadMoreWithNoMoreData()
                            if (currentPage == 1) {
                                mAdapter.clear()
                            }
                            mAdapter.addAll(it1)
                        }
                    }
                }
            })
    }

    override fun getImplLayoutId(): Int {
        return R.layout.fragment_video_comment2
    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * 0.65f).toInt()
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


    private val commentViewModel by getViewModel(CommentViewModel::class.java) {
        commentVideoData.observe(it, Observer {
            refreshLayout?.finishLoadMore()
            it?.let {
                tv_comment_num.text = "全部${it.total}条评论"
                it.data.let { it1 ->
                    if (it1.size < pageSize) refreshLayout?.finishLoadMoreWithNoMoreData()
                    if (currentPage == 1) {
                        mAdapter.clear()
                    }
                    mAdapter.addAll(it1)
                }
            }
        })
        _commentShortVideoResult.observe(it, Observer {
            refreshLayout?.finishLoadMore()
            it?.let {
                //评论成功
//                keyboard?.clearInputContent()
//                keyboard?.hideKeyboard()
//                currentPage =1
//                loadData()
//                et_content.text.clear()
//                et_content.closeInputMethodAndClearFocus()
            }
        })
    }
}