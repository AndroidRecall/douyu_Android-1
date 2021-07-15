package com.mp.douyu.ui.mine.system_setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.SystemSettingBean
import com.mp.douyu.bean.TokenBean
import com.mp.douyu.bean.UserInfoBean
import com.mp.douyu.cacheSession
import com.mp.douyu.im.ImManager
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.provider.TokenProvider
import com.mp.douyu.singleClick
import com.mp.douyu.utils.DataCleanManagerUtils
import com.mp.douyu.utils.ToastUtils
import com.mp.douyu.view.popupwindow.BottomActionPopupWindow
import com.tencent.imsdk.v2.V2TIMCallback
import kotlinx.android.synthetic.main.activity_system_set.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class SystemSetActivity : MBaseActivity() {
    var TAG =SystemSetActivity::class.java.simpleName
    var settingData: SystemSettingBean? = null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_system_set

    override fun initView() {
        tv_login_out.singleClick {
            BottomActionPopupWindow(this).apply {
                title = "确认退出登录"
                cancel = "取消"
                items = arrayListOf("退出登录")
                color = R.color.colorError
                itemCheckedListener = {
                    val tokenProvider = TokenProvider.get()
                    TokenBean("",
                        userName = tokenProvider.clientName,
                        userPsw = tokenProvider.clientPsw).cacheSession()
                    StoredUserSources.putUserInfo(UserInfoBean())
//                    startActivityWithTransition(LoginActivity.open(context),true)
                    ImManager.instance.logoutIM(object :V2TIMCallback{
                        override fun onSuccess() {
                            Log.e(TAG,"IM 退出登录成功")
                        }

                        override fun onError(p0: Int, p1: String?) {
                            Log.e(TAG,"IM 退出登录异常 code=${p0},msg=${p1}")
                        }
                    })
                    finishView()
                }
                show(window.decorView)
            }
        }
        iftTitle.text = "系统设置"
        ibReturn.singleClick {
            onBackPressed()
        }
        tv_copy.singleClick {
            settingData?.let { it1 ->
                it1.email?.let {
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip: ClipData =
                        ClipData.newPlainText(getString(R.string.change_success), it1.email)
                    clipboard.setPrimaryClip(clip)
                    ToastUtils.showToast(getString(R.string.copy_cuccess), true)
                }
            }

        }

        tv3.singleClick {
            BottomActionPopupWindow(this).apply {
                title = "确认清除缓存"
                cancel = "取消"
                items = arrayListOf("清除缓存")
                color = R.color.colorError
                itemCheckedListener = {
                    DataCleanManagerUtils.clearAllCache(this@SystemSetActivity)
                    tv_cache.text = "0KB"
                }
                show(window.decorView)
            }
        }
        initData()
    }

    private fun initData() {
        settingData = StoredUserSources.getSettingData()
        tv_email.text = settingData?.email
        tv_code.text = "V${BuildConfig.VERSION_NAME}"
        var cacheSize = "0"
        try {
            cacheSize = DataCleanManagerUtils.getTotalCacheSize(this)
        } catch (e: Exception) {
            cacheSize = "0"
        }
        tv_cache.text = "$cacheSize"

    }

    companion object {
        fun open(context: Context?): Intent {
            return Intent(context, SystemSetActivity::class.java)
        }
    }

}
