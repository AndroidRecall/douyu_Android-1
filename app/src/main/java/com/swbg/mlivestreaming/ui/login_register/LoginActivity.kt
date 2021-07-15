package com.swbg.mlivestreaming.ui.login_register

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import com.swbg.mlivestreaming.GestureDetectorHolder
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.BaseActivity
import com.swbg.mlivestreaming.matchWaterDropScreen
import com.swbg.mlivestreaming.setActivityImmersion
import com.swbg.mlivestreaming.ui.login_register.forget.ForgetPswActivity
import com.swbg.mlivestreaming.ui.login_register.login.LoginFragment

class LoginActivity : BaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_login

    override fun initView() {
        matchWaterDropScreen(this@LoginActivity)
        setActivityImmersion(this)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, LoginFragment())
            .commit()
    }


    fun openForgetPswFragment() {
        startActivity(Intent(this, ForgetPswActivity::class.java))
    }

    private val gestureDetectorHolder by lazy {
        GestureDetectorHolder(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return gestureDetectorHolder.apply {
            excludeViews = hashSetOf(findViewById(R.id.edit_user_name),
                findViewById(R.id.edit_user_psw),
                findViewById(R.id.register_edit_extension_code),
                findViewById(R.id.register_confirm_edit_user_psw),
                findViewById(R.id.register_edit_user_name),
                findViewById(R.id.register_edit_verify_code),
                findViewById(R.id.register_edit_user_psw))
        }.dispatchMotionEvent(ev,
            findViewById(R.id.btn_login),
            findViewById(R.id.btn_register),
            findViewById(R.id.register_btn_register),
            findViewById(R.id.btn_remember),
            findViewById(R.id.btn_personal_service),
            findViewById(R.id.register_btn_personal_service),
            findViewById(R.id.register_btn_register_submit)) && super.dispatchTouchEvent(ev)
    }

    val getExtraLogin by lazy {
        intent.getStringExtra(EXTRA_LOGIN) ?: "-1"
    }

    companion object {
        const val EXTRA_LOGIN = "EXTRA_LOGIN"
        fun open(context: Context?): Intent {
            return Intent(context,
                LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        fun openClearTask(context: Context?, loginError: String = "1"): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(EXTRA_LOGIN, loginError)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }
}