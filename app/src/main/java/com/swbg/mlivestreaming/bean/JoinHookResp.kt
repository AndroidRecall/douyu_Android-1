package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class JoinHookResp(
    var luck_number:Int?=0
):Parcelable {

}