package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.*
import com.swbg.mlivestreaming.adapter.LiveRoomChatAdapter.Companion.TYPE_TIP
import com.swbg.mlivestreaming.bean.ChatMsgBean
import com.swbg.mlivestreaming.bean.LiveBean
import com.swbg.mlivestreaming.ui.anchor.live.TXLivePlayerManager
import com.swbg.mlivestreaming.utils.DisplayUtils
import com.swbg.mlivestreaming.view.clearscreen.SlideDirection
import com.tencent.rtmp.ITXLivePlayListener
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN
import com.tencent.rtmp.TXLivePlayer
import kotlinx.android.synthetic.main.layout_live_medal_list.view.*
import kotlinx.android.synthetic.main.layout_live_notice.view.*
import kotlinx.android.synthetic.main.layout_live_pusher_info.view.*
import kotlinx.android.synthetic.main.layout_live_pusher_info.view.iv_anchor_head_icon
import kotlinx.android.synthetic.main.layout_live_right.view.*
import kotlinx.android.synthetic.main.live_recycle_item_live_detail.view.*
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
import org.jzvd.jzvideo.TAG
import java.util.*

class LiveDetailAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<LiveBean>() {
    var TAG = LiveDetailAdapter::class.java.simpleName
    override var data: MutableList<LiveBean>
        get() = super.data
        set(value) {}
    var mListener: OnLiveListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return LiveHolder(context,
            LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_live_detail, parent, false)).apply {
            itemView.apply {

            }
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            drawerView.addClearViews(cl_drawer)
            drawerView.setSlideDirection(SlideDirection.RIGHT)
//            TXLivePlayerManager.getInstance(context).livePlayer.apply {
//                setPlayerView(anchor_video_view)
////                setRenderMode(RENDER_MODE_FULL_FILL_SCREEN)
//                setPlayListener(object : ITXLivePlayListener {
//                    override fun onPlayEvent(event: Int, param: Bundle?) {
//                        when (event) {
//                            TXLiveConstants.PLAY_EVT_CONNECT_SUCC -> {
//                                //已经连接服务器
//                                audience_background.visibility = View.GONE
//                            }
//                            TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME -> {
//                                Log.e("Player", "收到首帧数据")
//                            }
//                            TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
//                                //视频播放开始，如果您自己做 loading，会需要它
//                                loading_background3.visibility = View.GONE
//                            }
//                            TXLiveConstants.PLAY_EVT_PLAY_LOADING -> {
//                                //视频播放进入缓冲状态，缓冲结束之后会有 PLAY_BEGIN 事件
//                                loading_background3.visibility = View.VISIBLE
//                            }
//                            TXLiveConstants.PLAY_ERR_NET_DISCONNECT -> {
//                                Log.e("Player", "拉流失败")
//                                mListener?.onPlayEnd(position)
//                            }
//                            TXLiveConstants.PLAY_EVT_PLAY_END -> {
//                                Log.e("Player", "播放结束")
//                                mListener?.onPlayEnd(position)
//                            }
//                        }
//
//                    }
//
//                    override fun onNetStatus(p0: Bundle?) {
//
//                    }
//
//                })
//            }
//            (holder as LiveHolder).mLivePlayer = TXLivePlayer(context).apply {
//                setPlayerView(anchor_video_view)
//                setRenderMode(RENDER_MODE_FULL_FILL_SCREEN)
//                setPlayListener(object : ITXLivePlayListener {
//                    override fun onPlayEvent(event: Int, param: Bundle?) {
//                        when (event) {
//                            TXLiveConstants.PLAY_EVT_CONNECT_SUCC -> {
//                                //已经连接服务器
//                                audience_background.visibility = View.GONE
//                            }
//                            TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME -> {
//                                Log.e("Player", "收到首帧数据")
//                            }
//                            TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
//                                //视频播放开始，如果您自己做 loading，会需要它
//                                loading_background3.visibility = View.GONE
//                            }
//                            TXLiveConstants.PLAY_EVT_PLAY_LOADING -> {
//                                //视频播放进入缓冲状态，缓冲结束之后会有 PLAY_BEGIN 事件
//                                loading_background3.visibility = View.VISIBLE
//                            }
//                            TXLiveConstants.PLAY_ERR_NET_DISCONNECT -> {
//                                Log.e("Player", "拉流失败")
//                                mListener?.onPlayEnd(position)
//                            }
//                            TXLiveConstants.PLAY_EVT_PLAY_END -> {
//                                Log.e("Player", "播放结束")
//                                mListener?.onPlayEnd(position)
//                            }
//                        }
//
//                    }
//
//                    override fun onNetStatus(p0: Bundle?) {
//
//                    }
//
//                })
//            }
//            Glide.with(context).load(get(position).user?.avatar).into(jz_player.posterImageView)
            if (get(position).anchor?.avatar?.contains("http") == false) {
                get(position).anchor?.avatar = BuildConfig.IMAGE_BASE_URL+get(position).anchor?.avatar
            }
            if (get(position).image?.contains("http") == false) {
                get(position).image = BuildConfig.IMAGE_BASE_URL+get(position).image
            }
            Glide.with(context).load(get(position).anchor?.avatar)
                .placeholder(R.mipmap.default_avatar).into(iv_anchor_head_icon)
            Glide.with(context).load(get(position).image).placeholder(R.mipmap.login_code_failed).centerCrop()
                .into(audience_background)
            tv_anchor_name.text = get(position).user?.nickname?:"     "
            tv_hot_num.text = "${get(position).hot?:0}"
            tv_medal_num.text = "${get(position).gold?:0}"
            iv_follow_status.visibility =
                if (get(position).isFollowLive == 0) View.VISIBLE else View.GONE
            tv_scroll.text = "${get(position).notice}"
            recycle_audience.apply {
                adapter = (holder as LiveHolder).audienceAdapter.apply {
                    addAll(this@LiveDetailAdapter.get(position).listAvatars)
                    initializeWidth(data, recycle_audience, 40, 3)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            }
            recycle_bet.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = (holder as LiveHolder).betRecordAdapter.apply {
                    setOnLiveListener(object :LiveWinRecordAdapter.OnBetListener{
                        override fun onBet(position: Int) {
                            val gameType =
                                holder.betRecordAdapter.mData?.get(position)?.gameBean?.gameType
                            this@LiveDetailAdapter.mListener?.onBetClick(gameType)
                        }
                    })
                    initializeHeight(this.mData!!, recycle_bet, 30, 3)
                }

            }

            recycle_chat.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = LiveRoomChatAdapter({}, context).apply {
                    add(ChatMsgBean(itemViewType = TYPE_TIP))
                }
            }
//            tv_title.text = "${get(position).title}"
////            tv_desc.text = "${get(position).title}"
//            tv_comment_num.text = "${get(position).comment_count ?: 0}"
//            tv_repost_num.text = "${get(position).share_count ?: 0}"
//            tv_like_num.text = "${get(position).like_count ?: 0}"
//
//            tv_like_num.isSelected = get(position).is_like_short_video == 1
//            fl_follow.visibility =
//                if (get(position).is_follow == 0) View.VISIBLE else View.GONE
//            iv_avatar.apply {
//                singleClick {
//                    shortVideoListener?.onHeaderClick(position)
//                }
//            }
            if (holder is LiveHolder) {

                holder.initDanmaku(danmaku)
            }

            initOnclickListener(position)

        }
    }


    override fun onViewRecycled(holder: CacheViewHolder) {
        super.onViewRecycled(holder)
        holder.itemView.apply {
//            (holder as LiveHolder).mLivePlayer = null
            anchor_video_view.onDestroy()
        }

    }

    private fun View.initOnclickListener(position: Int) {
        layout_live_pusher_info.singleClick {
            mListener?.onHeaderClick(position)
        }
        iv_follow_status.singleClick {
            mListener?.onFollow(position)
        }
        layout_live_medal_list.singleClick {
            mListener?.onRankListClick(position)
        }
        iv_vip.singleClick {
            mListener?.onVipClick(position)
        }
        iv_back.singleClick {
            mListener?.onCloseClick(position)
        }
        iv_recharge.singleClick {
            mListener?.onRechargeClick(position)
        }
        iv_yp.singleClick {
            mListener?.onHookClick(position)
        }
        iv_right_share.singleClick {
            mListener?.onShareClick(position)
        }
        iv_share.singleClick {
            mListener?.onShareClick(position)
        }
        iv_bet.singleClick {
            mListener?.onBetGameClick(position)
        }
        cl_send.singleClick {
            mListener?.onSendClick(position)
        }
        iv_danmaku.singleClick {
            iv_danmaku.isSelected = !iv_danmaku.isSelected
            get(position).isOpenDanmaku = iv_danmaku.isSelected
            if (get(position).isOpenDanmaku) {
                danmaku.show()
            } else {
                danmaku.hide()
            }
            mListener?.onDanmakuClick(get(position).isOpenDanmaku, position)
        }
        iv_gift1.singleClick {
            mListener?.onGiftClick(position)
        }
        drawerView.setOnClickListener {
            Log.e(TAG,"onclick")
            if (drawerView.isIfCleared) {
                drawerView.restoreWithAnim()
            }
        }

    }


    class LiveHolder(context: Context?, containerView: View) : CacheViewHolder(containerView) {
//        var mLivePlayer: TXLivePlayer? = null
        var danmakuContext: DanmakuContext? = null
        var danmakuParser: BaseDanmakuParser? = null

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
            danmakuView.addDanmaku(danmaku)
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

            danmakuView.setCallback(object :DrawHandler.Callback{
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
            danmakuView.prepare(danmakuParser,danmakuContext)
//            danmakuView.showFPS(BuildConfig.DEBUG)
            danmakuView.enableDanmakuDrawingCache(true)
        }
        val audienceAdapter by lazy {
            LiveAudienceInfoAdapter({}, context).apply {

            }
        }
        val betRecordAdapter by lazy {
            LiveWinRecordAdapter(context).apply {

            }

        }

    }

    fun setListener(onShortVideoListener: OnLiveListener) {
        mListener = onShortVideoListener
    }

    interface OnLiveListener {
        fun onFollow(position: Int)
        fun onHeaderClick(position: Int)
        fun onRankListClick(position: Int)
        fun onRechargeClick(position: Int)
        fun onBetClick(type: Int?)
        fun onHookClick(id: Int?)
        fun onBetGameClick(position: Int)
        fun onGiftClick(position: Int)
        fun onShareClick(position: Int)
        fun onDanmakuClick(isOpen: Boolean, position: Int)
        fun onSendClick(position: Int)
        fun onVipClick(position: Int)
        fun onCloseClick(position: Int)
        fun onPlayEnd(position: Int)

    }

}