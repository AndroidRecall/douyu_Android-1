package com.mp.douyu.ui.mine.self_info

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.mp.douyu.R
import com.mp.douyu.base.GroupedRecyclerViewAdapter
import com.mp.douyu.bean.SelfInfoBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.item_self_info.view.*
import kotlinx.android.synthetic.main.item_self_info.view.tv_title
import kotlinx.android.synthetic.main.item_self_info_header_0.view.*

class SelfInfoAdapter(context: Context, val listener: (String?) -> Unit) :
    GroupedRecyclerViewAdapter(context) {
    var selfInfoList: List<SelfInfoBean> = listOf(
//        SelfInfoBean("昵称", "", "未绑定"),
//        SelfInfoBean("年龄", "", "未绑定"),
//        SelfInfoBean("性别", "", "未绑定"),
//        SelfInfoBean("个性签名", "", "未绑定"),
//        SelfInfoBean("我的账号", "", "未绑定"),
//        SelfInfoBean("手机号", "", "未绑定"),
//        SelfInfoBean("QQ号", "", "未绑定"),
//        SelfInfoBean("微信号", "", "未绑定"),
//        SelfInfoBean("邮箱", "", "未绑定")
    )
    var selfHeader = SelfInfoBean("头像", "", "")

    override fun onBindHeaderViewHolder(var1: CacheViewHolder, var2: Int) {
        when (getHeaderViewType(var2)) {
            R.layout.item_self_info_header_0 -> {
                var1.itemView.apply {
                    cl_root_1.singleClick {
                        listener.invoke("头像")
                    }
                    Glide.with(context).load(selfHeader.value).error(R.mipmap.default_avatar)
                        .into(self_avatar)
                }
            }
        }
    }

    override fun getHeaderViewType(groupPosition: Int): Int {
        return when (groupPosition) {
            0 -> R.layout.item_self_info_header_0
            else -> 0
        }
    }

    override fun hasHeader(var1: Int): Boolean = when (var1) {
        0 -> true
        else -> false
    }

    override fun getHeaderLayout(var1: Int): Int = var1

    override fun getChildrenCount(var1: Int): Int = when (var1) {
        1 -> selfInfoList.size
        else -> 0
    }

    override fun onBindChildViewHolder(var1: CacheViewHolder, var2: Int, var3: Int) {
        when (getChildViewType(var2, var3)) {
            R.layout.item_self_info -> {
                val selfInfoBean = selfInfoList[var3]
                var1.itemView.apply {
                    tv_value.text =
                        selfInfoBean.value.takeIf { !it.isNullOrEmpty() } ?: selfInfoBean.hint
                    tv_title.text = selfInfoBean.title
                    tv_title_account.visibility =
                        if (selfInfoBean.title == "我的账号") View.VISIBLE else View.GONE
                    iv_phone_num_alarm.apply {
                        Glide.with(context).load(when (selfInfoBean.title) {
                            "手机号" -> R.mipmap.self_phone_num_alarm
                            "QQ号" -> R.mipmap.bind_qq_get_bean
                            else -> R.mipmap.bind_wechat_get_bean
                        }).into(this)
                        visibility = when (selfInfoBean.title) {
                            "手机号", "QQ号", "微信号" -> {
                                if (selfInfoBean.value.isNullOrEmpty() || selfInfoBean.value == "未绑定") {
                                    View.VISIBLE
                                } else {
                                    View.GONE
                                }
                            }
                            else -> View.GONE
                        }
                    }
                    cl_root.singleClick {
                        when (selfInfoBean.title) {
                            "手机号", "QQ号", "微信号" -> {
                                selfInfoBean.value.takeIf { it.isNullOrEmpty() }
                                    ?: return@singleClick
                            }
                        }
                        listener.invoke(selfInfoBean.title)
                    }
                }
            }
        }
    }

    override fun getChildViewType(groupPosition: Int, childPosition: Int): Int {
        return when (groupPosition) {
            1 -> R.layout.item_self_info
            else -> 0
        }
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder?, viewType: Int) {
        when (viewType) {
            R.layout.item_self_info_header_0 -> {
            }
            R.layout.item_self_info -> {
            }
        }
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false

    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 2

}
