package com.mp.douyu.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.bean.LiveBean
import com.mp.douyu.singleClick
import com.mp.douyu.utils.DrawableUtils
import kotlinx.android.synthetic.main.mine_recycle_item_relation.view.*
import kotlinx.android.synthetic.main.search_recycle_item_anchor_detail.view.iv_avatar
import kotlinx.android.synthetic.main.search_recycle_item_anchor_detail.view.tv_name
import kotlinx.android.synthetic.main.square_recycle_item_circle_vertical.view.tv_people_num
import kotlinx.android.synthetic.main.square_recycle_item_circle_vertical.view.tv_post_num

class SearchAnchorDetailAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<LiveBean>() {
    private val TAG: String = javaClass.simpleName
    var mListener: OnAnchorListener? = null
    override var data: MutableList<LiveBean>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        when (viewType) {
             0->{
                 return CacheViewHolder(LayoutInflater.from(context)
                     .inflate(R.layout.search_recycle_item_anchor_detail, parent, false)).apply {
                     itemView.apply {}
                 }
             }
            else ->{
                return CacheViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.search_recycle_item_anchor_detail_vertical, parent, false)).apply {
                    itemView.apply {}
                }
            }
        }

    }
    override fun getItemViewType(position: Int): Int {
        return when (get(position).isHorizontal) {
            false -> 1
            else -> 0
        }
    }
    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        Log.e(TAG, "SearchAnchorDetailAdapter onBindViewHolder")
        if (get(position).isHorizontal!!) {
            setHorizontalItemView(holder, position)
        } else setVerticalItemView(holder, position)

    }

    private fun setVerticalItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(context).load(BuildConfig.IMAGE_BASE_URL + get(position).avatar).circleCrop()
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_name.text = "${get(position).anchor?.name ?: "名称"}"
            tv_people_num.text = "${get(position).fans_count ?: "0"}"
            tv_post_num.text = "${get(position).post_count ?: "0"}"
            tv_follow_num.text = "${if (get(position).is_follow == 0) "关注" else "已关注"}"
            if (get(position).is_follow == 0) {
                DrawableUtils.setDrawable(drawable = R.mipmap.ic_post_views,textView = tv_follow_num)
            } else {
                tv_follow_num.setCompoundDrawables(null, null, null, null)
            }
            cl_join.setBackgroundResource(if (get(position).is_follow == 0) R.drawable.btn_join_circle_bg1 else R.drawable.btn_gray_c30_bg)
//            tv_join.setTextColor(if (get(position).is_join == 0) resources.getColor(R.color.colorBtnBlue)
//            else resources.getColor(R.color.colorC8LightGray))
//            cl_join.isSelected = get(position).is_join == 0
            cl_join.setOnClickListener {
                mListener?.apply {
                    onFollow(position)
                }
            }
            singleClick {
                listener.invoke(position)
                mListener?.apply {
                    onItemViewClick(position)
                }
            }
        }
    }

    private fun setHorizontalItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if ("全部" == get(position).title) {
                Glide.with(context).load(R.mipmap.icon_more).circleCrop()
                    .placeholder(R.mipmap.icon_more).into(iv_avatar)
            } else Glide.with(context)
                .load(BuildConfig.IMAGE_BASE_URL + get(position).anchor?.avatar)
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_name.text = "${get(position).anchor?.name ?: "名称"}"
            singleClick {
                if ("全部" === get(position).title) {
                    mListener?.onAnchorList()
                } else {

                    mListener?.onHeaderClick(get(position).user_id)
                }

            }


        }
    }

    fun setListener(onBetListener: OnAnchorListener) {
        this.mListener = onBetListener
    }

    interface OnAnchorListener {
        fun onAnchorList()
        fun onHeaderClick(userId: Int?)
        fun onFollow(position: Int)
        fun onItemViewClick(position: Int)
    }

}