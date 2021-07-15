package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class CommonUserBean (
    var avatar:String?=null,
    var id: Int?=0,
    var channel: Int?=0,
    var pid: Int?=0,
    var is_follow: Int?=0,
    var is_fans: Int?=0,
    var rate: Int?=0,
    var basketball_rate: Int?=0,
    var football_rate: Int?=0,
    var plan: String?=null,
    var status: Int?=0,
    var play_times: Int?=0,
    var download_times: Int?=0,
    var balance: Int?=0,
    var points: Int?=0,
    var sign_days: Int?=0,
    var age: Int?=0,
    var is_anchor: Int?=0,
    var email: String?=null,
    var qq: String?=null,
    var phone: String?=null,
    var username: String?=null,
    var password: String?=null,
    var wechat: String?=null,
    var nickname: String?=null,
    var clear_text: String?=null,
    var update_time: String?=null,
    var reg_ip: String?=null,
    var os: String?=null,
    var user_agent: String?=null,
    var model: String?=null,
    var token: String?=null,
    var create_time: String?=null,
    var sign: String?=null,
    var vip_time: String?=null,
    var invitation_code: String?=null,
    var vip: Int?=0,
    var gender:Int?=0,
    var post_count: Int?=0,
    var circle_count: Int?=0,
    var follow_count: Int?=0,
    var fans_count: Int?=0
) : Parcelable