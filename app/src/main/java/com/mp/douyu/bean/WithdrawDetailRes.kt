package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class WithdrawDetailRes (
    var id: Int?=0,
    var union_id: Int?=0,
    var amount: Int?=0,
    var account_id: String?=null,
    var status: Int?=0,
    var anchor_id: Int?=0,
    var bank_card:String?=null,
    var bank_name:String?=null,
    var name:String?=null,
    var alipay_account:String?=null,
    var alipay_name:String?=null,
    var comment:String?=null,
    var mi_dou:String?=null,
    var order_id: String?=null,
    var user_id: String?=null,
    var reason: String?=null,
    var create_time:String?=null,
    var account: AccountBean?=null,
    var update_time:String?=null
) : Parcelable
@Parcelize
data class AccountBean (
    var id: Int?=0,
    var uid: Int?=0,
    var anchor_id: Int?=0,
    var amount: Int?=0,
    var status: Int?=0,
    var user_id: Int?=0,
    var type: Int?=0,
    var bank_card:String?=null,
    var bank_name:String?=null,
    var name:String?=null,
    var comment:String?=null,
    var order_id:String?=null,
    var alipay_name:String?=null,
    var alipay_account:String?=null,
    var reason:String?=null,
    var create_time:String?=null,
    var update_time:String?=null
) : Parcelable