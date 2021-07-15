package com.mp.douyu.ui.home.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.CachedStatusWrapper
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.DownLoadBean
import com.mp.douyu.download.DownLoadManager
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.play.VideoPlayActivity
import com.mp.douyu.utils.LogUtils
import com.mp.douyu.utils.ScreenUtils
import com.tencent.rtmp.downloader.ITXVodDownloadListener
import com.tencent.rtmp.downloader.TXVodDownloadMediaInfo
import kotlinx.android.synthetic.main.activity_play_cache.*
import kotlinx.android.synthetic.main.activity_play_recorder.choose_all
import kotlinx.android.synthetic.main.activity_play_recorder.g1
import kotlinx.android.synthetic.main.activity_play_recorder.m_delete
import kotlinx.android.synthetic.main.activity_play_recorder.rv_history
import kotlinx.android.synthetic.main.record_recycle_item_video_recorder.view.*
import kotlinx.android.synthetic.main.title_bar_simple.*

/**
 * 视频缓存记录
 */
class VideoCacheRecordActivity : MBaseActivity() {
    var cacheHistory: ArrayList<DownLoadBean> = arrayListOf()
    override val contentViewLayoutId: Int
        get() = R.layout.activity_play_cache

    override fun initView() {
        rv_history.apply {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            layoutManager = LinearLayoutManager(this@VideoCacheRecordActivity)
            adapter = mAdapter
        }
        initData()
        setListener()
    }

    private fun setListener() {
        ibReturn.singleClick {
            finish()
        }
    }

    private fun initData() {
        iftTitle.text = "缓存记录"
        iftActionRightSub.text = getString(R.string.edit)
        iftActionRightSub.visibility = VISIBLE
        iftActionRightSub.singleClick {
            LogUtils.i("==", "${getString(R.string.edit)}click")
            iftActionRightSub.text =
                if (iftActionRightSub.text.toString() == getString(R.string.m_cancel)) getString(R.string.edit) else getString(
                    R.string.m_cancel)

            g1.visibility =
                if (iftActionRightSub.text.toString() == getString(R.string.m_cancel)) VISIBLE else GONE
            cacheHistory.apply {
                map {
                    it.isEdit = (iftActionRightSub.text.toString() == getString(R.string.m_cancel))
                }
                mAdapter.takeInstance<CachedAutoRefreshAdapter<DownLoadBean>>()
                    .refresh(cacheHistory, null)
            }

        }

        choose_all.singleClick {
            cacheHistory.apply {
                map {
                    it.isSelect = !it.isSelect!!
                }
                mAdapter.takeInstance<CachedAutoRefreshAdapter<DownLoadBean>>()
                    .refresh(cacheHistory, null)
            }
        }

        m_delete.singleClick {
            var tempList: ArrayList<DownLoadBean> = arrayListOf()
            cacheHistory.apply {
                map {
                    DownLoadManager.instance.stopDownload(it)
                    DownLoadManager.instance.delDownloadFile(it)
                    if (it.isSelect!!) tempList.add(it)
                }

                removeAll(tempList)
                mAdapter.takeInstance<CachedAutoRefreshAdapter<DownLoadBean>>()
                    .refresh(cacheHistory, null)
                StoredUserSources.putCacheRecord(cacheHistory)
                cl_empty.visibility = if (cacheHistory.isNotEmpty()) GONE else VISIBLE
            }
        }

        cacheHistory = StoredUserSources.getCacheRecord().apply {
            map {
                it.isEdit = false
                it.isSelect = false
            }
        }
        cl_empty.visibility = if (cacheHistory.isNotEmpty()) GONE else VISIBLE
        mAdapter.takeInstance<CachedAutoRefreshAdapter<DownLoadBean>>()
            .refresh(cacheHistory, null)
//        DownLoadManager.instance.startCacheVideo()
        DownLoadManager.instance.addDownLoadListener(downloadListener)
    }

    private val mAdapter by lazy {
        CachedStatusWrapper().apply {
            client = object : CachedAutoRefreshAdapter<DownLoadBean>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                    return CacheViewHolder(LayoutInflater.from(this@VideoCacheRecordActivity)
                        .inflate(R.layout.record_recycle_item_video_recorder,
                            parent,
                            false)).apply {}
                }

                override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                    holder.itemView.apply {
                        val data = get(position)
                        loadGlobleVideo(context, data.cover, iv_video_image)

//                        Glide.with(context).load(data.faceImageUrl).into(iv_video_image)
                        tv_title.text = data.title
                        tv_downloadSize.text = "${ScreenUtils.byteToMB(data.mediaInfo?.downloadSize?.toLong()?:0)}"
                        if (data.downState == 1) {
                            progressBar.visibility = View.INVISIBLE
                            tv_speed.text = "下载完成"
                        } else if (data.downState==2){
                            tv_speed.text = "等待中…"
                        } else {
                            tv_speed.text = ScreenUtils.randomSpeed()
                            progressBar.max = 100
                            progressBar.progress = ((data.mediaInfo?.progress ?: 0f) * 100).toInt()
                        }

                        v_choose.apply {
                            visibility = if (data.isEdit!!) VISIBLE else GONE
                            isSelected = data.isSelect!!
                            singleClick {
                                data.isSelect = !data.isSelect!!
                                notifyItemChanged(position)
                            }
                        }
                        iv_video_image.singleClick {
                            startActivityWithTransition(VideoPlayActivity.open(this@VideoCacheRecordActivity,
                                data.video_id))
                        }
                        tv_title.singleClick {
                            startActivityWithTransition(VideoPlayActivity.open(this@VideoCacheRecordActivity,
                                data.video_id))
                        }

                    }
                }
            }
        }
    }
    private val downloadListener by lazy {
        object : ITXVodDownloadListener {
            override fun onDownloadStart(mediaInfo: TXVodDownloadMediaInfo?) {

            }

            override fun onDownloadError(mediaInfo: TXVodDownloadMediaInfo?, error: Int, reason: String?) {
                if (cacheHistory.isNotEmpty()) {
                    for ((index,bean) in cacheHistory.withIndex()) {
                        if (mediaInfo?.url.equals(bean.url)) {
                            bean.mediaInfo = mediaInfo
                            bean.downState = 2
                            StoredUserSources.putCacheRecord(cacheHistory)
                            mAdapter.notifyItemChanged(index)
                            return
                        }
                    }
                }
            }

            override fun hlsKeyVerify(mediaInfo: TXVodDownloadMediaInfo?, p1: String?, p2: ByteArray?): Int {

                return -1
            }

            override fun onDownloadStop(mediaInfo: TXVodDownloadMediaInfo?) {

            }

            override fun onDownloadFinish(mediaInfo: TXVodDownloadMediaInfo?) {
                if (cacheHistory.isNotEmpty()) {
                    for ((index,bean) in cacheHistory.withIndex()) {
                        if (mediaInfo?.url.equals(bean.url)) {
                            bean.mediaInfo = mediaInfo
                            bean.downState = 1
                            StoredUserSources.putCacheRecord(cacheHistory)
                            mAdapter.notifyItemChanged(index)
                            return
                        }
                    }
                }
            }

            override fun onDownloadProgress(mediaInfo: TXVodDownloadMediaInfo?) {
                if (cacheHistory.isNotEmpty()) {
                    for ((index,bean) in cacheHistory.withIndex()) {
                        if (mediaInfo?.url.equals(bean.url)) {
                            bean.mediaInfo = mediaInfo
                            mAdapter.notifyItemChanged(index)
                            return
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun open(context: Context): Intent {
            return Intent(context, VideoCacheRecordActivity::class.java)
        }
    }
}
