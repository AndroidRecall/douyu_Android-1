package com.swbg.mlivestreaming.ui.square.comment

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.hadcn.keyboard.ChatKeyboardLayout
import cn.jzvd.Jzvd
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.GlobeStatusViewHolder
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.SquareDynamicCommentAdapter
import com.swbg.mlivestreaming.adapter.SquareDynamicCommentAdapter.Companion.TYPE_HEADER
import com.swbg.mlivestreaming.adapter.SquareDynamicCommentAdapter.Companion.TYPE_TITLE
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.AdvBean
import com.swbg.mlivestreaming.bean.DynamicBean
import com.swbg.mlivestreaming.bean.CommentBean
import com.swbg.mlivestreaming.relation.RelationManager
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.activity.ActivityViewModel
import com.swbg.mlivestreaming.ui.mine.IWantExtensionActivity
import com.swbg.mlivestreaming.ui.mine.space.UserSpaceActivity
import com.swbg.mlivestreaming.ui.square.RelationViewModel
import com.swbg.mlivestreaming.ui.square.circle.DynamicViewModel
import kotlinx.android.synthetic.main.fragment_activity.refreshLayout
import kotlinx.android.synthetic.main.square_activity_dymaic.recyclerView
import kotlinx.android.synthetic.main.square_fragment_dymaic_comment.*
import kotlinx.android.synthetic.main.title_bar_confirm_btn.*

class DynamicCommentFragment(var type: Int = TYPE_MINE_FOLLOWED) : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;
    private var dynamicBean: DynamicBean? = null
    private var curRecommendCommentSize: Int = 0

    companion object {
        const val TYPE_MINE_FOLLOWED = 0//关注


        const val COMMENT_TYPE = "comment_type"
        const val COMMENT_DATA = "comment_data"
        fun newInstance(data: DynamicBean?): DynamicCommentFragment {
            val fragment = DynamicCommentFragment()
            val bundle = Bundle()
            bundle.putParcelable(COMMENT_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.square_fragment_dymaic_comment
    val mAdapter by lazy {
        SquareDynamicCommentAdapter({

        }, context).apply {
            setListener(object : SquareDynamicCommentAdapter.OnCommentListener {
                override fun onFollowClick(position: Int) {
                    when (get(position).headerBean?.is_follow) {
                        0 -> follow(position)
                        1 -> unFollow(position)
                    }
                }

                override fun onLikePostClick(position: Int, isLike: Int?) {

                }

                override fun onCommentClick(position: Int) {
                    startActivityWithTransition(DynamicCommentDetailActivity.open(context,
                        get(position)))
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
        dynamicViewModel.follow(hashMapOf("to_id" to "${mAdapter.data[position].headerBean?.user?.id}"))
    }

    private fun unFollow(position: Int) {
        currentPosition = position
        dynamicViewModel.cancelFollow(hashMapOf("to_id" to "${mAdapter.data[position].headerBean?.user?.id}"))
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
        warningView.hideWarning()
        tv_content.apply {
            singleClick {
                onKeyBoardClick()
            }
        }
        iv_comment.apply {
            singleClick {
                onKeyBoardClick()
            }
        }
        iv_like.singleClick {
            when (dynamicBean?.is_like) {
                0 -> likePosts(dynamicBean?.id)
                else -> unLikePosts(dynamicBean?.id)
            }
        }
        iv_share.singleClick {
            if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
                startActivityWithTransition(IWantExtensionActivity.open(context))
            }

        }
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (Jzvd.CURRENT_JZVD == null) return
                    val currentPlayPosition = Jzvd.CURRENT_JZVD.positionInList
                    val firstPosition =
                        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val lastPosition =
                        (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (currentPlayPosition >= 0) {
                        if (currentPlayPosition < firstPosition || currentPlayPosition > lastPosition) {
                            if (Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                                Jzvd.releaseAllVideos() //为什么最后一个视频横屏会调用这个，其他地方不会
                            }
                        }
                    }
                }
            })
        }
        type = arguments?.getInt(COMMENT_TYPE, TYPE_MINE_FOLLOWED)!!
        dynamicBean = arguments?.getParcelable<DynamicBean>(COMMENT_DATA)
        dynamicBean?.apply {
            mAdapter.data.add(CommentBean(itemType = TYPE_HEADER, headerBean = this))
            /*comments.apply {
                curRecommendCommentSize = this.size
                mAdapter.data.add(CommentBean(itemType = TYPE_TITLE,
                    title = "精彩评论",
                    comment_num = this.size))
                mAdapter.data.addAll(this)
            }*/
            mAdapter.notifyDataSetChanged()
            iv_like.setImageResource(if (dynamicBean?.is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.video_play_like_b_s)
        }
        initChatKeyboard()
        iftTitle.text = "详情"
        ibReturn.setOnClickListener {
            activity?.finish()
        }

        refreshLayout.setRefreshFooter(ClassicsFooter(context))
        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setOnRefreshListener { refreshlayout ->
            currentPage = 1
            loadData(dynamicBean)
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage++
            loadData(dynamicBean)
        }

        loadData(dynamicBean)
        activityViewModel.getAdvList(hashMapOf("type" to "1"))
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
            setEtHint("说点什么~")
            setOnChatKeyBoardListener(object : ChatKeyboardLayout.SimpleOnChatKeyboardListener() {
                override fun onSendButtonClicked(text: String?) {
                    super.onSendButtonClicked(text)
                    text?.let {
                        if (text.isNotEmpty()) {
                            dynamicViewModel.comment(hashMapOf("post_id" to "${dynamicBean?.id}",
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

    private fun loadData(data: DynamicBean?) {
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
                    it1.forEach { commentBean ->
                        setActionRelationData(commentBean)
                    }
                    if (currentPage == 1) {
                       /* if (curRecommendCommentSize > 0) {
                            mAdapter.data.subList(curRecommendCommentSize + 1,
                                mAdapter.data.size - 1).clear()
                        } else mAdapter.data.subList(curRecommendCommentSize + 1,
                            mAdapter.data.size - 1).clear()*/
                        if (mAdapter.data.size > 1) {
                            mAdapter.data.subList(curRecommendCommentSize + 1,
                                mAdapter.data.size).clear()
                        }
                        mAdapter.data.add(CommentBean(itemType = TYPE_TITLE,
                            title = "全部评论",
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
//                mAdapter[currentPosition].headerBean?.is_follow = 1
//                mAdapter.notifyItemChanged(currentPosition)
                relationViewModel.getAllFollowsData(hashMapOf())
//                if (it) {
//                mAdapter.data.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
//                mAdapter[currentPosition].headerBean?.is_follow = 0
//                mAdapter.notifyItemChanged(currentPosition)
                relationViewModel.getAllFollowsData(hashMapOf())
//                if (it) {
//                mAdapter.list.get(curSelCircle).is_join = 1
//                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _likePostsResult.observe(it, Observer {
            it?.let {

                mAdapter[0].headerBean?.is_like = 1
                mAdapter[0].headerBean?.like_count = mAdapter[0].headerBean?.like_count?.plus(1)
                mAdapter.notifyItemChanged(0)
                dynamicBean?.is_like = 1
                iv_like.setImageResource(if (dynamicBean?.is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.video_play_like_comment_s)
            }
        })
        _unlikePostResult.observe(it, Observer {
            it?.let {
                mAdapter[0].headerBean?.is_like = 0
                mAdapter[0].headerBean?.like_count = mAdapter[0].headerBean?.like_count?.minus(1)
                mAdapter.notifyItemChanged(0)
                dynamicBean?.is_like = 0
                iv_like.setImageResource(if (dynamicBean?.is_like == 0) R.mipmap.ic_box_like_false else R.mipmap.video_play_like_comment_s)
            }
        })
        _commentResult.observe(it, Observer {
            it?.let {
                if (it) {
                    //评论成功，刷新全部评论数据
                    refreshLayout.autoRefresh()
//                chat_key_board.hideKeyboard()
                    chat_key_board.visibility = View.GONE
                }
            }
        })
    }

    private fun setActionRelationData(commentBean: CommentBean) {
        //设置关注的人状态
        for (userBean in RelationManager.instance.follows) {
            if (userBean.id == commentBean.user?.id) {
                commentBean.is_follow = 1
                break
            } else {
                commentBean.is_follow = 0
            }
        }
    }
    private fun setActionRelationData(dynamicBean: DynamicBean) {
        //设置关注的人状态
        for (userBean in RelationManager.instance.follows) {
            if (userBean.id == dynamicBean.user?.id) {
                dynamicBean.is_follow = 1
                break
            } else {
                dynamicBean.is_follow = 0
            }
        }
    }
    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        followsData.observe(it, Observer {

            it?.let {
                RelationManager.instance.follows = it.toMutableList()
                for ((index, commentBean) in mAdapter.data.withIndex()) {
                    if (commentBean.itemType == TYPE_HEADER) {
                        val initialIsLike = commentBean.headerBean?.is_like
                        val initialIsFollow = commentBean.headerBean?.is_follow
                        if (it.isEmpty()) commentBean.headerBean?.is_follow = 0
                        else setActionRelationData(commentBean.headerBean!!)
                        if (initialIsLike != commentBean.headerBean?.is_like || initialIsFollow != commentBean.headerBean?.is_follow) {
                            mAdapter.notifyItemChanged(index)
                        }
                    } else {
                        val initialIsLike = commentBean.is_like
                        val initialIsFollow = commentBean.is_follow
                        if (it.isEmpty()) commentBean.is_follow = 0
                        else setActionRelationData(commentBean)
                        if (initialIsLike != commentBean.is_like || initialIsFollow != commentBean.is_follow) {
                            mAdapter.notifyItemChanged(index)
                        }
                    }

                }

            }
        })
        postLikesData.observe(it, Observer {
            it?.data?.let { it ->
                RelationManager.instance.postLikes = it.toMutableList()
                for ((index, commentBean) in mAdapter.data.withIndex()) {
                    if (commentBean.itemType == TYPE_HEADER) {

                    } else {

                        val initialIsLike = commentBean.is_like
                        val initialIsFollow = commentBean.is_follow
                        if (it.isEmpty()) commentBean.is_like = 0
                        else setActionRelationData(commentBean)
                        if (initialIsLike != commentBean.is_like || initialIsFollow != commentBean.is_follow) {
                            mAdapter.notifyItemChanged(index)
                        }
                    }

                }
            }
        })
    }
    private val activityViewModel by getViewModel(ActivityViewModel::class.java) {
        _getAdvList.observe(it, Observer {
            it?.let { list ->
                list.forEach { advBean: AdvBean? ->
                    if (advBean != null) {
                        mAdapter[0].advBean =advBean
                        mAdapter.notifyItemChanged(0)
//                        context?.let { it1 ->
//                            Glide.with(it1).load(BuildConfig.IMAGE_BASE_URL + advBean.cover).centerCrop()
//                                .placeholder(R.mipmap.bg_home_page_top).into(iv_banner)
//                            iv_banner.singleClick {
//                                var intent =  Intent(Intent.ACTION_VIEW, Uri.parse("${advBean.url}"));
//                                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//                                it1.startActivity(intent)
////                                ActivityUtils.jumpToWebView(advBean.url, it1)
//                            }
//                        }
                        return@forEach
                    }
                }
            }
        })
    }
}