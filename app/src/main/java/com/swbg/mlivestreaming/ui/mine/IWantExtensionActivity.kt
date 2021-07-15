package com.swbg.mlivestreaming.ui.mine

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.InviteFriendsBean
import com.swbg.mlivestreaming.matchWaterDropScreen
import com.swbg.mlivestreaming.setActivityImmersion
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.utils.SavePictureToAlbum
import com.swbg.mlivestreaming.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_i_want_extexsion.*
import kotlinx.android.synthetic.main.item_invited_p.view.*
import kotlinx.android.synthetic.main.title_bar_simple.*
import java.io.File


class IWantExtensionActivity : MBaseActivity() {
    var inviteFriend: InviteFriendsBean? = null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_i_want_extexsion

    override fun initView() {
        matchWaterDropScreen(this)
        setActivityImmersion(this)

        titleBar.setBackgroundResource(R.color.color00000000)
        ibReturn.apply {

            singleClick {
                finishView()
            }
        }
        Glide.with(this).load(R.mipmap.return_back_white).centerInside().into(ibReturn)
        tv_save_er_code.singleClick {//保持二維碼
            inviteFriend?.let {
//                SavePictureToAlbum().saveLocalUrlToAlbum(this,
//                    false,
//                    "推广二维码图片",
//                    inviteFriend!!.qrcode)
                inviteFriend!!.qrcode?.let { it1 -> SavePictureToAlbum().saveImgToLocal(this, it1) }
//                Glide.with(this).asBitmap().load(inviteFriend!!.qrcode)
//                    .into(object : SimpleTarget<Bitmap>() {
//                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//
//                        }
//                    });
            }
        }

        //copy
        tv_copy_link.singleClick {
            inviteFriend?.let {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData =
                    ClipData.newPlainText(getString(R.string.change_success), it.url)
                clipboard.setPrimaryClip(clip)
                ToastUtils.showToast(getString(R.string.copy_cuccess), true)
            }

        }

        mineViewModel.getInviteFriends()
    }

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getInviteFriends.observe(it, Observer {
            it?.let {
                inviteFriend = it
                Glide.with(this@IWantExtensionActivity).load(it.qrcode).into(iv_er_code)
                tv_invite_code.text = "邀请码${it.code}"
                it.users?.let {
                    if (it.size > 0) {
                        tv_no_low.visibility = View.GONE
                        ll_user.removeAllViews()
                        it.map {
                            val view = LayoutInflater.from(this@IWantExtensionActivity)
                                .inflate(R.layout.item_invited_p, null)
                            (view.ll_child.getChildAt(0) as TextView).text = it.username
                            (view.ll_child.getChildAt(1) as TextView).text = it.phone
                            (view.ll_child.getChildAt(2) as TextView).text = it.create_time
                            ll_user.addView(view)
                        }
                    }
                }

            }
        })
    }

    override fun finishView() {
        setResult(Activity.RESULT_OK, Intent())
        super.finishView()
    }

    companion object {
        fun open(context: Context?): Intent {
            return Intent(context, IWantExtensionActivity::class.java)
        }
    }

}
