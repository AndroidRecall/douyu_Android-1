package com.swbg.mlivestreaming.ui.anchor.invite

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.HookDescAdapter
import com.swbg.mlivestreaming.adapter.InviteCityAdapter
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.HookDescBean
import com.swbg.mlivestreaming.bean.InviteCityBean
import com.swbg.mlivestreaming.ui.anchor.AnchorViewModel
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.invite_fragment_desc.*
import kotlinx.android.synthetic.main.invite_fragment_desc.refreshLayout
import kotlinx.android.synthetic.main.invite_fragment_detail.*

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