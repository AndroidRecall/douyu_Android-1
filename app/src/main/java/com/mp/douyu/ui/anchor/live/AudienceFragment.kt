package com.mp.douyu.ui.anchor.live

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.mp.douyu.*
import com.mp.douyu.adapter.LiveDetailAdapter
import com.mp.douyu.adapter.LiveListAdapter
import com.mp.douyu.adapter.LiveRoomChatAdapter
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.*
import com.mp.douyu.dialog.AnchorInfoDialog
import com.mp.douyu.dialog.ContributeListDialog
import com.mp.douyu.dialog.GiftListDialog
import com.mp.douyu.event.LiveHomeEvent
import com.mp.douyu.im.ImManager
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.relation.RelationManager
import com.mp.douyu.ui.anchor.AnchorViewModel
import com.mp.douyu.ui.home.HomeViewModel
import com.mp.douyu.ui.home.recommended.HomeRecommendedChessActivity
import com.mp.douyu.ui.mine.IWantExtensionActivity
import com.mp.douyu.ui.mine.MineViewModel
import com.mp.douyu.ui.mine.space.UserSpaceActivity
import com.mp.douyu.ui.mine.vip.MineVipActivity
import com.mp.douyu.ui.mine.walnut.ChargeCenterActivity
import com.mp.douyu.ui.square.RelationViewModel
import com.mp.douyu.ui.square.circle.DynamicViewModel
import com.mp.douyu.ui.video.comment.CommentViewModel
import com.mp.douyu.utils.RxBus
import com.mp.douyu.view.OnViewPagerListener
import com.mp.douyu.view.ViewPagerLayoutManager
import com.tencent.imsdk.v2.*
import com.tencent.rtmp.ITXLivePlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLivePlayer
import io.alterac.blurkit.BlurKit
import kotlinx.android.synthetic.main.layout_live_pusher_info.view.*
import kotlinx.android.synthetic.main.layout_not_live.*
import kotlinx.android.synthetic.main.live_fragment_audience.*
import kotlinx.android.synthetic.main.live_fragment_audience.cl_gift
import kotlinx.android.synthetic.main.live_fragment_audience.container
import kotlinx.android.synthetic.main.live_fragment_audience.iv_gift
import kotlinx.android.synthetic.main.live_fragment_audience.keyboard
import kotlinx.android.synthetic.main.live_fragment_audience.tv_gift_num
import kotlinx.android.synthetic.main.live_recycle_item_live_detail.view.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class AudienceFragment : BaseLiveFragment() {
    private var isJoinOtherLive: Boolean = false
    private var isAutoFirst: Boolean = false
    private var isLoadLivedView: Boolean = false
    private var isShowPlayEnd: Boolean = false
    private var currentPage: Int = 1
    private var pageSize: Int = 20
    private var currentPosition = -1;
    var curLiveRoomInfo: EnterLiveRes? = null
    var listAvatars: MutableList<CommonUserBean> = arrayListOf()

    companion object {
        const val EXTRA_LIVE_LIST_DATA = "data"
        const val EXTRA_LIVE_CUR_ROOM_DATA = "cur_room_data"
        const val INITIAL_POSITION = "position"
        fun newInstance(data: ArrayList<LiveBean>?, position: Int, enterLiveRes: EnterLiveRes?): AudienceFragment {
            val fragment = AudienceFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(EXTRA_LIVE_LIST_DATA, data)
            bundle.putInt(INITIAL_POSITION, position)
            bundle.putParcelable(EXTRA_LIVE_CUR_ROOM_DATA, enterLiveRes)
            fragment.arguments = bundle
            return fragment
        }
    }


    val mAdapter by lazy {
        LiveDetailAdapter({}, context).apply {
            setListener(object : LiveDetailAdapter.OnLiveListener {
                override fun onRankListClick(position: Int) {
                    //排行榜
                    getRankData(position)
                }

                override fun onRechargeClick(position: Int) {
                    //充值
                    if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
                        startActivityWithTransition(ChargeCenterActivity.open(context))
                    }
                }

                override fun onBetClick(type: Int?) {
                    //下注
                    context?.let { it1 ->
                        type?.let { HomeRecommendedChessActivity.open(it1, it) }
                    }?.let { it2 -> startActivityWithTransition(it2) }
//                    showLoadingView(true)
//                    if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) homeViewModel.getGameLink(
//                        hashMapOf("game_id" to "0"))
//                    XPopup.Builder(activity).asCustom(TransferDialog(activity!!)).show()
                }

                override fun onHookClick(id: Int?) {
                    //约
                    RxBus.getInstance().post(LiveHomeEvent(1))
                    finishView()
                }

                override fun onBetGameClick(position: Int) {
                    //游戏
                    context?.let { it1 ->
                        HomeRecommendedChessActivity.open(it1, 0)
                    }?.let { it2 -> startActivityWithTransition(it2) }
//                    XPopup.Builder(activity).asCustom(GameBetDialog(activity!!)).show()
                }

                override fun onGiftClick(position: Int) {
                    //礼物
                    var bt = BlurKit.getInstance().blur(container, 25)
                    val bitmap =
                        imageCropWithRect(bt, (ScreenUtils.getAppScreenHeight() * 0.6).toInt())
                    XPopup.Builder(activity).asCustom(GiftListDialog(activity!!,
                        get(getPos()),
                        bitmap,
                        object : GiftListDialog.OnGiftListener {
                            override fun sendGift(bean: SendGiftBean) {
                                val messageBean = MessageBean(type = "礼物",
                                    msg = GsonUtils.toJson(bean.giftBean),
                                    num = "${bean.num}")
                                val msg = GsonUtils.toJson(messageBean)
                                sendLiveTextMsg(msg)
                            }
                        })).show()
                }

                override fun onShareClick(position: Int) {
                    //分享
                    if (GlobeStatusViewHolder.isNotNeedLogin(activity as MBaseActivity)) {
                        startActivityWithTransition(IWantExtensionActivity.open(context))
                    }
                }

                override fun onDanmakuClick(isOpen: Boolean, position: Int) {
                    //弹幕开启/关闭
                }

                override fun onSendClick(position: Int) {
                    //弹出键盘发送消息
                    keyboard.visibility = View.VISIBLE
                    keyboard.popKeyboard()
                }

                override fun onVipClick(position: Int) {
                    //充值vip
                    startActivityWithTransition(MineVipActivity.open(activity))
                }

                override fun onCloseClick(position: Int) {
                    activity?.finish()
                }

                override fun onPlayEnd(position: Int) {
                    //播放结束
                    if (!isShowPlayEnd && currentPosition == position) {
                        isShowPlayEnd = true
                        loadNotLivingView()
                    }
                }

                override fun onHeaderClick(position: Int) {
                    //主播信息
                    XPopup.Builder(activity).asCustom(AnchorInfoDialog(activity!!,
                        get(position),
                        object : AnchorInfoDialog.OnAnchorInfoListener {
                            override fun follow() {
                                onFollow(getPos())
                            }

                            override fun unFollow() {
                                onFollow(getPos())
                            }

                            override fun onHeader() {
                                startActivityWithTransition(UserSpaceActivity.open(context,
                                    get(position).anchor?.user_id))
                            }
                        })).show()
                }

                override fun onFollow(position: Int) {
                    currentPosition = position
                    when (get(position).isFollowLive) {
                        0 -> follow(position)
                        else -> unFollow(position)
                    }
                }

            })
        }

    }
    val otherAdapter by lazy {
        LiveListAdapter({}, context).apply {
            var datas = ArrayList(mAdapter.data)
            datas.removeAt(getPos())
            if (datas.size > 4) {
                addAll(datas.subList(0, 4))
            } else {
                addAll(datas)
            }
            setOnLiveListener(object : LiveListAdapter.OnLiveListener {
                override fun onBet(position: Int) {

                }

                override fun onInvite() {

                }

                override fun onRecharge() {
                }

                override fun onLive(position: Int) {
                    toOtherLiveRoom(position)
                }
            })
        }
    }

    private fun toOtherLiveRoom(position: Int) {
        currentPosition = position
        Log.e(TAG, "进入直播间: id= ${otherAdapter[position].id}")
        liveViewModel.enterLive(hashMapOf("live_id" to "${otherAdapter[position].id}","anchor_id" to "${otherAdapter[position].anchor_id}"))
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

    private fun LiveDetailAdapter.getRankData(position: Int) {
        liveViewModel.getRanksData(hashMapOf("anchor_id" to "${get(position).anchor?.id}"))
    }

    private fun follow(position: Int) {
//        dynamicViewModel.follow(hashMapOf("to_id" to "${mAdapter[position].user?.id}"))
        anchorViewModel.followAnchor(hashMapOf("anchor_id" to "${mAdapter.data[position].anchor?.id}"))
    }

    private fun unFollow(position: Int) {
//        dynamicViewModel.cancelFollow(hashMapOf("to_id" to "${mAdapter[position].user?.id}"))
        anchorViewModel.cancelFollowAnchor(hashMapOf("anchor_id" to "${mAdapter.data[position].anchor?.id}"))
    }

    val mViewPagerLayoutManager by lazy {
        ViewPagerLayoutManager(activity, OrientationHelper.VERTICAL).apply {
            setOnViewPagerListener(object : OnViewPagerListener {
                override fun onInitComplete() {
                    //自动播放第一条
                    Log.e(TAG, "onInitComplete ${getChildCount()}")
                    if (!isAutoFirst) {
                        Log.e(TAG, "第一次进入房间，播放视频")
                        isAutoFirst = true
                        joinLiveRoom(0)
                        autoPlayVideo(0)
                    }
                }

                override fun onPageRelease(isNext: Boolean, position: Int) {
                    Log.e(TAG,
                        "释放位置:position=${position},currentPosition =${currentPosition},下一页:isNext=${isNext}")
                    if (currentPosition == position) {
                        Log.e(TAG, "符合条件 暂停直播 脱出房间")
                        var index: Int
                        if (isNext) {
                            index = 0
                        } else {
                            index = 1
                        }
                        stopPlayVideo(index)
                        quitLiveRoom(currentPosition)
                    }
                }

                override fun onPageSelected(position: Int, isBottom: Boolean) {
                    Log.e(TAG, "选中位置:positon=${position},是否是滑动到底部:isBottom=${isBottom}")
                    if (currentPosition != position) {
                        isShowPlayEnd = false
                        toLiveRoom(position)
//                        mAdapter[position].user?.let { setActionRelationData(it) }
                        mAdapter[position].let { setActionRelationData(it) }
//                        setActionRelationData(mAdapter[position])
                        currentPosition = position
                        joinLiveRoom(currentPosition)
                        //给当前直播间设置观众头像
                        refreshAvatar()
                    }
                }
            })
        }
    }
    override val layoutId: Int
        get() = R.layout.live_fragment_audience

    override fun initView() {
    }

    override fun onVisible() {
        super.onVisible()


    }

    override fun onInvisible() {
        super.onInvisible()
        context?.let { TXLivePlayerManager.getInstance(it).livePlayer.pause() }
    }

    override fun onVisibleFirst() {
        super.onVisibleFirst()
        Log.e(TAG, "onVisibleFirst")
        var datas = arguments?.getParcelableArrayList<LiveBean>(EXTRA_LIVE_LIST_DATA)
        curLiveRoomInfo = arguments?.getParcelable<EnterLiveRes>(EXTRA_LIVE_CUR_ROOM_DATA)
        var position = arguments?.getInt(INITIAL_POSITION, 0)
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = mViewPagerLayoutManager
            addOnChildAttachStateChangeListener(object :
                RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewDetachedFromWindow(view: View) {
                    Log.e(TAG, "onChildViewDetachedFromWindow")
//                    if (jz_player != null && Jzvd.CURRENT_JZVD != null && jz_player.jzDataSource != null && jz_player.jzDataSource.containsTheUrl(
//                            Jzvd.CURRENT_JZVD.jzDataSource.currentUrl)
//                    ) {
//                        if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
//                            Jzvd.releaseAllVideos()
//                        }
//                    }
                }

                override fun onChildViewAttachedToWindow(view: View) {

                }
            })
        }

        refreshLayout.apply {

            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(ClassicsFooter(context))
            setOnRefreshListener {
                currentPage = 1
                loadData()
            }
            setOnLoadMoreListener { refreshlayout ->
                currentPage++
                loadData()
            }

        }
        datas?.removeAt(position!!).let { it ->
            datas?.add(0, it)
        }

        mAdapter.data.addAll(datas!!)
        mAdapter.notifyDataSetChanged()
//        mAdapter.addAll(datas!!)
//        currentPosition = position!!
//        currentPosition = 0
//        recyclerView.smoothScrollToPosition(currentPosition)
//        recyclerView.moveToPosition(mViewPagerLayoutManager, currentPosition)
        relationViewModel.getAllFollowLiveListData(hashMapOf())
//        relationViewModel.getAllFollowsData(hashMapOf())
        initKeyboard(keyboard)
        loadData()
//        loadNotLivingView()
        mineViewModel.getUserAvatar()
    }


    private fun loadNotLivingView() {
        if (activity == null || activity?.isFinishing == true) {
            return
        }
        if (!isLoadLivedView) {
            isLoadLivedView = true
            viewstub_not_live.inflate()
        } else {
            cl_no_living.visibility = View.VISIBLE
        }
        if (mAdapter[getPos()].anchor?.avatar?.contains("http") == false) {
            mAdapter[getPos()].anchor?.avatar =
                BuildConfig.IMAGE_BASE_URL + mAdapter[getPos()].anchor?.avatar
        }
        if (mAdapter[getPos()].image?.contains("http") == false) {
            mAdapter[getPos()].image = BuildConfig.IMAGE_BASE_URL + mAdapter[getPos()].image
        }
        Glide.with(this).load(mAdapter[getPos()].anchor?.avatar)
            .placeholder(R.mipmap.default_avatar).into(iv_anchor_avatar)
        Glide.with(this).load(mAdapter[getPos()].image).placeholder(R.mipmap.login_code_failed)
            .into(iv_other_bg)
//        iv_other_bg.post {
//            BlurKit.getInstance().fastBlur(iv_other_bg, 25, 1f)
//        }
//        var bt = BlurKit.getInstance().blur(container, 25)
//        iv_other_bg.setImageBitmap(bt)
        tv_live_time.text = mAdapter[getPos()].update_time
        iv_not_live_back.singleClick {
            finishView()
        }
        tv_in_live.singleClick {
            cl_no_living.visibility = View.GONE
        }
        recycler_other_live.apply {
            adapter = otherAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun loadData() {
        liveViewModel.getLivesData(hashMapOf("list_rows" to "$pageSize", "page" to "$currentPage"))
        /*  liveViewModel.getRecommendVideoData(hashMapOf("list_rows" to "${pageSize}",
              "page" to "${currentPage}"))*/
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        context?.let { TXLivePlayerManager.getInstance(it).livePlayer.resume() }
    }


    private val liveViewModel by getViewModel(LiveViewModel::class.java) {
        _livesData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.data?.let { it1 ->
                if (it1.size < pageSize) refreshLayout.finishLoadMoreWithNoMoreData()
                mAdapter.addAll(it1)
            }

        })
        _enterLiveData.observe(it, Observer {
            it?.let {
                curLiveRoomInfo = it
                if (!isShowPlayEnd) {
                    autoPlayVideo(0)
                } else {
//                    quitLiveRoom(getPos())
//                    stopPlayVideo(0)
                    isJoinOtherLive = true
                    cl_no_living?.visibility = View.GONE

                    finishView()
                }
            }
        })
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
        _ranksData.observe(it, Observer {
            it?.let {
                XPopup.Builder(activity).asCustom(ContributeListDialog(activity!!, it)).show()

            }
        })

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
    private val dynamicViewModel by getViewModel(DynamicViewModel::class.java) {
        _followResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllFollowsData(hashMapOf())

            }
        })
        _cancelFollowResult.observe(it, Observer {
            it?.let {
                relationViewModel.getAllFollowsData(hashMapOf())

            }
        })
    }

    private fun getPos(): Int {
        return if (currentPosition < 0) 0 else currentPosition
    }

    private val relationViewModel by getViewModel(RelationViewModel::class.java) {
        followLiveListData.observe(it, Observer {
            it?.let {
                it.data.let { it1 ->
                    RelationManager.instance.followLives = it1.toMutableList()
                    setActionRelationData(mAdapter[getPos()])
                }
            }
        })
        followsData.observe(it, Observer {

            it?.let {
                RelationManager.instance.follows = it.toMutableList()
                var user = mAdapter[getPos()].user

                user?.let { it1 -> setActionRelationData(it1) }

            }
        })

    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getGameLinkData.observe(it, Observer {
            dismissLoading()
            it?.let {
                jumpIsToGamePageDialog(it, activity as MBaseActivity)
//                jumpIsToGamePageDialog(it, this@MainActivity)
            }
        })
    }

    /**
     * 更新关系状态
     */
    private fun setActionRelationData(user: CommonUserBean) {
        //设置关注的人状态
        val initFollow = user.is_follow
        if (RelationManager.instance.follows.size > 0) {

            for (userBean in RelationManager.instance.follows) {
                if (userBean.id == user.id) {
                    user.is_follow = 1
                    break
                } else {
                    user.is_follow = 0
                }
            }
        } else {
            mAdapter[getPos()].user?.is_follow = 0
        }
        if (user.is_follow != initFollow) {
//            mAdapter.notifyItemChanged(getPos())
            recyclerView?.apply {
                getCurViewHolder().let { it1 ->
                    (it1 as LiveDetailAdapter.LiveHolder).apply {
                        iv_follow_status.visibility =
                            if (user.is_follow == 0) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    /**
     * 更新关系状态
     */
    private fun setActionRelationData(liveBean: LiveBean) {
        //设置关注的人状态
        val initFollowLive = liveBean.isFollowLive
        if (RelationManager.instance.followLives.size > 0) {

            for (followLiveBean in RelationManager.instance.followLives) {
                if (followLiveBean.pivot?.anchor_id == liveBean.anchor_id) {
                    liveBean.isFollowLive = 1
                    break
                } else {
                    liveBean.isFollowLive = 0
                }
            }
        } else {
            liveBean.isFollowLive = 0
        }
        if (liveBean.isFollowLive != initFollowLive) {
            recyclerView?.apply {
                getCurViewHolder().let { it1 ->
                    (it1 as LiveDetailAdapter.LiveHolder).apply {
                        iv_follow_status.visibility =
                            if (liveBean.isFollowLive== 0) View.VISIBLE else View.GONE
                    }
                }
            }
//            mAdapter.notifyItemChanged(getPos())
        }
    }

    private fun autoPlayVideo(position: Int) {
        Log.e(TAG, "autoPlayVideo")
//        context?.let {
//            TXLivePlayerManager.getInstance(it).livePlayer.startPlay(curLiveRoomInfo?.pull_url,
//                TXLivePlayer.PLAY_TYPE_VOD_HLS)
//        }
        recyclerView?.apply {
            getCurViewHolder().let { it1 ->
                audience_background?.visibility = View.VISIBLE
                loading_background3?.visibility = View.VISIBLE
                (it1 as LiveDetailAdapter.LiveHolder).apply {
                    TXLivePlayerManager.getInstance(context).livePlayer.apply {
                        setPlayerView(anchor_video_view)
                        setPlayListener(object : ITXLivePlayListener {
                            override fun onPlayEvent(event: Int, param: Bundle?) {
                                when (event) {
                                    TXLiveConstants.PLAY_EVT_CONNECT_SUCC -> {
                                        //已经连接服务器
                                        audience_background.visibility = View.GONE
                                    }
                                    TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME -> {
                                        Log.e("Player", "收到首帧数据")
                                        audience_background.visibility = View.GONE
                                    }
                                    TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
                                        //视频播放开始，如果您自己做 loading，会需要它
                                        loading_background3.visibility = View.GONE
                                    }
                                    TXLiveConstants.PLAY_EVT_PLAY_LOADING -> {
                                        //视频播放进入缓冲状态，缓冲结束之后会有 PLAY_BEGIN 事件
                                        loading_background3.visibility = View.VISIBLE
                                    }
                                    TXLiveConstants.PLAY_ERR_NET_DISCONNECT -> {
                                        Log.e("Player", "拉流失败")
//                                        mListener?.onPlayEnd(position)
                                        loading_background3.visibility = View.GONE
                                        isShowPlayEnd = true
                                        loadNotLivingView()
                                    }
                                    TXLiveConstants.PLAY_EVT_PLAY_END -> {
                                        Log.e("Player", "播放结束")
//                                        mListener?.onPlayEnd(position)
                                        loading_background3.visibility = View.GONE
                                        isShowPlayEnd = true
                                        loadNotLivingView()
                                    }
                                }

                            }

                            override fun onNetStatus(p0: Bundle?) {

                            }

                        })
                        context?.let {
                            Log.e(TAG,"播放流地址:${curLiveRoomInfo?.pull_url}")
                            TXLivePlayerManager.getInstance(it).livePlayer.startPlay(curLiveRoomInfo?.pull_url,
                                TXLivePlayer.PLAY_TYPE_VOD_HLS)
//                            TXLivePlayerManager.getInstance(it).livePlayer.startPlay(curLiveRoomInfo?.pull_url,
//                                TXLivePlayer.PLAY_TYPE_VOD_HLS)
                        }
                    }
//                    mLivePlayer?.apply {
//                        if (!isPlaying) startPlay(curLiveRoomInfo?.pull_url,
//                            TXLivePlayer.PLAY_TYPE_VOD_HLS)
//                    }
                }
            }
        }

    }

    private fun stopPlayVideo(position: Int) {
        Log.e(TAG, "stopPlayVideo")
        context?.let {
            TXLivePlayerManager.getInstance(it).livePlayer.apply {
                stopPlay(true)
                setPlayerView(null)
            }
        }
//        recyclerView?.apply {
//            getCurViewHolder().let { it1 ->
//                (it1 as LiveDetailAdapter.LiveHolder).apply {
//                    mLivePlayer?.apply {
//                        stopPlay(true)
//
//                    }
//                }
//            }
//        }
    }

    private fun joinLiveRoom(position: Int) {
        ImManager.instance.joinLiveRoom("${mAdapter.get(position).id}",
            "${StoredUserSources.getUserInfo2()?.nickname}",
            object : V2TIMCallback {
                override fun onSuccess() {
                    Log.e(TAG, "IM 加入直播群聊成功 position=${position}")
                }

                override fun onError(p0: Int, p1: String?) {
                    Log.e(TAG,
                        "IM 加入直播群聊异常 code=${p0}, msg=${p1},group_id=${mAdapter.get(position).id}")
                }
            })
    }

    private fun quitLiveRoom(position: Int) {
        ImManager.instance.quitLiveRoom("${mAdapter.get(position).id}", object : V2TIMCallback {
            override fun onSuccess() {
                Log.e(TAG, "IM 退出直播群聊成功")
            }

            override fun onError(p0: Int, p1: String?) {
                Log.e(TAG,
                    "IM 退出直播群聊异常 code=${p0}, msg=${p1},group_id=${mAdapter.get(position).id}")
            }
        })
    }

    override fun sendLiveTextMsg(text: String) {
        ImManager.instance.sendLiveTextMessage(text,
            "${mAdapter.get(getPos()).id}",
            V2TIMMessage.V2TIM_PRIORITY_NORMAL,
            object : V2TIMValueCallback<V2TIMMessage> {
                override fun onSuccess(p0: V2TIMMessage?) {
                    Log.e(TAG, "发送消息成功 :${p0?.message.toString()}")
                    p0?.textElem?.text?.let { fetchMsg("你", it) }
                }

                override fun onError(p0: Int, p1: String?) {
                    Log.e(TAG, "发送消息失败 :code=${p0}, msg=${p1}")
                    fetchMsg("你", text)
                }
            })
    }

    private fun fetchMsg(nickname: String, textStr: String) {
        var content: String? = null
        if (textStr.contains("{") && textStr.contains("}")) {
            val messageBean = GsonUtils.fromJson(textStr, MessageBean::class.java)
            when (messageBean.type) {
                "礼物" -> {
                    val giftBean = GsonUtils.fromJson(messageBean.msg, GiftBean::class.java)
                    content = "送出了${messageBean.num}个${giftBean.title}"
                    showGiftAnim(SendGiftBean(num = messageBean.num, giftBean = giftBean))

                }
                "弹幕" -> {
                    content = messageBean.msg
                }
            }
        } else {
            if (textStr == "@#直播间关闭") {
                //关闭直播间消息
                isShowPlayEnd = true
                loadNotLivingView()
                return
            }
            content = textStr
        }
        addMessage(ChatMsgBean(nickname = nickname, content = content), true)
    }

    override fun onMemberLeave(groupID: String?, member: V2TIMGroupMemberInfo?) {
        super.onMemberLeave(groupID, member)
        if (groupID != null && groupID == "${mAdapter[getPos()].id}") {
            addMessage(ChatMsgBean(nickname = member?.nickName, content = "离开直播间"))
        }
    }

    override fun onMemberEnter(groupID: String?, memberList: MutableList<V2TIMGroupMemberInfo>?) {
        super.onMemberEnter(groupID, memberList)
        if (groupID != null && groupID == "${mAdapter[getPos()].id}") {
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
        if (groupID != null && groupID == "${mAdapter[getPos()].id}") {
            fetchMsg(sender?.nickName!!, text!!)
        }
    }

    private fun addMessage(msgBean: ChatMsgBean, isSelf: Boolean = false) {

        recyclerView?.apply {
            getCurViewHolder().let { it1 ->
                if (mAdapter[getPos()].isOpenDanmaku) {
                    //开启弹幕
                    if (it1 is LiveDetailAdapter.LiveHolder) {
                        it1.addDanmaku(danmaku, text = msgBean.content, isSelf = isSelf)
                    } else {
                    }
                } else {
                    (recycle_chat.adapter as LiveRoomChatAdapter).add(msgBean)
                    recycle_chat.smoothScrollToPosition((recycle_chat.adapter as LiveRoomChatAdapter).itemCount - 1)
                }
            }
        }
    }

    private fun toLiveRoom(position: Int) {
        currentPosition = position
        Log.e(TAG, "进入直播间: id= ${mAdapter.get(position).id}")
        liveViewModel.enterLive(hashMapOf("live_id" to "${mAdapter[position].id}","anchor_id" to "${mAdapter[position].anchor_id}"))
    }

    private fun randomAvatarList(list: MutableList<CommonUserBean>): MutableList<CommonUserBean> {
        var randomList: MutableList<CommonUserBean> = arrayListOf()
        for (index in 0..3) {
            var nextInt = Random.nextInt(list.size)
            randomList.add(list[nextInt])
        }
        return randomList
    }

    override fun onDestroyView() {
        stopPlayVideo(0)
        quitLiveRoom(getPos())
        if (isJoinOtherLive) curLiveRoomInfo?.let {
            startActivity(AudienceActivity.open(context,
                otherAdapter.data.subList(otherAdapter.getHeaderCount(), otherAdapter.data.size),
                getPos() - otherAdapter.getHeaderCount(),
                it))
        }
        super.onDestroyView()
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
                refreshAvatar()
//                mAdapter[currentPosition].listAvatars = randomAvatarList(listAvatars)
//                mAdapter.notifyItemChanged(currentPosition)
            }
        })

    }

    private fun refreshAvatar() {
        recyclerView?.apply {
            getCurViewHolder().let { it ->
                (it as LiveDetailAdapter.LiveHolder).audienceAdapter.refresh(randomAvatarList(
                    listAvatars), null)
            }
        }
    }

    /**
     * 按长方形裁切图片
     *
     * @param bitmap
     * @param imgHeight 剪裁后的高度
     * @return
     */
    private fun imageCropWithRect(bitmap: Bitmap, imgHeight: Int): Bitmap? {
        var bitmap: Bitmap = bitmap ?: return null
        val w: Int = bitmap.width // 得到图片的宽，高
        val h: Int = bitmap.height

        /*int nw, nh, retX, retY;
        if (w > h) {
            nw = h / 2;
            nh = h;
            retX = (w - nw) / 2;
            retY = 0;
        } else {
            nw = w / 2;
            nh = w;
            retX = w / 4;
            retY = (h - w) / 2;
        }*/

        // 下面这句是关键
        val bmp: Bitmap = Bitmap.createBitmap(bitmap, 0, h - imgHeight, w, imgHeight, null, false)
        if (bitmap != null && bitmap != bmp && !bitmap.isRecycled) {
            bitmap.recycle()
        }
        return bmp // Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }
}

