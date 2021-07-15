package com.mp.douyu.ui.withdraw

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.R
import com.mp.douyu.adapter.WithdrawRecordAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.event.LiveEvent
import com.mp.douyu.singleClick
import io.reactivex.Observable
import kotlinx.android.synthetic.main.title_bar_simple.*
import kotlinx.android.synthetic.main.withdraw_activity_record.recyclerView
import kotlinx.android.synthetic.main.withdraw_activity_record.refreshLayout
import kotlinx.android.synthetic.main.withdraw_activity_record.warningView

class WithdrawRecordActivity : MBaseActivity() {
    private var currentPage: Int = 1
    private var pageSize: Int = 20
    var observable: Observable<LiveEvent>? = null
    override val contentViewLayoutId: Int
        get() = R.layout.withdraw_activity_record

    override fun initView() {
        iftTitle.text ="提现记录"
        anchorViewModel.getRule(hashMapOf())
        recyclerView?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        ibReturn.singleClick {
            finish()
        }

        refreshLayout.setRefreshHeader(ClassicsHeader(this))
        refreshLayout.setRefreshFooter(ClassicsFooter(this))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadData()
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentPage += 1
            loadData()
        }
        warningView.addOnRetryListener {
            warningView.hideWarning()
            anchorViewModel.getRecord(hashMapOf())
        }
        loadData()
    }

    private fun loadData() {
        anchorViewModel.getRecord(hashMapOf("list_rows" to "${pageSize}", "page" to "${currentPage}"))
    }

    val mAdapter by lazy {
        WithdrawRecordAdapter({pos->
            //记录详情
            toDetail(pos)
        }, this).apply {

        }
    }

    private fun toDetail(pos: Int) {
        startActivity(WithdrawRecordDetailActivity.open(this,mAdapter.get(pos)))
    }

    companion object {
        const val LIVE_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun open(context: Context?): Intent {
            return Intent(context, WithdrawRecordActivity::class.java).apply {

            }
        }
    }

    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _withdrawRecordList.observe(it, Observer {
            refreshLayout?.finishLoadMore()
            refreshLayout?.finishRefresh()
            it?.data?.let {recordList->
                if (recordList.size < pageSize) refreshLayout?.finishLoadMoreWithNoMoreData()
                if (currentPage == 1) {
                    mAdapter.clear()
                }
                mAdapter.refresh(recordList,null)
            }
            if (mAdapter.size == 0) {
                if (it == null) {
                    warningView.showNoNetWorkWarning()
                } else {
                    warningView.showOtherWarning(R.mipmap.ic_no_record, R.string.empty)
                }
            } else {
                warningView.hideWarning()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()

    }

}