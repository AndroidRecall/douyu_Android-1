package com.mp.douyu.ui.video.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.hadcn.keyboard.ChatKeyboardLayout
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.mp.douyu.R
import com.mp.douyu.adapter.CommentVideoAdapter
import com.mp.douyu.base.BaseFullBottomSheetFragment
import com.mp.douyu.ui.square.comment.DynamicCommentDetailActivity
import kotlinx.android.synthetic.main.fragment_video_comment.*

class CommentDialog(var videoId: Int? = 0) : BaseFullBottomSheetFragment() {
    private val pageSize = 10
    private var currentPage = 1
    private var dialogView: View? = null
    val keyboard by lazy {
        dialogView?.findViewById<ChatKeyboardLayout>(R.id.keyboard)
    }
    val recyclerView by lazy {
        dialogView?.findViewById<RecyclerView>(R.id.recyclerView)
    }
    val refreshLayout by lazy {
        dialogView?.findViewById<SmartRefreshLayout>(R.id.refreshLayout)
    }
    val mAdapter by lazy {
        CommentVideoAdapter({}, context).apply {
            setListener(object : CommentVideoAdapter.OnCommentVideoListener {
                override fun onLikeClick() {

                }

                override fun onCommentClick(position: Int) {
                    startActivity(DynamicCommentDetailActivity.open(context,get(position)))
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView = inflater.inflate(R.layout.fragment_video_comment, container, false)
        initView(dialogView)
        return dialogView
    }

    private fun initView(dialogView: View?) {
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

    private fun initKeyBoard() {
        keyboard?.run {
            setKeyboardStyle(ChatKeyboardLayout.Style.TEXT_EMOTICON)
            setOnChatKeyBoardListener(object : ChatKeyboardLayout.SimpleOnChatKeyboardListener() {
                override fun onSendButtonClicked(text: String?) {
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
                    visibility = View.GONE
                    hideLayout()
                }

                override fun onSoftKeyboardClosed() {
                    super.onSoftKeyboardClosed()
                    visibility = View.GONE
                    hideLayout()
                }
            })
        }
    }


    private fun loadData() {
        commentViewModel.getCommentListData(hashMapOf("video_id" to "${videoId}",
            "list_rows" to "${pageSize}",
            "page" to "${currentPage}"))
    }

    override val height: Int
        get() = super.height * 3 / 4

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
                keyboard?.clearInputContent()
                keyboard?.hideKeyboard()
//                currentPage =1
//                loadData()
            }
        })
    }

}