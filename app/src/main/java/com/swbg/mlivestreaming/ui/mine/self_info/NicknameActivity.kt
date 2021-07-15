package com.swbg.mlivestreaming.ui.mine.self_info

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_nickname.*
import kotlinx.android.synthetic.main.title_bar_confirm_btn.*

class NicknameActivity : MBaseActivity() {
    override val contentViewLayoutId: Int
        get() = R.layout.activity_nickname

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _setUserAvatar.observe(it, Observer {
            it?.let {
                finishView()
            }
        })
    }

    override fun initView() {
        iftTitle.text = "修改昵称"
        iftActionRightSub.apply {
            visibility = View.VISIBLE
            singleClick {
                //confirm
                val nickName = et_input_nickname.text.toString().trim()
                if (nickName.isEmpty()) {
                    ToastUtils.showToast(getString(R.string.please_input_inck_name))
                    return@singleClick
                }
                mineViewModel.editAvatar(hashMapOf("nickname" to nickName))
            }
        }
        ibReturn.singleClick {
            onBackPressed()
        }

        et_input_nickname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                tv_input_num.text = "${et_input_nickname.text.toString().length}/10"
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    companion object {
        fun open(context: Context): Intent {
            return Intent(context, NicknameActivity::class.java)
        }
    }

}
