package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class MineDynamicBean(
    var type: Int?,
    var channel: Int?,
    var pid: Int?,
    var is_recommend: Int?,
    var update_time:String?=null,
    var create_time:String?=null,
    var circle_id: Int?,
    var status: Int?,
    var play_times: Int?,
    var download_times: Int?,
    var like_count: Int?,
    var view_count: Int?,
    var is_top: Int?,
    var id: Int?,
    var title:String?=null,
    var view_content: Int?,
    var is_elite: Int?,
    var price: Int?,
    var uid: Int?,
    var rate: Int?,
    var points: Int?,
    var age: Int?,
    var gender: Int?,
    var sign_days: Int?,
    var vip: Int?,
    var invitation_code:String?=null,
    var vip_time:String?=null,
    var sign:String?=null,
    var balance:String?=null,
    var avatar:String?=null,
    var email:String?=null,
    var phone:String?=null,
    var password:String?=null,
    var nickname:String?=null,
    var username:String?=null,
    var wechat:String?=null,
    var qq:String?=null,
    var plan:String?=null,
    var clear_text:String?=null,
    var reg_ip:String?=null,
    var os:String?=null,
    var model:String?=null,
    var user_agent:String?=null,
    var token:String?=null,
    var content:String?=null,
    var likePosts:MutableList<DynamicBean> = arrayListOf(),
    var viewPosts:MutableList<DynamicBean> = arrayListOf(),
    var posts:MutableList<DynamicBean> = arrayListOf()
    ):Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
