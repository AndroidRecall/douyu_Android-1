package com.mp.douyu.http

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mp.douyu.BuildConfig
import com.mp.douyu.bean.*
import com.mp.douyu.service.AccountService
import com.mp.douyu.utils.DeviceUtil
import com.mp.douyu.utils.StringUtils.checkMobilePhone
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

object Respository {
    private val service = ServiceFactory.create(AccountService::class.java)

    fun getVerifyCode(userBean: UserBean): LiveData<Boolean?> {
        val mutableLiveData = MutableLiveData<Boolean>()
        service.getIdentifyCode(hashMapOf("phone" to userBean.phone,
            "type" to userBean.type,
            "captcha_id" to userBean.verifyCodeId,
            "captcha" to userBean.verifyCode)).retryWhen(RetryFilterFun()).map(HttpCodeBooleanFun())
            .compose(SchedulersTransformer())
            .subscribe(object : DisposableSubscriberAdapter<Boolean>() {
                override fun onNext(t: Boolean?) {
                    mutableLiveData.value = t
                }
            })
        return mutableLiveData
    }

    fun getImageVerify(): LiveData<UserBean> {
        val mutableLiveData = MutableLiveData<UserBean>()
        service.getImageVerify().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer())
            .subscribe(object : DisposableSubscriberAdapter<UserBean>() {
                override fun onNext(t: UserBean?) {
                    mutableLiveData.value = t
                }
            })
        return mutableLiveData
    }

    fun registerUser(it: UserBean, param: DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>) {
        service.registerUser(hashMapOf("phone" to it.phone,
            "password" to it.psw,
            "os" to "Android${DeviceUtil.getBuildVersion()}",
            "model" to DeviceUtil.getPhoneModel(),
            "reg_promo_code" to it.extensionCode,
            "channel" to BuildConfig.CHANNEL_ID,
            "code" to it.verifyCode)).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    fun registerUserUserName(it: UserBean, dispose: DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>) {
        service.registerUser(hashMapOf("username" to it.phone,
            "os" to "Android${DeviceUtil.getBuildVersion()}",
            "model" to DeviceUtil.getPhoneModel(),
            "password" to it.psw,
            "channel" to BuildConfig.CHANNEL_ID,
            "reg_promo_code" to it.extensionCode)).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    //login
    fun login(userBean: UserBean, dispose: DisposableSubscriberAdapter<TokenBean>) {
        var hm: HashMap<String, String?> = hashMapOf()
        userBean.phone?.let {
            if (checkMobilePhone(it)) {
                hm = hashMapOf("phone" to userBean.phone, "password" to userBean.psw)
            } else {
                hm = hashMapOf("username" to userBean.phone, "password" to userBean.psw)
            }
        }
        service.login(hm).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    //get channel id
    fun getChannelId(hm:HashMap<String,String?>, dispose: DisposableSubscriberAdapter<ChannelIdBean?>) {
        service.getChannelId(hm).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    //forget psw
    fun forgetPsw(it: UserBean, param: DisposableSubscriberAdapter<Boolean>) {
        service.forgetPsw(hashMapOf("username" to it.userName,
            "phone" to it.phone,
            "password" to it.psw,
            "code" to it.verifyCode,
            "re_password" to it.confirmPsw)).retryWhen(RetryFilterFun()).map(HttpCodeBooleanFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    //get system setting
    fun getSystemSetting(): LiveData<SystemSettingBean?> {
        val mutableLiveData = MutableLiveData<SystemSettingBean?>()
        service.getSettingSystem().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer())
            .subscribe(object : DisposableSubscriberAdapter<SystemSettingBean>() {
                override fun onNext(t: SystemSettingBean?) {
                    mutableLiveData.value = t
                }
            })
        return mutableLiveData
    }

    //get version update
    fun getVersionUpdate(param: DisposableSubscriberAdapter<VersionUpdateBean>) {
        service.getVersionUpdate().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    fun getInviteFriends(param: DisposableSubscriberAdapter<InviteFriendsBean>) {
        service.getInviteFriends().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    fun getGetTask(param: DisposableSubscriberAdapter<TaskCenterBean?>) {
        service.getGetTask().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    fun getSignInTask(param: DisposableSubscriberAdapter<SignInBean?>) {
        service.getSignInTask().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    fun getVipList(param: DisposableSubscriberAdapter<List<MyVipBean?>>) {
        service.getVipList().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    fun getDialogList(param: DisposableSubscriberAdapter<List<HomeDialogBean?>>) {
        service.getDialogList().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    fun chargeVipList(it: HashMap<String, String?>?, param: DisposableSubscriberAdapter<Any?>) {
        service.chargeVipList(it).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    fun getChargeResult(it: HashMap<String, String?>?, param: DisposableSubscriberAdapter<ChargeResultBean?>) {
        service.getChargeResult(it).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(param)
    }

    //获取用户信息
    fun getUserInfo(param: DisposableSubscriberAdapter<UserInfoBean?>) {
        service.getUserInfo().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer())
            .subscribe(param)
    }
    //获取用户信息
    fun getUserAvatars(): LiveData<AvatarChooseBean?> {
        val mutableLiveData = MutableLiveData<AvatarChooseBean?>()
        service.getUserAvatar().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer())
            .subscribe(object : DisposableSubscriberAdapter<AvatarChooseBean>() {
                override fun onNext(t: AvatarChooseBean?) {
                    mutableLiveData.value = t
                }
            })
        return mutableLiveData
    }

    fun setUserAvatars(it: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<Boolean>) {
        service.setUserAvatar(it).retryWhen(RetryFilterFun()).map(HttpCodeBooleanFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun bindPhone(it: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<Boolean>) {
        service.bindPhone(it).retryWhen(RetryFilterFun()).map(HttpCodeBooleanFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun tradeRecord(it: HashMap<String, String?>?, dispose: DisposableSubscriberAdapter<TradeRecordBean>) {
        service.getTradeRecord(it).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun feedback(it: HashMap<String, String?>?, subscriberAdapter: DisposableSubscriberAdapter<Boolean>) {
        service.feedback(it).retryWhen(RetryFilterFun()).map(HttpCodeBooleanFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }

    fun beginPay(it: HashMap<String, String?>?, subscriberAdapter: DisposableSubscriberAdapter<PayContentBean>) {
        service.beginPay(it).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }

    fun payList(subscriberAdapter: DisposableSubscriberAdapter<PayListBean>) {
        service.payList().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }

    fun getRecommend(dispose: DisposableSubscriberAdapter<RecommendedBean>) {
        service.getRecommend().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getMoreType(dispose: DisposableSubscriberAdapter<MoreTypeBean>) {
        service.getMoreType().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getVideoList(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<VideoListBean>) {
        service.getVideoList(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getVideoPlayDetail(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<VideoPlayBean>) {
        service.getVideoPlayDetail(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getVideoPlayLink(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<VideoPlayLinkBean?>) {
        service.getVideoPlayLink(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getVideoSpecialTopic(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<SpecialTopicBean?>) {
        service.getVideoSpecialTopic(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getNvInfo(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<NvInfoBean?>) {
        service.getNvInfo(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getNvVideo(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<NvVideoBean?>) {
        service.getNvVideo(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getCommendList(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<HttpDataListBean<CommentBean>?>) {
        service.getCommendList(hashMap).retryWhen(RetryFilterFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun commentVideo(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<String?>) {
        service.commentVideo(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getSearchContent(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<PageBaseBean<VideoBean>?>) {
        service.getSearchContent(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getTransferInfo(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<Any?>) {
        service.getTransferInfo(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getGameLink(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<GetGameLinkBean?>) {
        service.getGameLink(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun clickCollect(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<Any?>) {
        service.clickCollect(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getDownloadLink(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<VideoDoenloadLink?>) {
        service.getDownloadLink(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getActivityList(hashMap: HashMap<String, String?>, dispose: DisposableSubscriberAdapter<PageBaseBean<ActivityBean>?>) {
        service.getActivityList(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getRecommendGame(dispose: DisposableSubscriberAdapter<List<RecommandGameBean?>>) {
        service.getRecommendGame().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getLotteryOpen(dispose: DisposableSubscriberAdapter<LotteryOpenBean?>) {
        service.getLotteryOpen().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun getNvList(dispose: DisposableSubscriberAdapter<HashMap<String, List<NvDetailBean>>?>) {
        service.getNvList().retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getPopularVideoList(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<ShortVideoListBean?>){
        service.getPopularVideoList(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<ShortVideoListBean>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getRecommendVideoList(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<ShortVideoListBean?>){
        service.getRecommendVideoList(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<ShortVideoListBean>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getShortVideoComments(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<CommentBean>?>){
        service.getShortVideoComments(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<CommentBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun shortVideoLike(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<String?>){
        service.shortVideoLike(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun shortVideoUnlike(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<String?>){
        service.shortVideoUnlike(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun shortVideoComment(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<String?>){
        service.shortVideoComment(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getCommunityCircles(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<SquareCircleBean>?>){
        service.getCommunityCircles(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<SquareCircleBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun joinCircle(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.joinCircle(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)

    }
    fun exitCircle(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.exitCircle(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun follow(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.follow(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun cancelFollow(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.cancelFollow(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getRecommendPosts(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getRecommendPosts(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getLikeDynamics(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getLikeDynamics(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getCommentDynamics(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getCommentDynamics(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getPostDynamics(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getPostDynamics(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getUserDynamics(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getUserDynamics(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getFollowDynamics(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getFollowDynamics(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getRecommendComment(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<List<CommentBean>?>){
        service.getRecommendComment(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<CommentBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getComment(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<CommentBean>?>){
        service.getComment(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<CommentBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun comment(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.commentPost(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }

    fun getFans(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<List<CommonUserBean>?>){
        service.getFans(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<CommonUserBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getFollows(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<List<CommonUserBean>?>){
        service.getFollows(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<CommonUserBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getUserLikes(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<List<MineMessageBean>?>){
        service.getUserLikes(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<MineMessageBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getUserReplys(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<List<MineMessageBean>?>){
        service.getUserReplys(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<MineMessageBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getLikePosts(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getLikePosts(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getReplyPosts(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getReplyPosts(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getRecordPosts(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>){
        service.getRecordPosts(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getCommentVideos(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<MineVideoBean>?>){
        service.getCommentVideos(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<MineVideoBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getCollectVideos(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<MineVideoBean>?>){
        service.getCollectVideos(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<MineVideoBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getRecordVideos(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<MineVideoBean>?>){
        service.getRecordVideos(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<MineVideoBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getShortVideoLikes(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<LikeShortVideoBean>?>){
        service.getShortVideoLikes(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<LikeShortVideoBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun likePost(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.likePosts(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun unlikePost(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.unlikePosts(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getCityInvites(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<InviteCityBean>?>){
        service.getCityInvites(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<InviteCityBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getCityInviteDetail(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<InviteCityBean?>){
        service.getCityInviteDetail(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<InviteCityBean>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getHookRecords(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<HookRecordBean>?>){
        service.getHookRecords(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<HookRecordBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }
    fun getFollowLives(hashMap: HashMap<String, String?>,dispose: DisposableSubscriberAdapter<HttpDataListBean<LiveBean>?>){
        service.getFollowLives(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<LiveBean>>())
            .compose(SchedulersTransformer()).subscribe(dispose)
    }

    fun followAnchor(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>){
        service.followAnchor(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun cancelFollowAnchor(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity?>){
        service.cancelFollowAnchor(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun joinHook(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>){
        service.joinHook(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun enterLive(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>){
        service.enterLive(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getGifts(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<HttpDataListBean<GiftBean>>){
        service.getGifts(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<GiftBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getSearchShortVideo(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<HttpDataListBean<ShortVideoBean>>){
        service.getSearchShortVideo(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<ShortVideoBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getSearchPost(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>>){
        service.getSearchPost(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<DynamicBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getUserInfo2(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<List<UserInfo2Bean>>){
        service.getUserInfo2(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<UserInfo2Bean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getJoinCircles(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<List<SquareCircleBean>>){
        service.getJoinCircles(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<SquareCircleBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getLives(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<HttpDataListBean<LiveBean>>){
        service.getLives(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<LiveBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getSearchLive(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<List<LiveBean>>){
        service.getSearchLive(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<LiveBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun sendGift(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.sendGift(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getLiveVipList(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<List<MyVipBean?>>){
        service.getLiveVipList(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<MyVipBean?>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getLiveRanksList(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<LiveRanksBean>){
        service.getLiveRanksList(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<LiveRanksBean>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun publishVideo(@PartMap params: MutableMap<String, RequestBody>, @Part parts: List<MultipartBody.Part?>?, subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.publishVideo(params,parts).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun publishPost(@PartMap params: MutableMap<String, RequestBody>, @Part parts: List<MultipartBody.Part?>?, subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>){
        service.publishPost(params,parts).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun applyAnchor(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String?>){
        service.applyAnchor(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String?>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getBetGods(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<HttpDataListBean<BetGotBean>>){
        service.getBetGods(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<BetGotBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getBetGodFollows(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<HttpDataListBean<BetGotBean>>){
        service.getBetGodFollows(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<BetGotBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getBetRanks(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<BetRankBean>){
        service.getBetRanks(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<BetRankBean>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun startLive(@PartMap params: MutableMap<String, RequestBody>, @Part parts: List<MultipartBody.Part?>?,subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>){
        service.startLive(params,parts).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun stopLive(@PartMap params: MutableMap<String, RequestBody>, @Part parts: List<MultipartBody.Part?>?,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.stopLive(params,parts).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getAdvList(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<List<AdvBean?>>){
        service.getAdvList(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<AdvBean?>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }

    fun shareShortVideo(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<String>){
        service.shareShortVideo(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<String>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getAnchorInfo(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<AnchorBean>){
        service.getAnchorInfo(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<AnchorBean>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getAccountRecord(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<List<AccountRes>>){
        service.getAccountRecord(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<AccountRes>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getWithdrawInfo(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<WithdrawDetailRes>){
        service.getWithdrawInfo(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<WithdrawDetailRes>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getWithdrawRecord(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<HttpDataListBean<WithdrawRecordBean>>){
        service.getWithdrawRecord(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<HttpDataListBean<WithdrawRecordBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getAnchorLvRules(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<List<AnchorRuleBean>>){
        service.getAnchorLvRules(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<List<AnchorRuleBean>>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun addWithdrawAccount(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<AccountRes>){
        service.addWithdrawAccount(hashMap).retryWhen(RetryFilterFun()).map(ResponseFunction<AccountRes>())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun withdraw(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>){
        service.withdraw(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getAnchorApplyStatus(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>){
        service.getAnchorApplyStatus(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
    fun getPlayState(hashMap: HashMap<String, String?>,subscriberAdapter:DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>){
        service.getPlayState(hashMap).retryWhen(RetryFilterFun()).map(HttpBodyFun())
            .compose(SchedulersTransformer()).subscribe(subscriberAdapter)
    }
}