package com.mp.douyu.ui.square.circle

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnMultiListener
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.adapter.LiveAudienceInfoAdapter
import com.mp.douyu.adapter.TabNavigatorAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.AdvBean
import com.mp.douyu.bean.CommonUserBean
import com.mp.douyu.bean.MiniTabBean
import com.mp.douyu.bean.SquareCircleBean
import com.mp.douyu.initializeWidth
import com.mp.douyu.singleClick
import com.mp.douyu.ui.activity.ActivityViewModel
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.ui.square.SquareViewModel
import com.mp.douyu.ui.square.comment.PublishActivity
import com.mp.douyu.ui.square.comment.PublishImageFragment
import com.mp.douyu.ui.square.me.SquareDynamicFragment
import com.mp.douyu.ui.square.rank.RankActivity
import com.mp.douyu.utils.DrawableUtils
import com.mp.douyu.view.AppBarStateChangeListener
import com.mp.douyu.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.circle_fragment_detail.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import kotlin.random.Random

/**
 * 圈子详情
 */
class CircleDetailFragment : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;
    private var mOffset = 0
    private var mScrollY = 0
    private var circleBean: SquareCircleBean? = null
    var listAvatars: MutableList<CommonUserBean> = arrayListOf()
    private var appBarstate = AppBarStateChangeListener.State.EXPANDED
    private val fragments: Array<SquareDynamicFragment> by lazy {
        arrayOf(SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_CIRCLE_ALL,circleBean?.id),
            SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_CIRCLE_ELITE,circleBean?.id))
    }
    private val tabBeans: List<MiniTabBean> by lazy {
        val allTabBean = MiniTabBean("全部")
        val eliteTabBean = MiniTabBean("精华")
        listOf(allTabBean, eliteTabBean)
    }
    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager?.currentItem = position
            }
        })
    }
    override val layoutId: Int
        get() = R.layout.circle_fragment_detail

    override fun initView() {
    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        statusBarView.layoutParams.height = BarUtils.getStatusBarHeight()
        statusBarView2.layoutParams.height = BarUtils.getStatusBarHeight()
        arguments?.apply {
            circleBean = getParcelable<SquareCircleBean>(CIRCLE_DATA)?.apply {
                context?.let {
                    Glide.with(it).load(BuildConfig.IMAGE_BASE_URL + avatar).circleCrop()
                        .placeholder(R.mipmap.default_avatar).into(iv_big_header)
                    Glide.with(it).load(BuildConfig.IMAGE_BASE_URL + avatar).circleCrop()
                        .placeholder(R.mipmap.default_avatar).into(iv_small_header)
                    Glide.with(it).load(BuildConfig.IMAGE_BASE_URL + banner)
                        .placeholder(R.mipmap.bg_home_page_top).into(iv_bg)

                    //之前说广告信息从这里面拿，但后面接口又不返回，又给我提bug，所以直用广告接口去拿广告
//                    Glide.with(it).load(BuildConfig.IMAGE_BASE_URL + adv_img).centerCrop()
//                        .placeholder(R.mipmap.bg_home_page_top).into(iv_banner)
//                    iv_banner.singleClick {it1->
//                        if (adv_status == 1) {
//                            //1跳转
//                            var intent =  Intent(Intent.ACTION_VIEW, Uri.parse("${adv_url}"));
//                            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//                            it.startActivity(intent)
//                        }else{
//                            //2跳转到详情
//
//                        }
//
////                                ActivityUtils.jumpToWebView(advBean.url, it1)
//                    }
                }


                tv_big_name.text = title
                tv_small_name.text = title
                tv_detail.text = description
                tv_people_num.text = "${user_count}"
                tv_post_num.text = "${post_count}"
                refreshJoinUi(this)
                fl_join.setOnClickListener {
                    when (is_join) {
                        0 -> squareViewModel.joinCircle(hashMapOf("circle_id" to "${id}"))
                        else -> squareViewModel.exitCircle(hashMapOf("circle_id" to "${id}"))
                    }
                }
            }
        }
        initOnclickListener()
        view_pager?.apply {
            adapter = BaseFragmentPagerAdapter(childFragmentManager, fragments.toList())
            currentItem = 0
        }
        mCommonNavigatorAdapter?.apply {
            selectedColor = resources.getColor(R.color.TabSelect)
            normalColor = resources.getColor(R.color.tab_normal_color)
            val commonNavigator = CommonNavigator(context)
            selectedSize = 18
            normalSize = 16
            commonNavigator.isAdjustMode = true
            commonNavigator.adapter = this
            indicator.navigator = commonNavigator
            ViewPagerHelper.bind(indicator, view_pager)
        }
        /*refreshLayout.setOnRefreshListener {
            fragments[view_pager.currentItem].refreshData()
            refreshLayout.finishRefresh(500)
        }*/
        refreshLayout.setOnMultiListener(object : SimpleMultiPurposeListener() {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                fragments[view_pager.currentItem].refreshData()
                refreshLayout.finishRefresh(500)
            }

            override fun onHeaderMoving(header: RefreshHeader?, isDragging: Boolean, percent: Float, offset: Int, headerHeight: Int, maxDragHeight: Int) {
                Log.e(TAG,"onHeaderMoving offset=${offset} percent=${percent} headerHeight=${headerHeight} maxDragHeight=${maxDragHeight}")
                var rate=(1+percent*0.25f)
                iv_bg.scaleX = rate
                iv_bg.scaleY = rate
            }
        })
        app_bar_layout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                Log.d("appBarLayout STATE", state?.name);
                appBarstate = state!!
                refreshTitleBarState()
            }
        })
        recycler_redlist.apply {
            adapter = audienceAdapter.apply {
                addAll(listAvatars)
                initializeWidth(data, recycler_redlist, 40, 3)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        mineViewModel.getUserAvatar()
        //拿广告 圈子type传2
        activityViewModel.getAdvList(hashMapOf("type" to "2"))
    }

    private fun refreshTitleBarState() {
        if (appBarstate == AppBarStateChangeListener.State.EXPANDED) {
            //展开状态
            title_bar.visibility = View.INVISIBLE
            iv_small_bg.setImageResource(R.color.transparent)
            //显示提示按钮
            tv_top_join.visibility = View.GONE
            iv_prompt.visibility = View.VISIBLE
        } else if (appBarstate == AppBarStateChangeListener.State.COLLAPSED) {
            //折叠状态
            title_bar.visibility = View.VISIBLE
            context?.let {
                Glide.with(it).load(BuildConfig.IMAGE_BASE_URL + circleBean?.banner).centerCrop()
                    .placeholder(R.mipmap.bg_home_page_top).into(iv_small_bg)
            }
            if (circleBean?.is_join == 0) {
                //未加入 显示加入按钮
                tv_top_join.visibility = View.VISIBLE
                iv_prompt.visibility = View.GONE
            } else {
                //已加入 显示提示按钮
                tv_top_join.visibility = View.GONE
                iv_prompt.visibility = View.VISIBLE
            }
        } else {

            //中间状态

        }
    }

    private fun initOnclickListener() {
        ibReturn.singleClick { activity?.finish() }
        iv_prompt.singleClick {
            //圈子公告
            startActivityWithTransition(CircleExplainActivity.open(context, circleBean?.explain!!))
        }
        tv_top_join.singleClick {
            //加入圈子
            squareViewModel.joinCircle(hashMapOf("circle_id" to "${id}"))
        }
        cv_redlist.singleClick {
            //赌神排行榜
            startActivityWithTransition(RankActivity.open(activity))
        }
        iv_banner.singleClick {
            //banner
        }
        iv_edit.singleClick {
            startActivityWithTransition(PublishActivity.open(activity,type = PublishImageFragment.TYPE_PUBLISH_POST,circleId = circleBean?.id))
        }
    }

    private fun refreshJoinUi(bean: SquareCircleBean?) {
        tv_join.text = "${if (bean?.is_join == 0) "加入" else "已加入"}"
        tv_join.setTextColor(if (bean?.is_join == 0) resources.getColor(R.color.white)
        else resources.getColor(R.color.colorC8LightGray))
        if (bean?.is_join == 0) {
            DrawableUtils.setDrawable(drawable = R.mipmap.icon_join, textView = tv_join)
        } else {
            DrawableUtils.clearDrawable(tv_join)
        }
        fl_join.isSelected = bean?.is_join == 0
        refreshTitleBarState()
    }
    val audienceAdapter by lazy {
        LiveAudienceInfoAdapter({}, context).apply {

        }
    }
    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    companion object {
        const val CIRCLE_DATA = "circle_data"
        fun newInstance(datas: SquareCircleBean?): CircleDetailFragment {
            val fragment = CircleDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(CIRCLE_DATA, datas)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val squareViewModel by getViewModel(SquareViewModel::class.java) {

        _joinCircleResult.observe(it, Observer {
            it?.let {
                circleBean?.is_join = 1
                refreshJoinUi(circleBean)
            }
        })
        _exitCircleResult.observe(it, Observer {
            it?.let {
                circleBean?.is_join = 0
                refreshJoinUi(circleBean)
            }
        })
    }
    private val activityViewModel by getViewModel(ActivityViewModel::class.java) {
        _getAdvList.observe(it, Observer {
            it?.let { list ->
                if (list.size == 0) {
                    iv_banner.visibility = View.GONE
                }
                list.forEach { advBean: AdvBean? ->
                    setAdView(advBean)
                }
            }
        })
    }

    private fun setAdView(advBean: AdvBean?) {
        if (advBean != null) {
            context?.let { it1 ->
                if (advBean?.cover?.contains("http") == false) {
                    advBean?.cover = BuildConfig.IMAGE_BASE_URL + advBean?.cover
                }
                Glide.with(it1).load(advBean.cover).centerCrop().placeholder(R.mipmap.bg_home_page_top).into(iv_banner)
                iv_banner.singleClick {
                    var intent = Intent(Intent.ACTION_VIEW, Uri.parse("${advBean.url}"));
                    intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    it1.startActivity(intent)
    //                                ActivityUtils.jumpToWebView(advBean.url, it1)
                }
            }
            return
        }
    }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getUserAvatar.observe(it, Observer {
            it?.let {
                it.male?.let {
                    for (el in it) {
                        listAvatars.add(CommonUserBean(el.url))
                    }
                }
                it.female?.let {
                    for (el in it) {
                        listAvatars.add(CommonUserBean(el.url))
                    }
                }
                var randomList: MutableList<CommonUserBean> = randomAvatarList(listAvatars)
                audienceAdapter.refresh(randomList, null)
            }
        })

    }

    /**
     * 头像自己随机，后台不给数据
     */
    private fun randomAvatarList(list: MutableList<CommonUserBean>): MutableList<CommonUserBean> {
        var randomList: MutableList<CommonUserBean> = arrayListOf()
        for (index in 0..3) {
            var nextInt = Random.nextInt(list.size)
            randomList.add(list[nextInt])
        }
        return randomList
    }
    open class SimpleMultiPurposeListener:OnMultiListener{
        override fun onFooterMoving(footer: RefreshFooter?, isDragging: Boolean, percent: Float, offset: Int, footerHeight: Int, maxDragHeight: Int) {

        }

        override fun onHeaderStartAnimator(header: RefreshHeader?, headerHeight: Int, maxDragHeight: Int) {
        }

        override fun onFooterReleased(footer: RefreshFooter?, footerHeight: Int, maxDragHeight: Int) {
        }

        override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        }

        override fun onHeaderMoving(header: RefreshHeader?, isDragging: Boolean, percent: Float, offset: Int, headerHeight: Int, maxDragHeight: Int) {
        }

        override fun onFooterFinish(footer: RefreshFooter?, success: Boolean) {
        }

        override fun onFooterStartAnimator(footer: RefreshFooter?, footerHeight: Int, maxDragHeight: Int) {
        }

        override fun onHeaderReleased(header: RefreshHeader?, headerHeight: Int, maxDragHeight: Int) {
        }

        override fun onLoadMore(refreshLayout: RefreshLayout) {
        }

        override fun onRefresh(refreshLayout: RefreshLayout) {
        }

        override fun onHeaderFinish(header: RefreshHeader?, success: Boolean) {
        }
    }
}