package com.mp.douyu.download

import android.os.Environment
import android.util.Log
import com.blankj.utilcode.util.ToastUtils
import com.mp.douyu.MApplication
import com.mp.douyu.bean.DownLoadBean
import com.mp.douyu.provider.StoredUserSources
import com.tencent.rtmp.downloader.ITXVodDownloadListener
import com.tencent.rtmp.downloader.TXVodDownloadManager
import com.tencent.rtmp.downloader.TXVodDownloadMediaInfo
import java.io.File


class DownLoadManager {
    var downloader: TXVodDownloadManager? = null
    var downMap: HashMap<String, DownLoadBean> = hashMapOf()
    var downListeners: MutableList<ITXVodDownloadListener> = arrayListOf()

    companion object {
        private val TAG: String = DownLoadManager::class.java.simpleName
//        private val sdkAppID: Int = 1400547138

        val instance: DownLoadManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DownLoadManager()
        }
    }

    fun downLoadVideo(bean: DownLoadBean) {
        downLoadVideo(bean, false)
    }

    fun downLoadVideo(bean: DownLoadBean, showToast: Boolean) {
        Log.e(TAG, "bean:${bean}")
        if (downloader == null) {
            downloader = TXVodDownloadManager.getInstance()
            downloader?.setListener(object : ITXVodDownloadListener {
                override fun onDownloadStart(mediaInfo: TXVodDownloadMediaInfo?) {
                    if (showToast) ToastUtils.showShort("开始下载")
                    Log.e(TAG, "bean:${bean}")
                    downMap[mediaInfo?.url]?.let { it ->
                        saveCacheRecord(it)
                    }
                    Log.e(TAG,
                        "开始下载 onDownloadStart downloadSize=${mediaInfo?.downloadSize},duration=${mediaInfo?.duration}")
                    downListeners.forEach {
                        it.onDownloadStart(mediaInfo)
                    }
                }

                override fun onDownloadError(mediaInfo: TXVodDownloadMediaInfo?, error: Int, reason: String?) {
                    if (showToast) ToastUtils.showShort("$reason")
                    Log.e(TAG, "下载错误 onDownloadError error=${error} ,reason=${reason}")
                    downListeners.forEach {
                        it.onDownloadError(mediaInfo, error, reason)
                    }
                }

                override fun hlsKeyVerify(mediaInfo: TXVodDownloadMediaInfo?, p1: String?, p2: ByteArray?): Int {
                    Log.e(TAG, "hlsKeyVerify p1=${p1} ")
                    return -1
                }

                override fun onDownloadStop(mediaInfo: TXVodDownloadMediaInfo?) {
                    Log.e(TAG, "停止下载 onDownloadStop url=${mediaInfo?.url}")
                    downListeners.forEach {
                        it.onDownloadStop(mediaInfo)
                    }
                }

                override fun onDownloadFinish(mediaInfo: TXVodDownloadMediaInfo?) {
                    Log.e(TAG, "下载结束 onDownloadFinish")
                    val cacheRecord = StoredUserSources.getCacheRecord()
                    cacheRecord.forEach {
                        if (mediaInfo?.url.equals(it.url)) {
                            it.mediaInfo = mediaInfo
                            it.downState = 1
                        }
                    }
                    StoredUserSources.putCacheRecord(cacheRecord)
                    downListeners.forEach {
                        it.onDownloadFinish(mediaInfo)
                    }
                }

                override fun onDownloadProgress(mediaInfo: TXVodDownloadMediaInfo?) {
                    Log.e(TAG,
                        "正在下载 url=${mediaInfo?.url},playPath=${mediaInfo?.playPath}" +
                                "\n,progress=${mediaInfo?.progress}\n,size=${mediaInfo?.size}" +
                                "\n,downloadSize=${mediaInfo?.downloadSize}\n,duration=${mediaInfo?.duration}")
                    downListeners.forEach {
                        it.onDownloadProgress(mediaInfo)
                    }

                }
            })
        }
//        Environment.getExternalStorageDirectory().getPath()

        val path =
            MApplication.getApplicationInstances.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.path + "/" + bean.video_id
        bean.path = path
        downloader?.setDownloadPath(path)
        val downloadMediaInfo = downloader?.startDownloadUrl(bean.url)
        bean.mediaInfo = downloadMediaInfo

        downMap["${bean.url}"] = bean

    }

    /**
     * 中断下载
     */
    open fun stopDownload(bean: DownLoadBean) {
        val downloader = TXVodDownloadManager.getInstance()
        downloader.stopDownload(bean.mediaInfo)
    }

    /**
     * 删除文件
     */
    fun delDownloadFile(bean: DownLoadBean) {
        val downloader = TXVodDownloadManager.getInstance()
        downloader.deleteDownloadFile(bean.path)
    }

    /**
     * 添加记录
     */
    fun saveCacheRecord(bean: DownLoadBean) {
        val cacheRecords = StoredUserSources.getCacheRecord()
        for (cacheBean in cacheRecords) {
            //去重
            if (cacheBean.video_id == bean.video_id && cacheBean.url.equals(bean.url)) {
                return
            }
        }
        cacheRecords.add(bean)
        StoredUserSources.putCacheRecord(cacheRecords)
    }

    /**
     * 开始继续缓存视频
     */
    open fun startCacheVideo() {
        Log.e(TAG, "startCacheVideo ${StoredUserSources.getCacheRecord().size}")
        for ((index, downloadBean) in StoredUserSources.getCacheRecord().withIndex()) {
            Log.e(TAG, "开始 index=${index}")
            downLoadVideo(downloadBean)
        }
    }

    /**
     * 停止缓存视频
     */
    open fun stopCacheVideo() {
        Log.e(TAG, "stopCacheVideo ${StoredUserSources.getCacheRecord().size}")
        for ((index, downloadBean) in StoredUserSources.getCacheRecord().withIndex()) {
            Log.e(TAG, "停止 index=${index}")
            stopDownload(downloadBean)
        }

    }

    fun mkdirs(path: String?): String? {
        var path = path
        val baseUrl: String =
            MApplication.getApplicationInstances.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.path!!
        if (path?.indexOf(baseUrl) == -1) {
            path = baseUrl + (if (path.indexOf("/") == 0) "" else "/") + path
        }
        val destDir = File(path)
        if (!destDir.exists()) {
            path = makedir(path!!)
            if (path == null) {
                return null
            }
        }
        return path
    }

    private fun makedir(path: String): String? {
        val baseUrl: String =
            MApplication.getApplicationInstances.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.path!!
        val dirs = path.replace(baseUrl, "").split("/".toRegex()).toTypedArray()
        val filePath = StringBuffer(baseUrl)
        for (dir in dirs) {
            if ("" != dir && dir != baseUrl) {
                filePath.append("/").append(dir)
                val destDir = File(filePath.toString())
                if (!destDir.exists()) {
                    val b = destDir.mkdirs()
                    if (!b) {
                        return null
                    }
                }
            }
        }
        return filePath.toString()
    }

    /**
     * 创建多级文件目录
     * @param fileDir
     * @return
     */
    fun createFileDirectorys(fileDir: String) {
        val fileDirs = fileDir.split("\\/".toRegex()).toTypedArray()
        var topPath = ""
        for (i in fileDirs.indices) {
            topPath += "/" + fileDirs[i]
            val file = File(topPath)
            if (file.exists()) {
                continue
            } else {
                file.mkdir()
            }
        }
    }

    fun addDownLoadListener(listener: ITXVodDownloadListener) {
        downListeners.add(listener)
    }

    fun removeDownLoadListener(listener: ITXVodDownloadListener) {
        downListeners.remove(listener)
    }
}