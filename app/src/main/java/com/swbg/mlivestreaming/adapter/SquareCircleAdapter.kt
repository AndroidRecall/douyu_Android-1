package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.SquareCircleBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.utils.DrawableUtils
import kotlinx.android.synthetic.main.square_recycle_item_circle.view.tv_name
import kotlinx.android.synthetic.main.square_recycle_item_circle_vertical.view.*
import kotlinx.android.synthetic.main.video_recycle_item_short_video.view.iv_avatar

class SquareCircleAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<SquareCircleBean>() {
    private val TAG: String = javaClass.simpleName
    override var data: MutableList<SquareCircleBean>
        get() = super.data
        set(value) {}
    var onCircleListener: OnCircleListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        when (viewType) {
            0 -> return CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.square_recycle_item_circle, parent, false)).apply {
                itemView.apply {}
            }
            else -> return CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.square_recycle_item_circle_vertical, parent, false)).apply {
                itemView.apply {}
            }

        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        Log.e(TAG, "SquareCircleAdapter onBindViewHolder")
        if (get(position).isHorizontal!!) {
            setHorizontalItemView(holder, position)
        } else setVerticalItemView(holder, position)
    }

    private fun setVerticalItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(context).load(BuildConfig.IMAGE_BASE_URL + get(position).avatar).circleCrop()
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_name.text = "${get(position).title ?: "名称"}"
            tv_people_num.text = "${get(position).user_count ?: "0"}"
            tv_post_num.text = "${get(position).post_count ?: "0"}"
            tv_join.text = "${if (get(position).is_join == 0) "加入" else "已加入"}"
            tv_join.setTextColor(if (get(position).is_join == 0) resources.getColor(R.color.colorBtnBlue)
            else resources.getColor(R.color.colorC8LightGray))
            if (get(position).is_join == 0) {
                DrawableUtils.setDrawable(drawable = R.mipmap.icon_join,textView = tv_join)
            } else {
                DrawableUtils.clearDrawable(tv_join)
            }
            fl_join.isSelected = get(position).is_join == 0
            tv_join.singleClick {
                onCircleListener?.apply {
                    onJoin(position)
                }
            }
            singleClick {
                listener.invoke(position)
                onCircleListener?.apply {
                    onItemViewClick(position)
                }
            }
        }
    }

    private fun setHorizontalItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if ("全部" == get(position).title) {
                Glide.with(context).load( get(position).avatar)
                    .circleCrop().placeholder(R.mipmap.icon_more).into(iv_avatar)
            } else if (context.getString(R.string.server) == get(position).title) {
                Glide.with(context).load(BuildConfig.IMAGE_BASE_URL + get(position).avatar)
                    .circleCrop().placeholder(R.mipmap.icon_service).into(iv_avatar)
            } else {
                Glide.with(context).load(BuildConfig.IMAGE_BASE_URL + get(position).avatar)
                    .circleCrop().placeholder(R.mipmap.default_avatar).into(iv_avatar)
            }
            tv_name.text = "${get(position).title ?: "名称"}"

            singleClick {
                listener.invoke(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (get(position).isHorizontal) {
            false -> 1
            else -> 0
        }
    }

    fun setListener(onCircleListener: OnCircleListener) {
        this.onCircleListener = onCircleListener
    }

    interface OnCircleListener {
        fun onItemViewClick(position: Int)
        fun onJoin(position: Int)
    }

}