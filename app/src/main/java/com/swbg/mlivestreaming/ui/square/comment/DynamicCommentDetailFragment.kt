package com.swbg.mlivestreaming.ui.square.comment

import android.os.Bundle
import android.text.InputFilter
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.hadcn.keyboard.ChatKeyboardLayout
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.SquareDynamicCommentAdapter
import com.swbg.mlivestreaming.adapter.SquareDynamicCommentAdapter.Companion.TYPE_HEADER2
import com.swbg.mlivestreaming.adapter.SquareDynamicCommentAdapter.Companion.TYPE_TITLE
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.CommentBean
import com.swbg.mlivestreaming.closeInputMethodAndClearFocus
import com.swbg.mlivestreaming.relation.RelationManager
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.space.UserSpaceActivity
import com.swbg.mlivestreaming.ui.square.circle.DynamicViewModel
import kotlinx.android.synthetic.main.fragment_activity.refreshLayout
import kotlinx.android.synthetic.main.square_activity_dymaic.recyclerView
import kotlinx.android.synthetic.main.square_fragment_dymaic_comment_detail.*
import kotlinx.android.synthetic.main.title_bar_confirm_btn.*

/**
 * 回复页面
 */
class DynamicCommentDetailFragment(var data: CommentBean?=null) : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;
    private var curRecommendCommentSize: Int = 0
    private var curPid:Int? =0;
    companion object {


        const val COMMENT_DATA = "data"
        fun newInstance(data: CommentBean?): DynamicCommentDetailFragment {
            val fragment = DynamicCommentDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(COMMENT_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.square_fragment_dymaic_comment_detail
    val mAdapter by lazy {
        SquareDynamicCommentAdapter({}, context).apply {
            setListener(object : SquareDynamicCommentAdapter.OnCommentListener {
                override fun onFollowClick(position: Int) {
                    when (get(position).is_follow) {
                        0 -> follow(position)
                        1 -> unFollow(position)
                    }
                }

                override fun onLikePostClick(position: Int, isLike: Int?) {

                }

                override fun onCommentClick(position: Int) {
                    curPid = get(position).pid
                    chat_key_board.setEtHint("回复 ${get(position).user?.nickname}")
                   et_content.hint = "回复 ${get(position).user?.nickname}"
                    onKeyBoardClick()
                }

                override fun onHeaderClick(position: Int) {
                    startActivityWithTransition(UserSpaceActivity.open(context,
                        get(position).user?.id))
                }
            })
        }
    }

    private fun follow(position: Int) {
        currentPosition = position
        dynamicViewModel.follow(hashMapOf("to_id" to "${mAdapter.data[position].user?.id}"))
    }

    private fun unFollow(position: Int) {
        currentPosition = position
        dynamicViewModel.cancelFollow(hashMapOf("to_id" to "${mAdapter.data[position].user?.id}"))
    }

    private fun likePosts(id: Int?) {
        dynamicViewModel.likePosts(hashMapOf("post_id" to "${id}"))
    }

    private fun unLikePosts(id: Int?) {
        dynamicViewModel.unlikePost(hashMapOf("post_id" to "${id}"))
    }

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        data = arguments?.getParcelable<CommentBean>(COMMENT_DATA)!!
        warningView.hideWarning()
        et_content.apply {
            singleClick {
//                onKeyBoardClick()
            }
        }
//        iv_like.singleClick {
//            when (data?.is_like) {
//                0 -> likePosts(data?.id)
//                else -> unLikePosts(data?.id)
//            }
//        }
//        iv_share.singleClick {
//            if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
//                startActivityWithTransition(IWantExtensionActivity.open(context))
//            }
//
//        }
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        data?.let {it->
            curPid = it.pid
            it.itemType =TYPE_HEADER2
            for (userBean in RelationManager.instance.follows) {
                if (userBean.id == it.user?.id) {
                    it.is_follow = 1
                    break
                }
            }
            mAdapter.data.add(it)
            mAdapter.data.add(CommentBean(itemType = TYPE_TITLE,
                title = "全部回复",
                comment_num = it.data_i.size))
            mAdapter.data.addAll(it.data_i)
            mAdapter.notifyDataSetChanged()
//            iv_like.setImageResource(if (this?.is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.video_play_like_b_s)
        }
        et_content.apply {
            hint = "回复 ${data?.user?.nickname}"
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    text.toString().let {
                        if (text.isNotEmpty()) {
                            dynamicViewModel.comment(hashMapOf("post_id" to "${data?.post_id}",
                                "pid" to "${curPid}",
                                "content" to "$text"))
                        }
                    }
                    true
                }
                false
            }
        }
        initChatKeyboard()
        iftTitle.text = "评论详情"
        ibReturn.setOnClickListener {
            activity?.finish()
        }

//        refreshLayout.setRefreshFooter(ClassicsFooter(context))
//        refreshLayout.setRefreshHeader(ClassicsHeader(context))
//        refreshLayout.setOnRefreshListener { refreshlayout ->
//            currentPage = 1
//            loadData(data)
//        }
//        refreshLayout.setOnLoadMoreListener { refreshlayout ->
//            currentPage++
//            loadData(data)
//        }
//
//        loadData(data)
    }

    private fun onKeyBoardClick() {
        chat_key_board?.apply {
            clearInputContent()
            visibility = View.VISIBLE
            popKeyboard()
        }
    }

    private fun initChatKeyboard() {
        chat_key_board.apply {
            setKeyboardStyle(ChatKeyboardLayout.Style.TEXT_ONLY)
            setEtFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(800)))
            setEtHint("回复 ${data?.user?.nickname}")

            setOnChatKeyBoardListener(object : ChatKeyboardLayout.SimpleOnChatKeyboardListener() {
                override fun onSendButtonClicked(text: String?) {
                    super.onSendButtonClicked(text)
                    text?.let {
                        if (text.isNotEmpty()) {
                            dynamicViewModel.comment(hashMapOf("post_id" to "${data?.post_id}",
                                "pid" to "${curPid}",
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

    private fun loadData(data: CommentBean?) {
        dynamicViewModel.getAllCommentData(hashMapOf("list_rows" to "${pageSize}",
            "page" to "${currentPage}",
            "post_id" to "${data?.id}"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    private val dynamicViewModel by getViewModel(DynamicViewModel::class.java) {
        commentListData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let {
                it.data.let { it1 ->
                    if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                    if (currentPage == 1) {
                        if (curRecommendCommentSize > 0) {
                            mAdapter.data.subList(curRecommendCommentSize + 1,
                                mAdapter.data.size - 1).clear()
                        } else mAdapter.data.subList(curRecommendCommentSize + 1,
                            mAdapter.data.size - 1).clear()
                        mAdapter.data.add(CommentBean(itemType = TYPE_TITLE,
                            title = "全部回复",
                            comment_num = it1.size))
                        mAdapter.data.addAll(it1)
                        mAdapter.notifyDataSetChanged()
                    } else {
                        mAdapter.addAll(it1)

                    }

                }

            }
        })

        _followResult.observe(it, Observer {
            it?.let {
                mAdapter[currentPosition]?.is_follow = 1
                mAdapter.notifyItemChanged(currentPosition)
//                if (it) {
//                mAdapter.data.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
                mAdapter[currentPosition]?.is_follow = 0
                mAdapter.notifyItemChanged(currentPosition)
//                if (it) {
//                mAdapter.list.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _likePostsResult.observe(it, Observer {
            it?.let {
                mAdapter[0].is_like = 1
                mAdapter.notifyItemChanged(0)
                data?.is_like = 1
//                iv_like.setImageResource(if (data?.is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.video_play_like_comment_s)
            }
        })
        _unlikePostResult.observe(it, Observer {
            it?.let {
                mAdapter[0].is_like = 0
                mAdapter.notifyItemChanged(0)
                data?.is_like = 0
//                iv_like.setImageResource(if (data?.is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.video_play_like_comment_s)
            }
        })
        _commentResult.observe(it, Observer {
            it?.let {
                if (it) {
                    //评论成功，刷新全部评论数据
                    refreshLayout.autoRefresh()
//                chat_key_board.hideKeyboard()
//                    chat_key_board.visibility = View.GONE
                    et_content.text.clear()
                    et_content.closeInputMethodAndClearFocus()
                }
            }
        })
    }

}