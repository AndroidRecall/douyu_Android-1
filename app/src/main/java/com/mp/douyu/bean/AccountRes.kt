package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class AccountRes (
    var id: Int?=0,
    var uid: Int?=0,
    var type: Int?=0,
    var isSelect: Boolean?=false,
    var alipay_account:String?=null,
    var alipay_name:String?=null,
    var bank_card:String?=null,
    var bank_name:String?=null,
    var name:String?=null,
    var create_time:String?=null,
    var user: CommonUserBean?=null,
    var update_time:String?=null
) : Parcelable