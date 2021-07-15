package com.swbg.mlivestreaming.ui.anchor.invite

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.HookRecordAdapter
import com.swbg.mlivestreaming.adapter.InviteCityAdapter
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.ui.anchor.AnchorViewModel
import kotlinx.android.synthetic.main.anchor_fragment_follow_list.*
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_activity.refreshLayout
import kotlinx.android.synthetic.main.fragment_city_invite.*
import kotlinx.android.synthetic.main.invite_fragment_record.*
import kotlinx.android.synthetic.main.invite_fragment_record.recyclerView
import kotlinx.android.synthetic.main.invite_fragment_record.warningView

class InviteCityRecordFragment(var hookId: Int? = 0) : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;

    companion object {
        private val EXTRA_DATA: String? = "data"

        fun newInstance(hookId: Int): InviteCityRecordFragment {
            val fragment = InviteCityRecordFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_DATA, hookId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.invite_fragment_record

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
        refreshLayout.setRefreshFooter(ClassicsFooter(context))
        refreshLayout.setOnLoadMoreListener {
            currentPage += 1
            loadData()
        }
        warningView.hideWarning()
        warningView.addOnRetryListener {
            currentPage = 1
            loadData()
        }
        loadData()
    }

    private fun loadData() {
        anchorViewModel.getAllHookRecordData(hashMapOf("list_rows" to "${pageSize}",
            "page" to "${currentPage}",
            "hook_id" to "${hookId}"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        Log.e(TAG, "onVisibleExceptFirst")
    }

    val mAdapter by lazy {
        HookRecordAdapter({},context).apply {

        }
    }

    val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        hookRecordData.observe(it, Observer {
            refreshLayout?.finishLoadMore()
            it?.let {
                it.data?.let { it1 ->
                    if (it1.size < pageSize) refreshLayout?.finishLoadMoreWithNoMoreData()
                    if (currentPage == 1) {
                        mAdapter?.clear()
                    }
                    mAdapter.addAll(it1)

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
        })
    }

}