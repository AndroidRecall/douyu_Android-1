package com.mp.douyu.ui.mine.dynamic

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.mp.douyu.R
import com.mp.douyu.adapter.MineAvAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.HttpDataListBean
import com.mp.douyu.bean.MineVideoBean
import com.mp.douyu.ui.home.play.VideoPlayActivity
import com.mp.douyu.ui.mine.MineViewModel
import kotlinx.android.synthetic.main.mine_fragment_dynamic_detail.recyclerView
import kotlinx.android.synthetic.main.mine_fragment_dynamic_detail.refreshLayout
import kotlinx.android.synthetic.main.mine_fragment_dynamic_detail.warningView

class MineAVDetailFragment(var type: Int = TYPE_AV_COMMENT, var uid: Int = 0) :
    VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 20

    companion object {
        const val TYPE_AV_COMMENT = 0
        const val TYPE_AV_COLLECT = 1
        const val TYPE_AV_RECORD = 2
        const val EXTRA_UID = "uid"
        const val EXTRA_TYPE = "type"
        fun newInstance(type: Int = TYPE_AV_COMMENT, uid: Int = 0): MineAVDetailFragment {
            val fragment = MineAVDetailFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_TYPE, type)
            bundle.putInt(EXTRA_UID, uid)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.mine_fragment_dynamic_detail

    override fun initView() {
        print("initView:MineDynamicDetailFragment")
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
        type = arguments?.getInt(EXTRA_TYPE)!!
        uid = arguments?.getInt(EXTRA_UID)!!
        recyclerView?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        refreshLayout.setRefreshFooter(ClassicsFooter(context))

        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage++
            loadData(type)
        }
        warningView.addOnRetryListener {
            currentPage=1
            loadData(type)
        }
        loadData(type)
    }

    private fun loadData(type: Int) {
        when (type) {
            TYPE_AV_COMMENT -> mineViewModel.getAllCommentVideoData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$uid"))
            TYPE_AV_COLLECT -> mineViewModel.getAllCollectVideoData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$uid"))
            TYPE_AV_RECORD -> mineViewModel.getAllRecordVideoData(hashMapOf("list_rows" to "${pageSize}",
                "page" to "${currentPage}",
                "uid" to "$uid"))
        }

    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        Log.e(TAG, "onVisibleExceptFirst")
    }

    val mAdapter by lazy {
        MineAvAdapter({}, context, type).apply {
            setListener(object : MineAvAdapter.OnVideoListListener {
                override fun onVideoClick(position: Int) {
                    context?.startActivity(VideoPlayActivity.open(context,
                        get(position).pivot?.video_id))
                }
            })

        }
    }
    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        collectVideoData.observe(it, Observer {
            setData(it)
        })
        recordVideoData.observe(it, Observer {
            setData(it)
        })
        commentVideoData.observe(it, Observer {
            setData(it)
        })
    }

    private fun setData(it: HttpDataListBean<MineVideoBean>?) {
        refreshLayout.finishLoadMore()
        it?.data?.let { it1 ->
            if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()

            if (currentPage == 1) mAdapter.clear()
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
    }

}