package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
class AccountRecordBean (
    var id: Int?=0,
    var bank_card:String?=null,
    var bank_name:String?=null,
    var name:String?=null,
    var alipay_name:String?=null,
    var uid: Int?=0,
    var create_time:String?=null,
    var type: Int?=0,
    var alipay_account:String?=null,
    var user: CommonUserBean?=null,
    var update_time:String?=null
) : Parcelable