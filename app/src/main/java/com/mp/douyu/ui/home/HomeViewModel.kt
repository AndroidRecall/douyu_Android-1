package com.mp.douyu.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.*
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.Respository
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.utils.ToastUtils

class HomeViewModel : MBaseViewModel() {

    private val allRecommendData = MutableLiveData<HashMap<String, String?>>()
    private val allMoreTypeData = MutableLiveData<HashMap<String, String?>>()
    private val allVideoListData = MutableLiveData<HashMap<String, String?>>()
    private val allPlayDetailData = MutableLiveData<HashMap<String, String?>>()
    private val allGetPlayLinkData = MutableLiveData<HashMap<String, String?>>()
    private val allSpecialTopicData = MutableLiveData<HashMap<String, String?>>()
    private val allRecommandGameData = MutableLiveData<HashMap<String, String?>>()
    private val allNvListData = MutableLiveData<HashMap<String, String?>>()
    private val allNvInfoData = MutableLiveData<HashMap<String, String?>>()
    private val allNvVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allCommendData = MutableLiveData<HashMap<String, String?>>()
    private val allGetSearchData = MutableLiveData<HashMap<String, String?>>()
    private val allLotteryOpenData = MutableLiveData<HashMap<String, String?>>()
    private val allSearchHistoryData = MutableLiveData<HashMap<String, String?>>()
    private val allPutSearchHistoryData = MutableLiveData<ArrayList<SearchHistoryBean>?>()
    private val allTransferData = MutableLiveData<HashMap<String, String?>>()
    private val allGetGameLinkData = MutableLiveData<HashMap<String, String?>>()
    private val allClickColletData = MutableLiveData<HashMap<String, String?>>()
    private val allClickGetDownloadLinkData = MutableLiveData<HashMap<String, String?>>()
    private val allCommentVideoData = MutableLiveData<HashMap<String, String?>>()

    val _recommendData = Transformations.switchMap(allRecommendData) {
        val mutableLiveData = MutableLiveData<RecommendedBean?>()
        Respository.getRecommend(object : DisposableSubscriberAdapter<RecommendedBean>(this) {
            override fun onNext(t: RecommendedBean?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
            }
        })
        mutableLiveData
    }
    val _moreTypeData = Transformations.switchMap(allMoreTypeData) {
        val mutableLiveData = MutableLiveData<MoreTypeBean?>()
        Respository.getMoreType(object : DisposableSubscriberAdapter<MoreTypeBean>(this) {
            override fun onNext(t: MoreTypeBean?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
            }
        })
        mutableLiveData
    }

    val _videoListData = Transformations.switchMap(allVideoListData) {
        val mutableLiveData = MutableLiveData<VideoListBean?>()
        Respository.getVideoList(it, object : DisposableSubscriberAdapter<VideoListBean>(this) {
            override fun onNext(t: VideoListBean?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
            }
        })
        mutableLiveData
    }

    val _videoPlayDetailData = Transformations.switchMap(allPlayDetailData) {
        val mutableLiveData = MutableLiveData<VideoPlayBean?>()
        Respository.getVideoPlayDetail(it,
            object : DisposableSubscriberAdapter<VideoPlayBean>(this) {
                override fun onNext(t: VideoPlayBean?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }
    val _videoGetLinkData = Transformations.switchMap(allGetPlayLinkData) {
        val mutableLiveData = MutableLiveData<VideoPlayLinkBean?>()
        Respository.getVideoPlayLink(it,
            object : DisposableSubscriberAdapter<VideoPlayLinkBean?>(this) {
                override fun onNext(t: VideoPlayLinkBean?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }
    val _videoSpecialTopicData = Transformations.switchMap(allSpecialTopicData) {
        val mutableLiveData = MutableLiveData<SpecialTopicBean?>()
        Respository.getVideoSpecialTopic(it,
            object : DisposableSubscriberAdapter<SpecialTopicBean?>(this) {
                override fun onNext(t: SpecialTopicBean?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    val _videoRecommandGameData = Transformations.switchMap(allRecommandGameData) {
        val mutableLiveData = MutableLiveData<List<RecommandGameBean?>>()
        Respository.getRecommendGame(object :
            DisposableSubscriberAdapter<List<RecommandGameBean?>>(this) {
            override fun onNext(t: List<RecommandGameBean?>) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
            }
        })
        mutableLiveData
    }
    val _getNvListData = Transformations.switchMap(allNvListData) {
        val mutableLiveData = MutableLiveData<HashMap<String, List<NvDetailBean>>?>()
        Respository.getNvList(object :
            DisposableSubscriberAdapter<HashMap<String, List<NvDetailBean>>?>(this) {
            override fun onNext(t: HashMap<String, List<NvDetailBean>>?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _getNvInfoData = Transformations.switchMap(allNvInfoData) {
        val mutableLiveData = MutableLiveData<NvInfoBean?>()
        Respository.getNvInfo(it, object : DisposableSubscriberAdapter<NvInfoBean?>(this) {
            override fun onNext(t: NvInfoBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _getNvVideoData = Transformations.switchMap(allNvVideoData) {
        val mutableLiveData = MutableLiveData<NvVideoBean?>()
        Respository.getNvVideo(it, object : DisposableSubscriberAdapter<NvVideoBean?>(this) {
            override fun onNext(t: NvVideoBean?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
            }
        })
        mutableLiveData
    }

    val _getCommendData = Transformations.switchMap(allCommendData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<CommentBean>?>()
        Respository.getCommendList(it, object : DisposableSubscriberAdapter<HttpDataListBean<CommentBean>?>(this) {
            override fun onNext(t: HttpDataListBean<CommentBean>?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
            }
        })
        mutableLiveData
    }

    val _getSearchData = Transformations.switchMap(allGetSearchData) {
        val mutableLiveData = MutableLiveData<PageBaseBean<VideoBean>?>()
        Respository.getSearchContent(it,
            object : DisposableSubscriberAdapter<PageBaseBean<VideoBean>?>(this) {
                override fun onNext(t: PageBaseBean<VideoBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    val _getLotteryOpen = Transformations.switchMap(allLotteryOpenData) {
        val mutableLiveData = MutableLiveData<LotteryOpenBean?>()
        Respository.getLotteryOpen(object : DisposableSubscriberAdapter<LotteryOpenBean?>() {
            override fun onNext(t: LotteryOpenBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }
    val _getSearchHistoryData = Transformations.switchMap(allSearchHistoryData) {
        val mutableLiveData = MutableLiveData<ArrayList<SearchHistoryBean>?>()
        mutableLiveData.value = StoredUserSources.getSearchHistory()
        mutableLiveData
    }
    val _getTransferData = Transformations.switchMap(allTransferData) {
        val mutableLiveData = MutableLiveData<Any?>()
        Respository.getTransferInfo(it, object : DisposableSubscriberAdapter<Any?>(this) {
            override fun onNext(t: Any?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }
    val _getGameLinkData = Transformations.switchMap(allGetGameLinkData) {
        val mutableLiveData = MutableLiveData<GetGameLinkBean?>()
        Respository.getGameLink(it, object : DisposableSubscriberAdapter<GetGameLinkBean?>(this,true) {
            override fun onNext(t: GetGameLinkBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }
    val _getClickColletData = Transformations.switchMap(allClickColletData) {
        val mutableLiveData = MutableLiveData<Any?>()
        Respository.clickCollect(it, object : DisposableSubscriberAdapter<Any?>(this,true) {
            override fun onNext(t: Any?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }
    val _getDoenloadLinkData = Transformations.switchMap(allClickGetDownloadLinkData) {
        val mutableLiveData = MutableLiveData<VideoDoenloadLink?>()
        Respository.getDownloadLink(it, object : DisposableSubscriberAdapter<VideoDoenloadLink?>(this) {
            override fun onNext(t: VideoDoenloadLink?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _putSearchHistory = Transformations.switchMap(allPutSearchHistoryData) {
        val mutableLiveData = MutableLiveData<Any?>()
        it?.let {
            mutableLiveData.value = StoredUserSources.putSearchHistory(it)
        }
        mutableLiveData
    }
    val _commentVideoResult = Transformations.switchMap(allCommentVideoData) { it ->
        val mutableLiveData = MutableLiveData<String>()
        Respository.commentVideo(it,
            object : DisposableSubscriberAdapter<String?>(this) {
                override fun onNext(t: String?) {
                    ToastUtils.showToast("评论成功，等待审核~")
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    ToastUtils.showToast("${t?.localizedMessage}")
                }
            })
        mutableLiveData
    }
    fun getRecommendData() {
        allRecommendData.value = allRecommendData.value
    }

    fun getMorTypeData() {
        allMoreTypeData.value = allMoreTypeData.value
    }

    fun getVideoList(hashMap: HashMap<String, String?>) {
        allVideoListData.value = hashMap
    }

    fun getVideoPlayerDetail(hashMap: HashMap<String, String?>) {
        allPlayDetailData.value = hashMap
    }

    fun getPlayLink(hashMap: HashMap<String, String?>) {
        allGetPlayLinkData.value = hashMap
    }

    fun getSpecialTopic(hashMap: HashMap<String, String?>) {
        allSpecialTopicData.value = hashMap
    }

    fun getRecommandGame() {
        allRecommandGameData.value = allRecommandGameData.value
    }

    fun getNvList() {
        allNvListData.value = allNvListData.value
    }

    fun getNvInfo(hashMap: HashMap<String, String?>) {
        allNvInfoData.value = hashMap
    }

    fun getNvVideo(hashMap: HashMap<String, String?>) {
        allNvVideoData.value = hashMap
    }

    fun getCommendList(hashMap: HashMap<String, String?>) {
        allCommendData.value = hashMap
    }

    fun getSearchContent(hashMap: HashMap<String, String?>) {
        allGetSearchData.value = hashMap
    }

    fun getLotteryOpen() {
        allLotteryOpenData.value = allLotteryOpenData.value
    }

    fun getSearchHistory() {
        allSearchHistoryData.value = allSearchHistoryData.value
    }

    fun putSearchHistory(listSHistory: ArrayList<SearchHistoryBean>?) {
        allPutSearchHistoryData.value = listSHistory
    }

    fun beginTransfer(hashMapOf: HashMap<String, String?>) {
        allTransferData.value = hashMapOf

    }

    fun getGameLink(hashMapOf: java.util.HashMap<String, String?>) {
        allGetGameLinkData.value = hashMapOf
    }

    fun clickLike(hashMapOf: HashMap<String, String?>) {
        allClickColletData.value = hashMapOf
    }

    fun getDownloadLinks(map: HashMap<String, String?>) {
        allClickGetDownloadLinkData.value = map
    }


    fun commentVideo(hashMapOf: HashMap<String, String?>) {
        allCommentVideoData.value = hashMapOf
    }
}