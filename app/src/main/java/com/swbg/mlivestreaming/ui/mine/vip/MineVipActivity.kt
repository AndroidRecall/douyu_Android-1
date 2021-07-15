package com.swbg.mlivestreaming.ui.mine.vip

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.MyVipBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_my_vip.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class MineVipActivity : MBaseActivity() {
    private var mFragment: ArrayList<Fragment> = arrayListOf()
    private var mTitles: ArrayList<String> = arrayListOf()

    override val contentViewLayoutId: Int
        get() = R.layout.activity_my_vip

    override fun initView() {
        ibReturn.singleClick {
            finishView()
        }
        titleBar.setBackgroundResource(R.color.color00000000)
        Glide.with(this).load(R.mipmap.return_back_white).centerInside().into(ibReturn)

        home_tabLayout.apply {
            setupWithViewPager(view_pager)
        }

        mineViewModel.getVipList()
    }


    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getVipList.observe(it, Observer{
            it?.let {
                it.mapIndexed { index, myVipBean ->
                    initViewPage(index,myVipBean)
                }
//                for (i in mFragment.size..4){
//                    initViewPage(i,MyVipBean())
//                }
                view_pager.apply {
                    adapter = BaseFragmentPagerAdapter(supportFragmentManager,
                        mTitles,
                        fragments = mFragment)
                    currentItem = 0
                }
            }
        })
    }

    private fun initViewPage(index: Int, myVipBean: MyVipBean?) {
        mFragment.add(MyVipFragment.newInstance(index,myVipBean))
        mTitles.add(myVipBean?.title.toString())
    }


    companion object {
        fun open(context: Context?) : Intent {
            return Intent(context,MineVipActivity::class.java)
        }
    }
}
