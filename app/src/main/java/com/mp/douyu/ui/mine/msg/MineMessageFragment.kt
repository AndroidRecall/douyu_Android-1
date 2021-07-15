package com.mp.douyu.ui.mine.msg

import android.os.Bundle
import com.mp.douyu.R
import com.mp.douyu.adapter.TabNavigatorAdapter
import com.mp.douyu.adapter.ViewPager2TabAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.MiniTabBean
import com.mp.douyu.singleClick
import com.mp.douyu.view.ViewPager2Helper
import kotlinx.android.synthetic.main.mine_fragment_msg.*
import kotlinx.android.synthetic.main.title_bar_simple.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class MineMessageFragment : VisibilityFragment() {
    private val fragments: Array<MineMessageDetailFragment> by lazy {
        arrayOf(MineMessageDetailFragment.newInstance(MineMessageDetailFragment.TYPE_MINE_REPLY),
            MineMessageDetailFragment.newInstance(MineMessageDetailFragment.TYPE_MINE_LIKED),
            MineMessageDetailFragment.newInstance(MineMessageDetailFragment.TYPE_MSG_SYSTEM))
    }
    private val tabBeans: List<MiniTabBean> by lazy {
        val followTabBean = MiniTabBean("回复").apply {
            selectedSize = 14
            normalSize = 14
        }
        val likeTabBean = MiniTabBean("点赞").apply {
            selectedSize = 14
            normalSize = 14
        }
        val systemMsgTabBean = MiniTabBean("系统").apply {
            selectedSize = 14
            normalSize = 14
        }
        listOf(followTabBean, likeTabBean, systemMsgTabBean)
    }

    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager2?.currentItem = position
            }
        })
    }
    override val layoutId: Int
        get() = R.layout.mine_fragment_msg

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        iftTitle.text = "消息中心"
        ibReturn.singleClick { finishView() }
        view_pager2?.apply {
            adapter  = ViewPager2TabAdapter(activity, fragments.toMutableList())
            currentItem = 0
            offscreenPageLimit =3
        }
        mCommonNavigatorAdapter?.apply {
            selectedColor = resources.getColor(R.color.TabSelect)
            normalColor = resources.getColor(R.color.TabUnSelect)

            val commonNavigator = CommonNavigator(context)
            commonNavigator.adapter = this
            commonNavigator.isAdjustMode = true
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
    companion object{
        fun newInstance(): MineMessageFragment {
            val fragment = MineMessageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}