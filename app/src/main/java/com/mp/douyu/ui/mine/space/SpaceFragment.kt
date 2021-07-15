package com.mp.douyu.ui.mine.space

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.adapter.TabNavigatorAdapter
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.CommonUserBean
import com.mp.douyu.bean.MiniTabBean
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.relation.RelationManager
import com.mp.douyu.singleClick
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.ui.mine.dynamic.MineAVFragment
import com.mp.douyu.ui.mine.dynamic.MineDynamicFragment
import com.mp.douyu.ui.mine.dynamic.MinePlanFragment
import com.mp.douyu.ui.mine.relation.RelationActivity
import com.mp.douyu.ui.square.RelationViewModel
import com.mp.douyu.ui.square.circle.CircleListActivity
import com.mp.douyu.ui.square.circle.DynamicViewModel
import com.mp.douyu.view.AppBarStateChangeListener
import com.mp.douyu.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_user_space.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

class SpaceFragment(var uid: Int = 0) : VisibilityFragment() {
    private var isFollow = 0
    private var appBarstate = AppBarStateChangeListener.State.EXPANDED
    private val fragments: Array<VisibilityFragment> by lazy {
        arrayOf(MineDynamicFragment.newInstance(uid),
            MinePlanFragment.newInstance(uid),
            MineAVFragment.newInstance(uid))
    }

    private val tabBeans: List<MiniTabBean> by lazy {
        val dynamicTabBean = MiniTabBean("动态")
        val planTabBean = MiniTabBean("计划")
        val avTabBean = MiniTabBean("AV")
        listOf(dynamicTabBean, planTabBean, avTabBean)
    }
    private val mCommonNavigatorAdapter: TabNavigatorAdapter by lazy {
        TabNavigatorAdapter(tabBeans, object : TabNavigatorAdapter.OnTabItemClickListener {
            override fun onTabItemClick(position: Int) {
                view_pager?.currentItem = position
            }
        })
    }

    companion object {
        const val EXTRA_UID = "uid"
        const val EXTRA_USER_DATA = "user_data"
        fun newInstance(uid: Int = 0): SpaceFragment {
            val fragment = SpaceFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_UID, uid)
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(bean: CommonUserBean? = null): SpaceFragment {
            val fragment = SpaceFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_USER_DATA, bean)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_user_space

    override fun initView() {
    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        BarUtils.setNavBarColor(activity!!, resources.getColor(R.color.color00000000))
        BarUtils.setStatusBarLightMode(activity!!, false)
        statusBarView.layoutParams.height = BarUtils.getStatusBarHeight()
        statusBarView2.layoutParams.height = BarUtils.getStatusBarHeight()
        uid = arguments?.getInt(EXTRA_UID)!!.apply {
            if (uid > 0) {
                //他人信息
            } else {
                //我的信息
            }
        }
        app_bar_layout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                appBarstate = state!!
                if (appBarstate == AppBarStateChangeListener.State.EXPANDED) {
                    title_bar.visibility = View.INVISIBLE
                    cl_title_bar.setBackgroundColor(resources.getColor(R.color.transparent))
                } else if (appBarstate == AppBarStateChangeListener.State.COLLAPSED) {
                    title_bar.visibility = View.VISIBLE
                    cl_title_bar.setBackgroundResource(R.mipmap.bg_home_page_top)
                }
            }
        })
        view_pager?.apply {
            adapter = BaseFragmentPagerAdapter(childFragmentManager, fragments.toList())
            offscreenPageLimit = 3
            currentItem = 0
        }
        mCommonNavigatorAdapter.apply {
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
        onClickListener()
        mineViewModel.getUserInfo2(hashMapOf("uid" to "${uid}"))
    }

    private fun refreshFollowState(id: Int?) {
        if (RelationManager.instance.follows.size > 0) {
            for (userBean in RelationManager.instance.follows) {
                if (userBean.id == id) {
                    isFollow = 1
                    break
                } else {
                    isFollow = 0
                }
            }
        } else {
            isFollow = 0
        }
        if (isFollow == 1) {
            tv_follow.text = "已关注"
            tv_follow.setTextColor(resources.getColor(R.color.colorDarkGray))
            tv_follow.setBackgroundResource(R.drawable.btn_gray_c30_bg)
            tv_small_follow.text = "已关注"
        } else {
            tv_follow.text = "关注"
            tv_follow.setBackgroundResource(R.drawable.btn_gradient_201f1f_bg_layer)
            tv_follow.setTextColor(resources.getColor(R.color.white))
            tv_small_follow.text = "关注"
        }
    }

    private fun onClickListener() {
        ibReturn.singleClick {
            activity?.finish()
        }
        tv_follow.singleClick {
            handlerFollow()
        }
        tv_small_follow.singleClick {
            handlerFollow()
        }
        ll_people.singleClick {
            startActivityWithTransition(CircleListActivity.open(context,
                CircleListActivity.CIRCLE_TYPE_USER,
                uid))
        }
        ll_post.singleClick {
            startActivityWithTransition(RelationActivity.open(context,
                RelationActivity.RELATION_TYPE_FOLLOW,
                uid))
        }
        ll_fans.singleClick {
            startActivityWithTransition(RelationActivity.open(context,
                CircleListActivity.CIRCLE_TYPE_USER,
                uid))
        }
    }

    private fun handlerFollow() {
        when (isFollow) {
            0 -> follow()
            else -> unFollow()
        }
    }

    private fun follow() {
        dynamicViewModel.follow(hashMapOf("to_id" to "${uid}"))
    }

    private fun unFollow() {
        dynamicViewModel.cancelFollow(hashMapOf("to_id" to "${uid}"))
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getUserInfo2.observe(it, Observer {
            if (it != null && it.size > 0) {
                it[0].apply {
                    context?.let { context ->
                        if (avatar?.contains("http") == false) {
                            avatar = BuildConfig.IMAGE_BASE_URL+avatar
                        }
                        Glide.with(context).load(avatar)
                            .placeholder(R.mipmap.default_avatar).error(R.mipmap.default_avatar)
                            .circleCrop().into(iv_big_header)
                        Glide.with(context).load( avatar)
                            .placeholder(R.mipmap.default_avatar).error(R.mipmap.default_avatar)
                            .circleCrop().into(iv_small_header)
                    }
                    tv_small_name.text = "${nickname}"
                    tv_big_name.text = "${nickname}"
                    tv_circle.text = "${circle_count}"
                    tv_follow_num.text = "${follow_count}"
                    tv_fans.text = "${fans_count}"
                    if (uid != 0 && StoredUserSources.getUserInfo2()?.id != uid) {
                        tv_follow.visibility = View.VISIBLE
                        tv_small_follow.visibility = View.VISIBLE
                        refreshFollowState(id)
                    }
                }
            }
        })
    }
    private val dynamicViewModel by getViewModel(DynamicViewModel::class.java) {
        _followResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllFollowsData(hashMapOf())

            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllFollowsData(hashMapOf())

            }
        })
    }
    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        followsData.observe(it, Observer {

            it?.let {
                RelationManager.instance.follows = it.toMutableList()
                if (uid > 0) {
                    refreshFollowState(uid)
                }

            }
        })
    }
}