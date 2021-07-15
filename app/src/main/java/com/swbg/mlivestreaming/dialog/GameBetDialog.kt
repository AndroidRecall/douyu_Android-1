package com.swbg.mlivestreaming.dialog

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.swbg.mlivestreaming.GlobeStatusViewHolder
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.TabNavigatorAdapter
import com.swbg.mlivestreaming.adapter.VP2FragmentPagerAdapter
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.MiniTabBean
import com.swbg.mlivestreaming.provider.TokenProvider
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.anchor.live.bet.GameBetDetailFragment
import com.swbg.mlivestreaming.ui.anchor.live.gift.GiftDetailFragment
import com.swbg.mlivestreaming.ui.login_register.LoginActivity
import com.swbg.mlivestreaming.ui.mine.walnut.ChargeCenterActivity
import com.swbg.mlivestreaming.view.ViewPager2Helper
import kotlinx.android.synthetic.main.live_dialog_game_bet.view.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 游戏下注Dialog
 */
class GameBetDialog(context: Context) : BottomPopupView(context) {
    private val tabBeans: List<MiniTabBean> by lazy {
        val lotteryTabBean = MiniTabBean("彩票")
        val cardTabBean = MiniTabBean("棋牌")
        val realityTabBean = MiniTabBean("真人")
        val esportTabBean = MiniTabBean("电竞")
        val physicalTabBean = MiniTabBean("体育")
        val electronicTabBean = MiniTabBean("电子")
        listOf(lotteryTabBean, cardTabBean, realityTabBean,esportTabBean,physicalTabBean,electronicTabBean)
    }
    private val fragments: Array<GameBetDetailFragment> by lazy {
        arrayOf(GameBetDetailFragment.newInstance(GameBetDetailFragment.GAME_TYPE_LOTTERY),
            GameBetDetailFragment.newInstance(GameBetDetailFragment.GAME_TYPE_CARD),
            GameBetDetailFragment.newInstance(GameBetDetailFragment.GAME_TYPE_REALITY),
            GameBetDetailFragment.newInstance(GameBetDetailFragment.GAME_TYPE_ESPORT),
            GameBetDetailFragment.newInstance(GameBetDetailFragment.GAME_TYPE_PHYSICAL),
            GameBetDetailFragment.newInstance(GameBetDetailFragment.GAME_TYPE_ELECTRONIC))
    }

    override fun getImplLayoutId(): Int {
        return R.layout.live_dialog_game_bet
    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * .7f).toInt()
    }

    override fun onCreate() {
        super.onCreate()
        view_pager2?.apply {
            isUserInputEnabled = true
            adapter = VP2FragmentPagerAdapter(context as FragmentActivity,
                fragments.toMutableList() as List<Fragment>?)
        }
        mCommonNavigatorAdapter?.apply {
            selectedColor = resources.getColor(R.color.TabSelect)
            normalColor = resources.getColor(R.color.tab_normal_color)
            selectedSize = 12
            normalSize = 12
            val commonNavigator = CommonNavigator(context)
            commonNavigator.isAdjustMode = true
            commonNavigator.adapter = this
            indicator.navigator = commonNavigator
            ViewPager2Helper.bind(indicator, view_pager2)
        }
        initOnclick()
    }

    private fun initOnclick() {
        tv_recharge.singleClick {
            //充值
            if (!TokenProvider.get().hasToken()) {
                context.startActivity(LoginActivity.open(context))
            } else {
                context.startActivity(ChargeCenterActivity.open(context))
            }
        }
    }

    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager2?.currentItem = position
            }
        })
    }
}