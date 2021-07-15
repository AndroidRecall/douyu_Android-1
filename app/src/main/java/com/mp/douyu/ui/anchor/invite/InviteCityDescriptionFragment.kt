package com.mp.douyu.ui.anchor.invite

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.douyu.R
import com.mp.douyu.adapter.HookDescAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.HookDescBean
import com.mp.douyu.bean.InviteCityBean
import com.mp.douyu.ui.anchor.AnchorViewModel
import kotlinx.android.synthetic.main.invite_fragment_desc.*
import kotlinx.android.synthetic.main.invite_fragment_desc.refreshLayout

class InviteCityDescriptionFragment(var hookId: Int? = 0) : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;

    companion object {
        private val EXTRA_DATA: String? = "data"

        fun newInstance(hookId: Int): InviteCityDescriptionFragment {
            val fragment = InviteCityDescriptionFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_DATA, hookId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.invite_fragment_desc

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
        hookId = arguments?.getInt(EXTRA_DATA)
        recyclerView?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)

        }
//        loadData()
    }
    private fun loadData() {
        anchorViewModel.getAllCityInviteDetailData(hashMapOf("hook_id" to "$hookId"))
    }
    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        Log.e(TAG, "onVisibleExceptFirst")
    }

    fun updateData(it: InviteCityBean) {
        it?.let {
            //详情
            it.description?.split("|")?.forEach {desc->
                mAdapter.data.add(HookDescBean(tip =desc))
            }
            mAdapter.data.add(HookDescBean(tip =it.comment, itemViewType = HookDescAdapter.TYPE_FOOT))
            mAdapter.notifyDataSetChanged()

        }
    }

    val mAdapter by lazy {
        HookDescAdapter({}, context).apply {

        }
    }
    val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        cityInviteDetailData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout?.finishLoadMore()
            it?.let {
                //详情
                 it.description?.split("|")?.forEach {desc->
                    mAdapter.data.add(HookDescBean(tip =desc))
                }
                mAdapter.data.add(HookDescBean(tip =it.comment, itemViewType = HookDescAdapter.TYPE_FOOT))
                mAdapter.notifyDataSetChanged()

            }
        })
    }
}