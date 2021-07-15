package com.mp.douyu.dialog

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.bean.ChooseMoneyNumBean
import com.mp.douyu.bean.InviteCityBean
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.play.FastTransferAdapter
import com.mp.douyu.utils.SpanUtils
import com.mp.douyu.utils.ToastUtils
import com.mp.douyu.utils.WindowUtils
import kotlinx.android.synthetic.main.live_dialog_join_hook.view.*

/**
 * 约炮
 */
class JoinHookDialog(context: Context, var inviteCityBean: InviteCityBean, var listener: OnDialogListener?) :
    BaseCenterPopupView(context) {
    override fun onCreate() {
        super.onCreate()
        tv_title.text = "同城约炮*第${inviteCityBean.issue}期"
        tv_remain.text = "剩余${inviteCityBean.rest}份"
        tv_task_num.text = "需要${inviteCityBean.total}份"
        tv_rule_title.text = inviteCityBean.n_title
        tv_rule_content.text = inviteCityBean.n_content
        tv_bought.text = "${inviteCityBean.n_content}"
        progressBar.max = inviteCityBean.total!!
        progressBar.progress = inviteCityBean.total!! - inviteCityBean.rest!!
        val miMoneySpan =
            SpanUtils.matcherTitle2("${BuildConfig.APP_BLANCE_NAME}余额:${StoredUserSources.getUserInfo2()?.points}",
                "${StoredUserSources.getUserInfo2()?.points}",
                context.resources.getColor(R.color.color_fffd9f5b))
        val boughtSpan =
            SpanUtils.matcherTitle2("当前已购份额:${inviteCityBean.total!! - inviteCityBean.rest!!}",
                "${inviteCityBean.total!! - inviteCityBean.rest!!}",
                context.resources.getColor(R.color.color_fffd9f5b))
        tv_mi_money.text = miMoneySpan
        tv_bought.text = boughtSpan

        iv_dismiss.singleClick {
            dismiss()
        }
        tv_confirm.singleClick {
            if (et_input.text.toString().isNotBlank()) {
                val number = et_input.text.toString().toInt()
                if (number > 0) {
                    listener?.onJoinHook(number)
                    dismiss()
                } else {
                    ToastUtils.showToast("购买份额不能小于0")
                }
            } else {
                ToastUtils.showToast("请输入份额")
            }
        }
        tv_recharge.singleClick {
            listener?.onObtain()
            dismiss()
        }
        et_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshCopies(s.toString())
            }
        })

        rv_money.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3).apply {
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val viewPosition =
                            (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
                        outRect.set(WindowUtils.dip2Px(12f), WindowUtils.dip2Px(10f), 0, 0)
                    }
                })
            }
        }

        mAdapter.refresh(listOf(ChooseMoneyNumBean(true, "50", amount = "50"),
            ChooseMoneyNumBean(false, "100", amount = "100"),
            ChooseMoneyNumBean(false, "500", amount = "500"),
            ChooseMoneyNumBean(false, "1000", amount = "1000"),
            ChooseMoneyNumBean(false, "2000", amount = "2000"),
            ChooseMoneyNumBean(false, "全部", amount = "${calculateCopies()}")), null)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.live_dialog_join_hook
    }

    val mAdapter by lazy {
        FastTransferAdapter(context).apply {
            setOnSelectListener(object : FastTransferAdapter.OnSelectListener {
                override fun onSelect(position: Int) {
                    et_input.setText("${get(position).amount}")

                }
            })
        }
    }

    private fun refreshCopies(amount: String) {
        if (amount.isNotBlank()) {
            val expend = amount.toLong() * inviteCityBean.price!!
            val expendSpan = SpanUtils.matcherTitle2("您将花费${expend}${BuildConfig.APP_BLANCE_NAME}",
                "${expend}",
                context.resources.getColor(R.color.color_fffd9f5b))
            tv_expend.text = expendSpan
            if (expend > (StoredUserSources.getUserInfo2()?.points?.toFloat() ?: 0f).toInt()) {
                tv_tip.visibility = View.VISIBLE
                val tipSpan = SpanUtils.matcherTitle2("您的${context.getString(R.string.balance)}不足${expend}",
                    "${expend}",
                    context.resources.getColor(R.color.color_fffd9f5b))
                tv_tip.text = tipSpan
            } else {
                tv_tip.visibility = View.INVISIBLE
            }
        } else {
            tv_tip.visibility = View.INVISIBLE
        }

    }

    fun calculateCopies(): Int {
        return (StoredUserSources.getUserInfo2()?.points?.toFloat()?.div(inviteCityBean.price!!)
            ?: 0f).toInt()
    }

    fun setOnDialogListener(listener: OnDialogListener) {
        this.listener = listener
    }

    interface OnDialogListener {
        fun onObtain()
        fun onJoinHook(number: Int)
    }
}