package com.mp.douyu.ui.home.special

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.SpecialBean
import com.mp.douyu.bean.VideoBean
import com.mp.douyu.matchWaterDropScreen
import com.mp.douyu.setActivityImmersion
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.home.play.VideoPlayActivity
import kotlinx.android.synthetic.main.activity_special_topic_detail.recyclerView
import kotlinx.android.synthetic.main.activity_special_topic_detail.refreshLayout
import kotlinx.android.synthetic.main.title_bar_simple.*

class SpecialTopicDetailActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_special_topic_detail
    private var currentPage: Int = 1

    override fun initView() {
        matchWaterDropScreen(this)
        setActivityImmersion(this)
        titleBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color00000000))
        ibReturn.singleClick {
            onBackPressed()
        }
        initRecycler()
    }

    private fun initRecycler() {

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0) {
                            3
                        } else 1
                    }
                }
            }
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    if ((view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition % 3 == 0 && (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition > 0) {
                        outRect.set(0, 0, 24, 0)
                    }
                }
            })
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

        mAdapter.sb = specialBean
        mAdapter.changeHeader(0)
    }

    private val mAdapter by lazy {
        SpecialTopicDetailAdapter(this) { it: View, videoBean: VideoBean ->
            when (it.id) {
                R.id.tv_video_title, R.id.iv_video -> {
                    startActivityWithTransition(VideoPlayActivity.open(this,videoBean.id))
                }
            }
        }
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _videoListData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let {
                if (currentPage == 1) {
                    it.data?.let { it1 ->
                        mAdapter.videoList.clear()
                        mAdapter.videoList.addAll(it1)
                        mAdapter.changeDataSet()
                    }
                } else {

                    it.data?.let {
                        mAdapter.videoList.addAll(it)
                        mAdapter.changeDataSet()
                    }
                }
            }
        })
    }

    private fun loadBegin() {
        homeViewModel.getVideoList(hashMapOf("cate_id" to "",
            "special_id" to "${specialBean?.id}",
            "sort" to "new",
            "size" to "15",
            "page" to "${currentPage}"))
    }


    private val specialBean by lazy {
        intent.getParcelableExtra<SpecialBean>(SPLECIAL_BEAN)
    }

    companion object {
        const val SPLECIAL_BEAN = "SPLECIAL_BEAN"
        fun open(context: Context, sb: SpecialBean): Intent {
            return Intent(context, SpecialTopicDetailActivity::class.java).putExtra(SPLECIAL_BEAN,
                sb)
        }
    }
}
