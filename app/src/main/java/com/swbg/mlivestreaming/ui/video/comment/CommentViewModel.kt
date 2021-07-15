package com.swbg.mlivestreaming.ui.video.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.swbg.mlivestreaming.base.MBaseViewModel
import com.swbg.mlivestreaming.bean.CommentBean
import com.swbg.mlivestreaming.bean.HttpDataListBean
import com.swbg.mlivestreaming.http.DisposableSubscriberAdapter
import com.swbg.mlivestreaming.http.Respository
import com.swbg.mlivestreaming.utils.ToastUtils

class CommentViewModel : MBaseViewModel() {
    private val alCommentListData = MutableLiveData<HashMap<String, String?>>()
    private val allCommentShortVideoData = MutableLiveData<HashMap<String, String?>>()

    var commentVideoData = Transformations.switchMap(alCommentListData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<CommentBean>?>()
        Respository.getShortVideoComments(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<CommentBean>?>(this) {
                override fun onNext(t: HttpDataListBean<CommentBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getCommentListData2(hashMap: HashMap<String, String?>, param: DisposableSubscriberAdapter<HttpDataListBean<CommentBean>?>) {
//        alCommentListData.value = hashMap

        Respository.getShortVideoComments(hashMap, param)
    }

    fun getCommentListData(hashMap: HashMap<String, String?>) {
        alCommentListData.value = hashMap
    }

    val _commentShortVideoResult = Transformations.switchMap(allCommentShortVideoData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.shortVideoComment(it, object : DisposableSubscriberAdapter<String?>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("评论成功，等待审核~")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast(t?.localizedMessage!!)
            }
        })
        mutableLiveData
    }

    fun commentShortVideo(hm: HashMap<String, String?>) {
        allCommentShortVideoData.value = hm

    }

    fun commentShortVideo2(hm: HashMap<String, String?>, param: DisposableSubscriberAdapter<String?>) {
        Respository.shortVideoComment(hm, param)
    }

}