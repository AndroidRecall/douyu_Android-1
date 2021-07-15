package com.mp.douyu.ui.square.rank

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.adapter.LiveListAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.CommonUserBean
import com.mp.douyu.ui.square.SquareViewModel
import kotlinx.android.synthetic.main.square_fragment_rank_detail.*

class RankDetailFragment(var type: Int? = TYPE_FOOTBALL, var timeType: Int? = TIME_TYPE_DAY) :
    VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 20

    companion object {
        const val TYPE_FOOTBALL = 0
        const val TYPE_BASKETBALL = 1
        const val TIME_TYPE_DAY = 0
        const val TIME_TYPE_WEEK = 1
        const val TIME_TYPE_MONTH = 2

        const val EXTRA_TYPE = "type"
        const val EXTRA_TIME_TYPE = "time_type"
        fun newInstance(type: Int? = TYPE_FOOTBALL, timeType: Int? = TIME_TYPE_DAY): RankDetailFragment {
            val fragment = RankDetailFragment()
            val bundle = Bundle()
            type?.let { bundle.putInt(EXTRA_TYPE, it) }
            timeType?.let { bundle.putInt(EXTRA_TIME_TYPE, it) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.square_fragment_rank_detail

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
        type = arguments?.getInt(EXTRA_TYPE)
        timeType = arguments?.getInt(EXTRA_TIME_TYPE)
        refreshLayout.setOnRefreshListener {
            squareViewModel.getAllBetRankData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}","type" to "${type}"))
        }
        squareViewModel.getAllBetRankData(hashMapOf("list_rows" to "${pageSize}",
            "page" to "${currentPage}",
            "type" to "${type}"))
//        recyclerView?.apply {
//            adapter = mAdapter
//            layoutManager = LinearLayoutManager(context)
//        }
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

                }
            })
        }
    }
    private val squareViewModel by getViewModel(SquareViewModel::class.java) {
        betRankData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let { it1 ->
                when (timeType) {
                    TIME_TYPE_DAY -> {
                        it1.day?.let { it2 ->
                            setRankDetail(it2)
                        }

                    }
                    TIME_TYPE_WEEK -> {
                        it1.week?.let { it2 ->
                            setRankDetail(it2)
                        }
                    }
                    TIME_TYPE_MONTH -> {
                        it1.month?.let { it2 ->
                            setRankDetail(it2)
                        }
                    }
                    else -> {

                    }
                }
            }
        })
    }

    private fun setRankDetail(it2: MutableList<CommonUserBean>) {
        if (it2.size > 0) {
            context?.let { it3 ->
                Glide.with(it3).load(BuildConfig.IMAGE_BASE_URL + it2[0].avatar).circleCrop()
                    .into(iv_rank_top1)
            }
            tv_rank_top1_name.text = it2[0].nickname
            tv_rank_top1_rate.text =
                "${if (type == 0) it2[0].football_rate else it2[0].basketball_rate}%"
            tv_rank_top1_desc.text = "${it2[0].plan}"
            tv_rank_top1_desc.visibility = View.VISIBLE
        } else {
            tv_rank_top1_desc.visibility = View.GONE
        }
        if (it2.size > 1) {
            context?.let { it3 ->
                Glide.with(it3).load(BuildConfig.IMAGE_BASE_URL + it2[1].avatar).circleCrop().into(iv_rank_top2)
            }
            tv_rank_top2_name.text = it2[1].nickname
            tv_rank_top2_rate.text = "${if (type==0)it2[1].football_rate else it2[1].basketball_rate}%"
            tv_rank_top2_desc.text = "${it2[1].plan}"
            tv_rank_top2_desc.visibility = View.VISIBLE
        } else {
            tv_rank_top2_desc.visibility = View.GONE
        }
        if (it2.size > 2) {
            context?.let { it3 ->
                Glide.with(it3).load(BuildConfig.IMAGE_BASE_URL + it2[2].avatar).circleCrop().into(iv_rank_top3)
            }
            tv_rank_top3_name.text = it2[2].nickname
            tv_rank_top3_rate.text = "${if (type==0)it2[2].football_rate else it2[2].basketball_rate}%"
            tv_rank_top3_desc.text = "${it2[2].plan}"
            tv_rank_top3_desc.visibility = View.VISIBLE
        } else {
            tv_rank_top3_desc.visibility = View.GONE
        }
    }
}