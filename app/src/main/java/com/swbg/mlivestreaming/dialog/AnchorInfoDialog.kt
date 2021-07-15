package com.swbg.mlivestreaming.dialog

import android.content.Context
import com.bumptech.glide.Glide
import com.lxj.xpopup.core.CenterPopupView
import com.swbg.mlivestreaming.BuildConfig
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.LiveBean
import com.swbg.mlivestreaming.relation.RelationManager
import com.swbg.mlivestreaming.singleClick
import kotlinx.android.synthetic.main.live_dialog_anchor_info.view.*
import kotlinx.android.synthetic.main.live_dialog_anchor_info.view.iv_avatar
import kotlinx.android.synthetic.main.live_recycle_item_audience.view.*

/**
 * 主播信息
 */
class AnchorInfoDialog(context: Context, var liveBean: LiveBean? = null, var listener: OnAnchorInfoListener? = null) :
    CenterPopupView(context) {
    override fun onCreate() {
        super.onCreate()
        liveBean?.apply {
            Glide.with(context).load(BuildConfig.IMAGE_BASE_URL + anchor?.avatar)
                .placeholder(R.mipmap.default_avatar).into(iv_avatar)
            tv_name.text = anchor?.name
            tv_fans_num.text = "粉丝:${anchor?.fans_count}"
            tv_notice_content.text = "${anchor?.comment}"
            tv_follow_num.text = if (liveBean?.isFollowLive == 0) "关注Ta" else "已关注Ta"
        }
        iv_dismiss.singleClick {
            dismiss()
        }
        tv_space.singleClick {
            //个人空间
            listener?.onHeader()
            dismiss()
        }
        tv_follow_num.singleClick {
            //关注
            if (liveBean?.isFollowLive == 0) {
                listener?.follow()
            } else listener?.unFollow()
            dismiss()
        }

    }

    override fun getImplLayoutId(): Int {
        return R.layout.live_dialog_anchor_info
    }

    interface OnAnchorInfoListener {
        fun follow()
        fun unFollow()
        fun onHeader()
    }
}