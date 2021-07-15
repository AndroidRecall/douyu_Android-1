package com.swbg.mlivestreaming.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.swbg.mlivestreaming.adapter.DynamicAdapter.Companion.TYPE_ADV_CONTENT
import com.swbg.mlivestreaming.adapter.DynamicAdapter.Companion.TYPE_NOR_CONTENT
import com.swbg.mlivestreaming.adapter.DynamicAdapter.Companion.TYPE_VIDEO_CONTENT
import com.swbg.mlivestreaming.interfaces.ItemViewType
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class DynamicBean(var id: Int? = 0,
    var uid: Int? = 0,
    var content: String? = null,
    var status: Int? = 0,
    var title: String? = null,
    var price: Int? = 0,
    var circle_id: Int? = 0,
    var type: Int? = 0,
    var update_time: String? = null,
    var create_time: String? = null,
    var url: String? = null,
    var is_top: Int? = 0,
    var like_count: Int? = 0,
    var view_count: Int? = 0,
    var comm_count: Int? = 0,
    var is_recommend: Int? = 0,
    var is_elite: Int? = 0,
    var is_follow: Int? = 0,
    var is_like: Int? = 0,
    var images: MutableList<ImageBean>? = null,
    var user: CommonUserBean? = null,
    var comments: List<CommentBean> = arrayListOf(),
    var pivot: PivotBean? = null,
    var advBean: AdvBean? = null) :
    ItemViewType, Parcelable {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun getItemViewType(): Int {
        if (url?.isNotBlank() == true) {
            return TYPE_VIDEO_CONTENT
        } else if (images?.isNotEmpty() == true) {
            return TYPE_NOR_CONTENT
        } else if (type == TYPE_ADV_CONTENT) {
            return TYPE_ADV_CONTENT
        }
        return TYPE_NOR_CONTENT
    }

}
