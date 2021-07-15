package com.mp.douyu.ui.anchor.live

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.bumptech.glide.Glide
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.mp.douyu.*
import com.mp.douyu.adapter.LiveAudienceInfoAdapter
import com.mp.douyu.adapter.LiveRoomChatAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.*
import com.mp.douyu.dialog.GiftListDialog
import com.mp.douyu.dialog.TransferDialog
import com.mp.douyu.im.ImManager
import com.mp.douyu.ui.anchor.AnchorViewModel
import com.mp.douyu.ui.anchor.live.vip.LiveVipListActivity
import com.mp.douyu.ui.home.recommended.HomeRecommendedChessActivity
import com.mp.douyu.ui.mine.IWantExtensionActivity
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.ui.mine.walnut.ChargeCenterActivity
import com.mp.douyu.ui.square.RelationViewModel
import com.mp.douyu.ui.video.comment.CommentViewModel
import com.mp.douyu.utils.DisplayUtils
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo
import com.tencent.imsdk.v2.V2TIMMessage
import com.tencent.imsdk.v2.V2TIMUserInfo
import com.tencent.imsdk.v2.V2TIMValueCallback
import com.tencent.liteav.demo.beauty.model.ItemInfo
import com.tencent.liteav.demo.beauty.model.TabInfo
import com.tencent.liteav.demo.beauty.view.BeautyPanel
import com.tencent.rtmp.*
import io.alterac.blurkit.BlurKit
import kotlinx.android.synthetic.main.layout_live_medal_list.*
import kotlinx.android.synthetic.main.layout_live_pusher_info.*
import kotlinx.android.synthetic.main.layout_live_pusher_info.iv_anchor_head_icon
import kotlinx.android.synthetic.main.layout_live_right.*
import kotlinx.android.synthetic.main.live_fragment_anchor.*
import kotlinx.android.synthetic.main.live_fragment_anchor.cl_gift
import kotlinx.android.synthetic.main.live_fragment_anchor.container
import kotlinx.android.synthetic.main.live_fragment_anchor.iv_gift
import kotlinx.android.synthetic.main.live_fragment_anchor.keyboard
import kotlinx.android.synthetic.main.live_fragment_anchor.recycle_chat
import kotlinx.android.synthetic.main.live_fragment_anchor.tv_gift_num
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import master.flame.danmaku.ui.widget.DanmakuView
import java.util.HashMap
import kotlin.random.Random


class AnchorFragment(var bean: LiveStreamingBean? = null) : BaseLiveFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;

    var danmakuContext: DanmakuContext? = null
    var danmakuParser: BaseDanmakuParser? = null
    var listAvatars: MutableList<CommonUserBean> = arrayListOf()
    var isOpenDanmaku: Boolean = false
    val mChatAdapter by lazy {
        LiveRoomChatAdapter({}, context).apply {
//            add(ChatMsgBean(itemViewType = LiveRoomChatAdapter.TYPE_TIP))
        }
    }
    val mAudienceAvatar by lazy {
        LiveAudienceInfoAdapter({}, context).apply {
            initializeWidth(data, recycle_audience, 40, 3)
        }
    }

    companion object {
        const val LIVE_DATA = "data"
        fun newInstance(bean: LiveStreamingBean): AnchorFragment {
            val fragment = AnchorFragment()
            val bundle = Bundle()
            bundle.putParcelable(LIVE_DATA, bean)
            fragment.arguments = bundle
            return fragment
        }
    }


    private fun follow(position: Int) {
//        anchorViewModel.followAnchor(hashMapOf("anchor_id" to "${mAdapter.data[position].id}"))
    }

    private fun unFollow(position: Int) {
//        anchorViewModel.cancelFollowAnchor(hashMapOf("anchor_id" to "${mAdapter.data[position].id}"))
    }

    override val layoutId: Int
        get() = R.layout.live_fragment_anchor

    override fun initView() {
    }

    override fun onVisible() {
        super.onVisible()


    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        super.onVisibleFirst()
        bean = arguments?.getParcelable<LiveStreamingBean>(LIVE_DATA)?.apply {
            Glide.with(context!!).load(BuildConfig.IMAGE_BASE_URL + image).circleCrop()
                .placeholder(R.mipmap.default_avatar).into(iv_anchor_head_icon)
            tv_anchor_name.text = user?.nickname
            tv_hot_num.text = "${hot}"
            tv_medal_num.text = "${gold}"
            startPusher(this.push_url)
        }
        iv_follow_status.visibility = View.GONE
        recycle_chat.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mChatAdapter
        }
        recycle_audience.apply {

            adapter = mAudienceAvatar
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }
        initBeautyListener()
        initKeyboard(keyboard)
        loadData()
        initOnClick()
        initDanmaku(danmaku)
        mineViewModel.getUserAvatar()
    }

    private fun initBeautyListener() {
        beauty_panel.setOnBeautyListener(object : BeautyPanel.OnBeautyListener {
            override fun onClick(tabInfo: TabInfo?, tabPosition: Int, itemInfo: ItemInfo?, itemPosition: Int): Boolean {
                return false
            }

            override fun onTabChange(tabInfo: TabInfo?, position: Int) {

            }

            override fun onLevelChanged(tabInfo: TabInfo?, tabPosition: Int, itemInfo: ItemInfo?, itemPosition: Int, beautyLevel: Int): Boolean {
                return false
            }

            override fun onClose(): Boolean {
                beauty_panel.visibility = View.GONE
                tool_bar.visibility = View.VISIBLE
                return true
            }
        })
    }


    private fun initOnClick() {

        btn_message_input.singleClick {
            onSendClick()
        }
        anchor_btn_flash.singleClick {

        }
        switch_cam.singleClick {
            if (mLivePusher != null) {
                mLivePusher.switchCamera()
            }
        }
        beauty_btn.singleClick {
            if (beauty_panel.isShown) {
                beauty_panel.visibility = View.GONE
                tool_bar.visibility = View.VISIBLE
            } else {
                beauty_panel.visibility = View.VISIBLE
                tool_bar.visibility = View.GONE
            }
        }
        btn_audio_ctrl.singleClick {

        }
        btn_close.singleClick {
            onCloseClick()
        }
        iv_recharge.singleClick {
            onRechargeClick()
        }
        iv_yp.singleClick {
            onHookClick()
        }
        iv_right_share.singleClick {
            onShareClick()
        }
        iv_bet.singleClick {
            onBetGameClick()
        }
        layout_live_medal_list.singleClick {
            onRankListClick()
        }
        layout_live_pusher_info.singleClick {
            onHeaderClick()
        }
        iv_back.singleClick {
            stopDialog.show()
            //离开
        }
        iv_danmaku.singleClick {
            iv_danmaku.isSelected = !iv_danmaku.isSelected
            isOpenDanmaku = iv_danmaku.isSelected
            if (isOpenDanmaku) {
                danmaku.show()
            } else {
                danmaku.hide()
            }
        }
        iv_share.singleClick {
            onShareClick()
        }
    }
    val stopDialog by lazy {
        XPopup.Builder(context).asConfirm("提示","确定下播?",object :OnConfirmListener{
            override fun onConfirm() {
                var status = 1
                var groupId = "${bean?.GroupId}"
                liveViewModel.startLive(null, null,null, groupId, status)
                sendLiveTextMsg("@#直播间关闭")
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        stopPusher()
    }

    private fun startPusher(pushUrl: String?) {
        val rtmpURL = pushUrl //此处填写您的 rtmp 推流地址
        val ret = mLivePusher.startPusher(rtmpURL?.trim())
        if (ret == -5) {
            Log.e(TAG, "startRTMPPush: license 校验失败")
        }
    }

    private fun stopPusher() {
        mLivePusher.apply {
            stopPusher()
            stopCameraPreview(true);//如果已经启动了摄像头预览，请在结束推流时将其关闭。
        }
    }


    private fun loadData() {
        /*  liveViewModel.getRecommendVideoData(hashMapOf("list_rows" to "${pageSize}",
              "page" to "${currentPage}"))*/
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    override fun sendLiveTextMsg(text: String) {
        ImManager.instance.sendLiveTextMessage(text,
            "${bean?.GroupId}",
            V2TIMMessage.V2TIM_PRIORITY_NORMAL,
            object : V2TIMValueCallback<V2TIMMessage> {
                override fun onSuccess(p0: V2TIMMessage?) {
                    Log.e(TAG, "发送消息成功 :${p0?.message.toString()}")
                    p0?.textElem?.text?.let { fetchMsg("你",it) }
                }

                override fun onError(p0: Int, p1: String?) {
                    Log.e(TAG, "发送消息失败 :code=${p0}, msg=${p1}")
                    fetchMsg("你",text)
                }
            })
    }
    private fun fetchMsg(nickname:String,textStr: String) {
        var content: String? = null
        if (textStr?.contains("{") && textStr.contains("}")) {
            val messageBean = GsonUtils.fromJson(textStr, MessageBean::class.java)
            when (messageBean.type) {
                "礼物" -> {
                    val giftBean = GsonUtils.fromJson(messageBean.msg, GiftBean::class.java)
                    content = "送出了${messageBean.num}个${giftBean.title}"
                    showGiftAnim( SendGiftBean(num = messageBean.num,giftBean = giftBean))
                }
                "弹幕" -> {
                    content = messageBean.msg
                }

            }
        } else {
            if (textStr == "@#直播间关闭") {
                //关闭直播间消息
                finishView()
                return
            }
            content = textStr
        }
        addMessage(ChatMsgBean(nickname = nickname, content = content), true)
    }
    fun onRankListClick() {
        //排行榜
        liveViewModel.getRanksData(hashMapOf("anchor_id" to "${bean?.id}"))
    }

    fun onRechargeClick() {
        //充值
        if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
            startActivityWithTransition(ChargeCenterActivity.open(context))
        }
    }

    fun onBetClick(id: Int?) {
        //下注
        XPopup.Builder(activity).asCustom(TransferDialog(activity!!)).show()
    }

    fun onHookClick() {
        //约pao

    }

    fun onBetGameClick() {
        //游戏
        context?.let { it1 ->
            HomeRecommendedChessActivity.open(it1, 0)
        }?.let { it2 -> startActivityWithTransition(it2) }
    }

    fun onGiftClick() {
        //礼物
        //通过RenderScript进行高斯模糊并返回一个bitmap，iv1可以是一个View，也可以是一个ViewGroup，25是模糊半径，2代表缩放比例，如果值太大可能会出现OOM
        var bt = BlurKit.getInstance().blur(container, 25 )
        XPopup.Builder(activity)
            .asCustom(GiftListDialog(context =activity!!, bitmap = bt, listener = object : GiftListDialog.OnGiftListener {
                override fun sendGift(bean: SendGiftBean) {
                }
            })).show()
    }

    fun onShareClick() {
        //分享
        if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
            startActivityWithTransition(IWantExtensionActivity.open(context))
        }
    }

    fun onDanmakuClick(isOpen: Boolean, position: Int) {
        //弹幕开启/关闭
    }

    fun onSendClick() {
        //弹出键盘发送消息
        keyboard.visibility = View.VISIBLE
        keyboard.popKeyboard()
    }

    fun onVipClick(position: Int) {
        //充值vip
        startActivityWithTransition(LiveVipListActivity.open(activity))
    }

    fun onCloseClick() {
        //离开
        finishView()
    }

    fun onHeaderClick() {
        //个人信息
//        XPopup.Builder(activity).asCustom(AnchorInfoDialog(activity!!, get(position))).show()
    }

    fun onFollow(position: Int) {
        currentPosition = position
//        when (get(position).isFollowLive) {
//            0 -> follow(position)
//            else -> unFollow(position)
//        }
    }

    private fun showGiftAnim(bean: SendGiftBean) {
        if (bean.giftBean?.image == null) {
            Log.e(TAG, "礼物 url 为 null")
            return
        }
        cl_gift.visibility = View.VISIBLE
        if (bean.giftBean?.animation?.contains("http") == false) {
            bean.giftBean?.animation = BuildConfig.IMAGE_BASE_URL + bean.giftBean?.animation
        }
        Glide.with(context!!).load(bean.giftBean?.animation).into(iv_gift)
        tv_gift_num.text = "x${bean.num}"
        var duration = bean.giftBean?.duration
        if (duration == 0L) {
            duration = 2L
        }
        val l = duration?.times(1000)

        Handler().postDelayed(Runnable {
            cl_gift.visibility = View.GONE
        }, l!!)
    }

    override fun onMemberLeave(groupID: String?, member: V2TIMGroupMemberInfo?) {
        super.onMemberLeave(groupID, member)
        if (groupID != null && groupID == "${bean?.GroupId}") {
            addMessage(ChatMsgBean(nickname = member?.nickName, content = "离开直播间"))
        }
    }

    override fun onMemberEnter(groupID: String?, memberList: MutableList<V2TIMGroupMemberInfo>?) {
        super.onMemberEnter(groupID, memberList)
        if (groupID != null && groupID == "${bean?.GroupId}") {
            memberList?.forEach { member ->
                addMessage(ChatMsgBean(nickname = member.nickName, content = "进入直播间"))
            }
        }
    }

    override fun onRecvC2CTextMessage(msgID: String?, sender: V2TIMUserInfo?, text: String?) {
        super.onRecvC2CTextMessage(msgID, sender, text)
    }

    override fun onRecvC2CCustomMessage(msgID: String?, sender: V2TIMUserInfo?, customData: ByteArray?) {
        super.onRecvC2CCustomMessage(msgID, sender, customData)
    }

    override fun onRecvGroupCustomMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, customData: ByteArray?) {
        super.onRecvGroupCustomMessage(msgID, groupID, sender, customData)
    }

    override fun onRecvGroupTextMessage(msgID: String?, groupID: String?, sender: V2TIMGroupMemberInfo?, text: String?) {
        super.onRecvGroupTextMessage(msgID, groupID, sender, text)
        if (groupID != null && groupID == "${bean?.GroupId}") {
            fetchMsg(sender?.nickName!!,text!!)
        }
    }

    private fun addMessage(msgBean: ChatMsgBean, isSelf: Boolean = false) {
        if (isOpenDanmaku) {
            //开启弹幕
            addDanmaku(danmaku, text = msgBean.content, isSelf = isSelf)
        } else {
            mChatAdapter?.add(msgBean)
            recycle_chat?.smoothScrollToPosition(mChatAdapter.itemCount-1)
        }

    }

    private val liveViewModel by getViewModel(LiveViewModel::class.java) {

        _likeVideoResult.observe(it, Observer {
            it?.let {
//                if (it) {
//                mAdapter[currentPosition].is_like_short_video = 1
//                mAdapter.notifyItemChanged(currentPosition)
//                }
            }
        })
        _unlikeVideoResult.observe(it, Observer {
            it?.let {
//                if (it) {
//                mAdapter[currentPosition].is_like_short_video = 0
//                mAdapter.notifyItemChanged(currentPosition)
//                }
            }
        })

        _followResult.observe(it, Observer {
            it?.let {
//                mAdapter[currentPosition].is_follow = 1
//                mAdapter.notifyItemChanged(currentPosition)

            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
//                mAdapter[currentPosition].is_follow = 0
//                mAdapter.notifyItemChanged(currentPosition)
            }
        })
        _startLiveData.observe(it, Observer {
            it?.let {
                //本来是在接口关闭，但是接口经常请求错误，那就点击关闭按钮之后在发送关闭消息后结束页面
//                this@AnchorFragment.finishView()

            }
        })

    }
    val mLivePusher: TXLivePusher by lazy {
        TXLivePusher(activity).apply {
            // 一般情况下不需要修改 config 的默认配置
            config = TXLivePushConfig()
            startCameraPreview(anchor_video_view)
            setPushListener(object : ITXLivePushListener {
                override fun onNetStatus(p0: Bundle?) {
                    Log.e(TAG, "onNetStatus ")
                }

                override fun onPushEvent(event: Int, p1: Bundle?) {
                    if (event === TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
                        Log.e(TAG, "推流成功 event:${event}")
                    } else if (event === TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {
                        val msg = "[LivePusher] 推流失败[打开摄像头失败] event:${event}"
                        Log.e(TAG, msg)
                    } else if (event === TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
                        val msg = "[LivePusher] 推流失败[打开麦克风失败] event:${event}"
                        Log.e(TAG, msg)
                    } else if (event === TXLiveConstants.PUSH_ERR_NET_DISCONNECT || event === TXLiveConstants.PUSH_ERR_INVALID_ADDRESS) {
                        val msg = "[LivePusher] 推流失败[网络断开] event:${event}"
                        Log.e(TAG, msg)
                    } else if (event === TXLiveConstants.PUSH_ERR_SCREEN_CAPTURE_START_FAILED) {
                        val msg = "[LivePusher] 推流失败[录屏启动失败] event:${event}"
                        Log.e(TAG, msg)
                    }
                }
            })
        }
    }

    private val commentViewModel by getViewModel(CommentViewModel::class.java) {

    }
    private val anchorViewModel by getViewModel(AnchorViewModel::class.java) {
        _followAnchorResult.observe(it, Observer {
            relationViewModel.getAllFollowLiveListData(hashMapOf())
        })
        _cancelFollowAnchorResult.observe(it, Observer {
            relationViewModel.getAllFollowLiveListData(hashMapOf())
        })

    }
    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        followLiveListData.observe(it, Observer {
            it?.let {
                it.data.let { it1 ->


                }
            }
        })

    }
    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getUserAvatar.observe(it, Observer {
            it?.let {
                it.male?.let {
                    for (el in it) {
                        listAvatars.add(CommonUserBean(el.url))
                    }
                }
                it.female?.let {
                    for (el in it) {
                        listAvatars.add(CommonUserBean(el.url))
                    }
                }
                var randomList: MutableList<CommonUserBean> = randomAvatarList(listAvatars)
                mAudienceAvatar.refresh(randomList, null)
            }
        })

    }

    private fun randomAvatarList(list: MutableList<CommonUserBean>): MutableList<CommonUserBean> {
        var randomList: MutableList<CommonUserBean> = arrayListOf()
        for (index in 0..3) {
            var nextInt = Random.nextInt(list.size)
            randomList.add(list[nextInt])
        }
        return randomList
    }


    /**
     * 发送文字弹幕
     *
     * @param text   弹幕文字
     * @param isSelf 是不是自己发的
     */
    fun addDanmaku(danmakuView: DanmakuView, text: String?, isSelf: Boolean) {
        danmakuContext?.setCacheStuffer(SpannedCacheStuffer(), null)
        val danmaku: BaseDanmaku? =
            danmakuContext?.mDanmakuFactory?.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        if (danmaku == null || danmakuView == null) {
            return;
        }
        danmaku.text = text
        danmaku.priority = 3
        danmaku.isLive = false
        danmaku.time = danmakuView.getCurrentTime() + 1200
        danmaku.textSize = DisplayUtils.dp2px(if (isSelf) 20f else 12f).toFloat()
        danmaku.textColor = if (isSelf) Color.BLUE else Color.WHITE
        danmaku.textShadowColor = Color.GRAY
        danmaku.underlineColor = if (isSelf) Color.GREEN else Color.TRANSPARENT
        danmaku.borderColor = if (isSelf) Color.GREEN else Color.TRANSPARENT
        danmakuView?.addDanmaku(danmaku)
    }

    fun initDanmaku(danmakuView: DanmakuView) {
        val overlappingEnablePair = HashMap<Int, Boolean>()
        overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_RL] = true
        overlappingEnablePair[BaseDanmaku.TYPE_FIX_TOP] = true
        val maxLinesPair = HashMap<Int, Int>()
        maxLinesPair[BaseDanmaku.TYPE_SCROLL_RL] = 5 // 滚动弹幕最大显示5行,可设置多种类型限制行数

        danmakuContext = DanmakuContext.create()
        danmakuContext?.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3f)
            ?.setDuplicateMergingEnabled(false)?.setScrollSpeedFactor(1.2f)?.setScaleTextSize(1.2f)
            ?.setMaximumLines(maxLinesPair)?.preventOverlapping(overlappingEnablePair)
            ?.setDanmakuMargin(40)
        danmakuParser = object : BaseDanmakuParser() {
            override fun parse(): IDanmakus {
                return Danmakus()
            }
        }
        danmakuView.setCallback(object : DrawHandler.Callback {
            override fun prepared() {
                danmakuView.start()
            }

            override fun updateTimer(timer: DanmakuTimer) {}
            override fun danmakuShown(danmaku: BaseDanmaku) {}
            override fun drawingFinished() {}
        })

        danmakuView.setCallback(object : DrawHandler.Callback {
            override fun drawingFinished() {

            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {
            }

            override fun prepared() {
                //弹幕准备好的时候回掉，这里启动弹幕
                danmakuView.start()
            }

            override fun updateTimer(timer: DanmakuTimer?) {
            }
        })
        danmakuView.prepare(danmakuParser, danmakuContext)
//            danmakuView.showFPS(BuildConfig.DEBUG)
        danmakuView.enableDanmakuDrawingCache(true)
    }
}

