package com.mp.douyu.ui.login_register.forget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.UserBean
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.Respository

class ForgetViewModel : MBaseViewModel() {

    private val forgetData = MutableLiveData<UserBean>()
    private val allUserBean = MutableLiveData<UserBean>()

    private val _forgetResult = Transformations.switchMap(forgetData) {
        val mutableLiveData = MutableLiveData<Boolean?>()
        Respository.forgetPsw(it, object : DisposableSubscriberAdapter<Boolean>(this) {
            override fun onNext(t: Boolean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }
    private val _verifyCode = Transformations.switchMap(allUserBean) {
        Respository.getVerifyCode(it)
    }

    fun forgetUser(userBean: UserBean) {
        forgetData.value = userBean
    }


    fun getVerifyCode(userBean: UserBean) {
        allUserBean.value = userBean
    }

    val forgetResult: LiveData<Boolean?> = _forgetResult
    val verifyCode: LiveData<Boolean?> = _verifyCode
}