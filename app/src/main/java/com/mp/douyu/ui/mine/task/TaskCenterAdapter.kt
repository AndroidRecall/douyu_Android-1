package com.mp.douyu.ui.mine.task

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.iyao.recyclerviewhelper.adapter.CacheViewHolder
import com.mp.douyu.R
import com.mp.douyu.base.GroupedRecyclerViewAdapter
import com.mp.douyu.bean.TaskCenterBean
import com.mp.douyu.bean.WatchDownTimeBean
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.item_new_task.view.*
import kotlinx.android.synthetic.main.item_task_c_0.view.*
import kotlinx.android.synthetic.main.item_task_c_1.view.*
import kotlinx.android.synthetic.main.item_task_c_2.view.*
import kotlinx.android.synthetic.main.item_task_c_3.view.*
import kotlinx.android.synthetic.main.item_task_c_4.view.*
import kotlinx.android.synthetic.main.item_task_days.view.*
import kotlinx.android.synthetic.main.item_watch_task.view.*

class TaskCenterAdapter(context: Context, val listener: (View, String, Int) -> Unit) :
    GroupedRecyclerViewAdapter(context) {

    var taskCenterBean: TaskCenterBean = TaskCenterBean()

    override fun onBindHeaderViewHolder(hold: CacheViewHolder, var2: Int) {
        when (getHeaderViewType(var2)) {
            R.layout.item_task_c_0 -> {
                hold.itemView.apply {
                    tv_mi_bean.text = taskCenterBean.user?.points.toString()
                    tv_mi_day_n.text = taskCenterBean.user?.sign_days.toString()
                    ll_day.removeAllViews()
                    taskCenterBean.sign_reward?.mapIndexed { index, c ->
                        c.let {
                            val view =
                                LayoutInflater.from(context).inflate(R.layout.item_task_days, null)
                            view.tv_today.text = "${index + 1}天"
                            if ((index + 1) > taskCenterBean.user?.sign_days!!) {
                                view.tv_days.text = "+${it}"
                            } else {
                                view.tv_days.setBackgroundResource(R.mipmap.task_c_choose_dsy)
                                view.tv_days.text = " "
                            }
                            ll_day.addView(view)
                        }
                    }
                    tv_click_sign.apply {
                        isSelected = taskCenterBean.user?.sign_today ?: true
                        text = if (taskCenterBean.user?.sign_today != false) "已签到" else "签到"
                        singleClick {
                            if (!taskCenterBean.user?.sign_today!!) listener.invoke(it, "签到", 0)
                        }
                    }


                    val watchTime = arrayListOf<WatchDownTimeBean>()
                    val downTime = arrayListOf<WatchDownTimeBean>()
                    taskCenterBean.limit?.mapIndexed { index, limitBean ->
                        watchTime.add(WatchDownTimeBean("${limitBean.play_times}",
                            limitBean.title,
                            limitBean.id))
                        downTime.add(WatchDownTimeBean("${limitBean.download_times}",
                            limitBean.title,
                            limitBean.id))
                    }
                    //满足4条记录
                    for (i in watchTime.size..3) {
                        watchTime.add(WatchDownTimeBean(" ", " ", i + 1))
                    }
                    for (i in downTime.size..3) {
                        downTime.add(WatchDownTimeBean(" ", " ", i + 1))
                    }

                    ll_watching_num.removeAllViews()
                    watchTime.map {
                        val view =
                            LayoutInflater.from(context).inflate(R.layout.item_watch_task, null)
                        view.tv_name.text = when (it.id) {
//                            4 -> "蓝光"
                            else -> "${it.title}"
                        }
                        view.tv_value.text = it.time
                        ll_watching_num.addView(view)
                    }

                    ll_play_num.removeAllViews()
                    downTime.map {
                        val view =
                            LayoutInflater.from(context).inflate(R.layout.item_watch_task, null)
                        view.tv_name.text = when (it.id) {
//                            4 -> "蓝光"
                            else -> "${it.title}"
                        }
                        view.tv_value.text = it.time
                        ll_play_num.addView(view)
                    }
                }

            }
            R.layout.item_task_c_1 -> {
                hold.itemView.apply {
                    ll_new_task.removeAllViews()
                    taskCenterBean.newcomer_task?.mapIndexed { index, it ->
                        val view =
                            LayoutInflater.from(context).inflate(R.layout.item_new_task, null)
                        view.tv_add.text = when (index) {
                            0 -> {
                                "+${it.points}${context?.getString(R.string.balance)}"
                            }
                            1 -> {
                                "+${it.points}${context?.getString(R.string.balance)}"
                            }
                            else -> {
                                "1:${it.points}送${context?.getString(R.string.balance)}"
                            }
                        }

                        view.tv2.text = when (index) {
                            0 -> "首次直播间送礼"
                            1 -> "首次关注直播"
                            else -> "首充,每充值1元"
                        }
                        view.tv_add_bean.text = "+${it.points}"
                        view.iv_5.apply {
                            setBackgroundResource(when (index) {
                                0 -> {
                                    if (it.is_complete == 0) R.mipmap.task_c_item_btn else R.mipmap.already_get_bg
                                }
                                1 -> {
                                    if (it.is_complete == 0)  R.mipmap.task_c_item_c_bg else R.mipmap.already_get_bg
                                }
                                else -> {
                                    if (it.is_complete == 0)  R.mipmap.task_c_item_btn else R.mipmap.already_get_bg
                                }
                            })
                            text = when (index) {
                                0 -> if (it.is_complete == 0) "去送礼" else "已领取"
                                1 -> if (it.is_complete == 0)  "去关注" else "已领取"
                                else -> if (it.is_complete == 0) "去充值" else "已领取"
                            }
                            singleClick {
                                listener.invoke(it, text.toString(), index)
                            }
                        }
                        Glide.with(context).load(when (index) {
                            0 -> R.mipmap.task_c_gift
                            1 -> R.mipmap.task_c_collect
                            else -> R.mipmap.task_c_money
                        }).centerInside().into(view.iv_1)
                        ll_new_task.addView(view)
                    }
                }
            }
            R.layout.item_task_c_2 -> {
                hold.itemView.apply {
                    ll_day_task.removeAllViews()
                    taskCenterBean.daily_task?.mapIndexed { index, it ->
                        val view =
                            LayoutInflater.from(context).inflate(R.layout.item_new_task, null)
                        view.tv_add.text = when (it.id) {
                            1 -> {
                                "1:${it.points}送眯豆"
                            }
                            2 -> {
                                ""
                            }
                            else -> {
                                ""
                            }
                        }
                        view.tv2.text = when (it.id) {
                            1 -> {
                                "每充值1元"
                            }
                            2 -> {
                                "首次绑定QQ"
                            }
                            else -> {
                                "首次绑定微信"
                            }
                        }

                        view.tv_add_bean.text = "+${it.points}"
                        val it1 = it
                        view.iv_5.singleClick {
                            listener.invoke(it,if (it1.is_complete == 1) "-1" else when (it1.id) {
                                1 -> "去充值"
                                2 -> "QQ"
                                else -> "微信"
                            }, index)
                        }
                        view.iv_5.apply {
                            text = when (it.id) {
                                1 -> {
                                    "去充值"
                                }
                                else -> {
                                    if (it.is_complete == 1) "已领取" else "去绑定"
                                }
                            }
                            setBackgroundResource(if (it.is_complete == 0) R.mipmap.task_c_item_btn else R.mipmap.already_get_bg)
                        }
                        Glide.with(context).load(R.mipmap.task_c_money).centerInside()
                            .into(view.iv_1)
                        ll_day_task.addView(view)
                    }
                }
            }
            R.layout.item_task_c_3 -> {
                hold.itemView.apply {
                    ll_extension_task.removeAllViews()
                    taskCenterBean.promote_task?.mapIndexed { index, it ->
                        val view =
                            LayoutInflater.from(context).inflate(R.layout.item_new_task, null)
                        view.tv_add.text = "每日观影次数+${it.play}，缓存次数+${it.download}"
                        view.tv2.text = "推广${it.user}人"
                        view.tv_add_bean.visibility = View.GONE
                        view.tv5.visibility = View.GONE
                        view.iv_5.singleClick {
                            listener.invoke(it, view.iv_5.text.toString(), index)
                        }
                        view.iv_5.apply {
                            text = "去推广"
                            setBackgroundResource(R.mipmap.task_c_item_btn)
                        }
                        Glide.with(context).load(R.mipmap.task_c_broadcest).centerInside()
                            .into(view.iv_1)
                        ll_extension_task.addView(view)
                    }
                }
            }
            R.layout.item_task_c_4 -> {
                hold.itemView.apply {
                    tv_content.text = taskCenterBean.text
                }

            }
        }
    }

    override fun getHeaderViewType(groupPosition: Int): Int {
        return when (groupPosition) {
            0 -> R.layout.item_task_c_0
            1 -> R.layout.item_task_c_1
            2 -> R.layout.item_task_c_2
            3 -> R.layout.item_task_c_3
            4 -> R.layout.item_task_c_4
            else -> R.layout.item_task_c_4
        }
    }

    override fun hasHeader(var1: Int): Boolean = true

    override fun getHeaderLayout(var1: Int): Int = var1
    override fun getChildrenCount(var1: Int): Int = 0

    override fun onBindChildViewHolder(var1: CacheViewHolder?, var2: Int, var3: Int) {
    }

    override fun onViewHolderCreated(viewHolder: CacheViewHolder?, viewType: Int) {
        when (viewType) {
            R.layout.item_task_c_0 -> {
            }
            R.layout.item_task_c_1 -> {
            }
            R.layout.item_task_c_2 -> {
            }
            R.layout.item_task_c_3 -> {
            }
            R.layout.item_task_c_4 -> {
            }
        }
    }

    override fun getFooterLayout(var1: Int): Int = var1

    override fun hasFooter(var1: Int): Boolean = false
    override fun getChildLayout(var1: Int): Int = var1

    override fun onBindFooterViewHolder(var1: CacheViewHolder?, var2: Int) {
    }

    override fun getGroupCount(): Int = 5
}
