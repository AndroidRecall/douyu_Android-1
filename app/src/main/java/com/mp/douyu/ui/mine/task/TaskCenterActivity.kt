package com.mp.douyu.ui.mine.task

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mp.douyu.*
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.event.LiveHomeEvent
import com.mp.douyu.ui.mine.IWantExtensionActivity
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.ui.mine.self_info.BinderQQNumActivity
import com.mp.douyu.ui.mine.walnut.ChargeCenterActivity
import com.mp.douyu.utils.RxBus
import com.mp.douyu.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_task_center.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class TaskCenterActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_task_center

    override fun initView() {
        matchWaterDropScreen(this)
        setActivityImmersion(this)

        titleBar.setBackgroundResource(R.color.color00000000)
        ibReturn.apply {

            singleClick {
                finishView()
            }
        }
        iftTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
        iftTitle.text = "每日签到，轻松拿豆"
        Glide.with(this).load(R.mipmap.return_back_white).centerInside().into(ibReturn)


        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@TaskCenterActivity)
        }
        mineViewModel.getTaskContent()

    }

    private val mAdapter by lazy {
        TaskCenterAdapter(this) { view: View, s: String, i: Int ->
            when (s) {
                "签到" -> {
                    putSignInTask()
                }
                "去送礼" -> {
                    try {
                        RxBus.getInstance().post(LiveHomeEvent())
                        finishView()
                    } catch (e: Exception) {
                    }
                }
                "去关注" -> {
                    try {
                        RxBus.getInstance().post(LiveHomeEvent())
                        finishView()
                    } catch (e: Exception) {
                    }
                }
                "领取" -> {
                }
                "去充值" -> {
                    startActivityWithTransition(ChargeCenterActivity.open(this), TO_CHARGE)
                }
                "去推广" -> {
                    startActivityWithTransition(IWantExtensionActivity.open(this), TO_EXTENSION)
                }
                "QQ" -> {
                    startActivityWithTransition(BinderQQNumActivity.open(this,
                        BinderQQNumActivity.qqName), TO_BIND)
                }
                "微信" -> {
                    startActivityWithTransition(BinderQQNumActivity.open(this,
                        BinderQQNumActivity.wechatName), TO_BIND)
                }

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TO_CHARGE, TO_EXTENSION, TO_BIND -> {
                    mineViewModel.getTaskContent()
                }
            }
        }
    }

    private fun putSignInTask() {
        mineViewModel.signInTask()
    }


    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getGetTask.observe(it, Observer {
            it?.let {
                mAdapter.taskCenterBean = it
                mAdapter.changeDataSet()
            }
        })
        _getSignInTask.observe(it, Observer {
            it?.let {
                ToastUtils.showToast("签到成功", true)
                getTaskContent()
            }
        })
    }

    companion object {
        const val TO_CHARGE = 0X101
        const val TO_BIND = 0X102
        const val TO_EXTENSION = 0X103
        fun open(context: Context?): Intent {
            return Intent(context, TaskCenterActivity::class.java)
        }
    }
}
