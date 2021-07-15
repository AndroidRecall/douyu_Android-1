package com.mp.douyu.ui.square.bet

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.mp.douyu.R
import com.mp.douyu.adapter.SquareBetDetailAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.jumpIsToGamePageDialog
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.home.recommended.HomeRecommendedChessActivity
import com.mp.douyu.ui.square.SquareViewModel
import kotlinx.android.synthetic.main.fragment_activity.refreshLayout
import kotlinx.android.synthetic.main.square_activity_dymaic.*

/**
 * 投注详情页
 */
class BetDetailFragment(var type: Int? = TYPE_FOOTBALL, var place: Int? = PLACE_NOR) :
    VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 20

    companion object {
        const val EXTRA_TYPE = "type"
        const val TYPE_FOOTBALL = 0
        const val TYPE_BASKETBALL = 1

        const val EXTRA_PLACE = "place"
        const val PLACE_NOR = 0
        const val PLACE_FOLLOW = 1//关注

        fun newInstance(type: Int? = TYPE_FOOTBALL,place: Int? = PLACE_NOR ): BetDetailFragment {
            val fragment = BetDetailFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_TYPE, type!!)
            bundle.putInt(EXTRA_PLACE, place!!)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.square_activity_bet_detail
    val mAdapter by lazy {
        SquareBetDetailAdapter({}, context).apply {
            setListener(object : SquareBetDetailAdapter.OnBetListener {
                override fun onBet() {
//下注
                    context?.let { it1 ->
                        HomeRecommendedChessActivity.open(it1, 4)
                    }?.let { it2 -> startActivityWithTransition(it2) }
//                    (activity as MBaseActivity).showLoadingView(true)
//                    if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) homeViewModel.getGameLink(
//                        hashMapOf("game_id" to "0"))
                }

            })
        }
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
        type = arguments?.getInt(EXTRA_TYPE)
        place = arguments?.getInt(EXTRA_PLACE)!!
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

        }
        refreshLayout.setRefreshFooter(ClassicsFooter(context))

        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage += 1
            loadData(type!!)
        }
        loadData(type!!)

        warningView.addOnRetryListener {
            warningView.hideWarning()
            currentPage = 1
            loadData(type!!)
        }
    }

    private fun loadData(type: Int) {
//        when (type) {
//            TYPE_FOLLOWED ->
//                TYPE_LIKED ->
//            TYPE_COMMENTED ->
//            TYPE_PUBLISHED ->
//        }
        if (place == PLACE_FOLLOW) {
            squareViewModel.getAllBetGodFollowsData(hashMapOf("type" to "${type}"))
        } else {
            squareViewModel.getAllBetGodsData(hashMapOf("type" to "${type}"))
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    private val squareViewModel by getViewModel(SquareViewModel::class.java) {
        betGodsData.observe(it, Observer {
        refreshLayout.finishLoadMore()
            it?.data?.let { it1 ->
                if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                mAdapter.addAll(it1)
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
        betGodsFollowData.observe(it, Observer {
        refreshLayout.finishLoadMore()
            it?.data?.let { it1 ->
                if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                mAdapter.addAll(it1)
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
    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getGameLinkData.observe(it, Observer {
            dismissLoading()
            it?.let {
                jumpIsToGamePageDialog(it, activity as MBaseActivity)
//                jumpIsToGamePageDialog(it, this@MainActivity)
            }
        })
    }
}