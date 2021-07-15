package com.mp.douyu.ui.square.recommend

import com.mp.douyu.R
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.inTransaction
import com.mp.douyu.ui.square.me.SquareDynamicFragment

class SquareRecommendFragment : VisibilityFragment() {
    override val layoutId: Int
        get() = R.layout.square_activity_recommend

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        childFragmentManager.inTransaction{
            add(R.id.container,SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_MINE_RECOMMEND))
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }


}