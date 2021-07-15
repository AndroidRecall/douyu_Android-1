package com.mp.douyu.ui.home.nv_info

import android.graphics.Rect
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.douyu.R
import com.mp.douyu.base.MBaseFragment
import com.mp.douyu.bean.NvDetailBean
import com.mp.douyu.bean.NvInfoScrollBean
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_nv_info.*
import kotlin.collections.ArrayList

class NvInfoFragment : MBaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_nv_info

    override fun initView() {
        rv_nv_info.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (mAdapter.infoList[position].isHeader) {
                            3
                        } else 1
                    }
                }
            }
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    if ((view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition % 4 == 0 && (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition == 1) {
                        outRect.set(34, 50, 0, 0)
                    } else {
                        outRect.set(12, 50, 0, 0)
                    }
                }
            })
        }
        rv_nv_scroll.apply {
            adapter = mScrollAdapter
            layoutManager = LinearLayoutManager(context)
        }

        //获取女资料
        homeViewModel.getNvList()
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getNvListData.observe(it, Observer {
            it?.let {
                LogUtils.i("==", it.toString())
                mScrollAdapter.infoScrollList.clear()
                mAdapter.infoList.clear()
                val arrayList = ArrayList(it.keys)
                arrayList.sortBy { it.toUpperCase() }
                for (key in arrayList) {
                    val value = it.get(key)
                    mScrollAdapter.infoScrollList.add(NvInfoScrollBean(key, false))

                    mAdapter.infoList.add(NvDetailBean(isHeader = true, name = key))
                    value?.map {
                        it.isHeader = false
                    }
                    value?.let { it1 -> mAdapter.infoList.addAll(it1) }
                }
//                it.map { value1 ->
//                    mScrollAdapter.infoScrollList.add(NvInfoScrollBean(value1.key, false))
//
//                    mAdapter.infoList.add(NvDetailBean(isHeader = true, name = value1.key))
//                    value1.value.map {
//                        it.isHeader = false
//                    }
//                    mAdapter.infoList.addAll(value1.value)
//                }

                mAdapter.refresh(mAdapter.infoList, null)

//                mScrollAdapter.infoScrollList.first().isSelect = true
                mScrollAdapter.refresh(mScrollAdapter.infoScrollList, null)
            }
        })
    }

    private val mAdapter by lazy {
        NvInfoAdapter(context) {
            jumpActivity(it)
        }
    }

    private fun jumpActivity(it: Int) {
        val nvDetailBean = mAdapter.infoList[it]
        startActivityWithTransition(NvInfoDetailActivity.open(context,nvDetailBean))
    }

    private val mScrollAdapter by lazy {
        NvInfoScrollAdapter(context) {

        }
    }


}
