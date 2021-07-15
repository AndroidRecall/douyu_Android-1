package com.swbg.mlivestreaming.ui.square

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.GlobeStatusViewHolder
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.SquareCircleAdapter
import com.swbg.mlivestreaming.adapter.TabNavigatorAdapter
import com.swbg.mlivestreaming.adapter.VP2FragmentPagerAdapter
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.MiniTabBean
import com.swbg.mlivestreaming.bean.SquareCircleBean
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.search.Search2Activity
import com.swbg.mlivestreaming.ui.home.search.SearchActivity
import com.swbg.mlivestreaming.ui.home.search.SearchActivity.Companion.SEARCH_TYPE_POST
import com.swbg.mlivestreaming.ui.square.bet.BetListFragment
import com.swbg.mlivestreaming.ui.square.circle.CircleDetailActivity
import com.swbg.mlivestreaming.ui.square.circle.CircleListActivity
import com.swbg.mlivestreaming.ui.square.comment.PublishActivity
import com.swbg.mlivestreaming.ui.square.comment.PublishImageFragment.Companion.TYPE_PUBLISH_POST
import com.swbg.mlivestreaming.ui.square.me.SquareDynamicFragment
import com.swbg.mlivestreaming.ui.square.me.SquareMeFragment
import com.swbg.mlivestreaming.ui.square.rank.RankActivity
import com.swbg.mlivestreaming.utils.ActivityUtils
import com.swbg.mlivestreaming.utils.RxUtils
import com.swbg.mlivestreaming.view.ViewPager2Helper
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.square_fragment_home.*
import kotlinx.android.synthetic.main.square_fragment_home.refreshLayout
import kotlinx.android.synthetic.main.square_fragment_home.tv_scroll
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class SquareHomeFragment : VisibilityFragment() {
    private val fragments: Array<VisibilityFragment> by lazy {
        arrayOf(SquareMeFragment(),
            SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_MINE_RECOMMEND),
            BetListFragment.newInstance())
    }
    private val tabBeans: List<MiniTabBean> by lazy {
        val meTabBean = MiniTabBean("我的")
        val recommendTabBean = MiniTabBean("推荐")
        val listTabBean = MiniTabBean("赌神榜")
        listOf(meTabBean, recommendTabBean, listTabBean)
    }

    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager2?.currentItem = position
            }
        })
    }
    val mAdapter by lazy {
        SquareCircleAdapter({ position ->
            //点击"推荐圈子"头像
            onCircleClick(position)
        }, context).apply {
            add(SquareCircleBean(title = "${BuildConfig.APP_NAME_}客服"))
        }
    }

    private fun onCircleClick(position: Int) {
        val bean = mAdapter[position]
        when (bean.title) {
            "${BuildConfig.APP_NAME_}客服" -> {
                if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
                    val settingData = StoredUserSources.getSettingData()
                    settingData?.contact?.let {
                        context?.let { it1 -> ActivityUtils.jumpToWebView(it, it1) }
                    }
                }
            }
            "全部" -> startActivity(CircleListActivity.open(activity))
            else -> startActivity(CircleDetailActivity.open(activity, bean))
        }
    }

    override val layoutId: Int
        get() = R.layout.square_fragment_home

    override fun initView() {

    }

    override fun onVisibleFirst() {
        tv_scroll.post(Runnable {
            tv_scroll.text = StoredUserSources.getSettingData()?.headlines
        })
        iv_edit.singleClick {
            startActivityWithTransition(PublishActivity.open(activity, type = TYPE_PUBLISH_POST))
        }
        iv_rank.setOnClickListener {
            startActivityWithTransition(RankActivity.open(activity))
        }
        tv_search.setOnClickListener {
            startActivityWithTransition(Search2Activity.open(activity, type = SEARCH_TYPE_POST))
//            startActivityWithTransition(SearchActivity.open(activity, type = SEARCH_TYPE_POST))
        }
        recycle_circle.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = mAdapter
        }

        view_pager2?.apply {
            adapter = VP2FragmentPagerAdapter(requireActivity(), fragments.toList())
            offscreenPageLimit = 2
        }
        mCommonNavigatorAdapter.apply {
            selectedColor = resources.getColor(R.color.TabSelect)
            normalColor = resources.getColor(R.color.tab_normal_color)
            val commonNavigator = CommonNavigator(context)
            commonNavigator.adapter = this
            indicator.navigator = commonNavigator
            ViewPager2Helper.bind(indicator, view_pager2)
        }
       RxUtils.delay(300).subscribe {
           view_pager2?.currentItem = 1
       }
        squareViewModel.getAllSquareCircleData(hashMapOf())
        refreshLayout.setRefreshHeader(ClassicsHeader(context))
        refreshLayout.setOnRefreshListener {
            fragments[view_pager2.currentItem].let {
                when (it) {
                    is SquareMeFragment -> it.refreshData()
                    is SquareDynamicFragment -> it.refreshData()
                    is BetListFragment -> it.refreshData()
                }
                refreshLayout.finishRefresh(500)
            }
        }
    }

    private val squareViewModel by getViewModel(SquareViewModel::class.java) {
        squareCircleData.observe(it, Observer {
            it?.data?.let { it1 ->
                mAdapter.removeAll(mAdapter.subList(1, mAdapter.size))
                if (it1.size > 3) {
                    mAdapter.addAll(it1.subList(0, 3))
                    mAdapter.add(4, SquareCircleBean(title = "全部"))

                } else {
                    mAdapter.addAll(it1)
                    mAdapter.add(SquareCircleBean(title = "全部"))
                }
            }
        })
    }


}

