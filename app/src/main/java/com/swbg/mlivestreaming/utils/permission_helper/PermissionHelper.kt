package com.swbg.mlivestreaming.utils.permission_helper


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.yanzhenjie.permission.AndPermission

object PermissionHelper {


    fun hasPermission(context: Context, vararg permissions: String): Boolean {
        return AndPermission.hasPermissions(context, *permissions)
    }

    fun request(activity: FragmentActivity, callback: PermissionCallback, vararg permission: String) {
        if (hasPermission(
                activity,
                *permission)
        ) {
            callback.onSuccess()
            return
        }
        val manager = activity.supportFragmentManager
        var fragment: Fragment? = manager.findFragmentByTag("permission")
        if (fragment == null) {
            fragment =
                PermissionDelegate()
        }
        if (!fragment.isAdded) {
            val finalFragment = fragment
            manager.beginTransaction().add(fragment, "permission").runOnCommit { (finalFragment as PermissionDelegate).request(callback, *permission) }.commit()
        } else {
            (fragment as PermissionDelegate).request(callback, *permission)
        }

    }


    class PermissionDelegate : Fragment() {
        private var callback: PermissionCallback? = null
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return Space(activity)
        }

        internal fun request(callback: PermissionCallback, vararg permissions: String) {
            this.callback = callback
            if (isAdded && !isDetached) {
                AndPermission.with(this).runtime().permission(arrayOf(*permissions)).onGranted {
                    callback.onSuccess()
                }.onDenied {
                    if (AndPermission.hasAlwaysDeniedPermission(this, it)) {
                        this.activity?.let { SettingDialog(it).show() }
                        // 第一种：用默认的提示语。
//                        AndPermission.defaultSettingDialog(this, 1).show()
                    }
                }.start()
            }
        }


      /*  override fun onAttach(context: Context?) {
            super.onAttach(context)
        }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
        }

        @PermissionYes(100)
        private fun onPermissionRequestedSuccess(permissions: List<String>) {
            if (callback != null) {
                callback!!.onSuccess()
            }
        }

        @PermissionNo(100)
        fun onPermissionRequestedFailure(deniedPermissions: List<String>) {
            if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(this, 1).show()
            }

        }*/


    }


    interface PermissionCallback {
        fun onSuccess()
    }


}
