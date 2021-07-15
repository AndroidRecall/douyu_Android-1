package com.mp.douyu.ui.square.bet

import android.os.Bundle
import com.mp.douyu.R
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.inTransaction
import kotlinx.android.synthetic.main.square_fragment_bet_list.*

class BetListFragment() : VisibilityFragment() {

    override val layoutId: Int
        get() = R.layout.square_fragment_bet_list

    companion object{

        fun newInstance(): BetListFragment {
            val fragment = BetListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        tv_football_tab.setOnClickListener {
            setFootball()
        }
        tv_basketball_tab.setOnClickListener {
            setBasketball()
        }
        setFootball()
    }

    private fun setBasketball() {
        tv_football_tab.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_basketball_tab.setTextColor(resources.getColor(R.color.colorBtnBlue))
        childFragmentManager.inTransaction {
            add(R.id.container, BetTypeFragment.newInstance(BetTypeFragment.TYPE_BASKETBALL))
        }
    }

    private fun setFootball() {
        tv_football_tab.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_basketball_tab.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, BetTypeFragment.newInstance(BetTypeFragment.TYPE_FOOTBALL))
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    fun refreshData() {

    }
}