package com.mp.douyu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.bean.MineVideoBean
import com.mp.douyu.singleClick
import com.mp.douyu.ui.mine.dynamic.MineAVDetailFragment
import kotlinx.android.synthetic.main.mine_recycle_item_comment_video.view.*

class MineAvAdapter(private val listener: (Int) -> Unit, var context: Context?, var type: Int= MineAVDetailFragment.TYPE_AV_COMMENT) :
    CachedAutoRefreshAdapter<MineVideoBean>() {
    companion object {
        const val TYPE_CONTENT = 0
        const val TYPE_FOOT = 1
    }

    private val TAG: String = javaClass.simpleName
    var mListener: OnVideoListListener? = null
    override var data: MutableList<MineVideoBean>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {

        return when (viewType) {
            TYPE_FOOT -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.hook_recycle_item_desc_foot, parent, false)).apply {
                itemView.apply {}
            }
            else -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.mine_recycle_item_comment_video, parent, false)).apply {
                itemView.apply {}
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.size > 0) data[position].itemViewType else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (get(position).itemViewType) {
            TYPE_FOOT -> setFootItemView(holder,position)
            else -> setContentItemView(holder,position)
        }
    }

    private fun setContentItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if (type !== MineAVDetailFragment.TYPE_AV_COMMENT) {
                cl_info.visibility = View.GONE
                tv_comment_num.visibility = View.INVISIBLE
                tv_like_num.visibility = View.INVISIBLE
            }
            tv_video_content.text ="原文 ${get(position).title}"
            Glide.with(context).load(BuildConfig.IMAGE_BASE_URL+get(position).cover1).error(R.drawable.drawable_video_error_bg).centerCrop().into(iv_video_bg)
            tv_time.text ="${get(position).update_time}"
            tv_like_num.text = "${get(position).like ?: 0}"
            tv_comment_num.text = "${0}"
            singleClick {
                this@MineAvAdapter.listener.invoke(position)
                mListener?.onVideoClick(position)
            }
        }
    }

    private fun setFootItemView(holder: CacheViewHolder, position: Int) {

    }

    fun setListener(listener: OnVideoListListener) {
        this.mListener = listener
    }

    interface OnVideoListListener {
        fun onVideoClick(position: Int)
    }

}