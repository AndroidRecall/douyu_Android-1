package com.mp.douyu.ui.mine.self_info

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.SelfInfoBean
import com.mp.douyu.bean.UserInfoBean
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.view.popupwindow.BottomSingleActionPopupWindow
import kotlinx.android.synthetic.main.activity_self_info.recyclerView
import kotlinx.android.synthetic.main.title_bar_simple.*

class SelfInfoActivity : MBaseActivity() {

    var currentUpType: Pair<Int, String>? = null
    var userInfoBean: UserInfoBean = UserInfoBean()
        set(value) {
            this.let {
                mAdapter.selfHeader.value = value.user?.avatar
                mAdapter.changeHeader(0)

                val selfInfoList: List<SelfInfoBean> =
                    listOf(SelfInfoBean("昵称", "${value.user?.nickname}", "未绑定"),
                        SelfInfoBean("年龄", "${if (value.user?.age.equals("0"))"保密" else value.user?.age}", "未绑定"),
                        SelfInfoBean("性别",
                            "${if (value.user?.gender == 0) "保密" else if (value.user?.gender == 1) "男" else "女"}",
                            "未绑定"),
                        SelfInfoBean("个性签名", "${value.user?.sign}", "未绑定"),
                        SelfInfoBean("我的账号", "${value.user?.username}", "未绑定"),
                        SelfInfoBean("手机号", "${value.user?.phone}", "未绑定"),
                        SelfInfoBean("QQ号", "${value.user?.qq}", "未绑定"),
                        SelfInfoBean("微信号", "${value.user?.wechat}", "未绑定"),
                        SelfInfoBean("邮箱", "${value.user?.email}", "未绑定"))
                mAdapter.selfInfoList = selfInfoList
                mAdapter.changeChildren(1)
            }
            field = value
        }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getUserInfo.observe(it, Observer {
            it?.let {
                userInfoBean = it
                StoredUserSources.putUserInfo(it)
            }
        })
        _setUserAvatar.observe(it, Observer {
            mLoading = false
            when (currentUpType?.first) {
                0 -> {
                    StoredUserSources.getUserInfo()?.let {
                        it.user?.age = currentUpType?.second
                        userInfoBean = it
                        StoredUserSources.putUserInfo(it)
                    }

                }
                1 -> {
                    StoredUserSources.getUserInfo()?.let {
                        it.user?.gender = when (currentUpType?.second) {
                            "男" -> 1
                            "女" -> 2
                            else -> 0
                        }
                        userInfoBean = it
                        StoredUserSources.putUserInfo(it)
                    }
                }
            }
//            getUserInfo()
        })
    }
    override val contentViewLayoutId: Int
        get() = R.layout.activity_self_info

    override fun onResume() {
        super.onResume()
        mineViewModel.getUserInfo()
    }

    override fun initView() {
        iftTitle.text = "个人信息"
        ibReturn.singleClick {
            onBackPressed()
        }
        initRecycler()

        initData()
    }

    private fun initData() {
        userInfoBean = StoredUserSources.getUserInfo().takeIf { it != null } ?: UserInfoBean()
    }

    private fun initRecycler() {
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@SelfInfoActivity)
        }
    }

    private val mAdapter by lazy {
        SelfInfoAdapter(this) {
            when (it) {
                "头像" -> {
                    startActivityWithTransition(AvatarChooseActivity.open(this))
                }
                "昵称" -> {
                    startActivityWithTransition(NicknameActivity.open(this))
                }
                "年龄" -> {
                    initAgeGender(0)
                }
                "性别" -> {
                    initAgeGender(1)
                }
                "个性签名" -> {
                    startActivityWithTransition(ChangePersonalSignatureActivity.open(this), 1001)
                }
                "我的账号" -> {
                }
                "手机号" -> {
                    startActivityWithTransition(BindPhoneNumActivity.open(this))
                }
                "QQ号" -> {
                    startActivityWithTransition(BinderQQNumActivity.open(this,
                        BinderQQNumActivity.qqName))
                }
                "微信号" -> {
                    startActivityWithTransition(BinderQQNumActivity.open(this,
                        BinderQQNumActivity.wechatName))

                }
                "邮箱" -> {
                    startActivityWithTransition(BinderQQNumActivity.open(this,
                        BinderQQNumActivity.emailName))

                }
            }
        }
    }

    private var mLoading = false
    private fun initAgeGender(type: Int) {
        BottomSingleActionPopupWindow(this).apply {
            title = when (type) {
                0 -> {
                    "年龄"
                }
                1 -> {
                    "性别"
                }
                else -> ""
            }
            val mItems = arrayListOf<String>()
            when (type) {
                0 -> {
                    for (i in 18..60) {
                        mItems.add("$i")
                    }
                }
                else -> {
                    mItems.add("男")
                    mItems.add("女")
                }
            }
            items = mItems
            itemCheckedListener = { it ->
                if (0 <= it && it < items.size) {
                    when (type) {
                        0 -> {
                            if (!mLoading) {

                                mLoading = true
                                currentUpType = Pair(0, items[it])
                                mineViewModel.editAvatar(hashMapOf("age" to items[it]))
                            }
                        }
                        1 -> {
                            val isGender = when (items[it]) {
                                "男" -> {
                                    "1"
                                }
                                "女" -> {
                                    "2"
                                }
                                else -> {
                                    "0"
                                }
                            }
                            if (!mLoading) {
                                mLoading = true
                                currentUpType = Pair(1, items[it])
                                mineViewModel.editAvatar(hashMapOf("gender" to isGender))
                            }
                        }
                    }
                }
            }
        }.show(window.decorView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            initData()
        }
    }

    companion object {
        fun instance(context: Context?): Intent {
            return Intent(context, SelfInfoActivity::class.java)
        }
    }

}
