package com.swbg.mlivestreaming.ui.anchor.invite

import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.InviteCityAdapter
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.ui.anchor.AnchorViewModel
import kotlinx.android.synthetic.main.anchor_fragment_follow_list.*
import kotlinx.android.synthetic.main.fragment_city_invite.*
import kotlinx.android.synthetic.main.fragment_city_invite.recyclerView
import kotlinx.android.synthetic.main.fragment_city_invite.refreshLayout
import kotlinx.android.synthetic.main.fragment_city_invite.warningView
import kotlinx.android.synthetic.main.square_circle_list.*

class InviteCityFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;
    override val layoutId: Int
        get() = R.layout.fragment_city_invite

    override fun initView() {
        print("initView:CityInviteFragment")
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
        recyclerView?.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager

        }
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadData()
        }
        refreshLayout.setOnLoadMoreListener {
            currentPage++
            loadData()
        }
        warningView.hideWarning()
        warningView.addOnRetryListener {
            refreshLayout.autoRefresh()
        }
        loadData()
    }

    private fun loadData() {
        anchorViewModel.getAllCityInviteListData(hashMapOf("list_rows" to "${pageSize}",
            "page" to "${currentPage}"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        Log.e(TAG, "onVisibleExceptFirst")
    }

    val mAdapter by lazy {
        InviteCityAdapter(context).apply {
            setOnInviteCityListener(object : InviteCityAdapter.InviteCityListener {
                override fun onDetail(position: Int) {
                    startActivity(InviteCityDetailActivity.open(context, get(position).id!!))
                }
            })
        }
    }

    val mLayoutManager by lazy {
        LinearLayoutManager(context).apply {
//            addItemDecoration(object : RecyclerView.ItemDecoration() {
//
//                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//                    val mPosition =
//                        (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
//                    if (mPosition > 0 && (mPosition+1) % 3 == 0) {
//                        outRect.set(0,0, Utils().dp2px(this@VideoListActivity,10).toInt(),0)
//                    }else if ((mPosition) % 3 == 0) {
//                        outRect.set(Utils().dp2px(this@VideoListActivity,4).toInt(),0,0,0)
//                    }
//                }
//            })
        }
    }
    val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        cityInviteListData.observe(it, Observer {
            refreshLayout?.finishRefresh()
            refreshLayout?.finishLoadMore()
            it?.let {
                it.data.let { it1 ->
                    if (it1.size < pageSize) refreshLayout?.finishLoadMoreWithNoMoreData()
                    if (currentPage == 1) {
                        mAdapter.clear()
                    }
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
        })
    }

}