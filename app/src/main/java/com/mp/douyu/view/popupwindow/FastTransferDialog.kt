package com.mp.douyu.view.popupwindow

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.douyu.R
import com.mp.douyu.bean.ChooseMoneyNumBean
import com.mp.douyu.singleClick
import com.mp.douyu.ui.home.play.FastTransferAdapter
import com.mp.douyu.utils.WindowUtils
import kotlinx.android.synthetic.main.layout_dialog_fast_transfer.view.*
import java.util.*

class FastTransferDialog : DialogFragment() {
    private var onClickListener: DialogInterface.OnClickListener? = null
    var currentChooseItem: String? = "50"
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = arguments
        val balance = arguments?.getString(BALANCE)
        val title = arguments?.getString(TITLE)
        val gameBalance = arguments?.getString(GAME_BALANCE)
        return createDialog(this!!.requireActivity(), title, balance, gameBalance, onClickListener)
    }

    fun setOnClickListener(onClickListener: DialogInterface.OnClickListener?): FastTransferDialog {
        this.onClickListener = onClickListener
        return this
    }

    fun createDialog(context: Context, title: String?, balance: String?, gameBalance: String?, onClickListener: DialogInterface.OnClickListener?): AlertDialog {
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(context).inflate(R.layout.layout_dialog_fast_transfer, null)
        view.apply {
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

            mAdapter.refresh(listOf(ChooseMoneyNumBean(true, "50"),
                ChooseMoneyNumBean(false, "100"),
                ChooseMoneyNumBean(false, "500"),
                ChooseMoneyNumBean(false, "1000"),
                ChooseMoneyNumBean(false, "2000"),
                ChooseMoneyNumBean(false, "全部")), null)

            var miMoney: Double? = 0.00
            try {
                tv_mile_wallet.text = "￥${balance}"
                tv_game_wallet.text = "￥${gameBalance}"
                miMoney = balance?.toDouble()
                tv_title.text = title ?: "快速转账"
            } catch (e: Exception) {
                e.printStackTrace()
            }
            et_input_money.addTextChangedListener { it1->
               /* miMoney?.let {
                    if (it1.toString().trim().toDouble() > it) {
                        tv_alarm.visibility = View.VISIBLE
                        tv_confirm.isSelected = false
                    } else {
                        tv_alarm.visibility = View.GONE
                        tv_confirm.isSelected = true
                    }
                }*/
            }
            //确认转账
            tv_confirm.singleClick {
                //暂时不加判断钱多少逻辑
//                if (tv_confirm.isSelected) {
                    currentChooseItem = if (et_input_money.text.toString().isNotEmpty()) {
                        et_input_money.text.toString().trim()
                    } else {
                        when (mAdapter[mAdapter.currentChooseItem].content) {
                            "全部" -> {
                                miMoney.toString()
                            }
                            else -> mAdapter[mAdapter.currentChooseItem].content
                        }
                    }
                    onClickListener?.onClick(dialog, it.id)
//                }
            }
            //立即充值
            tv_immediately_charge.singleClick {
                onClickListener?.onClick(dialog, it.id)
            }
            //进入游戏
            tv_join_game.singleClick {
                onClickListener?.onClick(dialog, it.id)
            }

            v_click_close.singleClick {
                dismiss()
            }
        }
        val alertDialog =
            AlertDialog.Builder(context, R.style.Dialog_Global_TRANSPORT).setView(view).create()
        alertDialog.setOnShowListener { dialog: DialogInterface? ->
            if (dialog is AlertDialog) {
                val window = Objects.requireNonNull(dialog.window)
                val attributes = window?.attributes
                val p = Point()
                window?.windowManager?.defaultDisplay?.getSize(p)
                attributes?.width = (p.x * 0.85f).toInt()
                window?.attributes = attributes
                isCancelable = false
            }
        }
        return alertDialog
    }

    val mAdapter by lazy {
        FastTransferAdapter(context)
    }

    companion object {
        const val BALANCE = "BALANCE"
        const val TITLE = "TITLE"
        const val GAME_BALANCE = "GAME_BALANCE"
        fun newInstance(title: String? = "", balance: String? = "", gameBalance: String? = ""): FastTransferDialog {
            val dialog = FastTransferDialog()
            val arguments = Bundle()
            arguments.putString(BALANCE, balance)
            arguments.putString(TITLE, title)
            arguments.putString(GAME_BALANCE, gameBalance)
            dialog.arguments = arguments
            return dialog
        }
    }
}