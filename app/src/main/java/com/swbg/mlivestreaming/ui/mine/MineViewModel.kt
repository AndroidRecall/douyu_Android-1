package com.swbg.mlivestreaming.ui.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.swbg.mlivestreaming.base.MBaseViewModel
import com.swbg.mlivestreaming.bean.*
import com.swbg.mlivestreaming.http.DisposableSubscriberAdapter
import com.swbg.mlivestreaming.http.Respository
import com.swbg.mlivestreaming.http.exception.Http400Exception
import com.swbg.mlivestreaming.http.exception.Http401Exception
import com.swbg.mlivestreaming.utils.ToastUtils

class MineViewModel : MBaseViewModel() {

    private val allUserInfoData = MutableLiveData<UserInfoBean>()
    private val allUserInfo2Data = MutableLiveData<HashMap<String, String?>>()
    private val allAvatarChooseData = MutableLiveData<AvatarChooseBean>()
    private val allAvatarData = MutableLiveData<HashMap<String, String?>>()
    private val allBindPhoneData = MutableLiveData<HashMap<String, String?>>()
    private val allTradeRecorderData = MutableLiveData<HashMap<String, String?>>()
    private val allFeedBackData = MutableLiveData<HashMap<String, String?>>()
    private val allPayListData = MutableLiveData<HashMap<String, String?>>()
    private val allPayData = MutableLiveData<HashMap<String, String?>>()
    private val allVersionUpdate = MutableLiveData<HashMap<String, String?>>()
    private val allInviteFriendsData = MutableLiveData<HashMap<String, String?>>()
    private val allGetTaskData = MutableLiveData<HashMap<String, String?>>()
    private val allSignInTaskData = MutableLiveData<HashMap<String, String?>>()
    private val allVipList = MutableLiveData<HashMap<String, String?>>()
    private val allDialogList = MutableLiveData<HashMap<String, String?>>()
    private val allChargeVipList = MutableLiveData<HashMap<String, String?>>()
    private val allGetChargeResult = MutableLiveData<HashMap<String, String?>>()
    private val allApplyAnchorResult = MutableLiveData<HashMap<String, String?>>()
    private val allCollectVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allRecordVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allCommentVideoData = MutableLiveData<HashMap<String, String?>>()
    private val allUserReplyData = MutableLiveData<HashMap<String, String?>>()
    private val allUserLikeData = MutableLiveData<HashMap<String, String?>>()
    private val allAnchorApplyData = MutableLiveData<HashMap<String, String?>>()
    val _getUserInfo = Transformations.switchMap(allUserInfoData) {
        val mutableLiveData = MutableLiveData<UserInfoBean?>()
        Respository.getUserInfo(object : DisposableSubscriberAdapter<UserInfoBean?>(this) {
            override fun onNext(t: UserInfoBean?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
            }
        })
        mutableLiveData
    }
    val _getUserInfo2 = Transformations.switchMap(allUserInfo2Data) {
        val mutableLiveData = MutableLiveData<List<UserInfo2Bean>?>()
        Respository.getUserInfo2(it,
            object : DisposableSubscriberAdapter<List<UserInfo2Bean>>(this) {
                override fun onNext(t: List<UserInfo2Bean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }
    val _getUserAvatar = Transformations.switchMap(allAvatarChooseData) {
        Respository.getUserAvatars()
    }
    val _setUserAvatar = Transformations.switchMap(allAvatarData) {
        val mutableLiveData = MutableLiveData<Boolean?>()
        Respository.setUserAvatars(it, object : DisposableSubscriberAdapter<Boolean>(this) {
            override fun onNext(t: Boolean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _bindPhone = Transformations.switchMap(allBindPhoneData) {
        val mutableLiveData = MutableLiveData<Boolean?>()
        Respository.bindPhone(it, object : DisposableSubscriberAdapter<Boolean>(this) {
            override fun onNext(t: Boolean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }
    val _tradeRecord = Transformations.switchMap(allTradeRecorderData) {
        val mutableLiveData = MutableLiveData<TradeRecordBean?>()
        Respository.tradeRecord(it, object : DisposableSubscriberAdapter<TradeRecordBean>(this) {
            override fun onNext(t: TradeRecordBean?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = mutableLiveData.value
            }
        })
        mutableLiveData
    }

    val _feedback = Transformations.switchMap(allFeedBackData) {
        val mutableLiveData = MutableLiveData<Any?>()
        Respository.feedback(it, object : DisposableSubscriberAdapter<Boolean>(this) {
            override fun onNext(t: Boolean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _payList = Transformations.switchMap(allPayListData) {
        val mutableLiveData = MutableLiveData<PayListBean>()
        Respository.payList(object : DisposableSubscriberAdapter<PayListBean>(this) {
            override fun onNext(t: PayListBean) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _beginPay = Transformations.switchMap(allPayData) {
        val mutableLiveData = MutableLiveData<PayContentBean?>()
        Respository.beginPay(it, object : DisposableSubscriberAdapter<PayContentBean>(this) {
            override fun onNext(t: PayContentBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _verionUpdate = Transformations.switchMap(allVersionUpdate) {
        val mutableLiveData = MutableLiveData<VersionUpdateBean?>()
        Respository.getVersionUpdate(object : DisposableSubscriberAdapter<VersionUpdateBean>(this) {
            override fun onNext(t: VersionUpdateBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _getInviteFriends = Transformations.switchMap(allInviteFriendsData) {
        val mutableLiveData = MutableLiveData<InviteFriendsBean?>()
        Respository.getInviteFriends(object : DisposableSubscriberAdapter<InviteFriendsBean>(this) {
            override fun onNext(t: InviteFriendsBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _getGetTask = Transformations.switchMap(allGetTaskData) {
        val mutableLiveData = MutableLiveData<TaskCenterBean?>()
        Respository.getGetTask(object : DisposableSubscriberAdapter<TaskCenterBean?>(this) {
            override fun onNext(t: TaskCenterBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _getSignInTask = Transformations.switchMap(allSignInTaskData) {
        val mutableLiveData = MutableLiveData<SignInBean?>()
        Respository.getSignInTask(object : DisposableSubscriberAdapter<SignInBean?>(this) {
            override fun onNext(t: SignInBean?) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _getVipList = Transformations.switchMap(allVipList) {
        val mutableLiveData = MutableLiveData<List<MyVipBean?>>()
        Respository.getVipList(object : DisposableSubscriberAdapter<List<MyVipBean?>>(this) {
            override fun onNext(t: List<MyVipBean?>) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _getDialogList = Transformations.switchMap(allDialogList) {
        val mutableLiveData = MutableLiveData<List<HomeDialogBean?>>()
        Respository.getDialogList(object :
            DisposableSubscriberAdapter<List<HomeDialogBean?>>(this, false) {
            override fun onNext(t: List<HomeDialogBean?>) {
                mutableLiveData.value = t
            }
        })
        mutableLiveData
    }

    val _chargeVipList = Transformations.switchMap(allChargeVipList) {
        val mutableLiveData = MutableLiveData<Any?>()
        Respository.chargeVipList(it, object : DisposableSubscriberAdapter<Any?>(this, false) {
            override fun onNext(t: Any?) {
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                if (t is Http400Exception) mutableLiveData.value = t.message
            }
        })
        mutableLiveData
    }

    val _getChargeResult = Transformations.switchMap(allGetChargeResult) {
        val mutableLiveData = MutableLiveData<ChargeResultBean?>()
        Respository.getChargeResult(it,
            object : DisposableSubscriberAdapter<ChargeResultBean?>(this) {
                override fun onNext(t: ChargeResultBean?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    var _applyAnchorResult = Transformations.switchMap(allApplyAnchorResult) { it ->
        val mutableLiveData = MutableLiveData<String>()
        Respository.applyAnchor(it, object : DisposableSubscriberAdapter<String?>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("申请成功")
                mutableLiveData.value = t
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }


    fun getUserInfo() {
        allUserInfoData.value = allUserInfoData.value
    }

    fun getUserInfo(param: DisposableSubscriberAdapter<UserInfoBean?>) {
        Respository.getUserInfo(param)
    }

    fun getUserInfo2(map: HashMap<String, String?>) {
        allUserInfo2Data.value = map
    }

    fun getUserAvatar() {
        allAvatarChooseData.value = allAvatarChooseData.value
    }

    fun editAvatar(userBean: HashMap<String, String?>) {
        allAvatarData.value = userBean
    }

    fun bindPhone(hm: HashMap<String, String?>) {
        allBindPhoneData.value = hm
    }

    fun tradeRecorder(hm: HashMap<String, String?>) {
        allTradeRecorderData.value = hm
    }

    fun feedBack(hm: HashMap<String, String?>) {
        allFeedBackData.value = hm

    }

    fun getPayList() {
        allPayListData.value = allPayListData.value

    }

    fun beginPay(hm: HashMap<String, String?>) {
        allPayData.value = hm

    }

    fun getVersionUpdate() {
        allVersionUpdate.value = allVersionUpdate.value

    }

    fun getInviteFriends() {
        allInviteFriendsData.value = allInviteFriendsData.value
    }

    fun getTaskContent() {
        allGetTaskData.value = allGetTaskData.value

    }

    fun signInTask() {
        allSignInTaskData.value = allSignInTaskData.value

    }

    fun getVipList() {
        allVipList.value = allVipList.value

    }

    fun getDialogList() {
        allDialogList.value = allDialogList.value

    }

    fun chargeVip(hashMapOf: HashMap<String, String?>?) {
        allChargeVipList.value = hashMapOf
    }

    fun getPayResult(map: HashMap<String, String?>) {
        allGetChargeResult.value = map
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text


    fun applyAnchor(hm: HashMap<String, String?>) {
        allApplyAnchorResult.value = hm

    }

    val collectVideoData = Transformations.switchMap(allCollectVideoData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<MineVideoBean>?>()
        Respository.getCollectVideos(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<MineVideoBean>?>(this) {
                override fun onNext(t: HttpDataListBean<MineVideoBean>?) {
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

    fun getAllCollectVideoData(hashMap: HashMap<String, String?>) {
        allCollectVideoData.value = hashMap
    }

    val recordVideoData = Transformations.switchMap(allRecordVideoData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<MineVideoBean>?>()
        Respository.getRecordVideos(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<MineVideoBean>?>(this) {
                override fun onNext(t: HttpDataListBean<MineVideoBean>?) {
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

    fun getAllRecordVideoData(hashMap: HashMap<String, String?>) {
        allRecordVideoData.value = hashMap
    }

    val commentVideoData = Transformations.switchMap(allCommentVideoData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<MineVideoBean>?>()
        Respository.getCommentVideos(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<MineVideoBean>?>(this) {
                override fun onNext(t: HttpDataListBean<MineVideoBean>?) {
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

    fun getAllCommentVideoData(hashMap: HashMap<String, String?>) {
        allCommentVideoData.value = hashMap
    }

    val userReplyData = Transformations.switchMap(allUserReplyData) {
        val mutableLiveData = MutableLiveData<List<MineMessageBean>?>()
        Respository.getUserReplys(it,
            object : DisposableSubscriberAdapter<List<MineMessageBean>?>(this) {
                override fun onNext(t: List<MineMessageBean>?) {
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

    fun getUserReplyData(hashMap: HashMap<String, String?>) {
        allUserReplyData.value = hashMap
    }

    val userLikeData = Transformations.switchMap(allUserLikeData) {
        val mutableLiveData = MutableLiveData<List<MineMessageBean>?>()
        Respository.getUserLikes(it,
            object : DisposableSubscriberAdapter<List<MineMessageBean>?>(this) {
                override fun onNext(t: List<MineMessageBean>?) {
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

    fun getUserLikeData(hashMap: HashMap<String, String?>) {
        allUserLikeData.value = hashMap
    }
}