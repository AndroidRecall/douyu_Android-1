package com.mp.douyu.ui.anchor.live.contribute

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.R
import com.mp.douyu.adapter.LiveContributeListAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.RankBean
import com.mp.douyu.bean.RankListBean
import kotlinx.android.synthetic.main.live_fragment_contribute_detail.*
import kotlinx.android.synthetic.main.live_fragment_contribute_detail.refreshLayout

class ContributeDetailFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10

    companion object {
        const val EXTRA_CONTRIBUTE_TYPE = "type"
        const val EXTRA_DATA = "data"
        const val CONTRIBUTE_TYPE_DAY = 0
        const val CONTRIBUTE_TYPE_WEEK = 1
        const val CONTRIBUTE_TYPE_MONTH = 2
        fun newInstance(type: Int = CONTRIBUTE_TYPE_DAY): ContributeDetailFragment {
            val fragment = ContributeDetailFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_CONTRIBUTE_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(data: RankListBean): ContributeDetailFragment {
            val fragment = ContributeDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.live_fragment_contribute_detail

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
        val rankListBean = arguments?.getParcelable<RankListBean>(EXTRA_DATA)
        recycler_view?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setRefreshFooter(ClassicsFooter(context))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadData()
        }
        refreshLayout.finishRefreshWithNoMoreData()
        refreshLayout.finishLoadMoreWithNoMoreData()
//        refreshLayout.setOnLoadMoreListener { refreshlayout ->
//            currentPage += 1
//            loadData()
//        }
        loadData()
        rankListBean?.apply {
            data?.let {
                if (it.size > 0) {
                    mAdapter.clear()
                }
                mAdapter.addAll(it)
            }
        }

    }

    private fun loadData() {


    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    val mAdapter by lazy {
        LiveContributeListAdapter({}, context).apply {
            add(RankBean())
            add(RankBean())
            add(RankBean())
            add(RankBean())
            add(RankBean())
            add(RankBean())
            add(RankBean())
            add(RankBean())
            add(RankBean())
            add(RankBean())
        }
    }

}