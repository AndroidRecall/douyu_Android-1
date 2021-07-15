package com.mp.douyu.ui.square.comment

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import com.mp.douyu.R
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.bean.CommentBean
import com.mp.douyu.inTransaction
import kotlinx.android.synthetic.main.square_fragment_dymaic_comment.*

/**
 * 回复页面
 */
class DynamicCommentDetailActivity:MBaseActivity() {
    var fragment:DynamicCommentDetailFragment? =null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        val data = intent.getParcelableExtra<CommentBean>(DYNAMIC_DATA)

        fragment = DynamicCommentDetailFragment.newInstance(data)
        supportFragmentManager.inTransaction {
            add(R.id.container, fragment!!)
        }
//    ActivityUtils.add(supportFragmentManager,R.id.container,SpaceFragment(),"user_space")
    }
    companion object {
        const val DYNAMIC_DATA = "data"
        const val DYNAMIC_COMMENT_LIST = "list"
        fun open(context:Context?, data: CommentBean):Intent{
            return Intent(context,DynamicCommentDetailActivity::class.java).apply {
                putExtra(DYNAMIC_DATA,data)

            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        fragment?.apply {
            chat_key_board?.also {  keyboard ->
                if (keyboard.visibility==View.VISIBLE){
                keyboard.visibility=View.GONE
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}