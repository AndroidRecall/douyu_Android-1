package com.mp.douyu.ui.anchor.live.gift

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.mp.douyu.R
import com.mp.douyu.adapter.LiveGiftListAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.GiftBean
import com.mp.douyu.ui.anchor.live.LiveViewModel
import kotlinx.android.synthetic.main.live_fragment_contribute_detail.*


class GiftDetailFragment : VisibilityFragment() {
    private var curPosition: Int = 0
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var type: Int = 0;

    companion object {
        const val EXTRA_GIFT_TYPE = "type"
        const val GIFT_TYPE_NOR = 0
        const val GIFT_TYPE_INTERPLAY = 1
        const val GIFT_TYPE_HIGH = 2
        const val GIFT_TYPE_PACKAGE = 3
        fun newInstance(type: Int? = GIFT_TYPE_NOR): GiftDetailFragment {
            val fragment = GiftDetailFragment()
            val bundle = Bundle()
            type?.let { bundle.putInt(EXTRA_GIFT_TYPE, it) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.live_fragment_gift_detail

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
        type = arguments?.getInt(EXTRA_GIFT_TYPE)!!
        recycler_view?.apply {
            adapter = mAdapter
            layoutManager = this@GiftDetailFragment.layoutManager
        }
//        refreshLayout.setRefreshHeader(ClassicsHeader(context))
//        refreshLayout.setRefreshFooter(ClassicsFooter(context))
//        refreshLayout.setOnRefreshListener {
//            currentPage = 1
//           loadData()
//        }
//        refreshLayout.setOnLoadMoreListener { refreshlayout ->
//            currentPage += 1
//            loadData()
//        }
        loadData()
    }

    private fun loadData() {

        liveViewModel.getGiftsData(hashMapOf("type" to "${type}"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    val layoutManager by lazy {
        GridLayoutManager(context, 4)
    }
    val mAdapter by lazy {
        LiveGiftListAdapter({ position ->

        }, context).apply {
            setOnGiftListener(object : LiveGiftListAdapter.OnGiftListener {
                override fun onSelect(position: Int) {
                    if (curPosition != position) {
                        get(curPosition).isSelect = !get(curPosition).isSelect
                        get(position).isSelect = !get(position).isSelect
                        curPosition = position
                        notifyDataSetChanged()


//                        layoutManager.scrollToPositionWithOffset(position,ScreenUtils.dp2px(100f))
                    }
                }
            })
        }
    }


    open fun getCurSelectGift(): GiftBean? {
        for (bean in mAdapter) {
            if (bean.isSelect) {
                return bean
            }
        }
        return null
    }

    private val liveViewModel by getViewModel(LiveViewModel::class.java) {
        _giftsData.observe(it, Observer {
            /*  refreshLayout.finishRefresh()
              refreshLayout.finishLoadMore()*/
            it?.let {
                it.data.let { it1 ->
                    if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                    val list = it1.filter { bean ->
                        bean.type == type
                    }.toMutableList()
                    if (currentPage == 1) {
                        mAdapter.clear()
                        if (list.size > 0) {
                            list[curPosition].isSelect = true
                        }
                    }
                    mAdapter.addAll(list)
                }
            }
        })
    }

    //    fun smoothScrollUpToPosition(position: Int, itemHeight: Int) {
//        val height: Int = layoutManager.findLastVisibleItemPositon() * itemHeight
//        this.addOnScrollListener(object : OnScrollListener() {
//            fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//
//            fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val firstVisibleItem: Int =
//                    layoutManager.findFirstVisibleItemPosition() //获得屏幕第一个可见元素的位置
//                if (firstVisibleItem == position + 1) {
//                    smoothScrollBy(0, 0) //刹车，停止当前滚动
//                    removeOnScrollListener(this)
//                }
//            }
//        })
//        this.smoothScrollBy(0, -height)
//    }

}