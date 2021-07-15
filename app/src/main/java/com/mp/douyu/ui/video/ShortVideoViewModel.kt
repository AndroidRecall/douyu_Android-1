package com.mp.douyu.ui.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.HttpDataListBean
import com.mp.douyu.bean.ShortVideoBean
import com.mp.douyu.bean.ShortVideoListBean
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.NobodyConverterFactory
import com.mp.douyu.http.Respository
import com.mp.douyu.utils.ToastUtils
import org.json.JSONObject

class ShortVideoViewModel : MBaseViewModel() {
    private val allPopularData = MutableLiveData<HashMap<String, String?>>()
    private val allRecommendData = MutableLiveData<HashMap<String, String?>>()
    private val allSearchShortData = MutableLiveData<HashMap<String, String?>>()

    private val allFollowData = MutableLiveData<HashMap<String, String?>>()
    private val allCancelFollowData = MutableLiveData<HashMap<String, String?>>()
    private val allLikeVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allUnlikeVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allCommentShortVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allPlayStateData = MutableLiveData<HashMap<String, String?>>()

    val popularVideoData = Transformations.switchMap(allPopularData) {
        val mutableLiveData = MutableLiveData<ShortVideoListBean?>()
        Respository.getPopularVideoList(it,
            object : DisposableSubscriberAdapter<ShortVideoListBean?>(this) {
                override fun onNext(t: ShortVideoListBean?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }
    fun getPoplarVideoData(hashMap: HashMap<String, String?>) {
        allPopularData.value = hashMap
    }

    val recommendVideoData = Transformations.switchMap(allRecommendData) {
        val mutableLiveData = MutableLiveData<ShortVideoListBean?>()
        Respository.getRecommendVideoList(it,
            object : DisposableSubscriberAdapter<ShortVideoListBean?>(this) {
                override fun onNext(t: ShortVideoListBean?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getRecommendVideoData(hashMap: HashMap<String, String?>) {
        allRecommendData.value = hashMap
    }
    val _followResult = Transformations.switchMap(allFollowData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.follow(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("关注成功")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
            }
        })
        mutableLiveData
    }

    fun follow(hm: HashMap<String, String?>) {
        allFollowData.value = hm

    }

    val _cancelFollowResult = Transformations.switchMap(allCancelFollowData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.cancelFollow(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("取消关注")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
            }
        })
        mutableLiveData
    }

    fun cancelFollow(hm: HashMap<String, String?>) {
        allCancelFollowData.value = hm

    }

    val _likeVideoResult = Transformations.switchMap(allLikeVideoData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.shortVideoLike(it, object : DisposableSubscriberAdapter<String?>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("点赞成功")
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

    fun likeVideo(hm: HashMap<String, String?>) {
        allLikeVideoData.value = hm

    }

    val _unlikeVideoResult = Transformations.switchMap(allUnlikeVideoData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.shortVideoUnlike(it, object : DisposableSubscriberAdapter<String?>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("取消点赞")
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

    fun unlikeVideo(hm: HashMap<String, String?>) {
        allUnlikeVideoData.value = hm

    }

    val _commentShortVideoResult = Transformations.switchMap(allCommentShortVideoData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.shortVideoUnlike(it, object : DisposableSubscriberAdapter<String?>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("评论成功")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
            }
        })
        mutableLiveData
    }

    fun commentShortVideo(hm: HashMap<String, String?>) {
        allCommentShortVideoData.value = hm

    }

    val _searchShortData = Transformations.switchMap(allSearchShortData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<ShortVideoBean>?>()
        Respository.getSearchShortVideo(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<ShortVideoBean>>(this) {
                override fun onNext(t: HttpDataListBean<ShortVideoBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getSearchShortData(hashMap: HashMap<String, String?>) {
        allSearchShortData.value = hashMap
    }


    val _playStateData = Transformations.switchMap(allPlayStateData) {
        val mutableLiveData = MutableLiveData<String>()
        Respository.getPlayState(it, object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
            override fun onNext(t: NobodyConverterFactory.NoBodyEntity?) {
                t?.let {
                    try {
                        val result = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
//                        val code = result.getInt("status")
//                        val data = result.getString("data")
                        val message = result.getString("message")
                        mutableLiveData.value = message
                    } catch (e: Exception) {
                        mutableLiveData.value = "error"
                        ToastUtils.showToast(e.localizedMessage)
                    }
                }
            }
            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = t?.localizedMessage
//                ToastUtils.showToast("${t?.localizedMessage}")
            }

        })
        mutableLiveData
    }

    fun getPlayState(hashMap: HashMap<String, String?>) {
        allPlayStateData.value = hashMap
    }
}