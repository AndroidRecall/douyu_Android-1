package com.swbg.mlivestreaming.ui.square.recommend

import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.inTransaction
import com.swbg.mlivestreaming.ui.square.me.SquareDynamicFragment

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