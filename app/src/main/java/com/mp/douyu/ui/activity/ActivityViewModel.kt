package com.mp.douyu.ui.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.ActivityBean
import com.mp.douyu.bean.AdvBean
import com.mp.douyu.bean.PageBaseBean
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.Respository

class ActivityViewModel : MBaseViewModel() {

    private val allActivityListData = MutableLiveData<HashMap<String, String?>>()
    private val allAdvListData = MutableLiveData<HashMap<String, String?>>()


    val _getActivityList = Transformations.switchMap(allActivityListData) {
        val mutableLiveData = MutableLiveData<PageBaseBean<ActivityBean>?>()
        Respository.getActivityList(it,
            object : DisposableSubscriberAdapter<PageBaseBean<ActivityBean>?>(this) {
                override fun onNext(t: PageBaseBean<ActivityBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getActivityList(hashMap: HashMap<String, String?>) {
        allActivityListData.value = hashMap
    }
    val _getAdvList = Transformations.switchMap(allAdvListData) {
        val mutableLiveData = MutableLiveData<List<AdvBean?>>()
        Respository.getAdvList(it,
            object : DisposableSubscriberAdapter<List<AdvBean?>>(this) {
                override fun onNext(t: List<AdvBean?>) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAdvList(hashMap: HashMap<String, String?>) {
        allAdvListData.value = hashMap
    }
}