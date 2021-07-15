package com.mp.douyu.ui.square.circle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.DynamicBean
import com.mp.douyu.bean.CommentBean
import com.mp.douyu.bean.HttpDataListBean
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.Respository
import com.mp.douyu.utils.ToastUtils

class DynamicViewModel : MBaseViewModel() {
    val TAG: String? = javaClass.simpleName
    private val allRecommendPostsData = MutableLiveData<HashMap<String, String?>>()
    private val allLikeDynamicData = MutableLiveData<HashMap<String, String?>>()
    private val allCommentDynamicData = MutableLiveData<HashMap<String, String?>>()
    private val allPostDynamicData = MutableLiveData<HashMap<String, String?>>()
    private val allUserDynamicsData = MutableLiveData<HashMap<String, String?>>()
    private val allFollowDynamicData = MutableLiveData<HashMap<String, String?>>()
    private val allReplyPostData = MutableLiveData<HashMap<String, String?>>()
    private val allRecordPostData = MutableLiveData<HashMap<String, String?>>()

    private val allRecommendCommentData = MutableLiveData<HashMap<String, String?>>()

    private val allCommentListData = MutableLiveData<HashMap<String, String?>>()

    private val allFollowData = MutableLiveData<HashMap<String, String?>>()
    private val allCancelFollowData = MutableLiveData<HashMap<String, String?>>()
    private val allLikePostsData = MutableLiveData<HashMap<String, String?>>()
    private val allUnLikePostsData = MutableLiveData<HashMap<String, String?>>()
    private val allCommentData = MutableLiveData<HashMap<String, String?>>()
    private val allSearchPostData = MutableLiveData<HashMap<String, String?>>()
    private val allPostLikesData = MutableLiveData<HashMap<String, String?>>()

    val recommendPostsData = Transformations.switchMap(allRecommendPostsData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getRecommendPosts(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast(t!!.localizedMessage)
                }
            })
        mutableLiveData
    }

    fun getAllRecommendPostsData(hashMap: HashMap<String, String?>) {
        allRecommendPostsData.value = hashMap
    }

    val likeDynamicData = Transformations.switchMap(allLikeDynamicData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getLikeDynamics(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast(t!!.localizedMessage)
                }
            })
        mutableLiveData
    }

    fun getAllLikeDynamicData(hashMap: HashMap<String, String?>) {
        allLikeDynamicData.value = hashMap
    }


    val commentDynamicData = Transformations.switchMap(allCommentDynamicData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getCommentDynamics(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast(t!!.localizedMessage)
                }
            })
        mutableLiveData
    }

    fun getAllCommentDynamicData(hashMap: HashMap<String, String?>) {
        allCommentDynamicData.value = hashMap
    }

    val postDynamicData = Transformations.switchMap(allPostDynamicData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getPostDynamics(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast(t!!.localizedMessage)
                }
            })
        mutableLiveData
    }

    fun getAllPostDynamicData(hashMap: HashMap<String, String?>) {
        allPostDynamicData.value = hashMap
    }

    val _userDynamicData = Transformations.switchMap(allUserDynamicsData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getUserDynamics(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast(t!!.localizedMessage)
                }
            })
        mutableLiveData
    }

    fun getUserDynamicData(hashMap: HashMap<String, String?>) {
        allUserDynamicsData.value = hashMap
    }

    val followDynamicData = Transformations.switchMap(allFollowDynamicData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getFollowDynamics(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast(t!!.localizedMessage)
                }
            })
        mutableLiveData
    }

    fun getAllFollowDynamicData(hashMap: HashMap<String, String?>) {
        allFollowDynamicData.value = hashMap
    }

    val recommendCommentData = Transformations.switchMap(allRecommendCommentData) {
        val mutableLiveData = MutableLiveData<List<CommentBean>?>()
        Respository.getRecommendComment(it,
            object : DisposableSubscriberAdapter<List<CommentBean>?>(this) {
                override fun onNext(t: List<CommentBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllRecommendCommentData(hashMap: HashMap<String, String?>) {
        allRecommendCommentData.value = hashMap
    }

    var recommendComments = MutableLiveData<List<CommentBean>?>()
    fun getRecommendComment(hashMap: HashMap<String, String?>) {
        Respository.getRecommendComment(hashMap,
            object : DisposableSubscriberAdapter<List<CommentBean>?>(this) {
                override fun onNext(t: List<CommentBean>?) {
                    recommendComments.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                }
            })
    }

    val commentListData = Transformations.switchMap(allCommentListData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<CommentBean>?>()
        Respository.getComment(it,
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

    fun getAllCommentData(hashMap: HashMap<String, String?>) {
        allCommentListData.value = hashMap
    }

    val _followResult = Transformations.switchMap(allFollowData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.follow(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("关注成功")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun follow(hm: HashMap<String, String?>) {
        allFollowData.value = hm

    }

    val _cancelFollowResult = Transformations.switchMap(allCancelFollowData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.cancelFollow(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("取消关注")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun cancelFollow(hm: HashMap<String, String?>) {
        allCancelFollowData.value = hm

    }

    val _likePostsResult = Transformations.switchMap(allLikePostsData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.likePost(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("点赞成功")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun likePosts(hm: HashMap<String, String?>) {
        allLikePostsData.value = hm

    }

    val _unlikePostResult = Transformations.switchMap(allUnLikePostsData) {
        val mutableLiveData = MutableLiveData<String?>()
        Respository.unlikePost(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("取消点赞成功")
                mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun unlikePost(hm: HashMap<String, String?>) {
        allUnLikePostsData.value = hm

    }


    val _commentResult = Transformations.switchMap(allCommentData) {
        val mutableLiveData = MutableLiveData<Boolean?>()
        Respository.comment(it, object : DisposableSubscriberAdapter<String>(this) {
            override fun onNext(t: String?) {
                ToastUtils.showToast("评论成功，等待审核~")
                mutableLiveData.value = true
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
            }

            override fun onError(t: Throwable?) {
                super.onError(t)
                mutableLiveData.value = false
                ToastUtils.showToast("${t?.localizedMessage}")
            }
        })
        mutableLiveData
    }

    fun comment(hm: HashMap<String, String?>) {
        allCommentData.value = hm

    }

    val _searchResult = Transformations.switchMap(allSearchPostData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getSearchPost(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
//                when(t){
//                    true->ToastUtils.showToast("操作成功")
//                    else->ToastUtils.showToast("操作失败")
//                }
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                }
            })
        mutableLiveData
    }

    fun getSearchPost(hm: HashMap<String, String?>) {
        allSearchPostData.value = hm

    }

    val postLikesData = Transformations.switchMap(allPostLikesData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getLikePosts(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
//                    var dynamicDataListBean = HttpDataListBean<DynamicBean>()
//                    var listBean: MutableList<DynamicBean> = arrayListOf()
//                    t?.data?.let { likePostBean ->
//                        if (likePostBean.isNotEmpty()) {
//                            for (bean in likePostBean[0].likePosts!!) {
//                                listBean.add(DynamicBean(id = bean.id,
//                                    uid = bean.uid,
//                                    content = bean.content,
//                                    status = bean.status,
//                                    title = bean.title,
//                                    price = bean.price,
//                                    circle_id = bean.circle_id,
//                                    type = bean.type,
//                                    update_time = bean.update_time,
//                                    create_time = bean.create_time,
//                                    is_top = bean.is_top,
//                                    like_count = bean.like_count,
//                                    view_count = bean.view_count,
//                                    comm_count = bean.comm_count,
//                                    is_recommend = bean.is_recommend,
//                                    is_elite = bean.is_elite,
//                                    is_like = 1,
//                                    images = bean.images,
//                                    pivot = bean.pivot))
//                            }
//                            dynamicDataListBean.data = listBean
//                        }
//                        mutableLiveData.value = dynamicDataListBean
//                    }
                    mutableLiveData.value =t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                }
            })
        mutableLiveData
    }

    fun getAllPostLikesData(hashMap: HashMap<String, String?>) {
        allPostLikesData.value = hashMap
    }


    val replyPostData = Transformations.switchMap(allReplyPostData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getReplyPosts(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast(t!!.localizedMessage)
                }
            })
        mutableLiveData
    }

    fun getAllReplyPostData(hashMap: HashMap<String, String?>) {
        allReplyPostData.value = hashMap
    }

    val recordPostsData = Transformations.switchMap(allRecordPostData) {
        val mutableLiveData = MutableLiveData<HttpDataListBean<DynamicBean>?>()
        Respository.getRecordPosts(it,
            object : DisposableSubscriberAdapter<HttpDataListBean<DynamicBean>?>(this) {
                override fun onNext(t: HttpDataListBean<DynamicBean>?) {
                    mutableLiveData.value = t
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = mutableLiveData.value
                    ToastUtils.showToast(t!!.localizedMessage)
                }
            })
        mutableLiveData
    }

    fun getAllRecordPostData(hashMap: HashMap<String, String?>) {
        allRecordPostData.value = hashMap
    }

}