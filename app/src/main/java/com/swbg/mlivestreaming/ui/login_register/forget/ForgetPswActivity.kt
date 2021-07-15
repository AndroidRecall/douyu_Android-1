package com.swbg.mlivestreaming.ui.login_register.forget

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.CachedStatusWrapper
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.swbg.mlivestreaming.*
import com.swbg.mlivestreaming.base.BaseActivity
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.InputEditBean
import com.swbg.mlivestreaming.bean.UserBean
import com.swbg.mlivestreaming.utils.LogUtils
import com.swbg.mlivestreaming.utils.StringUtils
import com.swbg.mlivestreaming.utils.ToastUtils
import com.swbg.mlivestreaming.utils.Utils
import kotlinx.android.synthetic.main.activity_forget_psw.*
import kotlinx.android.synthetic.main.item_confirm_button.view.*
import kotlinx.android.synthetic.main.item_input_verify_code.*
import kotlinx.android.synthetic.main.item_input_verify_code.view.*
import kotlinx.android.synthetic.main.item_normal.view.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class ForgetPswActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_forget_psw

    override fun initView() {
//        initObserve()
        initTitle()
        initRecyclerView()
        initListener()
    }

    private val gestureDetectorHolder by lazy {
        GestureDetectorHolder(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return gestureDetectorHolder.apply {
            excludeViews = hashSetOf(findViewById(R.id.et_content))
        }.dispatchMotionEvent(ev,
            findViewById(R.id.btn_confirm)) && super.dispatchTouchEvent(ev)
    }


    private fun initListener() {
        Utils.SoftKeyBoardListener(this).apply {
            setListener(object : Utils.SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
                override fun keyBoardShow(height: Int) {
                }

                override fun keyBoardHide(height: Int) {
                    //键盘隐藏，开始更新数据
                    mAdapter.notifyItemChanged(inputListData.size - 1)
                }
            })
        }
    }

    private val forgetPswViewModel by getViewModel(ForgetViewModel::class.java) {
        verifyCode.observe(it, Observer {//获取短信验证码成功
            LogUtils.i("==", "$it")
            it?.let {
                tvVerifyCode?.apply {

                    val timer = object : CountDownTimer(60 * 1000, 1000) {
                        override fun onFinish() {
                            isClickable = true
                            text = getString(R.string.get_verify_code)
                            setCountdownTime(0L)
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onTick(millisUntilFinished: Long) {
                            text = "${millisUntilFinished / 1000}s"
                            setCountdownTime(millisUntilFinished)
                        }
                    }
                    timer.start().also {
                        isClickable = false
                    }
                    closeInputMethod()
                }
            }
        })
        forgetResult.observe(it, Observer {
            LogUtils.i("==", "$it")
            it?.let {
                ToastUtils.showToast(getString(R.string.change_psw_success))
                finish()
            }
        })
    }
    /*
    private fun initObserve() {
        forgetPswViewModel = ViewModelProvider(this).get(ForgetViewModel::class.java)
        forgetPswViewModel.verifyCode.observe(this, Observer {//获取短信验证码成功
            LogUtils.i("==", "$it")
            it?.let {
                tvVerifyCode?.apply {

                    val timer = object : CountDownTimer(60 * 1000, 1000) {
                        override fun onFinish() {
                            isClickable = true
                            text = getString(R.string.get_verify_code)
                            setCountdownTime(0L)
                        }

                        @SuppressLint("SetTextI18n")
                        override fun onTick(millisUntilFinished: Long) {
                            text = "${millisUntilFinished / 1000}s"
                            setCountdownTime(millisUntilFinished)
                        }
                    }
                    timer.start().also {
                        isClickable = false
                    }
                    closeInputMethod()
                }
            }
        })
        forgetPswViewModel.forgetResult.observe(this, Observer {
            LogUtils.i("==", "$it")
            it?.let {
                ToastUtils.showToast(getString(R.string.change_psw_success))
                finish()
            }
        })
    }*/

    private fun initTitle() {
        ibReturn.singleClick {
            finish()
        }
        iftTitle.text = getString(R.string.forget_psw)

    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        mAdapter.takeInstance<CachedAutoRefreshAdapter<InputEditBean>>().addAll(inputListData)
    }


    private val mAdapter by lazy {
        CachedStatusWrapper().apply {
            client = object : CachedAutoRefreshAdapter<InputEditBean>() {

                override fun getItemViewType(position: Int): Int {
                    return get(position).shapeType
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                    return when (viewType) {
                        0 -> {
                            CacheViewHolder(LayoutInflater.from(this@ForgetPswActivity)
                                .inflate(R.layout.item_normal, parent, false)).apply {}
                        }
                        1 -> {//验证码
                            CacheViewHolder(LayoutInflater.from(this@ForgetPswActivity)
                                .inflate(R.layout.item_input_verify_code, parent, false)).apply {
                                LogUtils.i("==", "$viewType$et_content_verify_code")

                                itemView.tv_get_verify.apply {
                                    singleClick {
                                        //获取验证码
                                        startCountdown(this)

                                    }
                                }
                            }
                        }
                        2 -> { //确认提交
                            CacheViewHolder(LayoutInflater.from(this@ForgetPswActivity)
                                .inflate(R.layout.item_confirm_button, parent, false)).apply {
                                itemView.btn_confirm.singleClick {
                                    submitData()
                                }
                            }
                        }
                        else -> {
                            CacheViewHolder(LayoutInflater.from(this@ForgetPswActivity)
                                .inflate(R.layout.item_normal, parent, false)).apply {}
                        }
                    }
                }

                override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {

                    try {
                        val itemBean = get(position)
                        when (holder.itemViewType) {
                            0 -> {
                                holder.containerView.let {
                                    it.tv_title.text = itemBean.inputTitle
                                    it.et_content.apply {
                                        setText(itemBean.inputContent)
                                        hint = itemBean.inputHint
                                        inputType = itemBean.inputType!![0]
                                        filters = arrayOf(itemBean.lengthFilter)
                                        if (itemBean.inputTitle?.contains(getString(R.string.psw))!!) {
                                            transformationMethod =
                                                PasswordTransformationMethod.getInstance()
                                        }

                                        onFocusChangeListener = View.OnFocusChangeListener { v, b ->
                                            if (!b) {
                                                itemBean.inputContent = text.toString().trim()
                                                listenerSubmit()
                                                notifyItemChanged(inputListData.size - 1)
                                            }

                                        }
                                        singleClick {
                                            openInputMethod()
                                        }
                                    }
                                }
                            }
                            1 -> {
                                holder.containerView.let {
                                    it.tv_title_verify_code.text = itemBean.inputTitle
                                    it.et_content_verify_code.apply {
                                        setText(itemBean.inputContent)
                                        hint = itemBean.inputHint
                                        inputType = itemBean.inputType!![0]
                                        filters = arrayOf(itemBean.lengthFilter)
                                        onFocusChangeListener = View.OnFocusChangeListener { v, b ->
                                            if (!b) {
                                                itemBean.inputContent = text.toString().trim()
                                                listenerSubmit()
                                                notifyItemChanged(inputListData.size - 1)
                                            }
                                        }
                                        singleClick {
                                            openInputMethod()
                                        }
                                    }
                                    it.tv_get_verify.apply {
                                        itemBean.countdown?.apply {
                                            if (this > 0) {
                                                text = "${this / 1000}s"
                                            } else {
                                                isClickable = true
                                                text = getString(R.string.get_verify_code)
                                            }
                                        }
                                    }
                                }
                            }
                            2 -> {
                                holder.containerView.btn_confirm.apply {
                                    val b = itemBean.inputContent.equals("1")
                                    (isSelected != b).takeIf { it }?.run {
                                        isSelected = b
                                        isClickable = b
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            }

        }
    }

    var tvVerifyCode: AppCompatTextView? = null

    //获取验证码倒计时
    private fun startCountdown(tv: AppCompatTextView?) {
        var phoneNum: String? = ""
        for (itemBean in inputListData) {
            if (itemBean.inputTitle.equals(getString(R.string.phone_num))) {
                phoneNum = itemBean.inputContent.toString().trim()
                if (!StringUtils.checkMobilePhone(phoneNum)) {
                    ToastUtils.showToast(getString(R.string.please_input_right_phone_num))
                    return
                }
            }
        }

        tvVerifyCode = tv
        forgetPswViewModel.getVerifyCode(UserBean(phone = phoneNum, type = "2"))
    }

    private fun listenerSubmit() {
        var recordPosition: InputEditBean? = null
        for (itemBean in inputListData) {
            when (itemBean.shapeType) {
                0, 1 -> {
                    itemBean.inputContent.takeIf { it.isNullOrEmpty() }?.run {
                        inputListData.forEach { it1 ->
                            it1.shapeType.takeIf { it == 2 }?.run {
                                it1.inputContent = "0"
                            }
                        }
                        return
                    }
                }
                2 -> {
                    recordPosition = itemBean
                }
            }
        }
        recordPosition?.let {
            it.inputContent = "1"
        }
    }

    private fun submitData() {
        val userBean = UserBean()
        for (itemBean in inputListData) {
            when (itemBean.shapeType) {
                0, 1 -> {
                    itemBean.inputContent.takeIf { !it.isNullOrEmpty() } ?: return
                }
            }
            when (itemBean.inputTitle) {
                getString(R.string.user_name) -> {
                   userBean.userName = itemBean.inputContent
                }
                getString(R.string.phone_num) -> {
                    userBean.phone = itemBean.inputContent

                }
                getString(R.string.get_verify_code) -> {
                    userBean.verifyCode = itemBean.inputContent

                }
                getString(R.string.psw) -> {
                    userBean.psw = itemBean.inputContent

                }
                getString(R.string.confirm_psw) -> {
                    userBean.confirmPsw = itemBean.inputContent
                    if (!userBean.psw.equals(userBean.confirmPsw)){
                        ToastUtils.showToast(getString(R.string.not_equals_input_twice))
                        return
                    }
                }
            }
        }
        forgetPswViewModel.forgetUser(userBean)
        //
        LogUtils.i("==", "可以成功提交")
    }


    private fun setCountdownTime(count: Long) {
        for (i in 0 until inputListData.size) {
            inputListData[i].shapeType.takeIf { it == 1 }?.run {
                inputListData[i].countdown = count
            }
        }
    }

    private val inputListData by lazy {
        arrayListOf<InputEditBean>().apply {
            add(InputEditBean(0,
                "",
                getString(R.string.please_input_user_name),
                getString(R.string.user_name),
                InputFilter.LengthFilter(11),
                arrayOf(InputType.TYPE_CLASS_TEXT)

            ))
            add(InputEditBean(0,
                "",
                getString(R.string.please_input_phone_num),
                getString(R.string.phone_num),
                InputFilter.LengthFilter(11),
                arrayOf(InputType.TYPE_CLASS_NUMBER)))
            add(InputEditBean(1,
                "",
                getString(R.string.please_input_verify_code),
                getString(R.string.get_verify_code),
                InputFilter.LengthFilter(6),
                arrayOf(InputType.TYPE_CLASS_NUMBER)))
            add(InputEditBean(0,
                "",
                getString(R.string.please_input_six_twelve_num_letter),
                getString(R.string.psw),
                InputFilter.LengthFilter(12),
                arrayOf(InputType.TYPE_TEXT_VARIATION_PASSWORD, InputType.TYPE_CLASS_TEXT)))
            add(InputEditBean(0,
                "",
                getString(R.string.please_input_six_twelve_num_letter),
                getString(R.string.confirm_psw),
                InputFilter.LengthFilter(12),
                arrayOf(InputType.TYPE_TEXT_VARIATION_PASSWORD, InputType.TYPE_CLASS_TEXT)))
            add(InputEditBean(2))
        }
    }
}