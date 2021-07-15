package com.swbg.mlivestreaming.ui.square.circle

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.SquareCircleAdapter
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.ui.square.SquareViewModel
import com.swbg.mlivestreaming.utils.ToastUtils
import kotlinx.android.synthetic.main.square_circle_list.recyclerView
import kotlinx.android.synthetic.main.square_circle_list.refreshLayout
import kotlinx.android.synthetic.main.square_circle_list.warningView
import kotlinx.android.synthetic.main.title_bar_simple.*

class CircleListActivity : MBaseActivity() {
    private var currentPage: Int = 1
    private var pageSize = 100;
    protected var curSelCircle: Int = -1;
    var type = CIRCLE_TYPE_ALL
    var uid = 0
    override val contentViewLayoutId: Int
        get() = R.layout.square_circle_list

    override fun initView() {
        uid = intent.getIntExtra(EXTRA_UID, 0)
        type = intent.getIntExtra(EXTRA_CIRCLE_TYPE, CIRCLE_TYPE_ALL)
        iftTitle.text = if (type == CIRCLE_TYPE_ALL) "全部圈子" else {
            if (uid == 0||uid== StoredUserSources.getUserInfo2()?.id) "我的圈子" else "TA加入的圈子"
        }
        ibReturn.setOnClickListener {
            finish()
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        refreshLayout.setRefreshHeader(ClassicsHeader(this))
        refreshLayout.setOnRefreshListener {
            currentPage = 1
            loadData(type)
        }
//        refreshLayout.setOnLoadMoreListener { refreshlayout ->
//            currentPage++
//            loadData()
//        }
        warningView.addOnRetryListener {
            warningView.hideWarning()
            currentPage = 1
            loadData(type)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData(type)

    }

    override fun showError(t: Throwable?) {
        super.showError(t)
        ToastUtils.showToast("${t?.message}")
    }

    private fun loadData(type: Int) {
        when (type) {
            CIRCLE_TYPE_USER -> squareViewModel.getJoinCirclesData(hashMapOf("uid" to "${uid}"))
            else -> squareViewModel.getAllSquareCircleData(hashMapOf("uid" to "${uid}"))
        }
    }

    val mAdapter by lazy {
        SquareCircleAdapter({ position ->
            //点击"推荐圈子"头像
            onCircleClick(position)
        }, this).apply {
            setListener(object : SquareCircleAdapter.OnCircleListener {
                override fun onItemViewClick(position: Int) {

                }

                override fun onJoin(position: Int) {
                    //加入圈子
                    handlerCircle(position)


                }
            })
        }
    }

    /**
     * 加入/退出圈子
     */
    private fun handlerCircle(position: Int) {
        curSelCircle = position
        mAdapter.get(position).apply {
            when (is_join) {
                0 -> squareViewModel.joinCircle(hashMapOf("circle_id" to "${id}"))
                else -> squareViewModel.exitCircle(hashMapOf("circle_id" to "${id}"))
            }
        }
    }

    private fun onCircleClick(position: Int) {
        val bean = mAdapter.get(position)
        startActivity(CircleDetailActivity.open(this, bean))
    }

    private val squareViewModel by getViewModel(SquareViewModel::class.java) {
        squareCircleData.observe(it, Observer {
            refreshLayout.finishRefresh()
            it?.data?.let { it1 ->
                for (ben in it1) {
                    ben.isHorizontal = false
                }
                mAdapter.clear()
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
        })
        _joinCirclesData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let {
                it.let { it1 ->
                    if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                    for (bean in it1) {
                        bean.isHorizontal = false
                        bean.is_join = 1
                    }
                    if (currentPage == 1) {
                        mAdapter.clear()
                    }
                    mAdapter.addAll(it1)
                }
            }
        })
        _joinCircleResult.observe(it, Observer {
            it?.let {
//                if (it) {
                mAdapter.get(curSelCircle).is_join = 1
                mAdapter.notifyDataSetChanged()
//                }
            }
        })
        _exitCircleResult.observe(it, Observer {
            it?.let {
//                if (it) {
                mAdapter.get(curSelCircle).is_join = 0
                mAdapter.notifyDataSetChanged()
//                }
            }
        })
    }

    companion object {
        const val EXTRA_CIRCLE_TYPE = "type"
        const val EXTRA_UID = "uid"
        const val CIRCLE_TYPE_ALL = 0
        const val CIRCLE_TYPE_USER = 1
        fun open(context: Context?, type: Int = CIRCLE_TYPE_ALL, uid: Int? = 0): Intent {
            return Intent(context, CircleListActivity::class.java).apply {
                putExtra(EXTRA_CIRCLE_TYPE, type)
                putExtra(EXTRA_UID, uid)
            }
        }
    }
}