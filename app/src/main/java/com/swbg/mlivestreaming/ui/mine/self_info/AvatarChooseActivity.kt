package com.swbg.mlivestreaming.ui.mine.self_info

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.bean.ManBean
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.mine.MineViewModel
import com.swbg.mlivestreaming.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_avatar_choose.*
import kotlinx.android.synthetic.main.title_bar_confirm_btn.*

class AvatarChooseActivity : MBaseActivity() {
    val listAvatars: ArrayList<ManBean> = arrayListOf(ManBean(isTitle = true))
    var currentChooseUrl: String? = ""
    override val contentViewLayoutId: Int
        get() = R.layout.activity_avatar_choose

    private val mineViewModel by getViewModel(MineViewModel::class.java) {
        _getUserAvatar.observe(it, Observer {
            it?.let {
                it.male?.let {
                    for (el in it) {
                        listAvatars.add(ManBean(el.url,
                            isSelect = false,
                            isMan = true,
                            isTitle = false))
                    }
                }
                listAvatars.add(ManBean(isTitle = true))
                it.female?.let {
                    for (el in it) {
                        listAvatars.add(ManBean(el.url,
                            isSelect = false,
                            isMan = false,
                            isTitle = false))
                    }
                }
                mAdapter.refresh(listAvatars, null)
            }
        })
        _setUserAvatar.observe(it, Observer {
            it?.let {
                finishView()
            }
        })
    }

    override fun initView() {
        iftTitle.text = "选择头像"
        iftActionRightSub.apply {
            visibility = View.VISIBLE
            isEnabled = false
            singleClick {
                if (currentChooseUrl.isNullOrEmpty()) return@singleClick
                //confirm
                mineViewModel.editAvatar(hashMapOf("avatar" to currentChooseUrl))
            }
        }
        ibReturn.singleClick {
            onBackPressed()
        }

        mineViewModel.getUserAvatar()
        mineViewModel.getUserInfo()

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(this@AvatarChooseActivity, 5).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0 || listAvatars[position].isTitle) {
                            5
                        } else 1
                    }
                }
            }
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    if ((view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition % 5 == 0 && (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition > 0) {
                        outRect.set(20, 30, 30, 0)
                    } else {
                        outRect.set(30, 30, 0, 0)
                    }
                }
            })
        }
    }

    private val mAdapter by lazy {
        AvatarChooseAdapter(this, listAvatars) { _: View, position: Int ->
            iftActionRightSub.apply {
                isEnabled = true
                background =
                    ContextCompat.getDrawable(this@AvatarChooseActivity, R.mipmap.avatars_choose_bg)
                setTextColor(ContextCompat.getColor(this@AvatarChooseActivity, R.color.white))
            }
            currentChooseUrl = listAvatars[position].imageUrl
        }
    }

    companion object {
        fun open(context: Context): Intent {
            return Intent(context, AvatarChooseActivity::class.java)
        }
    }


}
