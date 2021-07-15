package com.swbg.mlivestreaming.ui.anchor.live.vip

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.TabNavigatorAdapter
import com.swbg.mlivestreaming.adapter.ViewPager2TabAdapter
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.MiniTabBean
import com.swbg.mlivestreaming.ui.anchor.live.LiveViewModel
import com.swbg.mlivestreaming.view.ViewPager2Helper
import kotlinx.android.synthetic.main.live_activity_vip_list.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class LiveVipListActivity : MBaseActivity() {
    private var fragments: MutableList<VipFragment> = arrayListOf()
    private var tabBeans: MutableList<MiniTabBean> = arrayListOf()
    private var mCommonNavigatorAdapter: TabNavigatorAdapter? = null
    override val contentViewLayoutId: Int
        get() = R.layout.live_activity_vip_list

    override fun initView() {
        loadData()
    }

    private fun initTabData() {
        view_pager2?.apply {
            isUserInputEnabled = true
            adapter = ViewPager2TabAdapter(this@LiveVipListActivity, fragments.toMutableList())
            currentItem = 0
            offscreenPageLimit = 4
        }
        mCommonNavigatorAdapter =
            TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
                override fun onTabItemClick(position: Int) {
                    view_pager2?.currentItem = position
                }
            }).apply {
                selectedColor = resources.getColor(R.color.colorBtnBlue)
                normalColor = resources.getColor(R.color.tab_normal_color)

                val commonNavigator = CommonNavigator(application)
                commonNavigator.adapter = this
                commonNavigator.isAdjustMode = true
                indicator.navigator = commonNavigator
                ViewPager2Helper.bind(indicator, view_pager2)
            }
    }

    private fun loadData() {
        liveViewModel.getVipData(hashMapOf())
    }

    companion object {
        fun open(context: Context?): Intent {
            return Intent(context, LiveVipListActivity::class.java).apply {}
        }
    }

    private val liveViewModel by getViewModel(LiveViewModel::class.java) {
        _vipData.observe(it, Observer {
            it?.let {
                for ((index, bean) in it.withIndex()) {
                    tabBeans.add(MiniTabBean(bean?.title!!).apply {
                        selectedSize = 14
                        normalSize = 14
                    })
                    fragments.add(VipFragment.newInstance(bean))
                }
                initTabData()
            }
        })
    }
}