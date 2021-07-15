package com.mp.douyu.ui.anchor.activity

import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.R
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.ui.activity.ActivityAdapter
import com.mp.douyu.ui.activity.ActivityViewModel
import kotlinx.android.synthetic.main.fragment_activity.*

class PaActivityFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    override val layoutId: Int
        get() = R.layout.fragment_pa_activity
    private val mAdapter by lazy {
        ActivityAdapter(context).apply {

        }
    }

    override fun initView() {
        print("initView:PaActivityFragment")
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
        super.onVisibleFirst()
        rv_activity.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setRefreshFooter(ClassicsFooter(context))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadData()
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage += 1
            loadData()
        }
        loadData()
    }

    private fun loadData() {
        homeViewModel.getActivityList(hashMapOf(
            "size" to "10",
            "page" to "${currentPage}"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        Log.e(TAG, "onVisibleExceptFirst")
    }

    private val homeViewModel by getViewModel(ActivityViewModel::class.java) {
        _getActivityList.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let {
                if (currentPage == 1) {
                    it.data?.let { it1 -> mAdapter.refresh(it1, null) }
                } else {
                    it.data?.let { it1 -> mAdapter.addAll(it1) }
                }
            }
        })
    }
}