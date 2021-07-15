package com.swbg.mlivestreaming.ui.video

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.GlobeStatusViewHolder
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.ShortVideoAdapter
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.ShortVideoBean
import com.swbg.mlivestreaming.relation.RelationManager
import com.swbg.mlivestreaming.ui.mine.IWantExtensionActivity
import com.swbg.mlivestreaming.ui.mine.space.UserSpaceActivity
import com.swbg.mlivestreaming.ui.square.RelationViewModel
import com.swbg.mlivestreaming.ui.video.comment.CommentDialog
import com.swbg.mlivestreaming.view.OnViewPagerListener
import com.swbg.mlivestreaming.view.ViewPagerLayoutManager
import kotlinx.android.synthetic.main.video_fragment_short_video.*
import kotlinx.android.synthetic.main.video_fragment_short_video.refreshLayout
import kotlinx.android.synthetic.main.video_recycle_item_short_video.view.*

class SearchShortVideoFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;

    companion object {
        const val VIDEO_DATA = "data"
        const val INITIAL_POSITION = "position"

        fun newInstance(data: ArrayList<ShortVideoBean>, position: Int = 0): SearchShortVideoFragment {
            val fragment = SearchShortVideoFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(VIDEO_DATA, data)
            bundle.putInt(INITIAL_POSITION, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    val mAdapter by lazy {
        ShortVideoAdapter({}, context).apply {
            setListener(object : ShortVideoAdapter.OnShortVideoListener {
                override fun onLikeClick(position: Int) {
                    currentPosition = position
                    when (get(position).is_like_short_video) {
                        0 -> likeShortVideo(position)
                        else -> unLikeShortVideo(position)
                    }
                }

                override fun onCommentClick(id: Int?) {
                    CommentDialog(id).show(childFragmentManager, "comment")
                }

                override fun onHeaderClick(position: Int) {
                    startActivityWithTransition(UserSpaceActivity.open(activity!!,
                        get(position).user?.id))
                }

                override fun onRepostClick(position: Int) {
                    //分享
                    if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
                        startActivityWithTransition(IWantExtensionActivity.open(context))
                    }
                }

                override fun onFollow(position: Int) {
                    currentPosition = position
                    when (get(position).is_follow) {
                        0 -> follow(position)
                        else -> unFollow(position)
                    }
                }

            })
        }

    }

    private fun follow(position: Int) {
        currentPosition = position
        videoViewModel.follow(hashMapOf("to_id" to "${mAdapter.data[position].user?.id}"))
    }

    private fun unFollow(position: Int) {
        currentPosition = position
        videoViewModel.cancelFollow(hashMapOf("to_id" to "${mAdapter.data[position].user?.id}"))
    }

    private fun likeShortVideo(position: Int) {
        currentPosition = position
        videoViewModel.likeVideo(hashMapOf("video_id" to "${mAdapter.data[position].id}"))
    }

    private fun unLikeShortVideo(position: Int) {
        currentPosition = position
        videoViewModel.unlikeVideo(hashMapOf("video_id" to "${mAdapter.data[position].id}"))
    }

    val mViewPagerLayoutManager by lazy {
        ViewPagerLayoutManager(activity, OrientationHelper.VERTICAL).apply {
            setOnViewPagerListener(object : OnViewPagerListener {
                override fun onInitComplete() {
                    //自动播放第一条
                    autoPlayVideo(0)
                }

                override fun onPageRelease(isNext: Boolean, position: Int) {
                    if (currentPosition == position) {
                        Jzvd.releaseAllVideos()
                    }
                }

                override fun onPageSelected(position: Int, isBottom: Boolean) {
                    if (currentPosition != position) {
                        autoPlayVideo(position)
                        currentPosition = position
                    }
                }
            })
        }
    }
    override val layoutId: Int
        get() = R.layout.video_fragment_short_video

    override fun initView() {
    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
        Jzvd.releaseAllVideos()
    }

    override fun onVisibleFirst() {
        var list = arguments?.getParcelableArrayList<ShortVideoBean>(VIDEO_DATA)
        var initPos = arguments?.getInt(INITIAL_POSITION, 0)
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = mViewPagerLayoutManager
            addOnChildAttachStateChangeListener(object :
                RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewDetachedFromWindow(view: View) {
                    if (jz_player != null && Jzvd.CURRENT_JZVD != null && jz_player.jzDataSource != null && jz_player.jzDataSource.containsTheUrl(
                            Jzvd.CURRENT_JZVD.jzDataSource.currentUrl)
                    ) {
                        if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                            Jzvd.releaseAllVideos()
                        }
                    }
                }

                override fun onChildViewAttachedToWindow(view: View) {

                }
            })
        }
        refreshLayout.apply {
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnRefreshListener {
                currentPage = 1
                loadData()
            }
            setOnLoadMoreListener { refreshlayout ->
                currentPage++
                loadData()
            }

        }
        list?.let {
            mAdapter.addAll(it)
            initPos?.let { it1 -> recyclerView.smoothScrollToPosition(it1) }
        }
    }

    private fun loadData() {
        videoViewModel.getSearchShortData(hashMapOf("list_rows" to "${pageSize}",
            "page" to "${currentPage}"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        relationViewModel.getAllFollowsData(hashMapOf())
        relationViewModel.getAllShortVideoLikesData(hashMapOf())
    }


    private val videoViewModel by getViewModel(ShortVideoViewModel::class.java) {
        _searchShortData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let {
                it.data?.let { it1 ->
                    if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                    if (currentPage == 1) {
                        mAdapter.clear()
                    }
                    it1.forEach { shortVideoBean ->
                        setActionRelationData(shortVideoBean)
                    }
                    mAdapter.addAll(it1)
                }
            }
        })

        _likeVideoResult.observe(it, Observer {
            it?.let {
//                if (it) {
                relationViewModel.getAllShortVideoLikesData(hashMapOf())
//                }
            }
        })
        _unlikeVideoResult.observe(it, Observer {
            it?.let {
//                if (it) {
                relationViewModel.getAllShortVideoLikesData(hashMapOf())
//                }
            }
        })

        _followResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllFollowsData(hashMapOf())

            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllFollowsData(hashMapOf())
            }
        })

    }

    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        shortVideoLikesData.observe(it, Observer {
            it?.let { it1 ->
                RelationManager.instance.shortVideoLikes = it1.data.toMutableList()
                for ((index, shortVideoBean) in mAdapter.data.withIndex()) {
                    val initialIsLike = shortVideoBean.is_like_short_video
                    val initialIsFollow = shortVideoBean.is_follow
                    if (it1.data.isEmpty()) shortVideoBean.is_like_short_video = 0
                    else setActionRelationData(shortVideoBean)
                    if (initialIsLike != shortVideoBean.is_like_short_video || initialIsFollow != shortVideoBean.is_follow) {
                        mAdapter.notifyItemChanged(index)
                    }
                }
            }
        })
        followsData.observe(it, Observer {
            it?.let { it1 ->
                RelationManager.instance.follows = it1.toMutableList()
                for ((index, shortVideoBean) in mAdapter.data.withIndex()) {
                    val initialIsLike = shortVideoBean.is_like_short_video
                    val initialIsFollow = shortVideoBean.is_follow
                    if (it1.isEmpty()) shortVideoBean.is_follow = 0
                    else setActionRelationData(shortVideoBean)
                    if (initialIsLike != shortVideoBean.is_like_short_video || initialIsFollow != shortVideoBean.is_follow) {
                        mAdapter.notifyItemChanged(index)
                    }
                }
            }
        })
    }

    private fun setActionRelationData(shortVideoBean: ShortVideoBean) {
        //设置关注的人状态
        for (userBean in RelationManager.instance.follows) {
            if (userBean.id == shortVideoBean.user?.id) {
                shortVideoBean.is_follow = 1
                break
            } else {
                shortVideoBean.is_follow = 0
            }
        }
        //设置点赞的短视频状态
        for (likeShortVideoBean in RelationManager.instance.shortVideoLikes) {
            if (likeShortVideoBean.pivot?.video_id == shortVideoBean.id) {
                shortVideoBean.is_like_short_video = 1
                break
            } else {
                shortVideoBean.is_like_short_video = 0
            }
        }
    }


    private fun autoPlayVideo(position: Int) {
        recyclerView?.apply {
            getChildAt(0)?.apply {
                jz_player?.apply {
                    startVideoAfterPreloading()
                }
            }
        }
    }
}

