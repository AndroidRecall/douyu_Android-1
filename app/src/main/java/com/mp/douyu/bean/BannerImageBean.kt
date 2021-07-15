package com.mp.douyu.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class BannerImageBean (@SerializedName("cover") var imageUrl : String? = "",var url : String? = "",var id : String? = "") :
    Parcelable {

}
