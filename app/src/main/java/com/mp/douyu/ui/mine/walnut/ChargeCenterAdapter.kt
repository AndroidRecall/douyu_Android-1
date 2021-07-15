package com.mp.douyu.ui.mine.walnut

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.iyao.recyclerviewhelper.adapter.CachedAutoRefreshAdapter
import com.mp.douyu.R
import com.mp.douyu.bean.ChargeCenterBean
import com.mp.douyu.bean.ChooseMoneyNumBean
import com.mp.douyu.bean.PayBean
import com.mp.douyu.bean.PayListBean
import com.mp.douyu.loadGlobleVideo
import com.mp.douyu.singleClick
import com.mp.douyu.utils.WindowUtils
import kotlinx.android.synthetic.main.item_charge_center.view.*
import kotlinx.android.synthetic.main.item_money_num.view.tv_content
import kotlinx.android.synthetic.main.item_pay_way.view.*

class ChargeCenterAdapter(var context: Context?, val listener: (View) -> Unit) :
    CachedAutoRefreshAdapter<ChargeCenterBean>() {
    var chooseMoneyNums: ArrayList<ChooseMoneyNumBean> =
        arrayListOf()
    var mPayWayAdapter :CachedAutoRefreshAdapter<PayBean>? = null
    var payListBean: PayListBean = PayListBean()
    var currentChooseBtn = 0
    var currentId: Int? = 0
    var currentMoney: String? = "0"
    var currentName: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_charge_center, parent, false)).apply {
        }
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.itemView.apply {
            tv_hint.text = payListBean.tutorial_text
            jz_player.setUp(payListBean.tutorial_video, "")
            Glide.with(context).load(R.mipmap.charge_video_bg).centerCrop().into(jz_player.posterImageView)

            //confirm charge
            tv_confirm_charge.singleClick {
                val etCharge = et_charge.text.toString().trim()
                if (!TextUtils.isEmpty(etCharge)) {
                    currentMoney = etCharge
                }
                currentName = et_name.text.toString().trim()
                listener.invoke(it)
            }

            rv_charge_money.apply {
                adapter = mAdapter
                layoutManager = GridLayoutManager(context, 4)
            }

            //money
            chooseMoneyNums.clear()
            payListBean.pay_list?.indexOfFirst {
                it.amount_list?.mapIndexed { index1, listBean ->
                    chooseMoneyNums.add(ChooseMoneyNumBean(index1 == 0, "¥ $listBean", listBean))
                    if (index1 == 0) {
                        currentMoney = listBean
                    }
                }
                currentId = it.id
                return@indexOfFirst true
            }
            mAdapter.refresh(chooseMoneyNums, null)


            //pay way
            mPayWayAdapter = getPayWayAdapter{ _: View, payWayPosition: Int ->
                setPayBtnSelect(payWayPosition, holder.itemView)
            }.apply {
                payListBean.pay_list?.let {
                    if (it.isNotEmpty()){
                        it.first().isSelect = true
                        refresh(it,null)
                    }
                }
            }
            rv_pay_way.apply {
                adapter = mPayWayAdapter
                layoutManager = GridLayoutManager(context,4).apply {
                    addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                            val viewPosition =
                                (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                            if ((viewPosition + 1) % 5 == 0 || viewPosition == 0) {
                                outRect.set(WindowUtils.dip2Px(17f), WindowUtils.dip2Px(10f), 0, 0)
                            }  else if ((viewPosition + 1) % 4 == 0) {
                                outRect.set(WindowUtils.dip2Px(10f), WindowUtils.dip2Px(10f), WindowUtils.dip2Px(17f), 0)
                            }
                            else{
                                outRect.set(WindowUtils.dip2Px(10f), WindowUtils.dip2Px(10f), 0, 0)
                            }
//                            if (viewPosition != 0 && viewPosition % 2 != 0) {
//                                outRect.set(0, 0, WindowUtils.dip2Px(6f), 0)
//                            }
                        }
                    })
                }
            }

       /*
            ll_pay_channel.removeAllViews()
            val btnList = arrayListOf<View>()
            payListBean.pay_list?.mapIndexed { _, payBean ->
                payBean.let {
                    val view = LayoutInflater.from(context).inflate(R.layout.item_pay_channel, null)
                    Glide.with(context).load(payBean.icon).into(view.iv_cover)
                    view.tv_charge.text = payBean.title
                    btnList.add(view.cl_root)
                    ll_pay_channel.addView(view)
                }
            }
            btnList.mapIndexed { index, view ->
                view.apply {
                    isSelected = (index == 0)
                    singleClick {
                        setPayBtnSelect(index, btnList, holder.itemView)
                    }
                }
            }
*/


        }
    }

    private fun setPayBtnSelect(index: Int,  itemView: View) {
 /*       btnList.mapIndexed { i1, view1 ->
            view1.isSelected = index == i1
        }*/

        payListBean.pay_list?.mapIndexed{_index, payBean ->
            payBean.isSelect = _index == index
        }
        payListBean.pay_list?.let {
            mPayWayAdapter?.refresh(it,null)
        }
        currentChooseBtn = index


        chooseMoneyNums.clear()
        payListBean.pay_list?.mapIndexed { i2, payBean ->
            if (i2 == index) {
                payBean.amount_list?.mapIndexed { i3, s ->
                    chooseMoneyNums.add(ChooseMoneyNumBean(i3 == 0, "¥ ${s}",s))
                    if (i3 == 0) {
                        currentMoney = chooseMoneyNums.get(0).amount
                    }
                }
            }
        }
        mAdapter.refresh(chooseMoneyNums, null)


        itemView.et_charge.setText("")
        currentId = payListBean.pay_list?.get(index)?.id ?: 0

    }

    val mAdapter by lazy {
        object : CachedAutoRefreshAdapter<ChooseMoneyNumBean>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                return CacheViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_money_num, parent, false)).apply {}
            }

            override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                holder.itemView.apply {
                    tv_content.text = get(position).content
                    tv_content.apply {
                        text = chooseMoneyNums[position].content
                        singleClick {
                            setSelect(position)
                        }
                        isSelected = chooseMoneyNums[position].isSelect
                    }
                }
            }
        }
    }

    private fun getPayWayAdapter(clickListener : (View,Int) -> Unit):CachedAutoRefreshAdapter<PayBean>{
        return object : CachedAutoRefreshAdapter<PayBean>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
                return CacheViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_pay_way, parent, false)).apply {}
            }

            override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
                holder.itemView.apply {
                    tv_charge.text = get(position).title
//                    Glide.with(context).load(get(position).icon).error(R.drawable.drawable_video_error_bg).placeholder(R.drawable.drawable_video_error_bg).into(iv_cover)
                    loadGlobleVideo(context,get(position).icon,iv_cover)
                    cl_root.isSelected = get(position).isSelect
                    singleClick {
                        clickListener.invoke(it,position)
                    }
                }
            }

        }
    }

    private fun setSelect(position: Int) {
        var selectPosition = position
        for (i in chooseMoneyNums.indices) {
            if (chooseMoneyNums[i].isSelect && selectPosition == position) {
                selectPosition = i
            }
            chooseMoneyNums[i].isSelect = (i == position)
        }
        currentMoney = chooseMoneyNums[position].amount
        mAdapter.notifyItemChanged(position)
        mAdapter.notifyItemChanged(selectPosition)
    }
}
