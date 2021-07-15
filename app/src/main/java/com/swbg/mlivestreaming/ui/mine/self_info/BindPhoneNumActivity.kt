package com.swbg.mlivestreaming.ui.mine.self_info

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.UserBean
import com.swbg.mlivestreaming.closeInputMethod
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.login_register.login.LoginViewModel
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.utils.StringUtils
import com.swbg.mlivestreaming.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_binder_phone_num.*
import kotlinx.android.synthetic.main.activity_binder_phone_num.tv_get_verify
import kotlinx.android.synthetic.main.title_bar_simple.*

class BindPhoneNumActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_binder_phone_num


    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _bindPhone.observe(it, Observer {
            it?.let {
                ToastUtils.showToast(getString(R.string.bind_success))
                finishView()
            }
        })
    }

    private val loginViewModel by getViewModel(LoginViewModel::class.java) {
        verifyCode.observe(it, Observer {
            it?.let {
                tv_get_verify.apply {
                    timer.start().also {
                        isClickable = false
                    }
                }
            }
        })
    }

    override fun initView() {
        ibReturn.singleClick {
            onBackPressed()
        }
        iftTitle.text = "绑定手机号"
        tv_get_verify.apply {
            singleClick {
                val phone = et_phone_num.text.toString().trim()
                if (!StringUtils.checkMobilePhone(phone)) {
                    ToastUtils.showToast(getString(R.string.please_input_right_phone_num))
                    return@singleClick
                }
                loginViewModel.getVerifyCode(UserBean(phone = phone, type = "3"))

                closeInputMethod()
            }
        }


        btn_confirm.singleClick {
                val phone = et_phone_num.text.toString().trim()
                val code = et_identify_code.text.toString().trim()
                if (!StringUtils.checkMobilePhone(phone)) {
                    ToastUtils.showToast(getString(R.string.please_input_right_phone_num))
                    return@singleClick
                }
                if (code.isEmpty()) {
                    ToastUtils.showToast(getString(R.string.please_input_verify_code))
                    return@singleClick
                }
                mineViewModel.bindPhone(hashMapOf("phone" to phone, "code" to code))
            }


        et_phone_num.addTextChangedListener {
            val phone = et_phone_num.text.toString().trim()
            val code = et_identify_code.text.toString().trim()
//            val b = StringUtils.checkMobilePhone(phone) && code.isNotEmpty()
//            btn_confirm.apply {
//                isEnabled = b
//                isSelected = b
//            }
        }
    }

    private val timer = object : CountDownTimer(60 * 1000, 1000) {

        override fun onFinish() {
            tv_get_verify.apply {
                isClickable = true
                text = getString(R.string.get_verify_code)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_get_verify.apply {
                text = "(${millisUntilFinished / 1000}s)"
            }
        }
    }

    companion object {
        fun open(context: Context): Intent {
            return Intent(context, BindPhoneNumActivity::class.java)
        }
    }
}
