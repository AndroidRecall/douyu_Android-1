package com.swbg.mlivestreaming.adapter

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swbg.mlivestreaming.ui.anchor.live.vip.LiveVipListActivity
import com.swbg.mlivestreaming.ui.video.ShortVideoFragment

class ViewPager2TabAdapter(activity: FragmentActivity?, var list: MutableList<Fragment>) : FragmentStateAdapter(activity!!) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}