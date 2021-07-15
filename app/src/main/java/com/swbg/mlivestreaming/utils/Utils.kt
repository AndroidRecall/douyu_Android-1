package com.swbg.mlivestreaming.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import java.util.*

class Utils {
    /**
     * dp转px
     *
     * @param context
     * @param
     * @return
     */
    fun dp2px(context: Context, dpVal: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(),
            context.resources.displayMetrics)
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    fun sp2px(context: Context, spVal: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            spVal,
            context.resources.displayMetrics)
    }


    /**
     * 屏幕宽度
     *
     * @param context
     * @return
     */
    fun getDisplayWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 屏幕高度
     *
     * @param context
     * @return
     */
    fun getDisplayHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun drawable2Bitmap(drawable: Drawable): Bitmap? {
        return if (drawable is BitmapDrawable) {
            (drawable as BitmapDrawable).getBitmap()
        } else if (drawable is NinePatchDrawable) {
            val bitmap: Bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                if (drawable.getOpacity() !== PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight())
            drawable.draw(canvas)
            bitmap
        } else {
            null
        }
    }


    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    fun getRandColor(): String? {
        var R: String
        var G: String
        var B: String
        val random = Random()
        R = Integer.toHexString(random.nextInt(256)).toUpperCase()
        G = Integer.toHexString(random.nextInt(256)).toUpperCase()
        B = Integer.toHexString(random.nextInt(256)).toUpperCase()
        R = if (R.length == 1) "0$R" else R
        G = if (G.length == 1) "0$G" else G
        B = if (B.length == 1) "0$B" else B
        return "#$R$G$B"
    }

    fun getTimeSecond(size: Long? =0): String? {

        var time: String? = "00:00:00"

        size?.let {
            time = String.format("%02d:%02d:%02d", size / 3600, size % 3600 / 60, size % 60)
        }
       /* time = if (size < 60) {
            String.format("00:%02d", size % 60)
        } else if (size < 3600) {
            String.format("%02d:%02d", size / 60, size % 60)
        } else {
            String.format("%02d:%02d:%02d", size / 3600, size % 3600 / 60, size % 60)
        }*/
        return time

    }

    class SoftKeyBoardListener(activity: Activity) {
        private val rootView //activity的根视图
                : View
        var rootViewVisibleHeight //纪录根视图的显示高度
                = 0
        private var onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null
        private fun setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener) {
            this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener
        }

        interface OnSoftKeyBoardChangeListener {
            fun keyBoardShow(height: Int)
            fun keyBoardHide(height: Int)
        }

        fun setListener(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener) {
            setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener)
        }

        init {
            //获取activity的根视图
            rootView = activity.getWindow().getDecorView()

            //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        //获取当前根视图在屏幕上显示的大小
                        val r = Rect()
                        rootView.getWindowVisibleDisplayFrame(r)
                        val visibleHeight: Int = r.height()
                        if (rootViewVisibleHeight == 0) {
                            rootViewVisibleHeight = visibleHeight
                            return
                        }

                        //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                        if (rootViewVisibleHeight == visibleHeight) {
                            return
                        }

                        //根视图显示高度变小超过200，可以看作软键盘显示了
                        if (rootViewVisibleHeight - visibleHeight > 200) {
                            if (onSoftKeyBoardChangeListener != null) {
                                onSoftKeyBoardChangeListener!!.keyBoardShow(rootViewVisibleHeight - visibleHeight)
                            }
                            rootViewVisibleHeight = visibleHeight
                            return
                        }

                        //根视图显示高度变大超过200，可以看作软键盘隐藏了
                        if (visibleHeight - rootViewVisibleHeight > 200) {
                            if (onSoftKeyBoardChangeListener != null) {
                                onSoftKeyBoardChangeListener!!.keyBoardHide(visibleHeight - rootViewVisibleHeight)
                            }
                            rootViewVisibleHeight = visibleHeight
                            return
                        }
                    }
                })
        }
    }
}



