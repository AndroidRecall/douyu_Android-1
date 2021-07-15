package com.mp.douyu.ui.square.comment

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.mp.douyu.base.MBaseViewModel
import com.mp.douyu.bean.PublishItemBean
import com.mp.douyu.bean.UploadParam
import com.mp.douyu.http.DisposableSubscriberAdapter
import com.mp.douyu.http.NobodyConverterFactory
import com.mp.douyu.http.Respository
import com.mp.douyu.utils.RxUtils
import com.mp.douyu.utils.ToastUtils
import com.mp.douyu.utils.UploadUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class PublishViewModel : MBaseViewModel() {
    val TAG: String? = javaClass.simpleName
    private val allPublishVideoData = MutableLiveData<UploadParam?>()
    private val allPublishPostData = MutableLiveData<UploadParam?>()

    val _publishVideoResult = Transformations.switchMap(allPublishVideoData) {
        val mutableLiveData = MutableLiveData<Boolean?>()
        Respository.publishVideo(it!!.bodyMap,
            it.parts,
            object : DisposableSubscriberAdapter<String>(this) {
                override fun onNext(t: String?) {
                    Log.e(TAG, "publishVideo 上传成功")
                    ToastUtils.showToast("上传成功")
                    mutableLiveData.value = true
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    mutableLiveData.value = false
                    ToastUtils.showToast("${t?.localizedMessage}")
                    Log.e(TAG, "publishVideo 上传失败${t?.localizedMessage}")
                }
            })
        mutableLiveData
    }

    fun publishVideo(data: MutableList<PublishItemBean>, text: String) {
        Log.e(TAG, "publishVideo")
        val parts: MutableList<MultipartBody.Part> = arrayListOf()
        val bodyMap: MutableMap<String, RequestBody> = hashMapOf()
        for (itemBean in data) {
            var path: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                itemBean.localMedia?.androidQToPath!!
            } else {
                itemBean.localMedia?.path!!
            }
            val file = File(path)
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val part = MultipartBody.Part.createFormData("short_video", file.name, requestFile)
            parts.add(part)

            //上传视频第一帧
            val firstFrameFile = File(UploadUtils.getFirstFrameByVideo("video_first.png", path))
            val requestFirstBody: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), firstFrameFile)
            val firstPart =
                MultipartBody.Part.createFormData("image", firstFrameFile.name, requestFirstBody)
            parts.add(firstPart)
            //上传视频时长
//                    bodyMap["duration"] = toRequestBody(itemBean.localMedia?.duration.toString() + "")!!
        }
        bodyMap["title"] = UploadUtils.toRequestBody(text)!!
        allPublishVideoData.value = UploadParam(parts = parts, bodyMap = bodyMap)

    }

    val _publishPostResult = Transformations.switchMap(allPublishPostData) {
        val mutableLiveData = MutableLiveData<Boolean?>()
        showLoadingView(true)
        Respository.publishPost(it!!.bodyMap,
            it.parts,
            object : DisposableSubscriberAdapter<NobodyConverterFactory.NoBodyEntity>(this) {
                override fun onNext(t: NobodyConverterFactory.NoBodyEntity) {
                    t?.let {
                        var code = -1
                        var message: String = ""
                        try {
                            var result =
                                JSONObject((it as NobodyConverterFactory.NoBodyEntity).json)
                            code = result.getInt("status")
                            message = result.getString("message")
                            var data = result.getString("data")
                        } catch (e: Exception) {
                        }
                        if (code == 200) {
                            ToastUtils.showToast("发布成功，请等待审核通过展示")
                            mutableLiveData.value = true
                        } else {
                            ToastUtils.showToast(message)
                            mutableLiveData.value = false
                        }
                        dismissLoading()
                    }
                }

                override fun onError(t: Throwable?) {
                    super.onError(t)
                    dismissLoading()
                    RxUtils.delay(500).subscribe {
                        ToastUtils.showToast("${t?.localizedMessage}")
                    }
                    mutableLiveData.value = false
                    Log.e(TAG, "publishImgDynamic 上传失败${t?.localizedMessage}")
                }
            })
        mutableLiveData
    }

    fun publishPost(data: MutableList<PublishItemBean>, text: String, circleId: Int) {
        Log.e(TAG, "publishPost")
        val parts: MutableList<MultipartBody.Part> = arrayListOf()
        val bodyMap: MutableMap<String, RequestBody> = hashMapOf()
        for ((index, itemBean) in data.withIndex()) {
            when (PictureMimeType.getMimeType(itemBean.localMedia?.mimeType)) {
                PictureConfig.TYPE_IMAGE -> {
                    //图片
                    val file = File(itemBean.localMedia!!.compressPath)
                    val requestFile =
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    val part = MultipartBody.Part.createFormData("image${index + 1}",
                        file.name,
                        requestFile)
                    parts.add(part)
                }
                PictureConfig.TYPE_VIDEO -> {
                    //视频
                    var path: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        itemBean.localMedia?.androidQToPath!!
                    } else {
                        itemBean.localMedia?.path!!
                    }
                    val file = File(path)
                    val requestFile =
                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    val part = MultipartBody.Part.createFormData("video", file.name, requestFile)
                    parts.add(part)

//                    //上传视频第一帧
//                    val firstFrameFile = File(UploadUtils.getFirstFrameByVideo("video_first.png", path))
//                    val requestFirstBody: RequestBody =
//                        RequestBody.create("multipart/form-data".toMediaTypeOrNull(), firstFrameFile)
//                    val firstPart =
//                        MultipartBody.Part.createFormData("image", firstFrameFile.name, requestFirstBody)
//                    parts.add(firstPart)
                }
            }
        }
        bodyMap["content"] = UploadUtils.toRequestBody(text)!!
        if (circleId > 0) bodyMap["circle_id"] = UploadUtils.toRequestBody("$circleId")!!
        allPublishPostData.value = UploadParam(parts = parts, bodyMap = bodyMap)

    }


}