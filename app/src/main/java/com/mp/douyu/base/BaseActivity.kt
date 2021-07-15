package com.mp.douyu.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import com.mp.douyu.LoadingFragment
import com.mp.douyu.R
import com.mp.douyu.closeInputMethod
import com.mp.douyu.utils.ActivityUtils


/**
 * activity基类，所有activity会直接或间接继承此类
 */

abstract class BaseActivity : AppCompatActivity() {

    protected var activityOpenEnterAnimation: Int = 0
    protected var activityOpenExitAnimation: Int = 0
    protected var activityCloseEnterAnimation: Int = 0
    protected var activityCloseExitAnimation: Int = 0
    protected val loading by lazy {
        LoadingFragment()
    }
    protected var isLoading = false
    protected abstract val contentViewLayoutId: Int

    protected val statusBarHeight: Int
        get() = ActivityUtils.getStatusHeight(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupStatusBar()
        initAnimation()
        overridePendingTransition(activityOpenEnterAnimation, activityOpenExitAnimation)
        setContentView(contentViewLayoutId)
        supportFragmentManager.beginTransaction().add(android.R.id.content, loading).hide(loading)
            .commitAllowingStateLoss()
        initView()
        onPermissionChanged()
    }

    @Synchronized
    override fun onResume() {
        super.onResume()
        if (isLoading) {
            loading.show()
        }
    }

    override fun onPause() {
        super.onPause()
        loading.dismiss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        window.decorView.closeInputMethod()
    }

    override fun finish() {
        window.decorView.closeInputMethod()
        super.finish()
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation)
    }

    fun startActivityWithTransition(target: Intent) {
        startActivity(target)
    }

    fun startActivityWithTransition(target: Intent, newTask: Boolean? = true) {
        startActivity(target.apply {
            newTask.takeIf { it != true} ?:  addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    fun startActivityWithTransition(target: Intent, requestCode: Int) {
        startActivityForResult(target, requestCode)
    }

    fun startActivityWithTransition(intent: Intent, shareElement: View, shareName: String) {
        startActivityWithTransition(intent, shareElement, shareName, -1)
    }

    @SuppressLint("RestrictedApi")
    fun startActivityWithTransition(intent: Intent, shareElement: View, shareName: String, requestCode: Int) {
        val optionsCompat = makeSceneTransitionAnimation(this, shareElement, shareName)
        startActivityForResult(intent, requestCode, optionsCompat.toBundle())
    }

    @SuppressLint("RestrictedApi")
    @SafeVarargs
    fun startActivityWithTransition(intent: Intent, requestCode: Int, vararg shareElements: Pair<View, String>) {
        val optionsCompat = makeSceneTransitionAnimation(this, *shareElements)
        startActivityForResult(intent, requestCode, optionsCompat.toBundle())
    }

    protected abstract fun initView()

    protected open fun setupStatusBar() {
        setStatusBarDarkMode(false, this)
    }

    open fun onPermissionChanged() {}

    //设置小米状态栏字体颜色，#darkMode是否为深色
    @SuppressLint("PrivateApi")
    private fun setStatusBarDarkMode(darkmode: Boolean, activity: Activity) {
        val clazz = activity.window.javaClass
        try {
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType)
            extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
        } catch (e: Exception) {
        }

        try {//设置导航栏颜色为主题色
            activity.window.navigationBarColor =
                ContextCompat.getColor(activity, R.color.colorWindowBackground)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        } catch (e: Exception) {
        }
    }

    //Activity启动动画
    @SuppressLint("ResourceType")
    protected open fun initAnimation() {
        var activityStyle =
            theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowAnimationStyle))
        val windowAnimationStyleResId = activityStyle.getResourceId(0, 0)
        activityStyle = theme.obtainStyledAttributes(windowAnimationStyleResId,
            intArrayOf(android.R.attr.activityOpenEnterAnimation,
                android.R.attr.activityOpenExitAnimation,
                android.R.attr.activityCloseEnterAnimation,
                android.R.attr.activityCloseExitAnimation))
        activityOpenEnterAnimation = activityStyle.getResourceId(0, 0)
        activityOpenExitAnimation = activityStyle.getResourceId(1, 0)
        activityCloseEnterAnimation = activityStyle.getResourceId(2, 0)
        activityCloseExitAnimation = activityStyle.getResourceId(3, 0)
        activityStyle.recycle()
    }
}
