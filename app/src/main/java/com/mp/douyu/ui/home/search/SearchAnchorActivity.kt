package com.mp.douyu.ui.home.search

import android.content.Context
import android.content.Intent
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.inTransaction

class SearchAnchorActivity:MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        supportFragmentManager.inTransaction {
            add(R.id.container, SearchAnchorFragment.newInstance(intent.getStringExtra(EXTRA_KEYWORD)))
        }
    }
    companion object {
        const val EXTRA_KEYWORD = "keyword"
        fun open(context: Context?, keyword: String?=null): Intent {
            return Intent(context, SearchAnchorActivity::class.java).putExtra(EXTRA_KEYWORD, keyword)
        }
    }
}