package com.mp.douyu.ui.square.comment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureConfig.CHOOSE_REQUEST
import com.luck.picture.lib.config.PictureMimeType
import com.mp.douyu.R
import com.mp.douyu.adapter.DynamicPublishAdapter
import com.mp.douyu.adapter.DynamicPublishAdapter.Companion.TYPE_CONTENT
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.bean.PublishItemBean
import com.mp.douyu.image.GlideEngine
import com.mp.douyu.singleClick
import kotlinx.android.synthetic.main.dynamic_fragment_publish_image.*
import java.util.*

open class PublishImageFragment(var type: Int = TYPE_PUBLISH_POST,var circleId: Int?=-1) : VisibilityFragment() {
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;

    companion object {
        const val TYPE_PUBLISH_POST = 0//发布文章
        const val TYPE_PUBLISH_VIDEO = 1//发布视频


        const val PUBLISH_TYPE = "type"
        const val PUBLISH_CIRCLE = "circle_id"
        fun newInstance(type: Int = TYPE_PUBLISH_POST,circleId:Int? = -1): PublishImageFragment {
            val fragment = PublishImageFragment()
            val bundle = Bundle()
            bundle.putInt(PUBLISH_TYPE, type)
            circleId?.let {
                bundle.putInt(PUBLISH_CIRCLE, it)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.dynamic_fragment_publish_image
    val mAdapter by lazy {
        DynamicPublishAdapter({}, context).apply {
            setListener(object : DynamicPublishAdapter.OnSelectListener {
                override fun onDel(position: Int) {
                    showDelDialog(position)
                }

                override fun onAdd(position: Int) {
//                    removeFootView()
                    addOption()
                }
            })
            addFootView(type)
        }
    }

    private fun addOption() {
        when (type) {
            TYPE_PUBLISH_POST -> {
                if (mAdapter.getDataList().size > 0 && PictureMimeType.getMimeType(mAdapter.getDataList()
                        .get(0).localMedia?.mimeType) === PictureConfig.TYPE_IMAGE
                ) {
                    gotoSelectMedia(PictureMimeType.ofImage(), maxImgSelect = 9, maxVideoSelect = 1)
                } else gotoSelectMedia(PictureMimeType.ofAll(),
                    maxImgSelect = 9,
                    maxVideoSelect = 1)
            }
            else -> gotoSelectMedia(PictureMimeType.ofVideo(), maxVideoSelect = 1)
        }


    }

    override fun initView() {

    }

    override fun onVisible() {
        super.onVisible()
    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {
        type = arguments?.getInt(PUBLISH_TYPE, TYPE_PUBLISH_POST)!!
        circleId = arguments?.getInt(PUBLISH_CIRCLE, -1)!!
        tv_title.text = if (type == TYPE_PUBLISH_POST) "发帖" else "发布短视频"
        et_txt.hint = if (type == TYPE_PUBLISH_POST) "尬聊内容，写在这里哦~" else "请输入标题"
        tv_add.visibility = if (type == TYPE_PUBLISH_POST) View.VISIBLE else View.INVISIBLE
        et_txt.addTextChangedListener { it ->
            tv_txt_num.text = "${it?.length}/2000"
        }
        tv_publish.apply {
            singleClick {
                when (type) {
                    TYPE_PUBLISH_POST -> {
                        if (mAdapter.getDataList().size > 0 || et_txt.text.toString().isNotEmpty()
                        ) {
                            (activity as MBaseActivity).showLoadingView(true)
                            mAdapter.getFootCount()
                            publishViewModel.publishPost(mAdapter.getDataList(),
                                et_txt.text.toString(), circleId!!)
                        } else {
                            ToastUtils.showShort("请输入发布内容")
                        }
                    }
                    TYPE_PUBLISH_VIDEO -> {
                        if (et_txt.text.toString().isNullOrBlank()) {
                            ToastUtils.showShort("请输入视频标题")
                            return@singleClick
                        }
                        if (mAdapter.getDataList().size == 0) {
                            ToastUtils.showShort("请添加视频")
                            return@singleClick
                        }
                        (activity as MBaseActivity).showLoadingView(true)
                        publishViewModel.publishVideo(mAdapter.data, et_txt.text.toString())
                    }
                }


            }
        }
        iv_back.apply {
            singleClick {
                activity?.finish()
            }
        }
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, 3)

        }

    }

    override fun onVisibleExceptFirst() {
        super.onVisibleExceptFirst()
    }

    protected fun gotoSelectMedia(type: Int, maxImgSelect: Int = 9, maxVideoSelect: Int = 1) {
        PictureSelector.create(this).openGallery(type)
            .maxSelectNum(maxImgSelect - (mAdapter.itemCount - mAdapter.getFootCount())) // 最大图片选择数量 int
            .maxVideoSelectNum(maxVideoSelect - (mAdapter.itemCount - mAdapter.getFootCount()))
            .selectionMode(PictureConfig.MULTIPLE) // 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .isCamera(true) // 是否显示拍照按钮 true or false
            .isCompress(true) // 是否压缩 true or false
            .videoMaxSecond(45).imageEngine(GlideEngine.createGlideEngine())
            .forResult(CHOOSE_REQUEST) //结果回调onActivityResult code
    }

    fun chooseCallback(data: Intent?) {
        if (data != null) {
            mAdapter.removeFootView()
            val selectList = PictureSelector.obtainMultipleResult(data)
            val list: MutableList<PublishItemBean> = ArrayList()
            for (i in selectList.indices) {
                val bean = PublishItemBean()
                bean.itemType = TYPE_CONTENT
                bean.localMedia = selectList[i]
                list.add(bean)
            }
            mAdapter.addAll(list)
            mAdapter.addFootView(type)
        }
    }

    /**
     * Dialog对话框提示用户删除操作 position为删除图片位置
     */
    protected open fun showDelDialog(position: Int) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage(resources.getString(if (type == TYPE_PUBLISH_POST) R.string.delete_image_ok else R.string.delete_video_ok))
        builder.setTitle(resources.getString(R.string.tip))
        builder.setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
            dialog.dismiss()
            mAdapter.removeAt(position)
            mAdapter.addFootView(type)
        }
        builder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, which -> dialog.dismiss() }
        builder.create().show()
    }

    private val publishViewModel by getViewModel(PublishViewModel::class.java) {
        _publishVideoResult.observe(it, Observer {
            it?.let {
                (activity as MBaseActivity).showLoadingView(false)
                if (it) activity?.finish()

            }
        })
        _publishPostResult.observe(it, Observer {
            it?.let {
                (activity as MBaseActivity).showLoadingView(false)
                if (it) activity?.finish()
            }
        })
    }


}