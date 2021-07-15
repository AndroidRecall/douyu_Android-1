package com.swbg.mlivestreaming.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.swbg.mlivestreaming.base.MBaseViewModel
import com.swbg.mlivestreaming.bean.ActivityBean
import com.swbg.mlivestreaming.bean.AdvBean
import com.swbg.mlivestreaming.bean.PageBaseBean
import com.swbg.mlivestreaming.bean.VideoBean
import com.swbg.mlivestreaming.http.DisposableSubscriberAdapter
import com.swbg.mlivestreaming.http.Respository

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