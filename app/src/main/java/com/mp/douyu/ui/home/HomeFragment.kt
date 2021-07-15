package com.mp.douyu.ui.home

import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mp.douyu.R
import com.mp.douyu.base.MBaseFragment
import com.mp.douyu.bean.TypeBean
import com.mp.douyu.event.TabShortVideoEvent
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.high_definition.HighDefinitionCodelessFragment
import com.mp.douyu.ui.home.more_type.MoreTypeActivity
import com.mp.douyu.ui.home.nv_info.NvInfoFragment
import com.mp.douyu.ui.home.recommended.RecommendedFragment
import com.mp.douyu.ui.home.special.SpecialTopicFragment
import com.mp.douyu.ui.video.ShortVideoActivity
import com.mp.douyu.utils.RxBus
import com.mp.douyu.utils.RxUtils
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : MBaseFragment() {

    private var previous: Int? = 0
    override val layoutId: Int
        get() = R.layout.fragment_home
    private val mFragments: ArrayList<Fragment> = arrayListOf()
    private var tabTitles: ArrayList<String> = arrayListOf()
    private var allTabTitles: ArrayList<TypeBean>? = arrayListOf()

    override fun initView() {
        ib_more.singleClick {
            startActivityWithTransition(MoreTypeActivity.instance(context))
        }
        initData()
        RxBus.getInstance().register(TabShortVideoEvent::class.java).subscribe{
            if (previous!! > 1) {
                view_pager.currentItem = 0
            }else if (previous!! < 1) {
                view_pager.currentItem = 2
            }
        }
    }

    private fun initData() {
        homeViewModel.getMorTypeData()
    }


    private fun initViewpager(cate: List<TypeBean>?) {
        allTabTitles?.clear()
        mFragments.clear()
        tabTitles.clear()

        cate?.let {
            allTabTitles?.addAll(cate)
        }
        allTabTitles?.add(0, TypeBean(getString(R.string.special_topic), "1002"))
        allTabTitles?.add(0, TypeBean(getString(R.string.recommended), "1001"))
        allTabTitles?.add(TypeBean(getString(R.string.nv_info), "1003"))
        allTabTitles?.add(1, TypeBean("短视频", cateId = "1004"))
        allTabTitles?.mapIndexed { index, typeBean ->
            typeBean.name?.let { tabTitles.add(it) }
            when (typeBean.cateId) {
                "1001" -> {
                    mFragments.add(RecommendedFragment())
                }
                "1002" -> {
                    mFragments.add(SpecialTopicFragment())
                }
                "1003" -> {
                    mFragments.add(NvInfoFragment())
                }
                "1004" -> {
                    mFragments.add(EmptyFragment())
                }
                else -> {
                    mFragments.add(HighDefinitionCodelessFragment.newInstance(typeBean))
                }
            }
        }
//        tabTitles = arrayListOf(getString(R.string.recommended),
//            getString(R.string.special_topic),
//            getString(R.string.high_definition_codeless),
//            getString(R.string.homegrown_selfile),
//            getString(R.string.special_topic),
//            getString(R.string.special_topic),
//            getString(R.string.special_topic),
//            getString(R.string.nv_info))
        view_pager.apply {
            adapter = viewPagerAdapter(parentFragmentManager, mFragments, tabTitles!!).apply {
                addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {

                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {
                        Log.e(TAG,"onPageSelected position=${position}")
                        if (position == 1) {
                            //短视频
//                            startActivityWithTransition(ShortVideoActivity.open(context))
//                            view_pager.currentItem = (position - 1)
//                            if (previous!! > tab.position) {
//                                //往前滑
//                                view_pager.currentItem = (tab.position -1)
//                            } else {
//                                //往后滑
//                                view_pager.currentItem = (tab.position +1)
//                            }
                        }
                    }
                })
            }
        }
        initTab()
    }

    private fun initTab() {
        home_tabLayout.apply {
            setupWithViewPager(view_pager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    Log.e(TAG, "onTabReselected: tab=${tab?.position}")
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    Log.e(TAG, "onTabUnselected: tab=${tab?.position}")
                    previous = tab?.position
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    Log.e(TAG, "onTabSelected: tab=${tab?.position}")
                    if (tab?.position == 1) {
                        //短视频
                        startActivityWithTransition(ShortVideoActivity.open(context))
                        RxUtils.delay(1000).subscribe{
                            if (previous!! > tab.position) {
                                //往前滑
                                view_pager.currentItem = (tab.position - 1)
                            } else {
                                //往后滑
                                view_pager.currentItem = (tab.position + 1)
                            }
                        }

                    }

                }
            })
            home_tabLayout.getTabAt(1)?.view?.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    startActivityWithTransition(ShortVideoActivity.open(context))
                }
                true
            }
        }
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _moreTypeData.observe(it, Observer {
            it?.let {
                initViewpager(it.cate)
            }
        })
    }


    private class viewPagerAdapter(fm: FragmentManager, val mFragments: List<Fragment>, val tabTitles: List<String>) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
//            return when  {
//               position > 1 -> mFragments[position - 1]
//                position<1 -> mFragments[position]
//                    else -> mFragments[position]
//            }
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabTitles[position]
        }
    }
}