package com.swbg.mlivestreaming.view.popupwindow

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BaseFragmentPagerAdapter : FragmentPagerAdapter {
    private var mFragments: List<Fragment>
    private var mTitles: ArrayList<String>? = null

    constructor(fm: FragmentManager?, mFragments: List<Fragment>) : super(fm!!,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        this.mFragments = mFragments
    }

    constructor(fm: FragmentManager?, titles: ArrayList<String>, fragments: List<Fragment>) : super(fm!!,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        mFragments = fragments
        mTitles = titles
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (mTitles != null) mTitles!![position] else null
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }
}