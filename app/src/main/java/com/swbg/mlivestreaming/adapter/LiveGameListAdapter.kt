package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.GameBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.anchor.live.bet.GameBetDetailFragment.Companion.GAME_TYPE_CARD
import com.swbg.mlivestreaming.ui.anchor.live.bet.GameBetDetailFragment.Companion.GAME_TYPE_ELECTRONIC
import com.swbg.mlivestreaming.ui.anchor.live.bet.GameBetDetailFragment.Companion.GAME_TYPE_ESPORT
import com.swbg.mlivestreaming.ui.anchor.live.bet.GameBetDetailFragment.Companion.GAME_TYPE_LOTTERY
import com.swbg.mlivestreaming.ui.anchor.live.bet.GameBetDetailFragment.Companion.GAME_TYPE_PHYSICAL
import com.swbg.mlivestreaming.ui.anchor.live.bet.GameBetDetailFragment.Companion.GAME_TYPE_REALITY
import kotlinx.android.synthetic.main.live_recycle_item_game_card.view.*
import kotlinx.android.synthetic.main.live_recycle_item_game_electronic.view.*
import kotlinx.android.synthetic.main.live_recycle_item_game_esport.view.*
import kotlinx.android.synthetic.main.live_recycle_item_game_lottery.view.*
import kotlinx.android.synthetic.main.live_recycle_item_game_physical.view.*
import kotlinx.android.synthetic.main.live_recycle_item_game_reality.view.*

class LiveGameListAdapter(private val listener: (Int) -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<GameBean>() {
    private lateinit var mListener: OnGameListener
    private val TAG: String = javaClass.simpleName
    override var data: MutableList<GameBean>
        get() = super.data
        set(value) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return when (viewType) {
            GAME_TYPE_LOTTERY -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_game_lottery, parent, false)).apply {
                itemView.apply {}
            }
            GAME_TYPE_CARD -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_game_card, parent, false)).apply {
                itemView.apply {}
            }
            GAME_TYPE_REALITY -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_game_reality, parent, false)).apply {
                itemView.apply {}
            }
            GAME_TYPE_ESPORT -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_game_esport, parent, false)).apply {
                itemView.apply {}
            }
            GAME_TYPE_PHYSICAL -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_game_physical, parent, false)).apply {
                itemView.apply {}
            }
            GAME_TYPE_ELECTRONIC -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_game_electronic, parent, false)).apply {
                itemView.apply {}
            }
            else -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_game_lottery, parent, false)).apply {
                itemView.apply {}
            }
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            when (holder.itemViewType) {
                GAME_TYPE_LOTTERY -> setLotteryItemView(holder, position)

                GAME_TYPE_CARD -> setCardItemView(holder, position)

                GAME_TYPE_REALITY -> setRealityItemView(holder, position)
                GAME_TYPE_ESPORT -> setEsportItemView(holder, position)

                GAME_TYPE_PHYSICAL -> setPhysicalItemView(holder, position)
                GAME_TYPE_ELECTRONIC -> setElectronicItemView(holder, position)

                else -> setLotteryItemView(holder, position)
            }
            singleClick {
                listener.invoke(position)
                mListener?.onPlay(position)
            }
        }
    }

    private fun setElectronicItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            iv_egame_bg.setImageResource(get(position).iconId!!)
        }
    }

    private fun setPhysicalItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            iv_physical_bg.setImageResource(get(position).iconId!!)
        }
    }

    private fun setEsportItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            iv_esport_bg.setImageResource(get(position).iconId!!)
        }
    }

    private fun setRealityItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            iv_reality_bg.setImageResource(get(position).iconId!!)
        }
    }

    private fun setLotteryItemView(holder: CacheViewHolder, position: Int) {
            holder.itemView.apply {
                iv_lottery_bg.setImageResource(get(position).iconId!!)
            }
    }

    fun setCardItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            iv_card_bg.setImageResource(get(position).iconId!!)
        }
    }
    fun setOnGameListener(listener: OnGameListener) {
        mListener = listener
    }

    interface OnGameListener {
        fun onPlay(position: Int)
    }
}


