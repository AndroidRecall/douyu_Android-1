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
import com.mp.douyu.bean.GiftBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.live_recycle_item_gift.view.*

class LiveGiftListAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<GiftBean>() {
    private lateinit var mListener: OnGiftListener
    private val TAG: String = javaClass.simpleName
    override var data: MutableList<GiftBean>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.live_recycle_item_gift, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if (get(position).image?.contains("http") == false) {
                get(position).image = BuildConfig.IMAGE_BASE_URL + get(position).image
            }
            if (get(position).animation?.contains("http") == false) {
                get(position).animation = BuildConfig.IMAGE_BASE_URL + get(position).animation
            }
            if (get(position).isSelect) {
                context?.let { it1 ->
                    Glide.with(it1).load(get(position).animation)
                        .placeholder(R.mipmap.default_avatar).into(iv_avatar)
                }
            } else {
                context?.let { it1 ->
                    Glide.with(it1).load(get(position).image).placeholder(R.mipmap.default_avatar)
                        .into(iv_avatar)
                }
            }
            tv_price.text = "${get(position).price}"
            tv_name.text = "${get(position).title}"
            tv_label.text = "${get(position).comment}"

            fl_icon.visibility = if (get(position).isSelect) View.VISIBLE else View.INVISIBLE
            tv_label.visibility = if (get(position).isSelect) View.VISIBLE else View.INVISIBLE
//            tv_time.text="开奖时间:${get(position).update_time}"
//            tv_nickname.text="用户昵称:${get(position).nickname}"

            singleClick {
//                listener.invoke(position)
                mListener?.onSelect(position)

            }
        }
    }


    fun setOnGiftListener(listener: OnGiftListener) {
        mListener = listener
    }

    interface OnGiftListener {
        fun onSelect(position: Int)
    }
}