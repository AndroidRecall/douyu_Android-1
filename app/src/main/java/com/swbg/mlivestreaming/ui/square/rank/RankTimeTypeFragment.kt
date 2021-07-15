package com.swbg.mlivestreaming.ui.square.rank

import android.os.Bundle
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.TabNavigatorAdapter
import com.swbg.mlivestreaming.base.MBaseFragment
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.MiniTabBean
import com.swbg.mlivestreaming.ui.square.bet.BetDetailFragment
import com.swbg.mlivestreaming.ui.square.bet.BetTypeFragment
import com.swbg.mlivestreaming.ui.square.rank.RankDetailFragment.Companion.TIME_TYPE_DAY
import com.swbg.mlivestreaming.ui.square.rank.RankDetailFragment.Companion.TIME_TYPE_MONTH
import com.swbg.mlivestreaming.ui.square.rank.RankDetailFragment.Companion.TIME_TYPE_WEEK
import com.swbg.mlivestreaming.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.rank_fragment_time_type.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class RankTimeTypeFragment(var type: Int? = TYPE_FOOTBALL) : VisibilityFragment() {
    companion object {
        const val TYPE_FOOTBALL = 0
        const val TYPE_BASKETBALL = 1
        const val EXTRA_TYPE = "type"
        fun newInstance(type: Int = TYPE_FOOTBALL): RankTimeTypeFragment {
            val fragment = RankTimeTypeFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val fragments: Array<RankDetailFragment> by lazy {
        arrayOf(RankDetailFragment.newInstance(type, TIME_TYPE_DAY),
            RankDetailFragment.newInstance(type, TIME_TYPE_WEEK),
            RankDetailFragment.newInstance(type, TIME_TYPE_MONTH))
    }
    private val tabBeans: List<MiniTabBean> by lazy {
        val weekTabBean = MiniTabBean("周排行")
        val monthTabBean = MiniTabBean("月排行")
        val quarterTabBean = MiniTabBean("季排行")
        listOf(weekTabBean, monthTabBean, quarterTabBean)
    }
    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager?.currentItem = position
            }
        })
    }
    override val layoutId: Int
        get() = R.layout.rank_fragment_time_type

    override fun initView() {

    }

    override fun onVisibleFirst() {
        super.onVisibleFirst()
        type = arguments?.getInt(EXTRA_TYPE)
        view_pager?.apply {
            adapter = BaseFragmentPagerAdapter(childFragmentManager, fragments.toList())
            offscreenPageLimit = 3
            currentItem = 0
        }
        mCommonNavigatorAdapter?.apply {
            selectedColor = resources.getColor(R.color.white)
            normalColor = resources.getColor(R.color.tab_normal_color)
            val commonNavigator = CommonNavigator(context)
            commonNavigator.isAdjustMode = true
            commonNavigator.adapter = this
            indicator.navigator = commonNavigator
            ViewPagerHelper.bind(indicator, view_pager)
        }
    }
}