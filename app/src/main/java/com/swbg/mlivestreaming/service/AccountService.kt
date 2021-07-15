package com.swbg.mlivestreaming.service

import com.swbg.mlivestreaming.bean.*
import com.swbg.mlivestreaming.http.NobodyConverterFactory
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AccountService {

    /**
     * 获取验证码
     */
    @POST("/basic/send_code")
    fun getIdentifyCode(@Body body: HashMap<String, Any?>): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    /**
     * 获取图形验证码
     */
    @POST("/basic/captcha")
    fun getImageVerify(): Flowable<Response<UserBean>>


    @POST("/basic/register")
    fun registerUser(@Body hashMapOf: HashMap<String, String?>): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    @POST("/basic/login")
    fun login(@Body hashMapOf: HashMap<String, String?>): Flowable<Response<TokenBean>>

    @POST("/basic/get_channel")
    fun getChannelId(@Body hashMapOf: HashMap<String, String?>): Flowable<Response<ChannelIdBean?>>

    @POST("/basic/reset_password")
    fun forgetPsw(@Body hashMapOf: HashMap<String, String?>): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    @GET("/basic/setting")
    fun getSettingSystem(): Flowable<Response<SystemSettingBean>>

    @GET("/basic/version")
    fun getVersionUpdate(): Flowable<Response<VersionUpdateBean>>

    @GET("/common/mine")
    fun getUserInfo(): Flowable<Response<UserInfoBean>>

    @GET("/common/avatar")
    fun getUserAvatar(): Flowable<Response<AvatarChooseBean>>


    @POST("/common/edit_user")
    fun setUserAvatar(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    @POST("/common/bind_phone")
    fun bindPhone(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    @POST("/common/trade")
    fun getTradeRecord(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<TradeRecordBean>>

    @POST("/basic/video")
    fun getVideoList(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<VideoListBean>>

    @POST("/basic/get_video")
    fun getVideoPlayDetail(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<VideoPlayBean>>

    @POST("/basic/get_play")
    fun getVideoPlayLink(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<VideoPlayLinkBean>>

    @POST("/basic/special")
    fun getVideoSpecialTopic(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<SpecialTopicBean>>

    @POST("/basic/get_actress")
    fun getNvInfo(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<NvInfoBean>>

    @POST("/basic/actress_video")
    fun getNvVideo(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<NvVideoBean>>

    @POST("/basic/comments")
    fun getCommendList(@Body hashMapOf: HashMap<String, String?>?): Flowable<HttpDataListBean<CommentBean>>

    //视频评论
    @POST("/common/comment")
    fun commentVideo(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    @POST("/basic/search")
    fun getSearchContent(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<PageBaseBean<VideoBean>>>

    @POST("/common/transfer")
    fun getTransferInfo(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<Any?>>

    @POST("/basic/activity")
    fun getActivityList(@Body hashMapOf: HashMap<String, String?>?): Flowable<Response<PageBaseBean<ActivityBean>>>

    @POST("/common/feedback")
    fun feedback(@Body it: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    @POST("/common/pay")
    fun beginPay(@Body it: HashMap<String, String?>?): Flowable<Response<PayContentBean>>

    @POST("/common/recharge")
    fun payList(): Flowable<Response<PayListBean>>

    @GET("/basic/recommend")
    fun getRecommend(): Flowable<Response<RecommendedBean>>

    @GET("/basic/lottery")
    fun getLotteryOpen(): Flowable<Response<LotteryOpenBean>>

    @GET("/basic/game")
    fun getRecommendGame(): Flowable<Response<List<RecommandGameBean?>>>

    @GET("/basic/cate")
    fun getMoreType(): Flowable<Response<MoreTypeBean>>

    @GET("/basic/actress")
    fun getNvList(): Flowable<Response<HashMap<String, List<NvDetailBean>>>>

    @GET("/common/invite")
    fun getInviteFriends(): Flowable<Response<InviteFriendsBean>>

    @GET("/common/task")
    fun getGetTask(): Flowable<Response<TaskCenterBean>>

    @GET("/common/sign")
    fun getSignInTask(): Flowable<Response<SignInBean>>

    @GET("/common/vip")
    fun getVipList(): Flowable<Response<List<MyVipBean?>>>

    @GET("/basic/alert")
    fun getDialogList(): Flowable<Response<List<HomeDialogBean?>>>

    @POST("/common/buy_vip")
    fun chargeVipList(@Body it: HashMap<String, String?>?): Flowable<Response<Any?>>

    @POST("/common/check_trade")
    fun getChargeResult(@Body it: HashMap<String, String?>?): Flowable<Response<ChargeResultBean?>>

    @POST("/common/game_url")
    fun getGameLink(@Body it: HashMap<String, String?>?): Flowable<Response<GetGameLinkBean?>>

    @POST("/common/video_action")
    fun clickCollect(@Body it: HashMap<String, String?>?): Flowable<Response<Any?>>

    @POST("/basic/get_download")
    fun getDownloadLink(@Body it: HashMap<String, String?>?): Flowable<Response<VideoDoenloadLink?>>

    //热门短视频列表
    @POST("/short_video_u/popular_videos")
    fun getPopularVideoList(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<ShortVideoListBean>>

    //推荐短视频列表
    @POST("/short_video_u/recommend_videos")
    fun getRecommendVideoList(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<ShortVideoListBean>>

    //短视频评论列表
    @POST("/short_video_u/short_video_comments")
    fun getShortVideoComments(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<CommentBean>>>

    //点赞短视频
    @POST("/short_video_l/like_short_video")
    fun shortVideoLike(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //取消点赞短视频
    @POST("/short_video_l/unLike")
    fun shortVideoUnlike(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //短视频评论
    @POST("/short_video_l/comment_short_video")
    fun shortVideoComment(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //举报短视频
    @POST("/short_video_l/report_short_video")
    fun shortVideoReport(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<ShortVideoCommentLikeBean>>

    //圈子列表
    @POST("/community_u/circles")
    fun getCommunityCircles(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<SquareCircleBean>>>

    //加入圈子
    @POST("/community_l/join_circle")
    fun joinCircle(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //退出圈子
    @POST("/user/exitCircle")
    fun exitCircle(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //关注
    @POST("/community_l/follow")
    fun follow(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //取消关注
    @POST("/community_l/unFollow")
    fun cancelFollow(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //推荐文章列表
    @POST("/community_u/recommend_posts")
    fun getRecommendPosts(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>

    //点赞过的文章
    @POST("/community_l/me_likes")
    fun getLikeDynamics(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>

    //我评论过的
    @POST("/community_l/me_comments")
    fun getCommentDynamics(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>

    //我发布过的
    @POST("/community_l/me_posts")
    fun getPostDynamics(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>

    //我关注过的
    @POST("/community_l/followed_user_posts")
    fun getFollowDynamics(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>

    //精彩评论
    @POST("/community_u/recommend_comments")
    fun getRecommendComment(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<CommentBean>>>

    //全部评论
    @POST("/community_u/comments")
    fun getComment(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<CommentBean>>>

    //评论帖子
    @POST("/community_l/comment")
    fun commentPost(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //关注我的人
    @POST("/user/fans")
    fun getFans(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<CommonUserBean>>>

    //我关注的人
    @POST("/user/follows")
    fun getFollows(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<CommonUserBean>>>

    //点赞我的
    @POST("/user/likes")
    fun getUserLikes(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<MineMessageBean>>>
    //回复我的
    @POST("/user/replys")
    fun getUserReplys(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<MineMessageBean>>>
    //帖子发布列表(个人空间)
    @POST("/user/posts")
    fun getUserDynamics(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>
    //我点赞的动态列表
    @POST("/user/post_likes")
    fun getLikePosts(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>

    //我动态回复列表
    @POST("/user/post_replys")
    fun getReplyPosts(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>

    //我动态足迹列表
    @POST("/user/post_views")
    fun getRecordPosts(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>
    //Av评论列表
    @POST("/user/video_comments")
    fun getCommentVideos(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<MineVideoBean>>>
    //Av点赞列表
    @POST("/user/video_likes")
    fun getCollectVideos(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<MineVideoBean>>>
    //av足迹列表
    @POST("/user/video_views")
    fun getRecordVideos(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<MineVideoBean>>>

    //点赞短视频列表
    @POST("/user/short_video_likes")
    fun getShortVideoLikes(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<LikeShortVideoBean>>>

    //点赞文章
    @POST("/community_l/like")
    fun likePosts(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //取消点赞文章
    @POST("/community_l/unlike")
    fun unlikePosts(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //同城约炮列表
    @POST("/live_u/hooks")
    fun getCityInvites(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<InviteCityBean>>>

    //同城约炮详情
    @POST("/live_u/hookDetail")
    fun getCityInviteDetail(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<InviteCityBean>>

    //同城约炮记录
    @POST("/live_u/hookRecords")
    fun getHookRecords(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<HookRecordBean>>>

    //关注列表
    @POST("/live_l/follows")
    fun getFollowLives(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<LiveBean>>>

    //关注主播
    @POST("/live_l/followAnchor")
    fun followAnchor(@Body it: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    //取消关注主播
    @POST("/live_l/cancelFollowAnchor")
    fun cancelFollowAnchor(@Body it: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity?>>

    //参与约炮
    @POST("/live_l/joinHook")
    fun joinHook(@Body it: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    //进入直播间
    @POST("/live_u/enterLive")
    fun enterLive(@Body it: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    //礼物列表
    @POST("/live_l/gifts")
    fun getGifts(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<GiftBean>>>

    //搜索短视频
    @POST("/short_video_u/search")
    fun getSearchShortVideo(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<ShortVideoBean>>>

    //搜索帖子
    @POST("/community_u/search")
    fun getSearchPost(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<DynamicBean>>>
    //搜索直播间
    @POST("/live_u/lives_search")
    fun getSearchLive(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<LiveBean>>>

    //获取用户信息
    @POST("/user/me")
    fun getUserInfo2(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<UserInfo2Bean>>>

    //我加入的的圈子列表
    @POST("/user/joinedCircles")
    fun getJoinCircles(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<SquareCircleBean>>>

    //直播间列表
    @POST("/live_u/lives")
    fun getLives(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<LiveBean>>>

    //送礼
    @POST("/live_l/sendGifts")
    fun sendGift(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //VIP等级列表
    @POST("/live_u/vipLvs")
    fun getLiveVipList(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<MyVipBean?>>>

    //直播间贡献榜
    @POST("/live_u/ranks")
    fun getLiveRanksList(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<LiveRanksBean>>
    //发布短视频
    @Multipart
    @POST("/short_video_l/post_short_videos")
    fun publishVideo(@PartMap params: MutableMap<String, RequestBody>?, @Part parts: List<MultipartBody.Part?>?): Flowable<HttpWrapBean<String>>

    //发布文章
    @Multipart
    @POST("/community_l/post")
    fun publishPost(@PartMap params: MutableMap<String, RequestBody>?, @Part parts: List<MultipartBody.Part?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    //申请成为主播
    @POST("/user/applyAnchor")
    fun applyAnchor(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String?>>

    //赌神榜
    @POST("/community_u/bet_gods")
    fun getBetGods(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<BetGotBean>>>
    //赌神榜关注
    @POST("/community_u/bet_god_follows")
    fun getBetGodFollows(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<BetGotBean>>>
    //赌神排行榜
    @POST("/community_u/ranks")
    fun getBetRanks(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<BetRankBean>>

    //开播
    @Multipart
    @POST("/anchor/startLive")
    fun startLive(@PartMap params: MutableMap<String, RequestBody>?, @Part parts: List<MultipartBody.Part?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>

    //停播
    @Multipart
    @POST("/anchor/endLive")
    fun stopLive(@PartMap params: MutableMap<String, RequestBody>?, @Part parts: List<MultipartBody.Part?>?): Flowable<HttpWrapBean<String>>

    //广告列表
    @POST("/other/adv_list")
    fun getAdvList(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<AdvBean?>>>

    //分享短视频
    @POST("/short_video_l/share_short_video")
    fun shareShortVideo(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<String>>

    //获取主播信息
    @POST("/anchor/me")
    fun getAnchorInfo(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<AnchorBean?>>
    //获取主播等级规则
    @POST("/anchor/anchorLvRules")
    fun getAnchorLvRules(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<AnchorRuleBean>>>
    //提现
    @POST("/anchor/withDraw?amount")
    fun withdraw(@Body it: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>
    //提现详细
    @POST("/anchor/withdrawInfo")
    fun getWithdrawInfo(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<WithdrawDetailRes>>
    //新增提現賬號
    @POST("/anchor/addWithdrawAccount")
    fun addWithdrawAccount(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<AccountRes>>
    //绑定账号记录
    @POST("/anchor/withdrawAccounts")
    fun getAccountRecord(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<List<AccountRes>>>
    //提现记录
    @POST("/anchor/withdrawRecord")
    fun getWithdrawRecord(@Body it: HashMap<String, String?>?): Flowable<HttpWrapBean<HttpDataListBean<WithdrawRecordBean>>>
    //申请记录
    @POST("/user/checkApplyStatus")
    fun getAnchorApplyStatus(@Body it: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>
    //短视频播放权限
    @POST("/short_video_u/get_play")
    fun getPlayState(@Body it: HashMap<String, String?>?): Flowable<Response<NobodyConverterFactory.NoBodyEntity>>
}