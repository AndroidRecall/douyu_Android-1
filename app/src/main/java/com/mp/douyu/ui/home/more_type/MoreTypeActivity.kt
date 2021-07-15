package com.mp.douyu.ui.home.more_type

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_more_type.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class MoreTypeActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_more_type

    override fun initView() {
        iftTitle.text = getString(R.string.more_type)
        ibReturn.singleClick {
            onBackPressed()
        }
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        initData()
    }

    private val mAdapter by lazy {
        MoreTypeAdapter(this)
    }

    private fun initData() {
        homeViewModel.getMorTypeData()
    }

    companion object {
        fun instance(context: Context?): Intent {
            return Intent(context, MoreTypeActivity::class.java)
        }
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _moreTypeData.observe(it, Observer {
            it?.let {
                mAdapter.viewTypeTest = it.cate!!
                mAdapter.videoBeanList = it.special!!
                mAdapter.changeDataSet()
            }
        })
    }


}
