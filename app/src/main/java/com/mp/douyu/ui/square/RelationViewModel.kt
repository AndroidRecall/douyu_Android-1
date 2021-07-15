package com.mp.douyu.ui.square

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.*
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.Respository

class RelationViewModel : MBaseViewModel() {
    private val allShortVideoLikesData = MutableLiveData<HashMap<String, String?>>()
    private val allPostLikesData = MutableLiveData<HashMap<String, String?>>()
    private val allFollowsData = MutableLiveData<HashMap<String, String?>>()
    private val allFansData = MutableLiveData<HashMap<String, String?>>()
    private val allFollowLiveListData = MutableLiveData<HashMap<String, String?>>()

    val shortVideoLikesData = Transformations.switchMap(allShortVideoLikesData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<LikeShortVideoBean>?>()
        Respository.getShortVideoLikes(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<LikeShortVideoBean>?>(this) {
                override fun onNext(t: HttpDataListBean<LikeShortVideoBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllShortVideoLikesData(hashMap: HashMap<String, String?>) {
        allShortVideoLikesData.value = hashMap
    }


    val postLikesData = Transformations.switchMap(allPostLikesData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getLikePosts(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllPostLikesData(hashMap: HashMap<String, String?>) {
        allPostLikesData.value = hashMap
    }

    val followsData = Transformations.switchMap(allFollowsData) {
        val mutableLiveData = MutableLiveData<List<CommonUserBean>?>()
        Respository.getFollows(it,
            object : DisposableSubscriberAdapter<List<CommonUserBean>?>(this) {
                override fun onNext(t: List<CommonUserBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllFollowsData(hashMap: HashMap<String, String?>) {
        allFollowsData.value = hashMap
    }
    val fansData = Transformations.switchMap(allFansData) {
        val mutableLiveData = MutableLiveData<List<CommonUserBean>?>()
        Respository.getFans(it,
            object : DisposableSubscriberAdapter<List<CommonUserBean>?>(this) {
                override fun onNext(t: List<CommonUserBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getFansData(hashMap: HashMap<String, String?>) {
        allFansData.value = hashMap
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
                }
            })
        mutableLiveData
    }

    fun getAllFollowLiveListData(hashMap: HashMap<String, String?>) {
        allFollowLiveListData.value = hashMap
    }
}