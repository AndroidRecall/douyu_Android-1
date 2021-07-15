package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class UserInfo2Bean(
    var age: String? = "0",
    var avatar: String? = "",
    val balance: String? = "",
    val username: String? = "",
    val email: String? = "",
    var fans: Int? = 0,
    var follow: Int? = 0,
    val game_balance: String? = "",
    val token: String? = "",
    val plan: String? = "",
    var gender: Int? = 0,
    val id: Int? = 0,
    val nickname: String? = "",
    var node: Int? = 0,
    val phone: String? = "",
    val points: Int? = 0,
    val qq: String? = "",
    var sign: String? = "",
    var sign_days: Int? = 0,
    val vip: String? = "",
    var vip_time: String? = "",
    val wallet_url: String? = "",
    val wechat: String? = "",
    var follow_count:Int?=0,
    var fans_count:Int = 0,
    var circle_count:Int = 0,
    var status:Int = 0,
    var is_anchor:Int = 0
):Parcelable {

}