package com.mp.douyu.view.popupwindow

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import com.mp.douyu.R
import com.mp.douyu.ui.mine.MineFragment
import kotlinx.android.synthetic.main.bottom_home_pop_view.view.*

class BottomHomePopupWindow(val context: Context,val supportFragmentManager : FragmentManager) {

    private val animatorDuration = 200L
    private lateinit var popupWindow: PopupWindow
    var itemCheckedListener: ((Int) -> Unit)? = null
    var onDismissListener: (() -> Unit)? = null
    var title: String? = null
    var cancel = "取消"
    var items: ArrayList<String> = ArrayList()


    @SuppressLint("InflateParams")
    fun create() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.bottom_home_pop_view, null).apply {
            setOnClickListener {
                runDismissAnimator {
                    popupWindow.dismiss()
                    onDismissListener?.invoke()
                }
            }
            var mFragments = listOf( MineFragment(),MineFragment(),MineFragment(),MineFragment(),MineFragment(),MineFragment())
            view_pager_bottom.apply {
                adapter = BaseFragmentPagerAdapter(supportFragmentManager,mFragments)
            }

        }
        popupWindow = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true).apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            contentView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M || Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                isClippingEnabled = false
            }
            animationStyle = R.style.FadeInAnimation
        }
    }


    fun show(anchor: View) {
        if (!::popupWindow.isInitialized) {
            create()
        }
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0)
        runStartAnimator()

    }

    private fun runStartAnimator() {
        popupWindow.contentView.apply {
            ValueAnimator.ofArgb(Color.TRANSPARENT, ContextCompat.getColor(context, R.color.colorBlur)).apply {
                addUpdateListener {
                    (background as ColorDrawable).color = it.animatedValue as Int
                }
            }.setDuration(animatorDuration).start()
            this.translationY = 400f
            ViewCompat.animate(this).translationY(0f).setDuration(animatorDuration).start()
        }
    }

    private fun runDismissAnimator(endAction: () -> Unit) {
        popupWindow.contentView.apply {
            ValueAnimator.ofArgb(ContextCompat.getColor(context, R.color.colorBlur), Color.TRANSPARENT).apply {
                addUpdateListener {
                    (background as ColorDrawable).color = it.animatedValue as Int
                }
            }.setDuration(animatorDuration).start()
            val tranY = 400f
            ViewCompat.animate(this).translationY(tranY).setDuration(animatorDuration).withEndAction(endAction).start()
        }
    }
}