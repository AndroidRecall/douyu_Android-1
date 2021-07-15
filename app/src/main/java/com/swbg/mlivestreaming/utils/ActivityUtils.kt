package com.swbg.mlivestreaming.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.transition.*
import android.util.DisplayMetrics
import android.view.*
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.BaseWebViewActivity

object ActivityUtils {
    @Synchronized
    fun add(manager: FragmentManager, layoutId: Int, fragment: Fragment, tag: String?) {
        if (fragment.isAdded) {
            return
        }
        manager.beginTransaction().setCustomAnimations(R.anim.slide_right_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_right_out).add(layoutId, fragment, tag).addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    @Synchronized
    fun add(manager: FragmentManager, layoutId: Int, fragment: Fragment, tag: String?, anim1: Int, anim2: Int, anim3: Int, anim4: Int) {
        if (fragment.isAdded) {
            return
        }
        manager.beginTransaction().setCustomAnimations(anim1, anim2, anim3, anim4)
            .add(layoutId, fragment, tag).addToBackStack(tag).commitAllowingStateLoss()
    }

    @Synchronized
    fun replace(manager: FragmentManager, layoutId: Int, fragment: Fragment, shareElements: List<Pair<View?, String?>>, tag: String?) {
        if (fragment.isAdded) {
            return
        }
        val fade = Fade()
        fragment.enterTransition = fade
        fragment.exitTransition = fade
        val transaction = manager.beginTransaction()
        for (pair in shareElements) {
            transaction.addSharedElement(pair.first!!, pair.second!!)
        }
        val transition: TransitionSet = object : TransitionSet() {
            init {
                ordering = ORDERING_TOGETHER
                addTransition(ChangeBounds())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addTransition(ChangeClipBounds())
                    addTransition(ChangeTransform())
                    addTransition(ChangeImageTransform())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        addTransition(ChangeScroll())
                    }
                }
            }
        }
        fragment.sharedElementEnterTransition = transition
        fragment.sharedElementReturnTransition = transition
        transaction.replace(layoutId, fragment, tag).addToBackStack(tag).commitAllowingStateLoss()
    }

    @Synchronized
    fun replace(manager: FragmentManager, layoutId: Int, fragment: Fragment, tag: String?) {
        if (fragment.isAdded) {
            return
        }
        manager.beginTransaction().setCustomAnimations(R.anim.slide_right_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_right_out).replace(layoutId, fragment, tag).addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    @Synchronized
    fun replace(manager: FragmentManager, layoutId: Int, fragment: Fragment, tag: String?, anim1: Int, anim2: Int, anim3: Int, anim4: Int) {
        if (fragment.isAdded) {
            return
        }
        manager.beginTransaction().setCustomAnimations(anim1, anim2, anim3, anim4)
            .replace(layoutId, fragment, tag).addToBackStack(tag).commitAllowingStateLoss()
    }

    @Synchronized
    fun replaceWithOutBackStack(manager: FragmentManager, layoutId: Int, fragment: Fragment, tag: String?) {
        if (fragment.isAdded) {
            manager.beginTransaction().remove(fragment).commitAllowingStateLoss()
            return
        }
        manager.beginTransaction().setCustomAnimations(R.anim.slide_right_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_right_out).replace(layoutId, fragment, tag).commitAllowingStateLoss()
    }

    @Synchronized
    fun replaceWithOutBackStack(manager: FragmentManager, layoutId: Int, fragment: Fragment, tag: String?, anim1: Int, anim2: Int, anim3: Int, anim4: Int) {
        if (fragment.isAdded) {
            manager.beginTransaction().remove(fragment).commitAllowingStateLoss()
            return
        }
        manager.beginTransaction().setCustomAnimations(anim1, anim2, anim3, anim4)
            .replace(layoutId, fragment, tag).commitAllowingStateLoss()
    }

    @SuppressLint("PrivateApi")
    fun getStatusHeight(activity: Activity): Int {
        var statusHeight: Int
        val localRect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(localRect)
        statusHeight = localRect.top
        if (0 == statusHeight) {
            val localClass: Class<*>
            try {
                localClass = Class.forName("com.android.internal.R\$dimen")
                val localObject = localClass.newInstance()
                val i5 = localClass.getField("status_bar_height")[localObject].toString().toInt()
                statusHeight = activity.resources.getDimensionPixelSize(i5)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
        }
        return statusHeight
    }

    fun getNormalNavigationBarHeight(activity: Activity): Int {
//        if (!isNavigationBarExist(activity)) {
//            return 0;
//        }
        val navigationBar =
            getRealScreenSize(activity)[1] - getActivityWindowHeight(activity.window.decorView)
        if (navigationBar >= 0) {
            return navigationBar
        }
        val resourceId =
            activity.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            val dimensionPixelSize = activity.resources.getDimensionPixelSize(resourceId)
            if (dimensionPixelSize > 0) {
                return dimensionPixelSize
            }
        }
        return 0
    }

    fun getRealScreenSize(context: Context): IntArray {
        val size = IntArray(2)
        var widthPixels: Int
        var heightPixels: Int
        val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val d = w.defaultDisplay
        val metrics = DisplayMetrics()
        d.getMetrics(metrics)
        // since SDK_INT = 1;
        widthPixels = metrics.widthPixels
        heightPixels = metrics.heightPixels
        try {
            // used when 17 > SDK_INT >= 14; includes window decorations (statusbar bar/menu bar)
            widthPixels = Display::class.java.getMethod("getRawWidth").invoke(d) as Int
            heightPixels = Display::class.java.getMethod("getRawHeight").invoke(d) as Int
        } catch (ignored: Exception) {
        }
        try {
            // used when SDK_INT >= 17; includes window decorations (statusbar bar/menu bar)
            val realSize = Point()
            d.getRealSize(realSize)
            Display::class.java.getMethod("getRealSize", Point::class.java).invoke(d, realSize)
            widthPixels = realSize.x
            heightPixels = realSize.y
        } catch (ignored: Exception) {
        }
        size[0] = widthPixels
        size[1] = heightPixels
        return size
    }

    fun getActivityWindowHeight(scrollView: View): Int {
        val r = Rect()
        scrollView.getWindowVisibleDisplayFrame(r)
        return r.bottom
    }

    fun isNavigationBarExist(context: Context?): Boolean {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !hasMenuKey && !hasBackKey
    }

    fun jumpToWebView(url: String?, context: Context, isLocalWebView: Boolean = false, isGamePage: Boolean = false, webViewTitle: String = "") {
        url?.let {
            if (!url.startsWith("http")) {
                return
            }
            if (isLocalWebView) {
                context.startActivity(BaseWebViewActivity.open(context,
                    webViewTitle,
                    url,
                    isGamePage))
            } else {
                val uri: Uri = Uri.parse(url)
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                intent.data = uri
                context.startActivity(intent)
            }
        }
    }
}