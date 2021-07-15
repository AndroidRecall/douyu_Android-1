package com.swbg.mlivestreaming.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class AvatarChooseBean(var male: List<AvatarBean>?, var female: List<AvatarBean>?) {

}

@Parcelize
class ManBean(var imageUrl: String? = "", var isSelect: Boolean? = false, var isMan: Boolean? = false, var isTitle: Boolean):
     Parcelable {}
@Parcelize
data class AvatarBean(val id: String? = "", val url: String? = "") : Parcelable