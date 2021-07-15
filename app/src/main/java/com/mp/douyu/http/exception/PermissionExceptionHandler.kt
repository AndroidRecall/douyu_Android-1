package com.mp.douyu.http.exception

import android.app.Activity
import android.content.DialogInterface
import androidx.fragment.app.FragmentActivity
import com.mp.douyu.bean.TokenBean
import com.mp.douyu.bean.UserInfoBean
import com.mp.douyu.cacheSession
import com.mp.douyu.http.exception.Http401Exception.*
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.provider.TokenProvider
import com.mp.douyu.ui.login_register.LoginActivity
import com.mp.douyu.view.popupwindow.TitleAndMessageDialog
import org.json.JSONException
import org.json.JSONObject

internal class PermissionExceptionHandler : IExceptionHandler {
    override fun handle(activity: Activity, t: Throwable): Boolean {
        if (t !is Http401Exception) {
            return false
        }

        try {
            TitleAndMessageDialog.newInstance("", t.errorMessage ?: "您的账号异地登录", "确认")
                .setOnClickListener(DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        val tokenProvider = TokenProvider.get()
                        TokenBean("",
                            userName = tokenProvider.clientName,
                            userPsw = tokenProvider.clientPsw).cacheSession()
                        StoredUserSources.putUserInfo(UserInfoBean())
                        activity.startActivity(LoginActivity.openClearTask(activity))
                        activity.finish()
                    }
                }).show((activity as FragmentActivity).supportFragmentManager, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when (t.code) {
            ERROR_CLIENT -> {
            }
        }
        return true
    }


    private fun getJsonObjectStatus(responseBody: String?): ArrayList<String> {

        var userId = "-1"
        var bookStatus = "-1"
        try {
            responseBody?.let {
                val jsonObject = JSONObject(it)
                if (jsonObject.has("info")) {
                    val jsonObjectInfo = jsonObject.getJSONObject("info")
                    if (jsonObjectInfo.has("user_id")) {
                        userId = jsonObjectInfo.getString("user_id")
                    }
                    if (jsonObjectInfo.has("book_status")) { //0未预约 1已预约 2已联系 3不显示
                        bookStatus = jsonObjectInfo.getString("book_status")
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return arrayListOf(bookStatus, userId)
    }

}
