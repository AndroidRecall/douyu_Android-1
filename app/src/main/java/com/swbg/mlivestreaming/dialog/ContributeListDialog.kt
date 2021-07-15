package com.swbg.mlivestreaming.dialog

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.VP2FragmentPagerAdapter
import com.swbg.mlivestreaming.bean.LiveRanksBean
import com.swbg.mlivestreaming.ui.anchor.live.contribute.ContributeDetailFragment
import kotlinx.android.synthetic.main.live_dialog_contribute.view.*

/**
 * 贡献榜Dialog
 */
class ContributeListDialog(context: Context, var liveRanksBean: LiveRanksBean) :
    BottomPopupView(context) {
    private var fragments: MutableList<ContributeDetailFragment> = arrayListOf()
    override fun getImplLayoutId(): Int {
        return R.layout.live_dialog_contribute
    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * .80f).toInt()
    }

    override fun onCreate() {
        super.onCreate()
        liveRanksBean?.let { it->
            it.day?.let { day ->
                fragments.add(ContributeDetailFragment.newInstance(day))
            }
            it.week?.let { week ->
                fragments.add(ContributeDetailFragment.newInstance(week))
            }
            it.month?.let { month ->
                fragments.add(ContributeDetailFragment.newInstance(month))
            }
        }
        view_pager2?.apply {
            isUserInputEnabled = false
            adapter = VP2FragmentPagerAdapter(context as FragmentActivity,
                fragments.toMutableList() as List<Fragment>?)
        }
        tv_day_tab?.setOnClickListener {
            if (view_pager2.currentItem != 0) {
                tv_day_tab.isSelected = true
                tv_week_tab.isSelected = false
                tv_month_tab.isSelected = false
                view_pager2.currentItem = 0
            }
        }
        tv_week_tab?.setOnClickListener {
            if (view_pager2.currentItem != 1) {
                tv_day_tab.isSelected = false
                tv_week_tab.isSelected = true
                tv_month_tab.isSelected = false
                view_pager2.currentItem = 1
            }
        }
        tv_month_tab?.setOnClickListener {
            if (view_pager2.currentItem != 2) {
                tv_day_tab.isSelected = false
                tv_week_tab.isSelected = false
                tv_month_tab.isSelected = true
                view_pager2.currentItem = 2
            }
        }
        tv_day_tab?.isSelected = true
    }
}