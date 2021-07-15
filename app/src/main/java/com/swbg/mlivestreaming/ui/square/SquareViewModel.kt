package com.swbg.mlivestreaming.ui.square

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.swbg.mlivestreaming.base.MBaseViewModel
import com.swbg.mlivestreaming.bean.BetGotBean
import com.swbg.mlivestreaming.bean.BetRankBean
import com.swbg.mlivestreaming.bean.HttpDataListBean
import com.swbg.mlivestreaming.bean.SquareCircleBean
import com.swbg.mlivestreaming.http.DisposableSubscriberAdapter
import com.swbg.mlivestreaming.http.NobodyConverterFactory
import com.swbg.mlivestreaming.http.Respository
import com.swbg.mlivestreaming.utils.ToastUtils

class SquareViewModel : MBaseViewModel() {
    private val allSquareCircleData = MutableLiveData<HashMap<String, String?>>()
    private val joinCirclesData = MutableLiveData<HashMap<String, String?>>()
    private val allJoinCircleData = MutableLiveData<HashMap<String, String?>>()
    private val allExitCircleData = MutableLiveData<HashMap<String, String?>>()
    private val allBetGodsData = MutableLiveData<HashMap<String, String?>>()
    private val allBetGodFollowData = MutableLiveData<HashMap<String, String?>>()
    private val allBetRankData = MutableLiveData<HashMap<String, String?>>()

    val squareCircleData = Transformations.switchMap(allSquareCircleData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<SquareCircleBean>?>()
        Respository.getCommunityCircles(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<SquareCircleBean>?>(this) {
                override fun onNext(t: HttpDataListBean<SquareCircleBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllSquareCircleData(hashMap: HashMap<String, String?>) {
        allSquareCircleData.value = hashMap
    }

    val _joinCircleResult = Transformations.switchMap(allJoinCircleData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.joinCircle(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("加入成功")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun joinCircle(hm: HashMap<String, String?>) {
        allJoinCircleData.value = hm

    }

    val _exitCircleResult = Transformations.switchMap(allExitCircleData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.exitCircle(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("离开圈子")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun exitCircle(hm: HashMap<String, String?>) {
        allExitCircleData.value = hm

    }

    val _joinCirclesData = Transformations.switchMap(joinCirclesData) {
        val mutableLiveData = MutableLiveData<List<SquareCircleBean>?>()
        Respository.getJoinCircles(it,
            object : DisposableSubscriberAdapter<List<SquareCircleBean>>(this) {
                override fun onNext(t: List<SquareCircleBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getJoinCirclesData(hashMap: HashMap<String, String?>) {
        joinCirclesData.value = hashMap
    }

    val betGodsData = Transformations.switchMap(allBetGodsData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<BetGotBean>?>()
        Respository.getBetGods(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<BetGotBean>>(this) {
                override fun onNext(t: HttpDataListBean<BetGotBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllBetGodsData(hashMap: HashMap<String, String?>) {
        allBetGodsData.value = hashMap
    }
    val betGodsFollowData = Transformations.switchMap(allBetGodFollowData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<BetGotBean>?>()
        Respository.getBetGodFollows(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<BetGotBean>>(this) {
                override fun onNext(t: HttpDataListBean<BetGotBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllBetGodFollowsData(hashMap: HashMap<String, String?>) {
        allBetGodFollowData.value = hashMap
    }
    val betRankData = Transformations.switchMap(allBetRankData) {
        val mutableLiveData = MutableLiveData<BetRankBean?>()
        Respository.getBetRanks(it,
            object : DisposableSubscriberAdapter<BetRankBean>(this) {
                override fun onNext(t: BetRankBean?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllBetRankData(hashMap: HashMap<String, String?>) {
        allBetRankData.value = hashMap
    }
}