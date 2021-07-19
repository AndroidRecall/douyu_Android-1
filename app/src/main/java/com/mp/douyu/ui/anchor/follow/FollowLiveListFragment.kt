package com.mp.douyu.ui.anchor.follow

import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mp.douyu.GlobeStatusViewHolder
import com.mp.douyu.R
import com.mp.douyu.adapter.LiveListAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.ui.anchor.AnchorViewModel
import com.mp.douyu.ui.anchor.live.AudienceActivity
import com.mp.douyu.ui.anchor.live.LiveViewModel
import kotlinx.android.synthetic.main.anchor_fragment_follow_list.recyclerView
import kotlinx.android.synthetic.main.anchor_fragment_follow_list.refreshLayout
import kotlinx.android.synthetic.main.anchor_fragment_follow_list.warningView

class FollowLiveListFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition: Int = 0
    override val layoutId: Int
        get() = R.layout.anchor_fragment_follow_list

    override fun initView() {
        print("initView:FollowFragment")
    }

    override fun onVisible() {
        super.onVisible()
        Log.e(TAG, "onVisible")
    }

    override fun onInvisible() {
        super.onInvisible()
        Log.e(TAG, "onInvisible")
    }

    override fun onVisibleFirst() {
        recyclerView?.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return 1
                    }
                }

            }
        }
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadData()
        }
        refreshLayout.setOnLoadMoreListener {
            currentPage++
            loadData()
        }
        warningView.addOnRetryListener {
            warningView.hideWarning()
            refreshLayout.autoRefresh()
        }
        loadData()

    }

    private fun loadData() {
        anchorViewModel.getAllFollowLiveListData(hashMapOf("list_rows" to "${pageSize}",
            "page" to "${currentPage}"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        Log.e(TAG, "onVisibleExceptFirst")
    }

    val mAdapter by lazy {
        LiveListAdapter({}, context).apply {
            setOnLiveListener(object : LiveListAdapter.OnLiveListener {
                override fun onBet(position: Int) {

                }

                override fun onInvite() {

                }

                override fun onRecharge() {
                }

                override fun onLive(position: Int) {
                    if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
                        toLiveRoom(position)
                    }
                }
            })
        }
    }
    private fun toLiveRoom(position: Int) {
        currentPosition = position
        Log.e(TAG,"进入直播间: id= ${mAdapter.get(position).id}")
        Log.e(TAG,"进入直播间: anchor_id= ${mAdapter.get(position).anchor_id}")
        liveViewModel.enterLive(hashMapOf("live_id" to "${mAdapter[position].id}","anchor_id" to "${mAdapter[position].anchor_id}"))
    }
    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        followLiveListData.observe(it, Observer {
            refreshLayout?.finishLoadMore()
            it?.let {
                it.data.let { it1 ->
                    if (it1.size < pageSize) refreshLayout?.finishLoadMoreWithNoMoreData()
                    if (currentPage == 1) {
                        mAdapter.clear()
                    }
                    mAdapter.addAll(it1)
                }
            }
            if (mAdapter.size == 0) {
                if (it == null) {
                    warningView.showNoNetWorkWarning()
                } else {
                    warningView.showOtherWarning(R.mipmap.icon_empty_issue, R.string.empty)
                }
            } else {
                warningView.hideWarning()
            }
        })

    }
    private val liveViewModel by getViewModel(LiveViewModel::class.java) {
        _livesData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let {
                it.data.let { it1 ->
                    if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                    if (currentPage == 1) {
                        mAdapter.data.removeAll(mAdapter.subList(1, mAdapter.itemCount))
//                        mAdapter.clear()
                    }
                    mAdapter.data.addAll(it1)
                    mAdapter.notifyDataSetChanged()
                }
                if (mAdapter.itemCount<=1) {
                    if (it == null) {
                        warningView.showNoNetWorkWarning()
                    } else {
                        warningView.showOtherWarning(R.mipmap.icon_empty_issue, R.string.empty)
                    }
                } else {
                    warningView.hideWarning()
                }
            }
        })
        _enterLiveData.observe(it, Observer {
            it.let {
                startActivity(AudienceActivity.open(context,
                    mAdapter.data.subList(mAdapter.getHeaderCount(), mAdapter.data.size),
                    currentPosition - mAdapter.getHeaderCount(),it))
            }
        })
    }
}