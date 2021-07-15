package com.mp.douyu.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2TabAdapter(activity: FragmentActivity?, var list: MutableList<Fragment>) : FragmentStateAdapter(activity!!) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}