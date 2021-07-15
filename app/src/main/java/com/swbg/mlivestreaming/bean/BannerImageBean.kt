package com.swbg.mlivestreaming.bean

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.versionedparcelable.ParcelField
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class BannerImageBean (@SerializedName("cover") var imageUrl : String? = "",var url : String? = "",var id : String? = "") :
    Parcelable {

}
