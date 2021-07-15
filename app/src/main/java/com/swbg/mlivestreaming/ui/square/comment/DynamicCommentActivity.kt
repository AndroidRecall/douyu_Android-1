package com.swbg.mlivestreaming.ui.square.comment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.DynamicBean
import com.swbg.mlivestreaming.inTransaction
import kotlinx.android.synthetic.main.square_fragment_dymaic_comment.*

class DynamicCommentActivity:MBaseActivity() {
    var fragment:DynamicCommentFragment? =null
    override val contentViewLayoutId: Int
        get() = R.layout.activity_user_sapce

    override fun initView() {
        val data = intent.getBundleExtra(DYNAMIC_DATA).getParcelable<DynamicBean>(DYNAMIC_DATA)
        fragment = DynamicCommentFragment.newInstance(data)
        supportFragmentManager.inTransaction {
            add(R.id.container, fragment!!)
        }
//    ActivityUtils.add(supportFragmentManager,R.id.container,SpaceFragment(),"user_space")
    }
    companion object {
        const val DYNAMIC_DATA = "circle_data"
        fun open(context:Context?, bean: DynamicBean):Intent{
            val bundle = Bundle()
            bundle.putParcelable(DYNAMIC_DATA,bean)
            return Intent(context,DynamicCommentActivity::class.java).apply {
               putExtra(DYNAMIC_DATA,bundle)

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