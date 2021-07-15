package com.mp.douyu.ui.home.more_type

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
import com.mp.douyu.utils.Utils
import kotlinx.android.synthetic.main.activity_video_list.recyclerView
import kotlinx.android.synthetic.main.activity_video_list.refreshLayout
import kotlinx.android.synthetic.main.title_bar_simple.*

class VideoListActivity : MBaseActivity() {
    private var currentPage: Int = 1

    override val contentViewLayoutId: Int
        get() = R.layout.activity_video_list

    override fun initView() {
        ibReturn.singleClick {
            finishView()
        }
        iftTitle.text = tyBean?.name ?: "推荐"
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@VideoListActivity, 3).apply {
                addItemDecoration(object : RecyclerView.ItemDecoration() {

                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val mPosition =
                            (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                        if (mPosition > 0 && (mPosition+1) % 3 == 0) {
                           outRect.set(0,0,Utils().dp2px(this@VideoListActivity,10).toInt(),0)
                        }else if ((mPosition) % 3 == 0) {
                            outRect.set(Utils().dp2px(this@VideoListActivity,4).toInt(),0,0,0)
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
        _videoListData.observe(it, Observer {
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
        homeViewModel.getVideoList(hashMapOf("cate_id" to tyBean?.cateId,
            "special_id" to tyBean?.specialId,
            "sort" to "new",
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
            return Intent(context, VideoListActivity::class.java).putExtra(TYPE_BEAN, tb)
        }
    }
}
