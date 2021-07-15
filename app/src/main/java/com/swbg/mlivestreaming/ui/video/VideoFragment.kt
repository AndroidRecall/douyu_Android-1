package com.swbg.mlivestreaming.ui.video

import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.TabNavigatorAdapter
import com.swbg.mlivestreaming.adapter.ViewPager2TabAdapter
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.MiniTabBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.search.SearchActivity
import com.swbg.mlivestreaming.ui.home.search.SearchActivity.Companion.SEARCH_TYPE_SHORT_VIDEO
import com.swbg.mlivestreaming.ui.home.search.SearchActivity.Companion.SEARCH_TYPE_VIDEO
import com.swbg.mlivestreaming.ui.square.comment.PublishActivity
import com.swbg.mlivestreaming.ui.square.comment.PublishImageFragment.Companion.TYPE_PUBLISH_VIDEO
import com.swbg.mlivestreaming.view.ViewPager2Helper
import kotlinx.android.synthetic.main.video_fragment_video.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class VideoFragment : VisibilityFragment() {
    private val fragments: Array<ShortVideoFragment> by lazy {
        arrayOf(ShortVideoFragment.newInstance(ShortVideoFragment.TYPE_RECOMMEND),
            ShortVideoFragment.newInstance(ShortVideoFragment.TYPE_HOT))
    }
    private val tabBeans: List<MiniTabBean> by lazy {
        val recommendTabBean = MiniTabBean("推荐")
        val hotTabBean = MiniTabBean("热门")
        listOf(recommendTabBean, hotTabBean)
    }
    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager2?.currentItem = position
            }
        })
    }
    override val layoutId: Int
        get() = R.layout.video_fragment_video

    override fun initView() {


    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        ibReturn.singleClick {
            finishView()
        }
        tv_search.singleClick {
            startActivityWithTransition(SearchActivity.open(activity,SEARCH_TYPE_SHORT_VIDEO))
        }
        tv_publish.singleClick {
            startActivityWithTransition(PublishActivity.open(activity, TYPE_PUBLISH_VIDEO))
        }
        view_pager2?.apply {
            adapter = ViewPager2TabAdapter(activity, fragments.toMutableList())
            mCommonNavigatorAdapter.apply {
                selectedColor = resources.getColor(R.color.white)
                normalColor = resources.getColor(R.color.white)
                val commonNavigator = CommonNavigator(context)
                selectedSize = 16
                normalSize = 14
                commonNavigator.adapter = this
                commonNavigator.isAdjustMode = true
                indicator2.navigator = commonNavigator
                ViewPager2Helper.bind(indicator2, view_pager2)
            }
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

}