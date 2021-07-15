package com.mp.douyu.ui.square.bet

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.mp.douyu.BuildConfig
import com.mp.douyu.R
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.inTransaction
import com.mp.douyu.singleClick
import com.mp.douyu.ui.mine.space.UserSpaceActivity
import com.mp.douyu.ui.square.SquareViewModel
import com.mp.douyu.ui.square.bet.BetDetailFragment.Companion.PLACE_FOLLOW
import com.mp.douyu.ui.square.bet.BetDetailFragment.Companion.PLACE_NOR
import kotlinx.android.synthetic.main.square_activity_bet_type.*

class BetTypeFragment(var type: Int = TYPE_FOOTBALL): VisibilityFragment() {
    companion object {
        const val TYPE_FOOTBALL = 0
        const val TYPE_BASKETBALL = 1
        const val EXTRA_TYPE = "type"

        fun newInstance(type:Int? =TYPE_FOOTBALL): BetTypeFragment {
            val fragment = BetTypeFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_TYPE,type!!)
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.square_activity_bet_type

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        type = arguments?.getInt(EXTRA_TYPE)!!
        initOnclickListener()
        selectPlan()
        squareViewModel.getAllBetGodsData(hashMapOf("type" to "${type}"))
    }

    private fun initOnclickListener() {
        tv_plan.setOnClickListener {
            selectPlan()
        }
        tv_follow_num.setOnClickListener {
            selectFollow(PLACE_FOLLOW)
        }
    }

    private fun selectFollow(place: Int?=PLACE_NOR) {
        tv_plan.setTextColor(resources.getColor(R.color.colorNotChoice))
        tv_follow_num.setTextColor(resources.getColor(R.color.colorBtnBlue))
        childFragmentManager.inTransaction {
            add(R.id.container, BetDetailFragment.newInstance(type,place))
        }
    }

    private fun selectPlan() {
        tv_plan.setTextColor(resources.getColor(R.color.colorBtnBlue))
        tv_follow_num.setTextColor(resources.getColor(R.color.colorNotChoice))
        childFragmentManager.inTransaction {
            add(R.id.container, BetDetailFragment.newInstance(type))
        }
    }


    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    private val squareViewModel by getViewModel(SquareViewModel::class.java){
        betGodsData.observe(it, Observer {
            it?.data?.let {it1->
                var isChange = false
               for (bean in it1){
                   if (bean.user != null) {
                       tv_name.text = bean.user?.nickname
                       context?.let { it2 ->
                           Glide.with(it2).load(BuildConfig.IMAGE_BASE_URL+bean.user?.avatar).placeholder(R.mipmap.default_avatar).circleCrop().into(iv_avatar)
                       }
                       tv_status.text = bean.user?.plan
                       iv_avatar.singleClick {
                           startActivityWithTransition(UserSpaceActivity.open(context,bean.uid))
                       }
                       isChange = true
                       return@let
                   }
               }
                if (!isChange) {
                    cl_user.visibility = View.GONE
                }
            }
        })
    }
}