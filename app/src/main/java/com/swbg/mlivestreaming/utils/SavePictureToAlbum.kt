package com.swbg.mlivestreaming.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.base.Constant
import com.swbg.mlivestreaming.utils.permission_helper.PermissionHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.random.Random
import kotlin.random.nextUInt

class SavePictureToAlbum {
    /**
     * 网络图片或mipmap/drawable图片保存到相册 @glide
     * @param isLocalImage 是否保存的mipmap/drawable图片
     * @param saveLocalName 保存图片名字
     * @param url 保存图片下载地址
     * @param mipmapId 保存图片本地mipmap/drawable地址地址
     *
     */
    fun saveLocalUrlToAlbum(context: FragmentActivity, isLocalImage: Boolean, saveLocalName: String, url: String? = null, mipmapId: Int? = null) {

        if ((!isLocalImage && url.isNullOrEmpty()) || (isLocalImage && mipmapId == null)) {//下载地址为空 ，返回
            ToastUtils.showToast(context.getString(R.string.loading_please_waite))
            return
        }
        PermissionHelper.request(context, object : PermissionHelper.PermissionCallback {
            private var bitMap: Bitmap? = null
            private lateinit var load: FutureTarget<File>

            @SuppressLint("CheckResult")
            override fun onSuccess() {
                val file = File(Constant.SAVE_PATH_DIR,
                    "$saveLocalName${System.currentTimeMillis()}.png")
                if (isLocalImage) {
                    bitMap = BitmapFactory.decodeResource(context.resources, mipmapId!!)
                } else {
                    load = Glide.with(context).asFile().load(if (isLocalImage) mipmapId else url)
                        .submit()
                }

                Observable.just {}.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).map {
                        if (isLocalImage) {
                            if (!file.exists()) {
                                if (file.createNewFile()) {
                                    val fos = FileOutputStream(file)
                                    bitMap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                                    fos.flush()
                                    fos.close()
                                    bitMap?.recycle()
                                    bitMap = null
                                }
                            }
                            file
                        } else {
                            try {
                                load.get()
                            } catch (e: Exception) {
                                return@map file
                            }
                        }
                    }.observeOn(AndroidSchedulers.mainThread()).doOnError {
                        val alarmToast =
                            if (it is FileNotFoundException) context.getString(R.string.url_error_please_contact_administor)
                            else context.getString(R.string.load_failed)
                        ToastUtils.showToast(alarmToast)
                    }.subscribe {
                        try {
                            if ((isLocalImage && it.exists())  //已存在不需要复制
                                || (!isLocalImage && it.exists() && it.copyTo(file).exists())
                            ) {
                                val intent =
                                    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)// 告知相册添加图片
                                val uri = Uri.fromFile(file)
                                intent.data = uri
                                context.sendBroadcast(intent)

                                ToastUtils.showToast(context.getString(R.string.save_qr_code_album_success),
                                    true)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }



    /**
     * 下载到本地
     * @param context 上下文
     * @param url 网络图
     */
    open fun saveImgToLocal(context: Context, url: String) {
        PermissionHelper.request(context as FragmentActivity, object : PermissionHelper.PermissionCallback {
            override fun onSuccess() {
                //如果是网络图片，抠图的结果，需要先保存到本地
                Glide.with(context).downloadOnly().load(url).listener(object : RequestListener<File?> {

                    override fun onResourceReady(resource: File?, model: Any?, target: com.bumptech.glide.request.target.Target<File?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        saveToAlbum(context, resource!!.getAbsolutePath())
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File?>?, isFirstResource: Boolean): Boolean {
                        Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                        return false
                    }
                }).preload()
            }

        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }

    /**
     * 保存到相册中
     * @param context 上下文
     * @param srcPath 网络图保存到本地的缓存文件路径
     */
    private fun saveToAlbum(context: Context, srcPath: String) {
        val dcimPath: String = PathUtils.getExternalDcimPath()
        val file = File(dcimPath, "content_" + System.currentTimeMillis() + ".png")
        val isCopySuccess: Boolean = FileUtils.copy(srcPath, file.getAbsolutePath())
        if (isCopySuccess) {
            //发送广播通知
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + file.getAbsolutePath())))
            ToastUtils.showToast("图片保存到相册成功")
        } else {
            ToastUtils.showToast("图片保存到相册失败")
        }
    }
}