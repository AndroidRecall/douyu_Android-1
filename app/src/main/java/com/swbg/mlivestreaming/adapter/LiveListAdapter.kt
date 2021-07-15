package com.swbg.mlivestreaming.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.hadcn.keyboard.utils.Utils.dip2px
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.LiveBean
import com.swbg.mlivestreaming.loadGlobleVideo
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.utils.ScreenUtils
import com.swbg.mlivestreaming.utils.Utils
import kotlinx.android.synthetic.main.invite_recycle_item_invite.view.*
import kotlinx.android.synthetic.main.live_recycle_item_live.view.*
import kotlinx.android.synthetic.main.live_recycle_item_live.view.tv_title
import kotlinx.android.synthetic.main.live_recycle_item_live_header.view.*

class LiveListAdapter(private val listener: () -> Unit, var context: Context?) :
    CachedAutoRefreshAdapter<LiveBean>() {
    companion object {
        const val ITEM_VIEW_LIVE = 0
        const val ITEM_VIEW_HEADER = 1
    }

    var mListener: OnLiveListener? = null
    override var data: MutableList<LiveBean>
        get() = super.data
        set(value) {}

    override fun getItemViewType(position: Int): Int {
        return data[position].itemViewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        val view: View
        return when (viewType) {
            ITEM_VIEW_HEADER -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_live_header, parent, false)).apply {
                itemView.apply {}
            }

            else -> CacheViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.live_recycle_item_live, parent, false)).apply {
                itemView.apply {}
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getHeaderCount(): Int {
        var count: Int = 0
        for (bean in data) {
            if (bean.itemViewType == ITEM_VIEW_HEADER) {
                count++
            }
        }
        return count
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_VIEW_HEADER -> setHeaderItemView(holder)
            else -> setLiveItemView(holder, position)

        }

    }

    private fun setLiveItemView(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            if (data[position].image?.contains("http") == false) {
                data[position].image = BuildConfig.IMAGE_BASE_URL+data[position].image
            }
            loadGlobleVideo(context,data[position].image,iv_bg)
          /*  Glide.with(context).load(data[position].image)
                .placeholder(R.mipmap.login_code_failed).into(iv_bg)*/
            tv_title.text = get(position).title
            tv_name.text = get(position).anchor?.name
            tv_hot.text = "${get(position).hot?:0}"
            tv_state.text = "LIVE"
            tv_state.setBackgroundResource(R.mipmap.ic_avod_live)
            if (get(position).status == 0) {
                tv_state.text = "LIVE"
                tv_state.setBackgroundResource(R.mipmap.ic_avod_live)
            } else {
                tv_state.text = "直播间"
                tv_state.setBackgroundResource(R.mipmap.shape_avod_live_end)
            }
            setOnClickListener {
                mListener?.onLive(position)
            }
        }
    }

    private fun setHeaderItemView(holder: CacheViewHolder) {
        holder.itemView.apply {
            tv_notice.text = StoredUserSources.getSettingData()?.headlines
            recyclerLottery?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = LiveWinRecordAdapter(context)
                (adapter as LiveWinRecordAdapter).mListener =
                    object : LiveWinRecordAdapter.OnBetListener {
                        override fun onBet(position: Int) {
                            val gameType =
                                (adapter as LiveWinRecordAdapter).mData?.get(position)?.gameBean?.gameType
                            gameType?.let { mListener?.onBet(it) }
                        }
                    }
                (adapter as LiveWinRecordAdapter).mData?.let { initializeHeight(it, this) }
            }
            cv_recharge.setOnClickListener {
                mListener?.onRecharge()
            }
            cv_invite.setOnClickListener {
                mListener?.onInvite()
            }
        }


    }


    fun setOnLiveListener(listener: OnLiveListener) {
        mListener = listener
    }

    interface OnLiveListener {
        fun onBet(type: Int)
        fun onInvite()
        fun onRecharge()
        fun onLive(position: Int)
    }

    /**
     * 初始化recycler view 高度
     */
    private fun initializeHeight(list: List<Any>, recyclerView: RecyclerView) {
        val lp: ViewGroup.LayoutParams = recyclerView.layoutParams
        if (list.size > 5) {
            lp.height = Utils().dp2px(context!!, 30 * 5).toInt()
        } else {
            lp.height = Utils().dp2px(context!!, 30 * list.size).toInt()
        }
        recyclerView.layoutParams = lp
    }
}