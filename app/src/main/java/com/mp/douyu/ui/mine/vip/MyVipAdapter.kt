package com.mp.douyu.ui.mine.vip

import android.content.Context
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.mp.douyu.R
import com.mp.douyu.base.GroupedRecyclerViewAdapter
import com.mp.douyu.bean.MyVipBean
import kotlinx.android.synthetic.main.item_header_my_vip_0.view.*
import kotlinx.android.synthetic.main.item_vip.view.*

class MyVipAdapter(val context: Context) : GroupedRecyclerViewAdapter(context) {
    var vipBeans = listOf<MyVipBean>()
    var vipBean = MyVipBean()
    var type: Int? = 0
    override fun onBindHeaderViewHolder(holder: CacheViewHolder, var2: Int) {
        when (getHeaderViewType(var2)) {
            R.layout.item_header_my_vip_0 -> {
                holder.itemView.apply {
                    Glide.with(context).load(vipBean.cover).into(iv_center)
                    tv_title.text = "${context.getString(R.string.nobility)} · ${vipBean.title}"
                    when (type) {
                        0 -> {
                            Glide.with(context).load(R.mipmap.vip_sbg_one).into(iv_vip_nick)
                            tv_title.setTextColor(ContextCompat.getColor(context,
                                R.color.vip_t_one))

                        }
                        1 -> {
                            Glide.with(context).load(R.mipmap.vip_bg_two).into(iv_vip_nick)
                            tv_title.setTextColor(ContextCompat.getColor(context,
                                R.color.vip_t_two))
                        }
                        2 -> {
                            Glide.with(context).load(R.mipmap.vip_bg_three).into(iv_vip_nick)
                            tv_title.setTextColor(ContextCompat.getColor(context,
                                R.color.vip_t_three))
                        }
                        3 -> {
                            Glide.with(context).load(R.mipmap.vip_bg_four).into(iv_vip_nick)
                            tv_title.setTextColor(ContextCompat.getColor(context,
                                R.color.vip_t_four))
                        }
                        4 -> {
                            Glide.with(context).load(R.mipmap.vip_bg_five).into(iv_vip_nick)
                            tv_title.setTextColor(ContextCompat.getColor(context,
                                R.color.vip_t_five))
                        }
                    }
                }
            }
        }
    }

    override fun getHeaderViewType(groupPosition: Int): Int {
        return when (groupPosition) {
            0 -> R.layout.item_header_my_vip_0
            else -> 0
        }
    }

    override fun hasHeader(var1: Int): Boolean = 0 == var1

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int = when (var1) {
        1 -> vipBeans.size
        else -> 0
    }

    override fun onBindChildViewHolder(holder: CacheViewHolder, groupPosition: Int, childPosition: Int) {
        holder.itemView.apply {
            when (getChildViewType(groupPosition, childPosition)) {
                R.layout.item_vip -> {
                    val myVipBean = vipBeans[childPosition]
//                    if (childPosition == 0) {
//                        Glide.with(context).load(myVipBean.mount_icon).into(iv_round)
//                    } else {
                        Glide.with(context).load(myVipBean.localImage).into(iv_round)
//                    }
                    tv_name.text = myVipBean.title
                    tv_content.text = myVipBean.mount_text
                }
            }
        }
    }

    override fun getChildViewType(groupPosition: Int, childPosition: Int): Int {
        return when (groupPosition) {
            1 -> R.layout.item_vip
            else -> 0
        }
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder?, viewType: Int) {
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false

    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 2

}
