package com.swbg.mlivestreaming.ui.square.rank

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.VP2FragmentPagerAdapter
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.ui.square.rank.RankTimeTypeFragment.Companion.TYPE_BASKETBALL
import com.swbg.mlivestreaming.ui.square.rank.RankTimeTypeFragment.Companion.TYPE_FOOTBALL
import com.swbg.mlivestreaming.utils.ToastUtils
import kotlinx.android.synthetic.main.square_activity_rank.*

class RankActivity : MBaseActivity() {

    private val fragments: Array<RankTimeTypeFragment> by lazy {
        arrayOf(RankTimeTypeFragment.newInstance(TYPE_FOOTBALL), RankTimeTypeFragment.newInstance(TYPE_BASKETBALL))
    }
    override val contentViewLayoutId: Int
        get() = R.layout.square_activity_rank

    override fun initView() {
        iv_back.setOnClickListener {
            finish()
        }
        view_pager2?.apply {
            isUserInputEnabled = false
            adapter = VP2FragmentPagerAdapter(this@RankActivity,
                fragments.toMutableList() as List<Fragment>?)
        }
        tv_football_tab.setOnClickListener {
            if (view_pager2.currentItem != 0) {
                tv_football_tab.isSelected = true
                tv_basketball_tab.isSelected = false
                view_pager2.currentItem = 0
            }
        }
        tv_basketball_tab.setOnClickListener {
            if (view_pager2.currentItem != 1) {
                tv_football_tab.isSelected = false
                tv_basketball_tab.isSelected = true
                view_pager2.currentItem = 1
            }
        }
        tv_football_tab.isSelected = true
    }


    companion object {
        fun open(context: Context?): Intent {
            return Intent(context, RankActivity::class.java)
        }
    }


}