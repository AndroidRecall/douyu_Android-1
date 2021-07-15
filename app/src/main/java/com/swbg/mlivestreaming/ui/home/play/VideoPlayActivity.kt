package com.swbg.mlivestreaming.ui.home.play

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.hadcn.keyboard.ChatKeyboardLayout
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.hdl.m3u8.M3U8DownloadTask
import com.hdl.m3u8.M3U8InfoManger
import com.hdl.m3u8.bean.M3U8
import com.hdl.m3u8.bean.OnDownloadListener
import com.hdl.m3u8.bean.OnM3U8InfoListener
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.swbg.mlivestreaming.GlobeStatusViewHolder
import com.swbg.mlivestreaming.GlobeStatusViewHolder.isNotNeedLogin
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.*
import com.swbg.mlivestreaming.download.DownLoadManager
import com.swbg.mlivestreaming.jumpIsToGamePageDialog
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.provider.TokenProvider
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.HomeViewModel
import com.swbg.mlivestreaming.ui.home.search.SearchActivity
import com.swbg.mlivestreaming.ui.mine.IWantExtensionActivity
import com.swbg.mlivestreaming.ui.mine.self_info.BindPhoneNumActivity
import com.swbg.mlivestreaming.ui.mine.space.UserSpaceActivity
import com.swbg.mlivestreaming.ui.mine.vip.MineVipActivity
import com.swbg.mlivestreaming.ui.mine.walnut.ChargeCenterActivity
import com.swbg.mlivestreaming.ui.square.comment.DynamicCommentDetailActivity
import com.swbg.mlivestreaming.utils.AppUtils
import com.swbg.mlivestreaming.utils.LogUtils
import com.swbg.mlivestreaming.utils.ToastUtils
import com.swbg.mlivestreaming.utils.permission_helper.PermissionHelper
import com.swbg.mlivestreaming.view.popupwindow.GetWatchNumDialog
import kotlinx.android.synthetic.main.activity_video_play.*
import kotlinx.android.synthetic.main.activity_video_play.iv_share
import kotlinx.android.synthetic.main.activity_video_play.keyboard
import kotlinx.android.synthetic.main.custom_jzvd.*


class VideoPlayActivity : MBaseActivity() {
    private var currentPage: Int = 1
    private var currentCommendPage: Int = 1
    private var isDesc: Boolean = false
    private var listVideoPlayer = VideoPlayLinkBean()

    //当前点赞或者收藏的状态
    private var currentType: Int = 101

    //当前视频的id
    private var currentVideoId: Int = 0

    //1标清/2高清/3超清
    private var currentDefinitionNum: Int = 1
    override val contentViewLayoutId: Int
        get() = R.layout.activity_video_play

    override fun initView() {
//
//        matchWaterDropScreen(this@VideoPlayActivity)
//        setActivityImmersion(this)
        currentVideoId = getVideoId
        initRecyclerView()
        initKeyboard()
        initListener()
        initData()

        initCommendLoad()
    }

    private fun initCommendLoad() {

        refreshLayout.setRefreshHeader(ClassicsHeader(this))
        refreshLayout.setRefreshFooter(ClassicsFooter(this))
        refreshLayout.setOnRefreshListener {
            currentCommendPage = 1
            loadBeginCommend()
        }
        refreshLayout.setOnLoadMoreListener { refreshlayout ->
            currentCommendPage += 1
            loadBeginCommend()
        }
        loadBeginCommend()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //设置横屏
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;//设置竖屏

            super.onConfigurationChanged(newConfig);
        }
    }

    private fun loadBeginCommend() {
        homeViewModel.getCommendList(hashMapOf("video_id" to "$currentVideoId",
            "desc" to "${isDesc}",
            "size" to "10",
            "page" to "$currentCommendPage"))
    }

    private fun initData() {
        loadVideoDetail(currentVideoId)
        getPlayLink()
//        homeViewModel.getVideoPlayerDetail(hashMapOf("id" to "$currentVideoId"))
//        homeViewModel.getPlayLink(hashMapOf("video_id" to "$currentVideoId",
//            "token" to TokenProvider.get().accessToken,
//            "device" to AppUtils.getAndroidId(this)))
        refreshLottery()
    }

    private fun getPlayLink() {
        homeViewModel.getPlayLink(hashMapOf("video_id" to "$currentVideoId",
            "token" to TokenProvider.get().accessToken,
            "device" to AppUtils.getAndroidId(this)))
    }

    private fun initListener() {
        ib_return.singleClick {
            finish()
        }
        tv_video_title.singleClick {
            finish()
        }
        //share
        iv_share.singleClick {
            //分享
            if (isNotNeedLogin(this as MBaseActivity)) {
                startActivityWithTransition(IWantExtensionActivity.open(this))
            }
        }
        //collect
        iv_collect_b.singleClick {
            if (isNotNeedLogin(this)) clickCollect(when (iv_collect_b.isSelected) {
                true -> 202
                else -> 102
            })

        }
        //click like
        iv_click_like_b.singleClick {
            if (isNotNeedLogin(this)) clickCollect(when (iv_click_like_b.isSelected) {
                true -> 201
                else -> 101
            })
        }
        //comment
        iv_comment.singleClick {
            LogUtils.i("==", et_comment_content.text.toString().trim())
            recyclerView.smoothScrollToPosition(4)
        }
        et_comment_content.singleClick {
            keyboard.visibility = View.VISIBLE
            keyboard.showLayout()
        }

    }

    private fun initRecyclerView() {
        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.goOnPlayOnPause()
    }

    override fun onResume() {
        super.onResume()
        Jzvd.goOnPlayOnResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        Jzvd.releaseAllVideos()
    }

    private val mAdapter by lazy {
        VideoPlayAdapter(this) { it: View, videoId: Int ->
            when (it.id) {
                R.id.tv_change_batch1, R.id.tv_change_batch -> {
                    currentPage += 1
                    loadBegin()
                }
                R.id.iv_video -> {
                    if (videoId > -1) {
                        currentVideoId = videoId
                        //请求视频信息
                        loadVideoDetail(videoId)
                        getPlayLink()
                    }
                }
                R.id.tv_time -> {
                    if (videoId == 2002) refreshLottery()
                }
                R.id.iv_down_load -> {
                    if (isNotNeedLogin(this)) {
                        getDownloadLinks()
                    }
                }
                //立即下注
                R.id.iv_click_immediately -> {
                    if (isNotNeedLogin(this)) immediatelyBottomPour()
                }
                //点赞
                R.id.iv_click_like -> {
                    if (isNotNeedLogin(this)) clickCollect(videoId)
                }
                //收藏
                R.id.iv_collect -> {
                    if (isNotNeedLogin(this)) clickCollect(videoId)
                }
                //buy vip
                R.id.iv_buy_vip -> {
                    if (isNotNeedLogin(this)) initBuyVip()
                }
                R.id.tv_content ->{
                    //去搜所
                    startActivity(SearchActivity.open(context = this,keyword = (it as TextView).text.toString()))
//                    ToastUtils.showToast((it as TextView).text.toString())
                }

            }
        }.apply {
            setListener(object : VideoPlayAdapter.OnCommentListener {
                override fun onCommentClick(commentBean: CommentBean) {
                    startActivityWithTransition(DynamicCommentDetailActivity.open(this@VideoPlayActivity,
                        commentBean))
                }

                override fun onCommentHeaderClick(user: CommonUserBean?) {
                    startActivityWithTransition(UserSpaceActivity.open(this@VideoPlayActivity,
                        user?.id))
                }
            })
        }
    }

    private fun initBuyVip() {
        GetWatchNumDialog.newInstance("您还有观影次数").apply {
            setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    1 -> {//去购买
                        startActivityWithTransition(MineVipActivity.open(this@VideoPlayActivity))
                    }
                    2 -> {//去推广
                        startActivityWithTransition(IWantExtensionActivity.open(this@VideoPlayActivity))
                    }
                    3 -> {//去充值
                        startActivityWithTransition(ChargeCenterActivity.open(this@VideoPlayActivity))
                    }
                    4 -> {//去绑定
                        startActivityWithTransition(BindPhoneNumActivity.open(this@VideoPlayActivity))
                    }
                    Dialog.BUTTON_NEGATIVE -> {
                        dismiss()
                    }
                }
            })
            show(supportFragmentManager, null)
        }
    }

    private fun getDownloadLinks() {
//        ToastUtils.showToast(getString(R.string.not_develop_please_waite),false)
//        return
        /* listVideoPlayer.url?.let {

             DownLoadManager.instance.downLoadVideo(DownLoadBean(video_id = currentVideoId,title=mAdapter.videoPlayBean.title,cover = mAdapter.videoPlayBean.cover0,url = it),true)
 //            onDownload(it)
         }*/
        homeViewModel.getDownloadLinks(hashMapOf("video_id" to "$currentVideoId",
            "definition" to "$currentDefinitionNum",
            "token" to TokenProvider.get().accessToken,
            "device" to AppUtils.getAndroidId(this)))
    }


    private fun clickCollect(type: Int) {
        currentType = type
        homeViewModel.clickLike(hashMapOf("video_id" to "$currentVideoId", "action" to "$type"))

    }

    private fun immediatelyBottomPour() {
        //获取游戏链接
        mAdapter.lotteryOpen.game_id?.let {
            homeViewModel.getGameLink(hashMapOf("game_id" to it))
        }
    }

    //刷新彩票信息
    private fun refreshLottery() {
        homeViewModel.getLotteryOpen()
    }

    private fun loadVideoDetail(videoId: Int) {
        homeViewModel.getVideoPlayerDetail(hashMapOf("id" to "$videoId"))
    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _videoPlayDetailData.observe(it, Observer {
            it?.let {
                mAdapter.videoPlayBean = it
                //点赞和收藏的图标
                setCollectLike(mAdapter.videoPlayBean)
                mAdapter.changeHeader(1)
                Glide.with(this@VideoPlayActivity).asBitmap().load(it.cover0).centerCrop()
                    .into(jz_player.posterImageView)
                tv_video_title.text = it.title
                jz_player.setUserPlayNum(it)
                //开始加载4个视频
                loadBegin()

            }
        })

        _getDoenloadLinkData.observe(it, Observer {
            it?.let {
                beginDownloadVideo(it)
            }
        })

        //视频列表
        _videoListData.observe(it, Observer {
            it?.let {
                try {
                    if (currentPage <= it.total!!) {
                        mAdapter.videoList = it.data!!
                        mAdapter.changeHeader(3)
                    } else {
                        currentPage = 1
                        loadBegin()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        _videoGetLinkData.observe(it, Observer {
            it?.let { it1 ->
                initPlayerUrl(it1?.url)
//                jz_player.setUp(it1?.url, "")
//                jz_player.startVideo()


                //get info
//                getVideoInfo(it1?.url!!)
                listVideoPlayer = it1


                //save video history
                mAdapter.videoPlayBean.let { it2 ->
                    if (TextUtils.isEmpty(it2.id.toString()) || it2.id == 0) {
                        return@let
                    }
                    val playHistory = StoredUserSources.getPlayHistory()
                    val iterator = playHistory.iterator()
                    while (iterator.hasNext()) {
                        val next = iterator.next()
                        if (next.id == it2.id) {
                            iterator.remove()
                        }

                    }
                    playHistory.add(0,
                        VideoBean(videoTitle = it2.title,
                            id = it2.id,
                            faceImageUrl = it2.cover0,
                            play = it2.play,
                            designation = it2.designation))

                    StoredUserSources.putPlayHistory(playHistory)
                }

            }
        })

        _getCommendData.observe(it, Observer {
            refreshLayout.finishRefresh()
            refreshLayout.finishLoadMore()
            it?.let {
                if (currentCommendPage == 1) {
                    it.data?.let {
                        mAdapter.commendList.clear()
                        mAdapter.commendList.addAll(it)
                        mAdapter.changeHeader(4)
                    }
                } else {
                    it.data?.let {
                        mAdapter.commendList.addAll(it)
                        mAdapter.changeHeader(4)
                    }
                }
            }
        })

        _getLotteryOpen.observe(it, Observer {
            it?.let {
                mAdapter.lotteryOpen = it
                mAdapter.changeHeader(2)
            }
        })
        _getGameLinkData.observe(it, Observer {
            it?.let {
                jumpIsToGamePageDialog(it, this@VideoPlayActivity)
                /*if (it.alert == null){
                    ActivityUtils.jumpToWebView(it.url, this@VideoPlayActivity,true)
                } else{
                    FastTransferDialog.newInstance(it.alert.balance,it.alert.game_balance).apply {
                        setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                R.id.tv_immediately_charge -> {
                                    startActivityWithTransition(ChargeCenterActivity.open(this@VideoPlayActivity))
                                }
                                R.id.tv_confirm -> {
                                    startActivityWithTransition(TransferAccountActivity.open(this@VideoPlayActivity,
                                        currentChooseItem))
                                }
                                R.id.tv_join_game -> {
                                    ActivityUtils.jumpToWebView(it.url, this@VideoPlayActivity,true)
                                }

                            }
                        }).show(supportFragmentManager, null)
                    }
                }*/
            }
        })
        _getClickColletData.observe(it, Observer {
            it?.let {
                when (currentType) {
                    101 -> { //喜欢成功
                        ToastUtils.showToast("点赞成功")
                        setCollectLike(mAdapter.videoPlayBean.apply {
                            is_like = "1"
                        })
                    }
                    102 -> {
                        ToastUtils.showToast("收藏成功")
                        setCollectLike(mAdapter.videoPlayBean.apply {
                            is_star = "1"
                        })
                    }
                    201 -> {
                        ToastUtils.showToast("取消点赞")
                        setCollectLike(mAdapter.videoPlayBean.apply {
                            is_like = "0"
                        })
                    }
                    202 -> {
                        ToastUtils.showToast("取消收藏")
                        setCollectLike(mAdapter.videoPlayBean.apply {
                            is_star = "0"
                        })
                    }
                }
                mAdapter.changeHeader(1)
            }
        })
        _commentVideoResult.observe(it, Observer {
            it?.apply {
                keyboard.clearInputContent()
                keyboard.hideLayout()
                keyboard.visibility = View.GONE
            }
        })
    }

    private fun initPlayerUrl(url: String?) {
        GetM3U8Info().getM2U8Info(url) {
            if (!isFinishing)
            jz_player.setCurrentPlaySource(it)
        }
    }


    private fun beginDownloadVideo(videoLink: VideoDoenloadLink) {
        //下载
        videoLink.apply {
            url?.let { it1 ->
                PermissionHelper.request(this@VideoPlayActivity,
                    object : PermissionHelper.PermissionCallback {
                        override fun onSuccess() {
//                            onDownload(it1)
                            listVideoPlayer.url?.let {

                                DownLoadManager.instance.downLoadVideo(DownLoadBean(video_id = currentVideoId,
                                    title = mAdapter.videoPlayBean.title,
                                    cover = mAdapter.videoPlayBean.cover0,
                                    url = it), true)
                                //            onDownload(it)
                            }
                        }
                    },
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun setCollectLike(videoPlayBean: VideoPlayBean) {
        Glide.with(this@VideoPlayActivity).load(when (videoPlayBean.is_like) {
            "0" -> {
                iv_click_like_b.isSelected = false
                R.mipmap.player_click_evaluate
            }
            else -> {
                iv_click_like_b.isSelected = true
                R.mipmap.video_play_like_b_s
            }
        }).into(iv_click_like_b)
        Glide.with(this@VideoPlayActivity).load(when (videoPlayBean.is_star) {
            "0" -> {
                iv_collect_b.isSelected = false
                R.mipmap.player_collection_me

            }
            else -> {
                iv_collect_b.isSelected = true
                R.mipmap.video_play_collect_b_s
            }
        }).into(iv_collect_b)
    }


    private fun loadBegin() {
        homeViewModel.getVideoList(hashMapOf("cate_id" to "${mAdapter.videoPlayBean.cate_id}",
            "special_id" to "",
            "sort" to "new",
            "size" to "6",
            "page" to "${currentPage}"))
    }

    private val getVideoId by lazy {
        intent.getIntExtra(VIDEO_ID, 0)
    }


    fun getVideoInfo(url: String) {
        M3U8InfoManger.getInstance().getM3U8Info(url, object : OnM3U8InfoListener {
            override fun onSuccess(m3U8: M3U8?) {

                LogUtils.e("==获取成功了$m3U8")
            }

            override fun onStart() {
                LogUtils.e("==开始获取信息")
            }

            override fun onError(errorMsg: Throwable) {
                LogUtils.e("==出错了$errorMsg")
            }
        })
    }


    var task1 = M3U8DownloadTask("1001")

    //下载按钮监听事件
    fun onDownload(url: String) {

/*        val toFile = "/sdcard/" + System.currentTimeMillis() + ".ts"
//        tvSaveFilePathTip.setText("缓存目录在：/sdcard/11m3u8/\n最终导出的缓存文件在：$toFile")
        M3U8LiveManger.getInstance().setTempDir("/sdcard/11m3u8/")
            .setSaveFile(toFile) //（设置导出缓存文件）必须以.ts结尾
            .caching(url, object : OnDownloadListener {
                override fun onDownloading(itemFileSize: Long, totalTs: Int, curTs: Int) {
                    //此回调只有curTs有意义，表示开始缓存第几个ts
                    LogUtils.e("totalTs=$totalTs，curTs=$curTs，itemFileSize=$itemFileSize，")
                }

                override fun  () {
                    //此回调没有什么意义
                }

                override fun onProgress(curLength: Long) {
                    LogUtils.e("current=$curLength")
*//*
                    if (curLength - lastLength > 0) {
                        //计算缓存速度
                        val speed = NetSpeedUtils.getInstance()
                            .displayFileSize(curLength - lastLength) + "/s"
                        lastLength = curLength
                    }*//*
                }

                override fun onStart() {
                    //开始缓存
                    LogUtils.e("开始下载了")

                }

                override fun onError(errorMsg: Throwable) {
                    //缓存出错了
                    LogUtils.e("出错了" + errorMsg)

                }
            })*/




        task1.download(url, object : OnDownloadListener {
            override fun onDownloading(itemFileSize: Long, totalTs: Int, curTs: Int) {
                LogUtils.e(task1.taskId + "下载中.....itemFileSize=" + itemFileSize + "\ttotalTs=" + totalTs + "\tcurTs=" + curTs)
            }

            /**
             * 下载成功
             */
            override fun onSuccess() {
                LogUtils.e(task1.taskId + "下载完成了")
            }

            /**
             * 当前的进度回调
             *
             * @param curLenght*/

            override fun onProgress(curLenght: Long) {
                LogUtils.e("current=$curLenght")
                /* if (curLenght - lastLength > 0) {
                     val speed =
                         NetSpeedUtils.getInstance().displayFileSize(curLenght - lastLength) + "/s"
                     LogUtils.e(task1.taskId + "speed = " + speed)
                     runOnUiThread {
                         LogUtils.e("更新了")
                         tvSpeed1.setText(speed)
                         LogUtils.e(tvSpeed1.getText().toString())
                     }
                     lastLength = curLenght
                 }*/
            }

            override fun onStart() {
                LogUtils.e(task1.taskId + "开始下载了")
            }

            override fun onError(errorMsg: Throwable) {
                LogUtils.e(task1.taskId + "出错了" + errorMsg)
            }
        })
    }


    companion object {
        const val VIDEO_ID = "VIDEO_ID"
        fun open(context: Context?, videoId: Int?): Intent {
            return Intent(context, VideoPlayActivity::class.java).putExtra(VIDEO_ID, videoId)
        }
    }

    private fun initKeyboard() {
        keyboard?.setKeyboardStyle(ChatKeyboardLayout.Style.TEXT_EMOTICON)
        keyboard?.setOnChatKeyBoardListener(object :
            ChatKeyboardLayout.SimpleOnChatKeyboardListener() {
            override fun onSendButtonClicked(text: String) {
                super.onSendButtonClicked(text)
                text?.let {
                    if (text.isNotEmpty()) {
                        //发送信息
                        homeViewModel.commentVideo(hashMapOf("video_id" to "$currentVideoId",
                            "content" to "$text"))
                    }
                }
            }

            override fun onBackPressed() {
                super.onBackPressed()
            }

            override fun onSoftKeyboardClosed() {
                super.onSoftKeyboardClosed()
            }
        })
    }
}
