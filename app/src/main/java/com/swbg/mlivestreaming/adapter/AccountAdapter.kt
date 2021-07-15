package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.AccountRes
import com.swbg.mlivestreaming.bean.CommentBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.utils.HideDataUtil
import kotlinx.android.synthetic.main.activity_apply_withdraw.*
import kotlinx.android.synthetic.main.video_recycle_item_comment.view.*
import kotlinx.android.synthetic.main.withdraw_recycle_item_account.view.*

class AccountAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<AccountRes>() {
    var commentVideoListener: OnAccountListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.withdraw_recycle_item_account, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            val accountRes = get(position)
            if (accountRes.type == 1) {

                tv_account.text = "支付宝(${HideDataUtil.hidePhoneNo(accountRes.alipay_account)})"
            } else {

                tv_account.text = "银行卡(${accountRes.bank_name}${HideDataUtil.hideCardNo(accountRes.bank_card)})"
            }
            iv_select.setBackgroundResource(if (get(position).isSelect==true)R.mipmap.ic_withdraw_check else R.mipmap.ic_withdraw_uncheck)
            singleClick {
                commentVideoListener?.onSelectClick(position)
            }
        }
    }

    fun setListener(onShortVideoListener: OnAccountListener) {
        commentVideoListener = onShortVideoListener
    }

    interface OnAccountListener {
        fun onSelectClick(position: Int)
    }

}