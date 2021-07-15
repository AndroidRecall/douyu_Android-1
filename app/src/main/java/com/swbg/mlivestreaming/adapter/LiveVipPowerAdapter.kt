package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.HookRecordBean
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.hook_recycle_item_record.view.*

class LiveVipPowerAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<String>() {
    private val TAG: String= javaClass.simpleName
    override var data: MutableList<String>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.live_recycle_item_vip_power, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
//            context?.let { it1 -> Glide.with(it1).load(get(position).user?.avatar).placeholder(R.mipmap.default_avatar).into(iv_avatar) }
//            tv_luck_num.text="幸运号码:${get(position).luck_number}"
//            tv_issue.text="第${get(position).issue}期 中奖记录"
//            tv_time.text="开奖时间:${get(position).update_time}"
//            tv_nickname.text="用户昵称:${get(position).nickname}"

            singleClick {
                listener.invoke(position)
            }
        }
    }

}