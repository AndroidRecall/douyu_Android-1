package com.swbg.mlivestreaming.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UploadUtils {
    companion object {
        open fun toRequestBody(value: String?): RequestBody? {
            return RequestBody.create("text/plain".toMediaTypeOrNull(), value!!)
        }


        /**
         * 获取网络视频第一帧
         * @param videoUrl
         * @return
         */
        open fun getNetVideoBitmap(videoPath: String?): Bitmap {
            var bitmap: Bitmap? = null
            val retriever = MediaMetadataRetriever()
            try {
                //根据url获取缩略图
                retriever.setDataSource(videoPath, HashMap())
                //获得第一帧图片
                bitmap = retriever.frameAtTime
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }
            return bitmap!!
        }

        /**
         * 获取本地视频的第一帧
         *
         * @param localPath
         * @return
         */
        fun getLocalVideoBitmap(localPath: String?): Bitmap? {
            var bitmap: Bitmap? = null
            val retriever = MediaMetadataRetriever()
            try {
                //根据文件路径获取缩略图
                retriever.setDataSource(localPath)
                //获得第一帧图片
                bitmap = retriever.frameAtTime
            } catch (e: java.lang.IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }
            return bitmap
        }


        open fun getFirstFrameByVideo(fileName: String?, videoPath: String?): String? {
            val media = MediaMetadataRetriever()
            media.setDataSource(videoPath) // videoPath 本地视频的路径
            val bitmap = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            return saveMyBitmap(fileName!!, bitmap)
        }

        /**
         * 将压缩的bitmap保存到SDCard卡临时文件夹，用于上传
         *
         * @param filename
         * @param bit
         * @return
         */
        open fun saveMyBitmap(filename: String, bit: Bitmap): String? {
            val baseDir = Environment.getExternalStorageDirectory().absolutePath + "/laopai/"
            val filePath = baseDir + filename
            val dir = File(baseDir)
            if (!dir.exists()) {
                dir.mkdir()
            }
            val f = File(filePath)
            try {
                f.createNewFile()
                var fOut: FileOutputStream? = null
                fOut = FileOutputStream(f)
                bit.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.flush()
                fOut.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
            return filePath
        }
    }
}