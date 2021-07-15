package com.swbg.mlivestreaming.ui.home.play

import android.content.Intent
import android.net.Uri
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.utils.LogUtils
import com.swbg.mlivestreaming.utils.ToastUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GetM3U8Info {
    //拼接好播放路径返回，至少一个路径
    fun getM2U8Info(url: String?,callBack:(List<String>)->Unit) {
        val linkList = arrayListOf<String>()
        url?.let {
            Observable.just {}.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).map {
                val conn = URL(url).openConnection() as HttpURLConnection
                LogUtils.i("==连接==", "${url}")
                if (conn.responseCode == 200) {
                    val realUrl = conn.url.toString()
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))

                    var basepath = ""
                    realUrl.split("/").mapIndexed { index, s ->
                        if (index <= 2) basepath += "${s}/"
                    }
                    LogUtils.i("==基本连接==", "$basepath")
                    var line: String
                    val videoListData = ArrayList<Pair<String, String>>()
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            when {
                                line.contains("RESOLUTION") -> {
                                    videoListData.add(Pair("RESOLUTION", line))
                                }
                                line.contains(".m3u8") -> {
                                    videoListData.add(Pair("m3u8", line))
                                }
                            }
                            LogUtils.i("==视频数据==", "${line}")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    try {
                        videoListData.mapIndexed { index, p ->
                            //拼接
                            when {
                                (p.first == "m3u8") -> {
                                    linkList.add("${basepath}${p.second.replaceFirst("/","")}")
                                }
                                else-> {
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
            }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (linkList.size == 0) {
                    linkList.add(url)
                }
                callBack.invoke(linkList)
                LogUtils.i("==视频数据==", "$linkList")

            }
        }
    }
}