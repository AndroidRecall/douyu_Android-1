package com.swbg.mlivestreaming.http.exception

import android.app.Activity
import android.content.DialogInterface
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.swbg.mlivestreaming.bean.TokenBean
import com.swbg.mlivestreaming.bean.UserInfoBean
import com.swbg.mlivestreaming.cacheSession
import com.swbg.mlivestreaming.http.exception.Http400Exception.ERROR_PROJECT_NOT_EXIT
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.provider.TokenProvider
import com.swbg.mlivestreaming.ui.login_register.LoginActivity
import com.swbg.mlivestreaming.utils.ToastUtils
import com.swbg.mlivestreaming.view.popupwindow.TitleAndMessageDialog

internal class ProjectDeleteExceptionHandler : IExceptionHandler {
    override fun handle(activity: Activity, t: Throwable): Boolean {
        return when (t) {
            is Http400Exception -> {
                ToastUtils.showToast(t.errorMessage, false)
                true
            }
            else -> false
        }
    }
}
