package com.mp.douyu.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class BetGotBean(
    var id: Int? = 0,
    var type: Int? = 0,
    var content: String? =null,
    var uid: Int? = 0,
    var index: String? = null,
    var football_rate: String? = null,
    var basketball_rate: String? = null,
    var update_time: String? = null,
    var create_time: String? = null,
    var recommend: String? = null,
    var information: String? = null,
    var team_a: String? = null,
    var team_b: String? = null,
    var time: String? = null,
    var user: CommonUserBean? = null
    ):Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
