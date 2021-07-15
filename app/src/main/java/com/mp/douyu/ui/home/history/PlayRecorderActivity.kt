package com.mp.douyu.ui.home.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.CachedStatusWrapper
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.VideoBean
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.play.VideoPlayActivity
import com.mp.douyu.utils.LogUtils
import kotlinx.android.synthetic.main.activity_play_recorder.*
import kotlinx.android.synthetic.main.item_history_recorder.view.*
import kotlinx.android.synthetic.main.title_bar_simple.*

class PlayRecorderActivity : MBaseActivity() {
    var playHistory: ArrayList<VideoBean>? = null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_play_recorder

    override fun initView() {
        rv_history.apply {
            layoutManager = LinearLayoutManager(this@PlayRecorderActivity)
            adapter = mAdapter
        }
        initData()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        playHistory = StoredUserSources.getPlayHistory().apply {
            map {
                it.btnShow = false
                it.isSelect = false
            }
        }
        mAdapter.takeInstance<CachedAutoRefreshAdapter<VideoBean>>().refresh(playHistory!!, null)
    }
    private fun setListener() {
        ibReturn.singleClick {
            finish()
        }
    }

    private fun initData() {
        iftTitle.text = getString(R.string.play_recorder)
        iftActionRightSub.text = getString(R.string.edit)
        iftActionRightSub.visibility = VISIBLE
        iftActionRightSub.singleClick {
            LogUtils.i("==", "${getString(R.string.edit)}click")
            iftActionRightSub.text =
                if (iftActionRightSub.text.toString() == getString(R.string.m_cancel)) getString(R.string.edit) else getString(
                    R.string.m_cancel)

            g1.visibility =
                if (iftActionRightSub.text.toString() == getString(R.string.m_cancel)) VISIBLE else GONE
            playHistory?.apply {
                map {
                    it.btnShow = (iftActionRightSub.text.toString() == getString(R.string.m_cancel))
                }
                mAdapter.takeInstance<CachedAutoRefreshAdapter<VideoBean>>().refresh(playHistory!!, null)
            }

        }


        choose_all.singleClick {
            playHistory?.apply {
                map {
                    it.isSelect = !it.isSelect!!
                }
                mAdapter.takeInstance<CachedAutoRefreshAdapter<VideoBean>>().refresh(playHistory!!, null)
            }
        }

        m_delete.singleClick {
            var tempList : ArrayList<VideoBean> = arrayListOf()
            playHistory?.apply {
                map {
                    if (it.isSelect!!) tempList.add(it)
                }
                removeAll(tempList)
                mAdapter.takeInstance<CachedAutoRefreshAdapter<VideoBean>>().refresh(playHistory!!, null)
                StoredUserSources.putPlayHistory(playHistory!!)
            }
        }

//        playHistory = StoredUserSources.getPlayHistory().apply {
//            map {
//                it.btnShow = false
//                it.isSelect = false
//            }
//        }
//        mAdapter.takeInstance<CachedAutoRefreshAdapter<VideoBean>>().refresh(playHistory!!, null)
/*
        mAdapter.takeInstance<CachedAutoRefreshAdapter<PlayRecorderBean>>()
            .refresh(listOf(PlayRecorderBean(imageUrl = "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
                videoTitle = "视频xxx啦啦啦嘻嘻嘻",
                playCount = "50",
                designationCode = "1248098a34vfewr23"),
                PlayRecorderBean(imageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg",
                    videoTitle = "视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻视频xxx啦啦啦嘻嘻嘻",
                    playCount = "4756",
                    designationCode = "1248098a34vffdsa1"),
                PlayRecorderBean(imageUrl = "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
                    videoTitle = "视频xxx啦啦啦嘻嘻嘻",
                    playCount = "5012",
                    designationCode = "1248098a34vffdsa")),null)*/
    }

    private val mAdapter by lazy {
        CachedStatusWrapper().apply {
            client = object : CachedAutoRefreshAdapter<VideoBean>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                    return CacheViewHolder(LayoutInflater.from(this@PlayRecorderActivity)
                        .inflate(R.layout.item_history_recorder, parent, false)).apply {}
                }

                override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                    holder.itemView.apply {
                        val data = get(position)
                        loadGlobleVideo(context,data.faceImageUrl,iv_video_image)

//                        Glide.with(context).load(data.faceImageUrl).into(iv_video_image)
                        tv_title.text = data.videoTitle
                        tv_play_count.text = "${data.play}次播放"
                        tv_designation.text = data.designation ?: "无番号"
                        v_choose.apply {
                            visibility = if (data.btnShow!!) VISIBLE else GONE
                            isSelected = data.isSelect!!
                            singleClick {
                                data.isSelect = !data.isSelect!!
                                notifyItemChanged(position)
                            }
                        }
                        iv_video_image.singleClick {
                            startActivityWithTransition(VideoPlayActivity.open(this@PlayRecorderActivity,data.id))
                        }
                        tv_title.singleClick {
                            startActivityWithTransition(VideoPlayActivity.open(this@PlayRecorderActivity,
                                data.id))
                        }

                    }
                }
            }
        }
    }


    companion object {
        fun open(context: Context): Intent {
            return Intent(context, PlayRecorderActivity::class.java)
        }
    }
}
