package com.swbg.mlivestreaming.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class TokenBean(
    @SerializedName("token")
    var accessToken: String = "",
    @SerializedName("user_info")
    val userInfo: UserInfo? = null,
    var userName: String? = "",
    var userPsw: String? = "",
    var userSig: String? = "",
    var userID: Int? = 0) :
    Parcelable


@Parcelize
class UserInfo(@SerializedName("user_id") val id: String, val name: String, @SerializedName("head_image") val avatar: String, val law_firm_id: String?, val org_id: String?, @SerializedName(
    "org_name") val organizationName: String, @SerializedName("is_trial") val isTrial: String? = "0",//是否试用账号
    @SerializedName("expire_date") val expirationDate: String? = "",//过期时间
    @SerializedName("is_big_data_user") val isBigDataUser: Boolean? = false,//是否为大数据用户
    val phone: String, @SerializedName("head_color") val colorFlag: Int = 6, @SerializedName("first_login") val firstLogin: Boolean, @SerializedName(
        "is_super") val isSupper: Boolean = false, @SerializedName("is_timing_admin") val isTimingAdmin: Boolean = false) :
    Parcelable {

    fun isVisitor() = law_firm_id == "0" && org_id == "0"
}