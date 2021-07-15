package com.swbg.mlivestreaming.ui.mine.dynamic

import android.os.Bundle
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.inTransaction
import com.swbg.mlivestreaming.ui.square.me.SquareDynamicFragment
import kotlinx.android.synthetic.main.mine_fragment_dynamic_home.*

/**
 * 动态(个人空间)
 */
class MineDynamicFragment(var uid: Int = 0) : VisibilityFragment() {


    override val layoutId: Int
        get() = R.layout.mine_fragment_dynamic_home
    companion object{
        const val EXTRA_UID = "uid"
        fun newInstance(uid: Int = 0): MineDynamicFragment {
            val fragment = MineDynamicFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_UID, uid)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        uid = arguments?.getInt(EXTRA_UID)!!
        tv_publish.setOnClickListener {
            selPublish()
        }
        tv_reply.setOnClickListener {
            selReply()
        }
        tv_collect.setOnClickListener {
            selCollect()
        }
        tv_record.setOnClickListener {
            selRecord()
        }
        selPublish()
    }

    private fun selRecord() {
        tv_publish.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_reply.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_collect.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_record.setTextColor(resources.getColor(R.color.colorBtnBlue))
        childFragmentManager.inTransaction {
            add(R.id.container, SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_SPACE_PUBLISH,uid))
        }
    }

    private fun selCollect() {
        tv_publish.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_reply.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_collect.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_record.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_SPACE_COLLECT,uid))
        }
    }

    private fun selReply() {
        tv_publish.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_reply.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_collect.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_record.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_SPACE_REPLY,uid))
        }
    }

    private fun selPublish() {
        tv_publish.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_reply.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_collect.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_record.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, SquareDynamicFragment.newInstance(SquareDynamicFragment.TYPE_SPACE_PUBLISH,uid))
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }
}