package com.mp.douyu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.bean.InviteCityBean
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.invite_recycle_item_invite.view.*

class InviteCityAdapter(val context: Context?) :
    RecyclerView.Adapter<InviteCityAdapter.ViewHolder>() {
    var listener: InviteCityListener? = null
    var data: MutableList<InviteCityBean> = arrayListOf()


    fun update(data: MutableList<InviteCityBean>) {
        this.data.clear()
        data.let {
            this.data.addAll(data)
        }
        notifyDataSetChanged()

    }

    val size: Int
        get() = data.size

    fun clear() {
        size.also {
            data.clear().run {
                notifyItemRangeRemoved(0, it)
            }
        }
    }
    fun get(position: Int):InviteCityBean {
        return data[position]
    }
    fun addAll(elements: List<InviteCityBean>) {
        size.let {
            data.addAll(elements).also { add ->
                if (add) notifyItemRangeInserted(it, elements.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteCityAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.invite_recycle_item_invite, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size ?: 0
    }

    override fun onBindViewHolder(holder: InviteCityAdapter.ViewHolder, position: Int) {
        holder.itemView.apply {
            if (data[position].image?.contains("http") == false) {
                data[position].image = BuildConfig.IMAGE_BASE_URL+data[position].image
            }
            loadGlobleVideo(context,data[position].image,iv_header)
//            Glide.with(context).load(data[position].image).placeholder(R.mipmap.login_code_failed).into(iv_header)
            tv_title.text =data[position].title
            tv_remain.text =if (data[position].rest == 0) "目标达成!" else "剩余${data[position].rest}份"
            tv_task_num.text ="需要${data[position].total}份"
            tv_time.text ="发起时间${data[position].create_time}"
            progressBar.max = get(position).total!!
            progressBar.progress = get(position).total!!-get(position).rest!!
            singleClick {
                listener?.onDetail(position)
            }
        }

    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    }

    fun setOnInviteCityListener(listener: InviteCityListener) {
        this.listener = listener
    }

    interface InviteCityListener {
        fun onDetail(position: Int)
    }
}