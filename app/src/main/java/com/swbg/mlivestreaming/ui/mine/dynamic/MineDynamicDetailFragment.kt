package com.swbg.mlivestreaming.ui.mine.dynamic

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.adapter.DynamicAdapter
import com.swbg.mlivestreaming.base.VisibilityFragment
import com.swbg.mlivestreaming.bean.CommonUserBean
import com.swbg.mlivestreaming.ui.mine.space.UserSpaceActivity
import kotlinx.android.synthetic.main.mine_fragment_dynamic_detail.*

class MineDynamicDetailFragment(val type:Int = TYPE_MINE_PUBLISH) : VisibilityFragment() {
    companion object {
        const val TYPE_MINE_PUBLISH = 0
        const val TYPE_MINE_REPLY = 1
        const val TYPE_MINE_COLLECT = 2
        const val TYPE_MINE_RECORD = 3
    }
    override val layoutId: Int
        get() = R.layout.mine_fragment_dynamic_detail

    override fun initView() {
        print("initView:MineDynamicDetailFragment")
    }

    override fun onVisible() {
        super.onVisible()
        Log.e(TAG,"onVisible")
    }

    override fun onInvisible() {
        super.onInvisible()
        Log.e(TAG,"onInvisible")
    }

    override fun onVisibleFirst() {
        recyclerView?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
        Log.e(TAG,"onVisibleExceptFirst")
    }
    val mAdapter by lazy {
        DynamicAdapter({},context).apply {
            setListener(object : DynamicAdapter.OnDynamicListener{
                override fun onFollowClick(position: Int, isFollow: Int?) {

                }

                override fun onLikeClick(position: Int, isLike: Int?) {

                }

                override fun onCommentClick(position: Int) {

                }

                override fun onHeaderClick(position: Int) {
                    startActivityWithTransition(UserSpaceActivity.open(activity!!,
                        get(position).user?.id))
                }

                override fun onCommentHeaderClick(user: CommonUserBean?) {
                    startActivityWithTransition(UserSpaceActivity.open(context,
                        user?.id))
                }

                override fun onAdClick(position: Int) {

                }
            })

        }
    }

}