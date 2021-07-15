package com.swbg.mlivestreaming.ui.mine.walnut

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseFragment
import com.swbg.mlivestreaming.bean.MipaWalletBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.recommended.HomeChessFragment
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.ui.mine.walnut.MipaWalletActivity.Companion.TYPE_BEAN
import com.swbg.mlivestreaming.ui.mine.walnut.MipaWalletActivity.Companion.TYPE_MIPA_WALLET
import kotlinx.android.synthetic.main.fragment_mipa_wallet.*


class MipaWalletFragment : MBaseFragment() {
    private var currentPage: Int = 1
    private val datas: MipaWalletBean by lazy {
        arguments?.getParcelable(HomeChessFragment.LIST_TYPE) ?: MipaWalletBean("存款",
            TYPE_MIPA_WALLET)
    }

    override val layoutId: Int
        get() = R.layout.fragment_mipa_wallet

    override fun initView() {
        tv_alarm_btn.singleClick {
            startActivityWithTransition(ChargeCenterActivity.open(context))
        }
        when (datas.type) {
            TYPE_BEAN -> {
            }
            TYPE_MIPA_WALLET -> {
            }
        }
        initData()

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
    }

    private fun loadBegin() {
        mineViewModel.tradeRecorder(hashMapOf("type" to getTypeNum(),
            "size" to "10",
            "page" to "${currentPage}"))
    }

    private fun initData() {
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        loadBegin()
    }

    private fun getTypeNum(): String? {
        return when (datas.type) {
            TYPE_BEAN -> {
                when (datas.typeName) {
                    "获得" -> "4"
                    "消费" -> "5"
                    else -> "5"
                }
            }
            TYPE_MIPA_WALLET -> {
                when (datas.typeName) {
                    "存款" -> "1"
                    "转账" -> "3"
                    "消费" -> "2"
                    else -> "1"
                }
            }
            else -> "1"
        }
    }

    private val mAdapter by lazy {
        MipaWalletAdapter(activity,datas.typeName)
    }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _tradeRecord.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()

            it?.let {
                it.data?.let { it1 ->
                    if (currentPage == 1) {
                        mAdapter.refresh(it1, null)
                    } else {
                        mAdapter.addAll(it1)
                    }
                }
                if (mAdapter.count() == 0) {
                    g_n.visibility = VISIBLE
                } else {
                    g_n.visibility = GONE
                }
            }
        })
    }


    companion object {
        const val LIST_TYPE = "list_type"
        fun newInstance(datas: MipaWalletBean): MipaWalletFragment {
            val fragment = MipaWalletFragment()
            val bundle = Bundle()
            bundle.putParcelable(LIST_TYPE, datas)
            fragment.arguments = bundle
            return fragment
        }
    }
}
