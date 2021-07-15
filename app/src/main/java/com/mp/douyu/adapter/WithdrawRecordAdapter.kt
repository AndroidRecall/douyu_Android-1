package com.mp.douyu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.R
import com.mp.douyu.bean.WithdrawRecordBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.withdraw_recycle_item_record.view.*

class WithdrawRecordAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<WithdrawRecordBean>() {
    private val TAG: String= javaClass.simpleName
    override var data: MutableList<WithdrawRecordBean>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.withdraw_recycle_item_record, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            val recordBean = get(position)

            tv_time.text = "${recordBean.create_time}"
            tv_amount.text = "提现金额: ${recordBean.amount}元"
            if (recordBean.status==0) {
                tv_state.text="审核中"
                tv_state.setTextColor(context.resources.getColor(R.color.colorTextG2))
            }else if (recordBean.status == 1) {
                tv_state.text="提现成功"
                tv_state.setTextColor(context.resources.getColor(R.color.picture_color_20c064))
            }else if (recordBean.status == 2) {
                tv_state.text="提现失败"
                tv_state.setTextColor(context.resources.getColor(R.color.color_C52122))
            }

            singleClick {
                listener.invoke(position)
            }
        }
    }

}