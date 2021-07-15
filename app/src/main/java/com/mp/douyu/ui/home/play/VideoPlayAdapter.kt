package com.mp.douyu.ui.home.play

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.iyao.recyclerviewhelper.adapter.takeInstance
import com.mp.douyu.R
import com.mp.douyu.base.GroupedRecyclerViewAdapter
import com.mp.douyu.bean.*
import com.mp.douyu.singleClick
import com.mp.douyu.utils.DrawableUtils
import com.mp.douyu.utils.DrawableUtils.Companion.DRAWABLE_END
import com.mp.douyu.utils.Utils
import kotlinx.android.synthetic.main.item_header_video_play_1.view.*
import kotlinx.android.synthetic.main.item_header_video_play_2.view.*
import kotlinx.android.synthetic.main.item_header_video_play_3.view.*
import kotlinx.android.synthetic.main.item_header_video_play_4.view.*
import kotlinx.android.synthetic.main.item_lottery.view.*
import kotlinx.android.synthetic.main.item_tag.view.*
import java.util.*

class VideoPlayAdapter(context: Context, val click: (View, Int) -> Unit) :
    GroupedRecyclerViewAdapter(context) {
    var videoPlayBean = VideoPlayBean()
    var lotteryOpen = LotteryOpenBean()
    var videoList = listOf<VideoBean>()
    var commendList = arrayListOf<CommentBean>()
    var mListener: OnCommentListener? = null
    override fun onBindHeaderViewHolder(holder: CacheViewHolder, groupPosition: Int) {
        when (getHeaderViewType(groupPosition)) {
            R.layout.item_header_video_play_0 -> {
            }
            R.layout.item_header_video_play_1 -> {
                holder.itemView.apply {
                    tv_video_name.text = videoPlayBean.title
                    tv_video_detail.text =
                        "${videoPlayBean.create_time}/${videoPlayBean.play}次播放/${videoPlayBean.designation}"
                    ll_type.removeAllViews()
                    videoPlayBean.tags?.split(",")?.mapIndexed { index, c ->
                        c.let {
                            val view = LayoutInflater.from(context).inflate(R.layout.item_tag, null)
                            view.tv_content.text = it
                            ll_type.addView(view)
                            view.tv_content.singleClick {
                                click.invoke(it,-1)
                            }
                        }
                    }
                    tv_like_num.text = "${videoPlayBean.like}"
                    iv_down_load.singleClick {
                        click.invoke(it, -1)
                    }
                    Glide.with(context).load(when (videoPlayBean.is_like) {
                        "0" -> R.mipmap.player_click_like
                        else -> R.mipmap.video_play_like_s_s
                    }).into(iv_click_like)
                    Glide.with(context).load(when (videoPlayBean.is_star) {
                        "0" -> R.mipmap.player_collection
                        else -> R.mipmap.video_play_collect_s_s
                    }).into(iv_collect)
                    iv_click_like.singleClick {
                        click.invoke(it, when (videoPlayBean.is_like) {
                            "0" -> 101
                            else -> 201
                        })
                    }
                    iv_collect.singleClick {
                        click.invoke(it, when (videoPlayBean.is_star) {
                            "0" -> 102
                            else -> 202
                        })
                    }
                    iv_buy_vip.singleClick {
                        click.invoke(it, -1)
                    }
                }
            }
            R.layout.item_header_video_play_2 -> {
                holder.itemView.apply {
                    tv_player_num_title.text = "${lotteryOpen.title} 第${lotteryOpen.number}期"
                    tv_time.text = "${Utils().getTimeSecond(lotteryOpen.countdown?.toLong())}"

                    lotteryOpen.countdown?.let {
                        if (it > 0) startCountdown(tv_time, lotteryOpen.countdown)
                    }
                    ll_num.removeAllViews()
                    lotteryOpen.result?.split(",")?.mapIndexed { index, c ->
                        c.let {
                            val view =
                                LayoutInflater.from(context).inflate(R.layout.item_lottery, null)
                            view.tv_number.apply {
                                text = it


                                val color = Random()
                                val randomColor = Color.rgb(color.nextInt(256),
                                    color.nextInt(256),
                                    color.nextInt(256))

                                val pColor = Color.parseColor("#784566")
                                val drawable = GradientDrawable();
                                drawable.setStroke(Utils().dp2px(context, 2).toInt(), randomColor);
                                drawable.cornerRadius = Utils().dp2px(context, 15)
                                background = drawable
                            }
                            ll_num.addView(view)
                        }
                    }
                    iv_click_immediately.singleClick {
                        click.invoke(it, 0)
                    }
                }
            }
            R.layout.item_header_video_play_3 -> {
                holder.itemView.let {
                    it.tv_change_batch.singleClick {
                        click.invoke(it, -1)
                    }
                    it.tv_change_batch1.singleClick {
                        click.invoke(it, -1)
                    }

//                    val recommendedBean = RecommendedBean(videoList = listOf(VideoBean("视频1",
//                        faceImageUrl = "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg"),
//                        VideoBean("视频2",
//                            faceImageUrl = "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg"),
//                        VideoBean("视频3",
//                            faceImageUrl = "https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg"),
//                        VideoBean("视频1",
//                            faceImageUrl = "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg")))
                    mAdapter.takeInstance<CachedAutoRefreshAdapter<VideoBean>>()
                        .refresh(videoList, null)

                }
            }
            R.layout.item_header_video_play_4 -> {
                holder.itemView.let {
                    it.rv_comment.apply {
                        adapter = mCommentAdapter
                        layoutManager = LinearLayoutManager(context)
                    }
                    var order = 0
                    mCommentAdapter.takeInstance<CachedAutoRefreshAdapter<CommentBean>>()
                        .refresh(commendList, null)
                    it.tv_click_time_sort.singleClick {
                        //时间顺序
                        if (order == 0) {
                            order = 1
                            commendList.reverse()
                            mCommentAdapter.takeInstance<CachedAutoRefreshAdapter<CommentBean>>()
                                .refresh(commendList, null)
                            it.tv_click_time_sort.text="按时间降序"
                            DrawableUtils.setDrawable(drawable = R.mipmap.ic_movie_sort_desc,place = DRAWABLE_END,textView = it.tv_click_time_sort)
                            //
                        } else {
                            order = 0
                            commendList.reverse()
                            mCommentAdapter.takeInstance<CachedAutoRefreshAdapter<CommentBean>>()
                                .refresh(commendList, null)
                            it.tv_click_time_sort.text="按时间升序"
                            DrawableUtils.setDrawable(drawable = R.mipmap.player_sort,place = DRAWABLE_END,textView = it.tv_click_time_sort)
                        }
                    }
                }
            }
        }
    }

    var tv: AppCompatTextView? = null
    private fun startCountdown(tv: AppCompatTextView?, countdown: Int? = 0) {
        this.tv = tv
        tv?.apply {
            countdown?.apply {
                val timer = object : CountDownTimer((this * 1000).toLong(), 1000) {
                    override fun onFinish() {
                        tv.text = "00:00:00"
                        click.invoke(tv, 2002)
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onTick(millisUntilFinished: Long) {
                        text = "${Utils().getTimeSecond(millisUntilFinished / 1000)}"
                    }
                }
                timer.start()
            }
        }
    }


    private val mAdapter by lazy {
        VideoPlayerRecommendAdapter({ view: View, id: Int ->
            click.invoke(view, id)
        }, context)
    }

    private val mCommentAdapter by lazy {
        CommentAdapter({ view: View, position: Int ->
        }, context).apply {
            this.setListener(object : OnCommentListener {
                override fun onCommentClick(commentBean: CommentBean) {
                    this@VideoPlayAdapter.mListener?.onCommentClick(commentBean)
                }

                override fun onCommentHeaderClick(user: CommonUserBean?) {
                    this@VideoPlayAdapter.mListener?.onCommentHeaderClick(user)
                }
            })
        }
    }


    override fun getHeaderViewType(groupPosition: Int): Int = when (groupPosition) {
        0 -> R.layout.item_header_video_play_0
        1 -> R.layout.item_header_video_play_1
        2 -> R.layout.item_header_video_play_2
        3 -> R.layout.item_header_video_play_3
        4 -> R.layout.item_header_video_play_4
        else -> R.layout.item_header_video_play_0
    }

    override fun hasHeader(var1: Int): Boolean = true

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int {
        return 0
    }

    override fun onBindChildViewHolder(var1: CacheViewHolder?, var2: Int, var3: Int) {
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder?, viewType: Int) {
        when (viewType) {
            R.layout.item_header_video_play_0 -> {
                viewHolder.apply {

                }
            }
            R.layout.item_header_video_play_1 -> {
            }
            R.layout.item_header_video_play_2 -> {
            }
            R.layout.item_header_video_play_3 -> {
                viewHolder?.itemView?.let {
                    it.rv_recommend.apply {
                        adapter = mAdapter
                        layoutManager = GridLayoutManager(context, 3).apply {
                            addItemDecoration(object : RecyclerView.ItemDecoration() {

                                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                                    val mPosition =
                                        (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                                    if (mPosition > 0 && (mPosition + 1) % 3 == 0) {
                                        outRect.set(0, 0, Utils().dp2px(context, 6).toInt(), 0)
                                    }
                                }
                            })
                        }
                    }
                }
            }
            R.layout.item_header_video_play_4 -> {
            }
        }
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false

    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {}

    override fun getGroupCount(): Int {
        return 5
    }

    fun setListener(listener: OnCommentListener) {
        mListener = listener
    }

    interface OnCommentListener {
        fun onCommentClick(commentBean: CommentBean)
        fun onCommentHeaderClick(user: CommonUserBean?)
    }
}
