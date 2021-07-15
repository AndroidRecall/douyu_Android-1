package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.AnchorRuleBean
import com.swbg.mlivestreaming.bean.RankBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.utils.SpanUtils
import kotlinx.android.synthetic.main.withdraw_recycle_item_rule.view.*

class RuleAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<AnchorRuleBean>() {
    private val TAG: String= javaClass.simpleName
    override var data: MutableList<AnchorRuleBean>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.withdraw_recycle_item_rule, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            val ruleBean = get(position)
//            iv_icon.setImageResource( getLevelIcon(get(position).level!!))
            iv_icon.setImageResource( getLevelIcon("${position+1}"))
            val title =
                "${ruleBean.name}: 直播时长≥${ruleBean.time}，${ruleBean.rate}%提成比例，${ruleBean.rate}:${ruleBean.rmb_rate}兑换人民币比例；"
            var keywordColor = Color.parseColor("#000000");
            val titleSpan = SpanUtils.matcherTitle(title, "${ruleBean.name}:", keywordColor)
            tv_content.text = titleSpan

            singleClick {
                listener.invoke(position)
            }
        }
    }
    fun getLevelIcon(level:String):Int{
        return when(level){
            "1"->R.mipmap.ic_huangtong
            "2"->R.mipmap.ic_baiyin
            "3"->R.mipmap.ic_huangjin
            "4"->R.mipmap.ic_bojin
            "5"->R.mipmap.ic_zhuanshi
            "6"->R.mipmap.ic_wangzhe
            else ->R.mipmap.ic_huangtong
        }
    }
}