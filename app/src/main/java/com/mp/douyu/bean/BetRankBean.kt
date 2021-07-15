package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class BetRankBean(
    var day: MutableList<CommonUserBean>? = arrayListOf(),
    var week: MutableList<CommonUserBean>? = arrayListOf(),
    var month: MutableList<CommonUserBean>? = arrayListOf()
    ):Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
