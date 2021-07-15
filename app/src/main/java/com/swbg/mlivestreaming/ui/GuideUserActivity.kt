package com.swbg.mlivestreaming.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.FirstLoadUploadBean
import com.swbg.mlivestreaming.matchWaterDropScreen
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.setActivityImmersion
import com.swbg.mlivestreaming.ui.guide.GuideFragment
import com.swbg.mlivestreaming.ui.login_register.login.LoginViewModel
import com.swbg.mlivestreaming.utils.DeviceUtil
import com.swbg.mlivestreaming.view.popupwindow.BaseFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_guide_user.*

class GuideUserActivity : MBaseActivity() {
    val mFragments = arrayListOf<Fragment>()
    val mImages = arrayListOf<Int>()
    override val contentViewLayoutId: Int
        get() = R.layout.activity_guide_user

    override fun initView() {
        matchWaterDropScreen(this)
        setActivityImmersion(this)

        StoredUserSources.putIsFirstUpdate(FirstLoadUploadBean(isFirstLoad = "0"))

        initData()
    }

    private fun initData() {
        mImages.add(R.mipmap.guide_one)
        mImages.add(R.mipmap.guide_two)
        mImages.add(R.mipmap.guide_three)
        mImages.add(R.mipmap.guide_four)
        mImages.add(R.mipmap.guide_five)
        mImages.mapIndexed { index, i ->
            mFragments.add(GuideFragment.newInstance(index, mImages))
        }
        view_pager.apply {
            adapter = BaseFragmentPagerAdapter(supportFragmentManager, mFragments)
            currentItem = 0
        }

      /*  loginViewModel.getChannelId(hashMapOf(
            "clipboard" to DeviceUtil.getClipboard(this),
            "screen" to "${DeviceUtil.deviceHeight(this)}x${DeviceUtil.deviceWidth(this)}",
            "os" to "Android${DeviceUtil.getBuildVersion()}",
            "model" to "${DeviceUtil.getPhoneModel()}"
        ))*/
    }

    private val loginViewModel by getViewModel(LoginViewModel::class.java) {
        getChannelId.observe(it, Observer {
            it?.let {
                StoredUserSources.putChannelIdUpdate(it.channel)
            }
        })
    }

    companion object {
        fun open(context: Context): Intent {
            return Intent(context, GuideUserActivity::class.java)
        }
    }


}
