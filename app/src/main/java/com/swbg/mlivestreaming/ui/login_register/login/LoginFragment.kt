package com.swbg.mlivestreaming.ui.login_register.login

import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioManager.STREAM_SYSTEM
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.text.*
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.swbg.mlivestreaming.*
import com.swbg.mlivestreaming.base.BaseFragment
import com.swbg.mlivestreaming.base.MBaseFragment
import com.swbg.mlivestreaming.bean.UserBean
import com.swbg.mlivestreaming.http.NobodyConverterFactory
import com.swbg.mlivestreaming.im.ImManager
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.provider.TokenProvider
import com.swbg.mlivestreaming.ui.login_register.LoginActivity
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.utils.*
import com.tencent.imsdk.v2.V2TIMCallback
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject
import java.io.IOException


class LoginFragment : MBaseFragment(), SurfaceHolder.Callback {
    private var mPlayer: MediaPlayer? = null
    var holder: SurfaceHolder? = null

    //    var path: String? = "http://v.ysbang.cn/data/video/2015/rkb/2015rkb01.mp4"
    var path: String? = "http://440046.cn:88/storage/d9cd1d263569f4bbdf57de32152f26aab7c648ed.mp4"

    //0登录 1验证码注册 2用户名注册
    var signNum = 0
        set(value) {
            when (value) {
                0 -> initBtn(true)
                1 -> {
                    initBtn(false)
                    register_edit_user_name.apply {
                        hint = getString(R.string.phone_num)
                        inputType = EditorInfo.TYPE_CLASS_NUMBER
                        setText("")
                        register_tv_alarm_name.text = getString(R.string.please_input_eleven_phone)
                    }

                    //获取验证码
                    tv_get_verify.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.get_verify_code)
                        isClickable = true
                        setTextColor(ContextCompat.getColor(this!!.context, R.color.colorRedPink))
                    }
                    //显示验证码
                    setViewVisible(tv_get_verify, true)
                    setViewVisible(register_edit_verify_code, true)
                    setViewVisible(iv_user_verify_code, true)
                    //隐藏密码
//                    setViewVisible(register_edit_user_psw, false)
//                    setViewVisible(register_confirm_edit_user_psw, false)
//                    setViewVisible(register_tv_alarm_psw, false)
//                    setViewVisible(register_confirm_tv_alarm_psw, false)
//                    setViewVisible(iv_user_psw_register, false)
//                    setViewVisible(iv_user_psw_confirm_register, false)
                }
                2 -> {
                    initBtn(false)
                    register_edit_user_name.apply {
                        hint = getString(R.string.user_name)
                        inputType = EditorInfo.TYPE_CLASS_TEXT
                        setText("")
                        register_tv_alarm_name.text =
                            getString(R.string.please_input_four_eleven_num_letter)

                    }
                    //隐藏验证码
                    setViewVisible(tv_get_verify, false)
                    setViewVisible(register_edit_verify_code, false)
                    setViewVisible(iv_user_verify_code, false)
                    //显示密码
//                    setViewVisible(register_edit_user_psw, true)
//                    setViewVisible(register_confirm_edit_user_psw, true)
//                    setViewVisible(iv_user_psw_register, true)
//                    setViewVisible(iv_user_psw_confirm_register, true)
                }
            }
            field = value
        }


    override val layoutId: Int
        get() = R.layout.fragment_login

    override fun initView() {
        signNum = 0
        initListener()
//        initObserve()
        initTab()
        initPlayer()

        initData()
    }

    private fun initData() {
        val tBean = TokenProvider.get()
        edit_user_name.setText(tBean.clientName)
        edit_user_psw.setText(tBean.clientPsw)
        LogUtils.e(TokenProvider.get().accessToken)
        LogUtils.e(tBean.accessToken)

        //
//        val channelId = StoredUserSources.getChannelIdData()
        val channelId = ""
        register_edit_extension_code.setText(channelId.takeIf { it.isNotEmpty() } ?: "")
    }


    private val loginViewModel by getViewModel(LoginViewModel::class.java) {
        verifyCode.observe(it, Observer {//获取短信验证码成功
            LogUtils.i("==", "$it")
            it?.let {
                tv_get_verify.apply {
                    timer.start().also {
                        isClickable = false
                        setTextColor(ContextCompat.getColor(context, R.color.colorRedHint))
                    }
                    closeInputMethod()
                }
            }
//            textView.text = it
        })

        imageVerify.observe(it, Observer {
            LogUtils.i("==", it.toString())
        })
        //注册
        registerResult.observe(it, Observer {

            val jsonObject = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
            val message = jsonObject.getString("message")

            LogUtils.i("==", message)
            showLoadingView(false)
            if (message != "注册成功") {
                ToastUtils.showToast(message)
                return@Observer
            }
            it?.let {
                ToastUtils.showToast(getString(R.string.register_success))
                val userPsw = register_edit_user_psw.text.toString().trim()
                val userName = register_edit_user_name.text.toString().trim()
                login(UserBean(phone = userName, psw = userPsw))

//                signNum = 0
                register_edit_user_name.apply {
                    closeInputMethod()
                    edit_user_name.setText(text.toString().trim())
                    setText("")
                }
            }
        })
        registerUserResult.observe(it, Observer {
            val jsonObject = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
            val message = jsonObject.getString("message")
            LogUtils.i("==", it.toString())
            showLoadingView(false)
            if (message != "注册成功") {
                ToastUtils.showToast(message)
                return@Observer
            }
            it?.let {
                ToastUtils.showToast(getString(R.string.register_success))
                val userPsw = register_edit_user_psw.text.toString().trim()
                val userName = register_edit_user_name.text.toString().trim()

                login(UserBean(phone = userName, psw = userPsw))
//                signNum = 0
                register_edit_user_name.apply {
                    closeInputMethod()
                    edit_user_name.setText(text.toString().trim())
                    setText("")
                }
            }
        })
        //登录
        loginResult.observe(it, Observer {
            it?.let {
//                mineViewModel.getUserInfo()
                mineViewModel.getUserInfo2(hashMapOf())
                it.userPsw = if (im_record.isSelected) edit_user_psw.text.toString().trim() else ""
                it.userName = edit_user_name.text.toString().trim()
                it.cacheSession()
                if (activity is LoginActivity) {
                    if ((activity as LoginActivity).getExtraLogin == "1") {
                        startActivityWithTransition(MainActivity.open(activity as LoginActivity))
                    }
                }
                //IM登录
                ImManager.instance.loginIM("${it.userID}", "${it.userSig}", object : V2TIMCallback {
                    override fun onSuccess() {
                        //Im登录成功
                        Log.e(TAG, "IM 登录成功 success")
                        finishView()
                    }

                    override fun onError(p0: Int, p1: String?) {
                        //Im登录失败
                        Log.e(TAG, "IM 登录失败 onError: code=${p0},msg=${p1}")
                        finishView()
                    }
                })

                LogUtils.i("==", "${it},${it.accessToken}")

            }
        })

        sysSettingResult.observe(it, Observer {
            it?.let {
                StoredUserSources.putSettingData(it)
                val openFd =
                    this@LoginFragment.activity?.assets?.openFd("login_backgroup_video.mp4")
//                path = it.bg_video
//                path = openFd.fileDescriptor
                mPlayer?.apply {
                    reset() //重置播放器
//                    setDataSource(path) //设置播放源

                    openFd?.let { it1 ->
                        setDataSource(it1.fileDescriptor, it1.startOffset, it1.length)
                    }
                    //设置播放源
                    setDisplay(holder)
                    setVolume(0.1f, 0.1f)
                    prepareAsync()
                    setOnPreparedListener {
                        start() //开始播放
                    }
                    setOnCompletionListener {
                        start()
                        isLooping = true //循环
                    }
                }
            }
        })
    }
    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getUserInfo.observe(it, Observer {
            it?.let {
                StoredUserSources.putUserInfo(it)
            }
        })
        _getUserInfo2.observe(it, Observer {
            it?.let {
                StoredUserSources.putUserInfo2(it[0])
            }
        })
    }

    private fun initTab() {
        arrayListOf(getString(R.string.identify_register),
            getString(R.string.user_name_regiter)).map {
            register_tabLayout.newTab().apply { text = it }
        }.forEach { register_tabLayout.addTab(it, 0 == register_tabLayout.tabCount) }
        register_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab) {
            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                LogUtils.i("==", "当前位置：${p0.position}")
                signNum = if (p0.position == 0) 1 else 2
            }
        })
        val tab = register_tabLayout.getTabAt(0) ?: return
        tab.select()
    }

    private fun initPlayer() {
        mPlayer = MediaPlayer()
        try {
            mPlayer?.apply {
                //安卓6.0以后
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //配置播放器
                    val aa = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE).build()
                    setAudioAttributes(aa)
                } else {
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                }
                holder = sv.holder
                holder?.addCallback(this@LoginFragment)

/*
                val openFd = this@LoginFragment.activity?.assets?.openFd("login_back_video.mp4")
//                path = it.bg_video
//                path = openFd.fileDescriptor
                reset() //重置播放器
                openFd?.let { it1 ->
                    setDataSource(it1.fileDescriptor, it1.startOffset, it1.length)
                } //设置播放源
                setDisplay(holder)
                setVolume(0.1f, 0.1f)
                prepareAsync()
                setOnPreparedListener {
                    start() //开始播放
                }
                setOnCompletionListener {
                    start()
                    isLooping = true //循环
                }*/

                /*   object : SurfaceHolder.Callback {
                       override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

                       }

                       override fun surfaceDestroyed(p0: SurfaceHolder?) {
                       }

                       override fun surfaceCreated(p0: SurfaceHolder?) {
                           setDisplay(holder)
                           setVolume(0.5f, 0.5f)
                           prepareAsync()
                           setOnPreparedListener {
                               start() //开始播放
                           }
                       }
                   })*/


            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e1: IllegalArgumentException) {
            e1.printStackTrace()
        }

    }

    override fun onStop() {
        super.onStop()
        mPlayer?.stop()
    }

    override fun onDestroy() {
        mPlayer?.reset()
        mPlayer?.release()
        super.onDestroy()
    }


    private fun initListener() {
        btn_acb.apply {
            singleClick {
                signNum = if (register_tabLayout.selectedTabPosition == 0) 1 else 2
                closeInputMethod()
            }
        }
        btn_acb_register.apply {
            singleClick {
                signNum = 0
                closeInputMethod()
            }
        }
        tv_get_verify.apply {
            singleClick {
                getCode()
            }
        }
        //login submit
        btn_login.apply {
            singleClick {
                loginSubmit()
                LogUtils.i("==", "btn_login")
            }
        }
        //register submit
        register_btn_register_submit.apply {
            singleClick {
                registerSubmit()
                LogUtils.i("==", "register_btn_register_submit")
            }

        }
        //remember psw submit
        btn_remember.apply {
            singleClick {
                LogUtils.i("==", "btn_remember")
                im_record.isSelected = !im_record.isSelected
            }
        }
        im_record.singleClick {
            im_record.isSelected = !im_record.isSelected
        }
/*
        register_edit_click_verify.singleClick {
            //获取图形验证码
            loginViewModel.getImageVerify()
        }*/

        //go home psw submit
        btn_go_walk.run {
            singleClick {
                LogUtils.i("==", "btn_go_walk")

                if (activity is LoginActivity) {
                    if ((activity as LoginActivity).getExtraLogin == "1") {
                        startActivityWithTransition(MainActivity.open(context))
                    }
                }
                finishView()

                /*      companion object{
                      const val MAIN_TAB = "MAIN_TAB"
                      fun open(context: Context,tab :Int? = 0): Intent {
                          return Intent(context,MainActivity::class.java)
                              .putExtra(MAIN_TAB,tab)
                      }
                  }*/
            }
        }
        //personal service
        register_btn_personal_service.singleClick {
            LogUtils.i("==", "register_btn_personal_service")
            val settingData = StoredUserSources.getSettingData()
            settingData?.contact?.apply {
                activity?.let { it1 -> ActivityUtils.jumpToWebView(this, it1) }
            }
        }
        btn_personal_service.singleClick {
            LogUtils.i("==", "btn_personal_service")
            val settingData = StoredUserSources.getSettingData()
            settingData?.contact?.apply {
                activity?.let { it1 -> ActivityUtils.jumpToWebView(this, it1) }
            }
        }

        //forget psw
        btn_forget_psw.singleClick {
            LogUtils.i("==", "btn_forget_psw")

            if (activity is LoginActivity) {
                (activity as LoginActivity).openForgetPswFragment()
            }
        }


        //init regist
        setEditText(edit_user_name, tv_alarm_name)
        setEditText(edit_user_psw, tv_alarm_psw)
        setEditText(register_edit_user_name, register_tv_alarm_name)
        setEditText(register_edit_user_psw, register_tv_alarm_psw)
        setEditText(register_confirm_edit_user_psw, register_confirm_tv_alarm_psw)
        setEditText(register_edit_extension_code, register_tv_alarm_extension)
        setViewVisible(tv_alarm_name, false)
        setViewVisible(tv_alarm_psw, false)
        setViewVisible(register_tv_alarm_name, false)
        setViewVisible(register_tv_alarm_psw, false)
        setViewVisible(register_confirm_tv_alarm_psw, false)
        setViewVisible(register_tv_alarm_extension, false)


        //设置输入点击事件
        setEditClickListener(edit_user_name)
        setEditClickListener(edit_user_psw)
        setEditClickListener(register_edit_user_name)
        setEditClickListener(register_edit_user_psw)
        setEditClickListener(register_confirm_edit_user_psw)
        setEditClickListener(register_edit_extension_code)
        setEditClickListener(register_edit_verify_code)

    }

    //登录
    private fun loginSubmit() {
        if (signNum == 0) {
            val phone = edit_user_name.text.toString().trim()
            val userPsw = edit_user_psw.text.toString().trim()
            if (phone.length < 4) {
                ToastUtils.showToast(getString(R.string.please_input_right_phone_num_or_user_name))
                return
            }
            if (userPsw.length < 6) {
                ToastUtils.showToast(getString(R.string.please_input_right_psw))
                return
            }
            loginViewModel.login(UserBean(phone = phone, psw = userPsw))
        }
    }

    private fun registerSubmit() {
        when (signNum) {
            1 -> {
                val phone = register_edit_user_name.text.toString().trim()
                val verifyCode = register_edit_verify_code.text.toString().trim()
                val extensionCode = register_edit_extension_code.text.toString().trim()
                val userPsw = register_edit_user_psw.text.toString().trim()
                val confirmUserPsw = register_confirm_edit_user_psw.text.toString().trim()
                if (!StringUtils.checkMobilePhone(phone)) {
                    ToastUtils.showToast(getString(R.string.please_input_right_phone_num))
                    return
                }
                if (TextUtils.isEmpty(verifyCode)) {
                    ToastUtils.showToast(getString(R.string.please_input_verify_code))
                    return
                }
                if (userPsw.length < 6 && confirmUserPsw.length < 6) {
                    ToastUtils.showToast(getString(R.string.please_input_right_psw))
                    return
                }
                if (userPsw != confirmUserPsw) {
                    ToastUtils.showToast(getString(R.string.not_equals_input_twice))
                    return
                }
                if (!isLetterDigit(userPsw)) {
                    ToastUtils.showToast("密码中必须含有字母和数字")
                    return
                }
                loginViewModel.registerUser(UserBean(phone = phone,
                    verifyCode = verifyCode,
                    psw = userPsw,
                    extensionCode = extensionCode

                ))
            }
            2 -> {
                val phone = register_edit_user_name.text.toString().trim()
                val extensionCode = register_edit_extension_code.text.toString().trim()
                val userPsw = register_edit_user_psw.text.toString().trim()
                val confirmUserPsw = register_confirm_edit_user_psw.text.toString().trim()
                if (phone.length < 4) {
                    ToastUtils.showToast(getString(R.string.please_input_right_user_name))
                    return
                }
                if (!isLetterDigit(phone)) {
                    ToastUtils.showToast("用户名中必须含有字母和数字")
                    return
                }
                if (userPsw != confirmUserPsw) {
                    ToastUtils.showToast(getString(R.string.not_equals_input_twice))
                    return
                }
                if (userPsw.length < 6 && confirmUserPsw.length < 6) {
                    ToastUtils.showToast("密码不能小于6位")
                    return
                }
                if (!isLetterDigit(userPsw)) {
                    ToastUtils.showToast("密码中必须含有字母和数字")
                    return
                }
                loginViewModel.registerUserUserName(UserBean(phone = phone,
                    psw = userPsw,
                    extensionCode = extensionCode))
            }
        }
    }

    private fun getCode() {
        when (signNum) {
            0 -> {
            }
            1 -> {
                val phone = register_edit_user_name.text.toString().trim()
                if (!StringUtils.checkMobilePhone(phone)) {
                    ToastUtils.showToast(getString(R.string.please_input_right_phone_num))
                    return
                }
                loginViewModel.getVerifyCode(UserBean(phone = phone, type = "1"))
            }
        }
        layoutPhoneRegister
    }

    private fun setEditClickListener(editText: AppCompatEditText?) {
        editText?.apply {
            singleClick {
                openInputMethod()
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    judgeIsBtnClick(editText)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
        }
    }

    private fun judgeIsBtnClick(editText: AppCompatEditText) {
        when (signNum) {
            0 -> {
                val editUserName = edit_user_name.text.toString().trim()
                val editPsw = edit_user_psw.text.toString().trim()
                if (editUserName.length >= 4 && editPsw.length >= 6) {
                    btn_login.apply {
                        isClickable = true
                        isSelected = true
                    }
                } else {
                    btn_login.apply {
                        isClickable = false
                        isSelected = false
                    }
                }
            }
            1 -> {
                val registerEditUserName = register_edit_user_name.text.toString().trim()
                val registerEditVerifyCode = register_edit_verify_code.text.toString().trim()
                val registerEditPsw = register_edit_user_psw.text.toString().trim()
                val registerConfirmEditPsw = register_confirm_edit_user_psw.text.toString().trim()
                if (registerEditUserName.length >= 11 && registerEditVerifyCode.length >= 4 && registerEditPsw.length >= 6 && registerConfirmEditPsw.length >= 6) {
                    register_btn_register_submit.apply {
                        isClickable = true
                        isSelected = true
                    }
                } else {
                    register_btn_register_submit.apply {
                        isClickable = false
                        isSelected = false
                    }
                }
            }
            2 -> {
                val registerEditUserName = register_edit_user_name.text.toString().trim()
                val registerEditPsw = register_edit_user_psw.text.toString().trim()
                val registerConfirmEditPsw = register_confirm_edit_user_psw.text.toString().trim()
                if (registerEditUserName.length >= 4 && registerEditPsw.length >= 6 && registerConfirmEditPsw.length >= 6) {
                    register_btn_register_submit.apply {
                        isClickable = true
                        isSelected = true
                    }
                } else {
                    register_btn_register_submit.apply {
                        isClickable = false
                        isSelected = false
                    }
                }
            }
        }
    }

    private fun setViewVisible(v: View, b: Boolean) {
        v.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun setEditText(editText: AppCompatEditText?, tvAlarm: AppCompatTextView?) {
        editText?.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tvAlarm?.visibility = View.VISIBLE
            } else {
                tvAlarm?.visibility = View.GONE
            }
        }
    }

    private fun initBtn(isLogin: Boolean) {
        if (isLogin) {
            layoutPhone.visibility = View.VISIBLE
            layoutPhoneRegister.visibility = View.GONE
            group_personal_service.visibility = View.VISIBLE
            register_group_personal_service.visibility = View.GONE
            iftLogo.visibility = View.VISIBLE
            iftLogoRegister.visibility = View.GONE
        } else {
            layoutPhone.visibility = View.GONE
            layoutPhoneRegister.visibility = View.VISIBLE
            group_personal_service.visibility = View.GONE
            register_group_personal_service.visibility = View.VISIBLE
            iftLogo.visibility = View.GONE
            iftLogoRegister.visibility = View.VISIBLE
        }
    }


    private val timer = object : CountDownTimer(60 * 1000, 1000) {

        override fun onFinish() {
            tv_get_verify?.apply {
                isClickable = true
                text = getString(R.string.get_verify_code)
                setTextColor(ContextCompat.getColor(activity!!, R.color.colorRedPink))
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) {
            tv_get_verify?.apply {
                text = "(${millisUntilFinished / 1000}s)"
            }
        }
    }


    override fun showError(t: Throwable?) {
        super.showError(t)
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        loginViewModel.getSysSetting()
    }

    fun isLetterDigit(str: String): Boolean {
        var isDigit = false
        var isLetter = false
        for (chart in str) {
            if (Character.isDigit(chart)) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(chart)) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        return isDigit && isLetter
    }
}
