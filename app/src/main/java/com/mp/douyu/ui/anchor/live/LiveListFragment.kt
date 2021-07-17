package com.mp.douyu.ui.anchor.live

import android.Manifest
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.*
import com.mp.douyu.adapter.LiveListAdapter
import com.mp.douyu.adapter.LiveListAdapter.Companion.ITEM_VIEW_HEADER
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.LiveBean
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.home.recommended.HomeRecommendedChessActivity
import com.mp.douyu.ui.mine.apply.AnchorApplyActivity
import com.mp.douyu.ui.mine.walnut.ChargeCenterActivity
import com.mp.douyu.ui.withdraw.AnchorViewModel
import com.mp.douyu.utils.permission_helper.PermissionHelper
import kotlinx.android.synthetic.main.live_fragment_list.*
import kotlinx.android.synthetic.main.live_fragment_list.recyclerView
import kotlinx.android.synthetic.main.live_fragment_list.refreshLayout

class LiveListFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 20
    private var currentPosition: Int = 0
    override val layoutId: Int
        get() = R.layout.live_fragment_list

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        iv_edit.singleClick {

            if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {

                if (StoredUserSources.getUserInfo2()?.is_anchor == 1) {
                    PermissionHelper.request(activity!!,
                        object : PermissionHelper.PermissionCallback {
                            override fun onSuccess() {
                                startActivityWithTransition(AnchorActivity.open(context))
                                //下播
                                liveViewModel.startLive(null,
                                    null,
                                    null,
                                    StoredUserSources.getGroupId(),
                                    1)
                            }
                        },
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECORD_AUDIO)

                } else {
                    anchorViewModel.getApplyState(hashMapOf())
                }

            }

        }
        recyclerView?.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
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
        mAdapter.add(LiveBean(itemViewType = ITEM_VIEW_HEADER))
        loadData()

//        warningView.addOnRetryListener {
//            warningView.hideWarning()
//            refreshLayout.autoRefresh()
//        }
    }

    private fun loadData() {
        liveViewModel.getLivesData(hashMapOf("list_rows" to "$pageSize", "page" to "$currentPage"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    val mAdapter by lazy {
        LiveListAdapter({}, context).apply {
            setOnLiveListener(object : LiveListAdapter.OnLiveListener {
                override fun onBet(type: Int) {
                    //下注
                    context?.let { it1 ->
                        HomeRecommendedChessActivity.open(it1, type)
                    }?.let { it2 -> startActivityWithTransition(it2) }
//                    (activity as MBaseActivity).showLoadingView(true)
//                    if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) homeViewModel.getGameLink(
//                        hashMapOf("game_id" to "0"))
                }

                override fun onInvite() {
                    if (activity is MainActivity) {
                        (activity as MainActivity).toHook()
                    }
                }

                override fun onRecharge() {
                    if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
                        startActivityWithTransition(ChargeCenterActivity.open(context))
                    }
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
        showLoadingView(true)
        currentPosition = position
        Log.e(TAG, "进入直播间: id= ${mAdapter[position].id}")
        Log.e(TAG, "进入直播间: anchor_id= ${mAdapter[position].anchor_id}")
        liveViewModel.enterLive(hashMapOf("live_id" to "${mAdapter[position].id}","anchor_id" to "${mAdapter[position].anchor_id}"))
    }

    val mLayoutManager by lazy {
        GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (mAdapter[position].itemViewType == ITEM_VIEW_HEADER) {
                        2
                    } else {
                        1
                    }
                }
            }

        }
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
//                if (mAdapter.itemCount<=1) {
//                    if (it == null) {
//                        warningView.showNoNetWorkWarning()
//                    } else {
//                        warningView.showOtherWarning(R.mipmap.icon_empty_issue, R.string.empty)
//                    }
//                } else {
//                    warningView.hideWarning()
//                }
            }
        })
        _enterLiveData.observe(it, Observer {
            dismissLoading()
            it.let {
                startActivity(AudienceActivity.open(context,
                    mAdapter.data.subList(mAdapter.getHeaderCount(), mAdapter.data.size),
                    currentPosition - mAdapter.getHeaderCount(),
                    it))
            }
        })
    }
    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getGameLinkData.observe(it, Observer {
            (activity as MBaseActivity).showLoadingView(false)
            it?.let {
                jumpIsToGamePageDialog(it, activity as MBaseActivity)
//                jumpIsToGamePageDialog(it, this@MainActivity)
            }
        })
    }
    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _applyResult.observe(it, Observer {
            it?.let {
                if (it.equals("3")) {
                    //未申请，去申请
                    startActivityWithTransition(AnchorApplyActivity.open(context, it))

                } else if (it.equals("1")) {
                    //通过审核
                    startActivityWithTransition(AnchorActivity.open(context))

                } else if (it.equals("2")) {
                    //未通过
                    startActivityWithTransition(AnchorApplyActivity.open(context, it))
                } else {
                    //审核中
                    startActivityWithTransition(AnchorApplyActivity.open(context, it))
                }
            }
        })
    }

    override fun showLoadingView(show: Boolean) {
        super.showLoadingView(show)
    }

    override fun dismissLoading() {
        super.dismissLoading()
    }
}