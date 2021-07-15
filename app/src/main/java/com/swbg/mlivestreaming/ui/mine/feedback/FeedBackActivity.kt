package com.swbg.mlivestreaming.ui.mine.feedback

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.utils.ToastUtils
import com.swbg.mlivestreaming.view.popupwindow.SingleChoicePopupWindow
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.title_bar_confirm_btn.*

class FeedBackActivity : MBaseActivity() {
    private var selectPosition = 0
    private val titleItems =
        arrayListOf("功能缺陷                                            ", "用户体验", "其他")
    override val contentViewLayoutId: Int
        get() = R.layout.activity_feedback

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _feedback.observe(it, Observer {
            it?.let {
                ToastUtils.showToast(getString(R.string.feedback_success),true)
                finishView()
            }
        })
    }

    override fun initView() {
        iftTitle.text = "意见反馈"
        ibReturn.singleClick {
            onBackPressed()
        }
        iftActionRightSub.apply {
            text = "提交"
            visibility = View.VISIBLE
            isEnabled = false
            singleClick {
                mineViewModel.feedBack(hashMapOf("type" to when (tv_choose.text.toString()) {
                    "功能缺陷" -> "1"
                    "用户体验" -> "2"
                    "其他" -> "0"
                    else -> "0"
                }, "content" to et_content.text.toString().trim()))
            }
        }

        et_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                tv_input_num.text = "${et_content.text.toString().length}/500"

                iftActionRightSub.apply {
                    if (et_content.text.toString().isNotEmpty()) {
                        isEnabled = true
                        background = ContextCompat.getDrawable(this@FeedBackActivity,
                            R.mipmap.avatars_choose_bg)
                        setTextColor(ContextCompat.getColor(this@FeedBackActivity, R.color.white))
                    } else {
                        isEnabled = false
                        background = ContextCompat.getDrawable(this@FeedBackActivity,
                            R.drawable.title_bar_confirm_bg)
                        setTextColor(ContextCompat.getColor(this@FeedBackActivity,
                            R.color.colorCharacterGrayHint))
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        tv_choose.singleClick {
            SingleChoicePopupWindow(0f).apply {
                checkedPosition = selectPosition
                itemCheckedListener = {
                    selectPosition = it
                    tv_choose.text = titleItems[selectPosition].trim()
                }
                onDismissListener = {
                    ViewCompat.animate(iv_down_arrow).rotation(0f).setDuration(200).start()
                }
                create(this@FeedBackActivity, titleItems)
                ViewCompat.animate(iv_down_arrow).withStartAction { show(tv_choose) }.rotation(180f)
                    .setDuration(200).start()
            }

        }
    }

    companion object {
        fun open(context: Context?): Intent {
            return Intent(context, FeedBackActivity::class.java)
        }
    }

}
