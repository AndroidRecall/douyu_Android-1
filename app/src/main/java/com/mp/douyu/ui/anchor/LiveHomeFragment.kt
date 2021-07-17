package com.mp.douyu.ui.anchor

import com.mp.douyu.R
import com.mp.douyu.adapter.TabNavigatorAdapter
import com.mp.douyu.base.MBaseFragment
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.MiniTabBean
import com.mp.douyu.ui.anchor.activity.PaActivityFragment
import com.mp.douyu.ui.anchor.follow.FollowLiveListFragment
import com.mp.douyu.ui.anchor.invite.InviteCityFragment
import com.mp.douyu.ui.anchor.live.LiveListFragment
import com.mp.douyu.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.live_fragment_home.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class LiveHomeFragment : MBaseFragment() {
    private val fragments: Array<VisibilityFragment> by lazy {
        arrayOf(LiveListFragment(), InviteCityFragment(), FollowLiveListFragment())
    }
    private val tabBeans: List<MiniTabBean> by lazy {
        val liveTabBean = MiniTabBean("美女主播")
        val inviteTabBean = MiniTabBean("同城约炮")
        inviteTabBean.selectedTextColor = "#f02b3a"
        inviteTabBean.normalTextColor = "#f02b3a"
        inviteTabBean.isHot = true
//        val activityTabBean = MiniTabBean(getString(R.string.activity_tab))
        val followTabBean = MiniTabBean("关注")
        listOf(liveTabBean, inviteTabBean, followTabBean)
    }
    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager?.currentItem = position
            }
        })
    }
    override val layoutId: Int
        get() = R.layout.live_fragment_home

    override fun initView() {
        view_pager?.apply {
            adapter = BaseFragmentPagerAdapter(childFragmentManager, fragments.toList())
            offscreenPageLimit = 3
            currentItem = 0
        }
        mCommonNavigatorAdapter?.apply {
            selectedColor = resources.getColor(R.color.TabSelect)
            normalColor = resources.getColor(R.color.tab_normal_color)

            val commonNavigator = CommonNavigator(context)
            commonNavigator.isAdjustMode = true
            commonNavigator.adapter = this
            indicator.navigator = commonNavigator
            ViewPagerHelper.bind(indicator, view_pager)
        }
    }
    open fun toHook() {
        view_pager?.currentItem = 1
    }
     fun toLive() {
         view_pager?.currentItem = 0
    }

}