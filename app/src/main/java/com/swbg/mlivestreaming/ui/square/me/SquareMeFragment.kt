package com.swbg.mlivestreaming.ui.square.me

import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.TabNavigatorAdapter
import com.swbg.mlivestreaming.adapter.ViewPager2TabAdapter
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.MiniTabBean
import com.swbg.mlivestreaming.ui.square.bet.BetListFragment
import com.swbg.mlivestreaming.view.ViewPager2Helper
import kotlinx.android.synthetic.main.square_fragment_home.*
import kotlinx.android.synthetic.main.square_fragment_me.*
import kotlinx.android.synthetic.main.square_fragment_me.indicator
import kotlinx.android.synthetic.main.square_fragment_me.view_pager2
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class SquareMeFragment : VisibilityFragment() {
    private val fragments: Array<SquareDynamicFragment> by lazy {
        arrayOf(SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_MINE_FOLLOWED),
            SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_MINE_LIKED),
            SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_MINE_COMMENTED),
            SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_MINE_PUBLISHED))
    }
    private val tabBeans: List<MiniTabBean> by lazy {
        val followTabBean = MiniTabBean("关注过的").apply {
            selectedSize = 14
            normalSize = 14
        }
        val likeTabBean = MiniTabBean("点赞过的").apply {
            selectedSize = 14
            normalSize = 14
        }
        val commentTabBean = MiniTabBean("评论过的").apply {
            selectedSize = 14
            normalSize = 14
        }
        val publishTabBean = MiniTabBean("我发布的").apply {
            selectedSize = 14
            normalSize = 14
        }
        listOf(followTabBean, likeTabBean, commentTabBean,publishTabBean)
    }

    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager2?.currentItem = position
            }
        })
    }
    override val layoutId: Int
        get() = R.layout.square_fragment_me

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        view_pager2?.apply {
            isUserInputEnabled = false
            adapter  = ViewPager2TabAdapter(activity, fragments.toMutableList())
            currentItem = 0
            offscreenPageLimit =4
        }
        mCommonNavigatorAdapter?.apply {
            selectedColor = resources.getColor(R.color.colorBtnBlue)
            normalColor = resources.getColor(R.color.tab_normal_color)

            val commonNavigator = CommonNavigator(context)
            commonNavigator.adapter = this
            indicator.navigator = commonNavigator
            ViewPager2Helper.bind(indicator, view_pager2)
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    open fun refreshData(){
        fragments[view_pager2.currentItem].run {
           refreshData()
    }
    }
}