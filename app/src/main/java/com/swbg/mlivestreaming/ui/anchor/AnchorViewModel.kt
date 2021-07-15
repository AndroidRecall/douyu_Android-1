package com.swbg.mlivestreaming.ui.anchor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.blankj.utilcode.util.GsonUtils
import com.swbg.mlivestreaming.base.MBaseViewModel
import com.swbg.mlivestreaming.bean.*
import com.swbg.mlivestreaming.http.DisposableSubscriberAdapter
import com.swbg.mlivestreaming.http.NobodyConverterFactory
import com.swbg.mlivestreaming.http.Respository
import com.swbg.mlivestreaming.utils.ToastUtils
import org.json.JSONObject

class AnchorViewModel : MBaseViewModel() {
    private val allCityInviteListData = MutableLiveData<HashMap<String, String?>>()
    private val allCityInviteDetailData = MutableLiveData<HashMap<String, String?>>()
    private val allHookRecordData = MutableLiveData<HashMap<String, String?>>()
    private val allFollowLiveListData = MutableLiveData<HashMap<String, String?>>()

    private val allFollowAnchorData = MutableLiveData<HashMap<String, String?>>()
    private val allCancelFollowAnchorData = MutableLiveData<HashMap<String, String?>>()

    val cityInviteListData = Transformations.switchMap(allCityInviteListData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<InviteCityBean>?>()
        Respository.getCityInvites(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<InviteCityBean>?>(this) {
                override fun onNext(t: HttpDataListBean<InviteCityBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllCityInviteListData(hashMap: HashMap<String, String?>) {
        allCityInviteListData.value = hashMap
    }

    val cityInviteDetailData = Transformations.switchMap(allCityInviteDetailData) {
        val mutableLiveData = MutableLiveData<InviteCityBean?>()
        Respository.getCityInviteDetail(it,
            object : DisposableSubscriberAdapter<InviteCityBean?>(this) {
                override fun onNext(t: InviteCityBean?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllCityInviteDetailData(hashMap: HashMap<String, String?>) {
        allCityInviteDetailData.value = hashMap
    }

    val hookRecordData = Transformations.switchMap(allHookRecordData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<HookRecordBean>?>()
        Respository.getHookRecords(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<HookRecordBean>?>(this) {
                override fun onNext(t: HttpDataListBean<HookRecordBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllHookRecordData(hashMap: HashMap<String, String?>) {
        allHookRecordData.value = hashMap
    }

    val followLiveListData = Transformations.switchMap(allFollowLiveListData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<LiveBean>?>()
        Respository.getFollowLives(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<LiveBean>?>(this) {
                override fun onNext(t: HttpDataListBean<LiveBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast("${t?.localizedMessage}")
                }
            })
        mutableLiveData
    }

    fun getAllFollowLiveListData(hashMap: HashMap<String, String?>) {
        allFollowLiveListData.value = hashMap
    }

    val _followAnchorResult = Transformations.switchMap(allFollowAnchorData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.followAnchor(it,
            object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
                override fun onNext(t: NobodyConverterFactory.NoBodyEntity) {
                    t?.let {
                        val result = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
                        val code = result.getInt("status")
                        val message = result.getString("message")
                        val data = result.getString("data")

                        if (code == 200) {
                            mutableLiveData.value = message
                        }
                        ToastUtils.showToast(message)

                        dismissLoading()
                    }

                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    ToastUtils.showToast("${t?.localizedMessage}")
                }
            })
        mutableLiveData
    }

    fun followAnchor(hm: HashMap<String, String?>) {
        allFollowAnchorData.value = hm

    }

    val _cancelFollowAnchorResult = Transformations.switchMap(allCancelFollowAnchorData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.cancelFollowAnchor(it, object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity?>(this) {
            override fun onNext(t: NobodyConverterFactory.NoBodyEntity?) {
                t?.let {
                    val result = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
                    val code = result.getInt("status")
                    val message = result.getString("message")
                    val data = result.getString("data")

                    if (code == 200) {
                        mutableLiveData.value = message
                    }
                    ToastUtils.showToast(message)

                    dismissLoading()
                }

            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun cancelFollowAnchor(hm: HashMap<String, String?>) {
        allCancelFollowAnchorData.value = hm

    }
}