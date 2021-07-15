package com.mp.douyu.ui.mine.self_info

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.singleClick
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_binder_qq_num.*
import kotlinx.android.synthetic.main.activity_binder_qq_num.tv_title
import kotlinx.android.synthetic.main.item_confirm_button.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class BinderQQNumActivity :MBaseActivity(){
    override val contentViewLayoutId: Int
        get() = R.layout.activity_binder_qq_num

    override fun initView() {
        iftTitle.text = name
        tv_title.text = when(name){
            qqName->{"QQ号"}
            wechatName->{"微信号"}
            emailName->{"邮箱号"}
            else ->"QQ号"
        }
        et_phone_num.hint = when(name){
            qqName->{"请输入QQ号"}
            wechatName->{"请输入微信号"}
            emailName->{"请输入邮箱号"}
            else ->"请输入QQ号"
        }

        ibReturn.singleClick {
            onBackPressed()
        }

        btn_confirm.singleClick {
            val nickName = et_phone_num.text.toString().trim()
            if (nickName.isEmpty()) {
                ToastUtils.showToast(et_phone_num.hint.toString())
                return@singleClick
            }
            mineViewModel.editAvatar(hashMapOf(when(name) {
                qqName -> "qq"
                wechatName -> "wechat"
                emailName -> "email"
                else -> "email"
            } to nickName))
        }

        et_phone_num.addTextChangedListener {
            val nickName = et_phone_num.text.toString().trim()
            btn_confirm.apply {
                isEnabled = nickName.isNotEmpty()
                isSelected = nickName.isNotEmpty()
            }
        }
    }
    private val name  by lazy {
        intent.getStringExtra(binderType)
    }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _setUserAvatar.observe(it, Observer {
            it?.let {
                setResult(Activity.RESULT_OK, Intent())
                finishView()
            }
        })
    }

    companion object{
        private const val binderType = "BINDER_TYPE"
        const val qqName = "绑定QQ号"
        const val wechatName = "绑定微信号"
        const val emailName = "绑定邮箱号"
        fun open(context: Context,typeName : String):Intent{
            return Intent(context,BinderQQNumActivity::class.java).putExtra(binderType,typeName)
        }
    }

}
