package com.swbg.mlivestreaming.ui.home.nv_info

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseFragment
import com.swbg.mlivestreaming.bean.VideoBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.HomeViewModel
import com.swbg.mlivestreaming.ui.home.play.VideoPlayActivity
import com.swbg.mlivestreaming.utils.Utils
import com.swbg.mlivestreaming.utils.WindowUtils
import kotlinx.android.synthetic.main.fragment_nv_product.recyclerView
import kotlinx.android.synthetic.main.fragment_nv_product.refreshLayout
import kotlinx.android.synthetic.main.item_nv_info_product.*

class NvInfoProductFragment : MBaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_nv_product
    private var currentPage: Int = 1

    override fun initView() {
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3).apply {
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val position =
                            (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                        outRect.set(WindowUtils.dip2Px(12f), WindowUtils.dip2Px(10f), 0, 0)
//                        if (position % 2 == 0) outRect.set(Utils().dp2px(context, 15).toInt(),
//                            Utils().dp2px(context, 15).toInt(),
//                            0,
//                            0)
//                        else outRect.set(Utils().dp2px(context, 11).toInt(),
//                            Utils().dp2px(context, 15).toInt(),
//                            0,
//                            0)
                    }
                })
            }
        }


        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setRefreshFooter(ClassicsFooter(context))
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

    private fun loadBegin() {
        homeViewModel.getNvVideo(hashMapOf("actress_id" to aId,
            "size" to "10",
            "page" to "${currentPage}"))
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getNvVideoData.observe(it, Observer {
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

    private val mAdapter by lazy {
        NvDetailVideoAdapter(context) {}
    }

    private val aId by lazy {
        arguments?.getString(LIST_TYPE)
    }

    companion object {
        const val LIST_TYPE = "list_type"
        fun newInstance(datas: String): NvInfoProductFragment {
            val fragment = NvInfoProductFragment()
            val bundle = Bundle()
            bundle.putString(LIST_TYPE, datas)
            fragment.arguments = bundle
            return fragment
        }
    }

}
