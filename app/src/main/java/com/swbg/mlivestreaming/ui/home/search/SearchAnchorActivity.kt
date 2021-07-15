package com.swbg.mlivestreaming.ui.home.search

import android.content.Context
import android.content.Intent
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.inTransaction

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