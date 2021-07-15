package com.mp.douyu.ui.square

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.BuildConfig
import com.mp.douyu.GlobeStatusViewHolder
import com.mp.douyu.R
import com.mp.douyu.adapter.SquareCircleAdapter
import com.mp.douyu.adapter.TabNavigatorAdapter
import com.mp.douyu.adapter.VP2FragmentPagerAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.MiniTabBean
import com.mp.douyu.bean.SquareCircleBean
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.search.Search2Activity
import com.mp.douyu.ui.home.search.SearchActivity.Companion.SEARCH_TYPE_POST
import com.mp.douyu.ui.square.bet.BetListFragment
import com.mp.douyu.ui.square.circle.CircleDetailActivity
import com.mp.douyu.ui.square.circle.CircleListActivity
import com.mp.douyu.ui.square.comment.PublishActivity
import com.mp.douyu.ui.square.comment.PublishImageFragment.Companion.TYPE_PUBLISH_POST
import com.mp.douyu.ui.square.me.SquareDynamicFragment
import com.mp.douyu.ui.square.me.SquareMeFragment
import com.mp.douyu.ui.square.rank.RankActivity
import com.mp.douyu.utils.ActivityUtils
import com.mp.douyu.utils.RxUtils
import com.mp.douyu.view.ViewPager2Helper
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

