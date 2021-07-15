package com.mp.douyu.ui.login_register.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.ChannelIdBean
import com.mp.douyu.bean.SystemSettingBean
import com.mp.douyu.bean.TokenBean
import com.mp.douyu.bean.UserBean
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.NobodyConverterFactory
import com.mp.douyu.http.Respository
import java.util.HashMap

class LoginViewModel : MBaseViewModel() {

    private val allUserBean = MutableLiveData<UserBean>()
    private val imageVerifyData = MutableLiveData<Any?>()
    private val registerData = MutableLiveData<UserBean>()
    private val registerUserNameData = MutableLiveData<UserBean>()
    private val loginData = MutableLiveData<UserBean>()
    private val sysSettingData = MutableLiveData<SystemSettingBean>()
    private val allGetChannelIdData = MutableLiveData<HashMap<String,String?>>()

    private val _verifyCode = Transformations.switchMap(allUserBean) {
        Respository.getVerifyCode(it)
    }
    private val _imageVerifyResult = Transformations.switchMap(imageVerifyData) {
        Respository.getImageVerify()
    }
    private val _registerResult = Transformations.switchMap(registerData) {
        val mutableLiveData = MutableLiveData<Any?>()
        Respository.registerUser(it, object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
            override fun onNext(t: NobodyConverterFactory.NoBodyEntity?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }
    private val _registerUserNameResult = Transformations.switchMap(registerUserNameData) {
        val mutableLiveData = MutableLiveData<Any?>()
        Respository.registerUserUserName(it, object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
            override fun onNext(t: NobodyConverterFactory.NoBodyEntity?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }
    private val _loginResult = Transformations.switchMap(loginData) {
        val mutableLiveData = MutableLiveData<TokenBean?>()
        Respository.login(it, object : DisposableSubscriberAdapter<TokenBean>(this) {
            override fun onNext(t: TokenBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    private val _sysSetting = Transformations.switchMap(sysSettingData) {
        Respository.getSystemSetting()
    }

    private val _allGetChannelId = Transformations.switchMap(allGetChannelIdData) {
        val mutableLiveData = MutableLiveData<ChannelIdBean?>()
        Respository.getChannelId(it, object : DisposableSubscriberAdapter<ChannelIdBean?>(this) {
            override fun onNext(t: ChannelIdBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }


    fun getVerifyCode(userBean: UserBean) {
        allUserBean.value = userBean
    }

    fun getImageVerify() {
        imageVerifyData.value = imageVerifyData.value
    }

    fun registerUser(userBean: UserBean) {
        registerData.value = userBean
    }

    fun registerUserUserName(userBean: UserBean) {
        registerUserNameData.value = userBean
    }

    fun login(userBean: UserBean) {
        loginData.value = userBean
        /*
            remoteDataSource.enqueueLoading({
                //主动延迟一段时间，避免弹窗太快消失
                delay(2000)
                login(hashMapOf("phone" to userBean.phone, "password" to userBean.psw))
            }).apply {

            }*/
    }

    fun getSysSetting() {
        sysSettingData.value = sysSettingData.value
    }

    fun getChannelId(hm: HashMap<String, String?>) {
        allGetChannelIdData.value = hm
    }

    val verifyCode: LiveData<Boolean?> = _verifyCode
    val imageVerify: LiveData<UserBean?> = _imageVerifyResult
    val registerResult: LiveData<Any?> = _registerResult
    val registerUserResult: LiveData<Any?> = _registerUserNameResult
    val loginResult: LiveData<TokenBean?> = _loginResult
    val sysSettingResult: LiveData<SystemSettingBean?> = _sysSetting
    val getChannelId : LiveData<ChannelIdBean?> = _allGetChannelId


}