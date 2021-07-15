package com.swbg.mlivestreaming.ui.home.nv_info

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.NvDetailBean
import com.swbg.mlivestreaming.bean.NvInfoBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.HomeViewModel
import com.swbg.mlivestreaming.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_nv_info_detail.*
import kotlinx.android.synthetic.main.activity_nv_info_detail.iv_avatar
import kotlinx.android.synthetic.main.title_bar_simple.*

class NvInfoDetailActivity : MBaseActivity() {
    private var mFragment: List<Fragment>? = null
    private var mNInfo: NvInfoBean? = null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_nv_info_detail

    override fun initView() {

        mFragment = listOf(NvInfoProductFragment.newInstance("${getDataBean?.id}"),
            NvInfoIntroduceFragment.newInstance(mNInfo?.summary))
        titleBar.setBackgroundColor(ContextCompat.getColor(this, R.color.color00000000))
        ibReturn.singleClick {
            onBackPressed()
        }

        home_tabLayout.apply {
            setupWithViewPager(view_pager)
        }
        view_pager.apply {
            adapter = BaseFragmentPagerAdapter(supportFragmentManager,
                arrayListOf("女优作品", "女优简介"),
                fragments = mFragment!!)
            currentItem = 0
        }

        homeViewModel.getNvInfo(hashMapOf("id" to "${getDataBean?.id}"))
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getNvInfoData.observe(it, Observer {
            it?.apply {
                mNInfo = it
                tv_name.text = name
                tv_birthday.text = birthday
                tv_local.text = region
                tv_height.text = "${height}CM"
                tv_cup.text = "罩杯${cup}"
                Glide.with(this@NvInfoDetailActivity).load(getDataBean?.cover).error(R.mipmap.default_avatar).into(iv_avatar)
                (mFragment?.get(1) as NvInfoIntroduceFragment).setContent(summary)
                measurements?.split("-")?.apply {
                    try {
                        tv_chest_c.text = "${this[0]}CM"
                        tv_wai_c.text = "${this[1]}CM"
                        tv_hip_c.text = "${this[2]}CM"
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }

    private val getDataBean by lazy {
        intent.getParcelableExtra<NvDetailBean>(DATA_BEAN)
    }

    companion object {
        const val DATA_BEAN = "DATA_BEAN"
        fun open(context: Context?, nvDetailBean: NvDetailBean): Intent {
            return Intent(context, NvInfoDetailActivity::class.java).putExtra(DATA_BEAN,
                nvDetailBean)
        }
    }
}
