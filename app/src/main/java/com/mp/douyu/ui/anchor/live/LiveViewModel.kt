package com.mp.douyu.ui.anchor.live

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.blankj.utilcode.util.GsonUtils
import com.luck.picture.lib.entity.LocalMedia
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.*
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.NobodyConverterFactory
import com.mp.douyu.http.Respository
import com.mp.douyu.utils.ToastUtils
import com.mp.douyu.utils.UploadUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class LiveViewModel : MBaseViewModel() {


    private val allFollowData = MutableLiveData<HashMap<String, String?>>()
    private val allCancelFollowData = MutableLiveData<HashMap<String, String?>>()
    private val allLikeVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allUnlikeVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allLivesData = MutableLiveData<HashMap<String, String?>>()
    private val allGiftsData = MutableLiveData<HashMap<String, String?>>()
    private val allSendGiftData = MutableLiveData<HashMap<String, String?>>()
    private val allVipData = MutableLiveData<HashMap<String, String?>>()
    private val allRanksData = MutableLiveData<HashMap<String, String?>>()
    private val allEnterLiveData = MutableLiveData<HashMap<String, String?>>()
    private val allSearchLiveData = MutableLiveData<HashMap<String, String?>>()
    private val allJoinHookData = MutableLiveData<HashMap<String, String?>>()
    private val allStartLiveData = MutableLiveData<UploadParam?>()
    private val allStopLiveData = MutableLiveData<UploadParam?>()

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
                ToastUtils.showToast("${t?.localizedMessage}")
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
                ToastUtils.showToast("${t?.localizedMessage}")
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

    val _livesData = Transformations.switchMap(allLivesData) { it ->
        val mutableLiveData = MutableLiveData<HttpDataListBean<LiveBean>?>()
        Respository.getLives(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<LiveBean>>(this) {
                override fun onNext(t: HttpDataListBean<LiveBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                }
            })
        mutableLiveData
    }

    fun getLivesData(hm: HashMap<String, String?>) {
        allLivesData.value = hm

    }

    val _giftsData = Transformations.switchMap(allGiftsData) { it ->
        val mutableLiveData = MutableLiveData<HttpDataListBean<GiftBean>?>()
        Respository.getGifts(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<GiftBean>>(this) {
                override fun onNext(t: HttpDataListBean<GiftBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                }
            })
        mutableLiveData
    }

    fun getGiftsData(hm: HashMap<String, String?>) {
        allGiftsData.value = hm

    }

    var _sendGiftsResult = Transformations.switchMap(allSendGiftData) { it ->
        val mutableLiveData = MutableLiveData<String>()
        Respository.sendGift(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String) {
                ToastUtils.showToast("赠送成功")
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun sendGift(hm: HashMap<String, String?>) {
        allSendGiftData.value = hm

    }

    fun sendGift2(hm: HashMap<String, String?>, param: DisposableSubscriberAdapter<String>) {
        val mutableLiveData = MutableLiveData<String>()
        allSendGiftData.value = hm
        Respository.sendGift(allSendGiftData.value!!, param)
    }

    val _vipData = Transformations.switchMap(allVipData) { it ->
        val mutableLiveData = MutableLiveData<List<MyVipBean?>>()
        Respository.getLiveVipList(it,
            object : DisposableSubscriberAdapter<List<MyVipBean?>>(this) {
                override fun onNext(t: List<MyVipBean?>) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                }
            })
        mutableLiveData
    }

    fun getVipData(hm: HashMap<String, String?>) {
        allVipData.value = hm

    }

    val _ranksData = Transformations.switchMap(allRanksData) { it ->
        val mutableLiveData = MutableLiveData<LiveRanksBean>()
        Respository.getLiveRanksList(it, object : DisposableSubscriberAdapter<LiveRanksBean>(this) {
            override fun onNext(t: LiveRanksBean) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
            }
        })
        mutableLiveData
    }

    fun getRanksData(hm: HashMap<String, String?>) {
        allRanksData.value = hm

    }

    val _enterLiveData = Transformations.switchMap(allEnterLiveData) { it ->
        showLoadingView(true)
        val mutableLiveData = MutableLiveData<EnterLiveRes?>()
        Respository.enterLive(it,
            object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
                override fun onNext(t: NobodyConverterFactory.NoBodyEntity) {
                    t?.let {
                        val result = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
                        val code = result.getInt("status")
                        val message = result.getString("message")
                        val data = result.getString("data")
                        var resultBean: EnterLiveRes? = null
                        if (code == 200) {
                            try {
                                resultBean = GsonUtils.fromJson(data, EnterLiveRes::class.java)
                            } catch (e: Exception) {
                            }
                            mutableLiveData.value = resultBean

                        } else {
                            ToastUtils.showToast(message)
                        }
                        dismissLoading()
                    }

                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    dismissLoading()
                    ToastUtils.showToast("${t?.localizedMessage}")
                }
            })
        mutableLiveData
    }

    fun enterLive(hm: HashMap<String, String?>) {
        allEnterLiveData.value = hm

    }

    val _startLiveData = Transformations.switchMap(allStartLiveData) { it ->
        val mutableLiveData = MutableLiveData<LiveStreamingBean>()
        Respository.startLive(it!!.bodyMap,
            it.parts,
            object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
                override fun onNext(t: NobodyConverterFactory.NoBodyEntity) {
                    t?.let {
                        val result = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
                        val code = result.getInt("status")
                        val message = result.getString("message")
                        val data = result.getString("data")
                        var resultBean: LiveStreamingBean? = null
                        if (code == 200) {
                            try {
                                resultBean = GsonUtils.fromJson(data, LiveStreamingBean::class.java)
                            } catch (e: Exception) {
                            }
                            mutableLiveData.value = resultBean

                        } else {
                            if (message.contains("重复")) {
                                //重复开播,拿上一次的流
                                try {
                                    resultBean =
                                        GsonUtils.fromJson(data, LiveStreamingBean::class.java)
                                } catch (e: Exception) {
                                }

                                mutableLiveData.value = resultBean
                            } else {
                                ToastUtils.showToast(message)
                            }
                        }
                        dismissLoading()
                    }
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    ToastUtils.showToast("${t?.localizedMessage}")
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }
    val _stopLiveData = Transformations.switchMap(allStopLiveData) { it ->
        val mutableLiveData = MutableLiveData<String>()
        Respository.stopLive(it!!.bodyMap,
            it.parts,
            object : DisposableSubscriberAdapter<String>(this) {
                override fun onNext(t: String) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                }
            })
        mutableLiveData
    }

    fun startLive(data: MutableList<LocalMedia>?, title: String?, notice: String?, groupId: String?, status: Int) {

        val parts: MutableList<MultipartBody.Part> = arrayListOf()
        val bodyMap: MutableMap<String, RequestBody> = hashMapOf()
        data?.let {
            for ((index, itemBean) in data.withIndex()) {
                val file = File(itemBean.compressPath)
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val part = MultipartBody.Part.createFormData("image", file.name, requestFile)
                parts.add(part)
            }
        }
        title?.let {
            bodyMap["title"] = UploadUtils.toRequestBody(it)!!
        }
        notice?.let {
            bodyMap["notice"] = UploadUtils.toRequestBody(it)!!
        }
        groupId?.let {
            bodyMap["GroupId"] = UploadUtils.toRequestBody(it)!!
        }
        bodyMap["status"] = UploadUtils.toRequestBody(status.toString())!!
        allStartLiveData.value = UploadParam(parts = parts, bodyMap = bodyMap)
    }

    fun stopLive(data: MutableList<LocalMedia>?, title: String?, groupId: String?, status: Int) {

        val parts: MutableList<MultipartBody.Part> = arrayListOf()
        val bodyMap: MutableMap<String, RequestBody> = hashMapOf()
        data?.let {
            for ((index, itemBean) in data.withIndex()) {
                val file = File(itemBean.compressPath)
                val requestFile =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                val part = MultipartBody.Part.createFormData("image", file.name, requestFile)
                parts.add(part)
            }
        }
        title?.let {
            bodyMap["title"] = UploadUtils.toRequestBody(it)!!
        }
        groupId?.let {
            bodyMap["GroupId"] = UploadUtils.toRequestBody(it)!!
        }
        bodyMap["status"] = UploadUtils.toRequestBody(status.toString())!!
        allStopLiveData.value = UploadParam(parts = parts, bodyMap = bodyMap)
    }

    val _searchLivesData = Transformations.switchMap(allSearchLiveData) { it ->
        val mutableLiveData = MutableLiveData<List<LiveBean>?>()
        Respository.getSearchLive(it, object : DisposableSubscriberAdapter<List<LiveBean>>(this) {
            override fun onNext(t: List<LiveBean>?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
            }
        })
        mutableLiveData
    }

    fun getSearchLivesData(hm: HashMap<String, String?>) {
        allSearchLiveData.value = hm

    }

    val _joinHookResult = Transformations.switchMap(allJoinHookData) {
        val mutableLiveData = MutableLiveData<JoinHookResp?>()
        Respository.joinHook(it,
            object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
                override fun onNext(t: NobodyConverterFactory.NoBodyEntity?) {
                    t?.let {
                        val result = JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
                        val code = result.getInt("status")
                        val message = result.getString("message")
                        val data = result.getString("data")
                        if (code == 200) {
                            val result = GsonUtils.fromJson(data, JoinHookResp::class.java)
                            ToastUtils.showToast("参与成功,您的幸运号码为[${result?.luck_number}]")
                            mutableLiveData.value = result

                        } else {
                            ToastUtils.showToast(message)
                        }
                    }
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    ToastUtils.showToast("${t?.localizedMessage}")
                }

            })
        mutableLiveData
    }

    fun joinHook(hm: HashMap<String, String?>) {
        allJoinHookData.value = hm

    }
}