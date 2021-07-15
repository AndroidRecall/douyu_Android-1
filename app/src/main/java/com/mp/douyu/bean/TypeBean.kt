package com.mp.douyu.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TypeBean(@SerializedName("title") var name: String? = "高清无码",
    @SerializedName("id") var cateId: String? = "",
    var specialId: String? = ""
) :
    Parcelable {

}

data class MoreTypeBean(val cate: List<TypeBean>? = listOf(), val special: List<SpecialBean>? = listOf())

@Parcelize
data class SpecialBean(val cover: String? = "", val id: Int? = 0, val summary: String? = "", val title: String? = "", val videos: String? = "") :
    Parcelable