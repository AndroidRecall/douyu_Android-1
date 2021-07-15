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
import com.mp.douyu.bean.BetGotBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.square_recycle_item_bet_detail.view.*
import kotlinx.android.synthetic.main.video_recycle_item_short_video.view.iv_avatar

/**
 * 投注详情adapter
 */
class SquareBetDetailAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<BetGotBean>() {
    private val TAG: String= javaClass.simpleName
    var betListener: OnBetListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.square_recycle_item_bet_detail, parent, false)).apply {
            itemView.apply {}
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        Log.e(TAG,"SquareCircleAdapter onBindViewHolder")
        holder.itemView.apply {
            Glide.with(context).load(BuildConfig.IMAGE_BASE_URL+get(position).user?.avatar).placeholder(R.mipmap.default_avatar).circleCrop().into(iv_avatar)
            tv_name.text= "${get(position).user?.nickname?:"名称"}"
            tv_table.text= "${get(position).user?.plan}"
            tv_rate.text= "${if (get(position).type==0)get(position).user?.football_rate else get(position).user?.basketball_rate}%"
            tv_content.text= "${get(position).content}"
            tv_game_title.text= "${get(position).information} ${get(position).time} ${get(position).team_a} vs ${get(position).team_b}"
            tv_recommend_content.text= "${get(position).recommend}"
            tv_game_type.text= "${get(position).index}"
            tv_time.text= "${get(position).update_time}"

            singleClick {
                listener.invoke(position)
            }
            tv_bet.singleClick {
                betListener?.onBet()
            }
        }
    }

    fun setListener(onBetListener: OnBetListener) {
        this.betListener = onBetListener
    }

    interface OnBetListener {
       fun onBet()
    }

}