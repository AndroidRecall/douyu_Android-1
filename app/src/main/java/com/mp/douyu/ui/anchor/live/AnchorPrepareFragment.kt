package com.mp.douyu.ui.anchor.live

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.lxj.xpopup.XPopup
import com.mp.douyu.R
import com.mp.douyu.base.VisibilityFragment
import com.mp.douyu.event.LiveEvent
import com.mp.douyu.image.GlideEngine
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.singleClick
import com.mp.douyu.utils.RxBus
import com.mp.douyu.utils.ToastUtils
import com.tencent.rtmp.TXLivePushConfig
import com.tencent.rtmp.TXLivePusher
import kotlinx.android.synthetic.main.activity_anchor_prepare.*
import kotlinx.android.synthetic.main.live_fragment_anchor.*


open class AnchorPrepareFragment : VisibilityFragment() {
    private lateinit var mPicChsDialog: Dialog
    private var currentPage: Int = 1
    private var pageSize: Int = 10
    private var currentPosition = -1;
    var selectList: List<LocalMedia>? = arrayListOf()
    val mLivePusher: TXLivePusher by lazy {
        TXLivePusher(activity).apply {
            // 一般情况下不需要修改 config 的默认配置
            config = TXLivePushConfig()
            startCameraPreview(anchor_video_view)
        }
    }

    companion object {
        const val LIVE_DATA = "data"
        const val INITIAL_POSITION = "position"
        fun newInstance(): AnchorPrepareFragment {
            val fragment = AnchorPrepareFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


    private fun follow(position: Int) {
//        anchorViewModel.followAnchor(hashMapOf("anchor_id" to "${mAdapter.data[position].id}"))
    }

    private fun unFollow(position: Int) {
//        anchorViewModel.cancelFollowAnchor(hashMapOf("anchor_id" to "${mAdapter.data[position].id}"))
    }

    override val layoutId: Int
        get() = R.layout.activity_anchor_prepare

    override fun initView() {
    }

    override fun onVisible() {
        super.onVisible()


    }

    override fun onInvisible() {
        super.onInvisible()
    }

    override fun onVisibleFirst() {

        initOnClick()

    }

    private fun initOnClick() {
        anchor_btn_cover.singleClick {
            //选择封面
            gotoSelectMedia(PictureMimeType.ofImage(), 1)
        }

        anchor_btn_publish.singleClick {
            //上传图片开播
            val title = anchor_tv_title.text.toString()
            val notice = anchor_tv_notice.text.toString()
            if (title.isBlank()) {
                ToastUtils.showToast("请输入直播标题")
                return@singleClick
            }
            if (notice.isBlank()) {
                ToastUtils.showToast("请输入直播公告")
                return@singleClick
            }

            asLoading.show()
            var status = 0
            liveViewModel.startLive(selectList!!.toMutableList(), title, notice, null, status)
        }
        anchor_btn_cancel.singleClick {
            finishView()
        }

    }

    val asLoading by lazy {
        XPopup.Builder(context)
            .dismissOnTouchOutside(false)
            .asLoading()

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun gotoSelectMedia(type: Int, maxSelect: Int) {
        PictureSelector.create(this).openGallery(type).maxSelectNum(maxSelect) // 最大图片选择数量 int
            .selectionMode(PictureConfig.MULTIPLE) // 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .isCamera(true) // 是否显示拍照按钮 true or false
            .isCompress(true) // 是否压缩 true or false
            .videoMaxSecond(45).imageEngine(GlideEngine.createGlideEngine())
            .forResult(PictureConfig.CHOOSE_REQUEST) //结果回调onActivityResult code
    }

    fun chooseCallback(data: Intent?) {
        if (data != null) {
            selectList = PictureSelector.obtainMultipleResult(data)
            if (selectList?.size!! > 0) {
                val url = selectList!![0].run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        androidQToPath
                    } else {
                        compressPath
                    }
                }
                Glide.with(context!!).load(url).placeholder(R.mipmap.default_avatar)
                    .into(anchor_btn_cover)
            }
        }
    }

    /**
     * 图片选择对话框
     */
    private fun initPhotoDialog() {
        mPicChsDialog = Dialog(activity!!, R.style.floag_dialog)
        mPicChsDialog.setContentView(R.layout.dialog_pic_choose)
        val windowManager: WindowManager = activity!!.windowManager
        val display = windowManager.defaultDisplay
        val dlgwin: Window = mPicChsDialog.window!!
        val lp = dlgwin.attributes
        dlgwin.setGravity(Gravity.BOTTOM)
        lp.width = display.width //设置宽度
        mPicChsDialog?.window?.attributes = lp
        val camera = mPicChsDialog.findViewById(R.id.chos_camera) as TextView
        val picLib = mPicChsDialog.findViewById(R.id.pic_lib) as TextView
        val cancel = mPicChsDialog.findViewById(R.id.anchor_btn_cancel) as TextView
        camera.setOnClickListener {
//            getPicFrom(com.tencent.qcloud.xiaozhibo.anchor.prepare.TCAnchorPrepareActivity.CAPTURE_IMAGE_CAMERA)
//            mPicChsDialog.dismiss()
        }
        picLib.setOnClickListener {
//            getPicFrom(com.tencent.qcloud.xiaozhibo.anchor.prepare.TCAnchorPrepareActivity.IMAGE_STORE)
//            mPicChsDialog.dismiss()
        }
        cancel.setOnClickListener { mPicChsDialog.dismiss() }
    }

    private val liveViewModel by getViewModel(LiveViewModel::class.java) {
        _startLiveData.observe(it, Observer {
            asLoading.dismiss()
            dismissLoading()
            it?.let {
                RxBus.getInstance().post(LiveEvent(it))
                StoredUserSources.putGroupId(it.Group_id)
            }
        })
    }
}

