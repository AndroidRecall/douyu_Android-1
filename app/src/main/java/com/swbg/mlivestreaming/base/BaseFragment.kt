package com.swbg.mlivestreaming.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.swbg.mlivestreaming.LoadingFragment
import java.lang.IllegalStateException

/**
 * fragment基类，所有fragment须继承此类
 */

abstract class BaseFragment : Fragment() {
    val TAG: String = javaClass.simpleName

    protected var isLoading = false
    protected val loading by lazy {
        LoadingFragment()
    }
    protected abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
            ?: throw IllegalStateException("${javaClass.simpleName}: error layoutId: $layoutId not found")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    protected abstract fun initView()

    open fun onPermissionChanged() {}

    fun getActivityOrThrowable(): FragmentActivity {
        return activity
            ?: throw IllegalStateException("fragment$this was detached, activity is null")
    }

    fun startActivityWithTransition(intent: Intent) {
        startActivity(intent)
    }

    @JvmOverloads
    fun startActivityWithTransition(intent: Intent, shareElement: View, shareName: String, requestCode: Int = -1) {
        val optionsCompat =
            makeSceneTransitionAnimation(getActivityOrThrowable(), shareElement, shareName)
        startActivityForResult(intent, requestCode, optionsCompat.toBundle())
    }

    @SafeVarargs
    fun startActivityWithTransition(intent: Intent, requestCode: Int, vararg shareElements: Pair<View, String>) {
        val optionsCompat = makeSceneTransitionAnimation(getActivityOrThrowable(), *shareElements)
        startActivityForResult(intent, requestCode, optionsCompat.toBundle())
    }

    fun startActivityWithTransition(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun toString(): String {
        return "${javaClass.simpleName}, ${super.toString()}"
    }
}
