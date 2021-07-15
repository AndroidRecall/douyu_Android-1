package com.mp.douyu.ui.home.search

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.TypeBean
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.home.recommended.VideoAdapter
import com.mp.douyu.utils.WindowUtils
import kotlinx.android.synthetic.main.activity_video_list.recyclerView
import kotlinx.android.synthetic.main.activity_video_list.refreshLayout
import kotlinx.android.synthetic.main.title_bar_simple.*

class SearchVideoListActivity : MBaseActivity() {
    private var currentPage: Int = 1

    override val contentViewLayoutId: Int
        get() = R.layout.activity_video_list

    override fun initView() {
        ibReturn.singleClick {
            finishView()
        }
        iftTitle.text = tyBean?.name ?: "相关视频"
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@SearchVideoListActivity, 2).apply {
                addItemDecoration(object : RecyclerView.ItemDecoration() {

                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val viewPosition =
                            (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                        if (viewPosition != 0 && viewPosition % 2 != 0) {
                            outRect.set(0, 0, WindowUtils.dip2Px(6f), 0)
                        }
                    }
                })
            }
        }

        refreshLayout.setRefreshHeader(ClassicsHeader(this))
        refreshLayout.setRefreshFooter(ClassicsFooter(this))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadBegin()
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage += 1
            loadBegin()
        }
        loadBegin()
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getSearchData.observe(it, Observer {
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

    private fun loadBegin() {
        homeViewModel.getSearchContent(hashMapOf("type" to "1",
            "kw" to tyBean?.cateId,
            "size" to "15",
            "page" to "${currentPage}"))
    }

    private val mAdapter by lazy {
        VideoAdapter({}, context = this)
    }

    private val tyBean by lazy {
        intent.getParcelableExtra<TypeBean>(TYPE_BEAN)
    }

    companion object {
        const val TYPE_BEAN = "typeBean"
        fun open(context: Context?, tb: TypeBean): Intent {
            return Intent(context, SearchVideoListActivity::class.java).putExtra(TYPE_BEAN, tb)
        }
    }
}
