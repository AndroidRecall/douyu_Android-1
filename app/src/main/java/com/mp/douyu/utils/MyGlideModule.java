package com.mp.douyu.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;


@GlideModule
public class MyGlideModule extends AppGlideModule {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        try {
            ActivityManager systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            boolean lowMemory;
            if (systemService.isLowRamDevice()) {
                lowMemory = true;
            } else {
                ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                systemService.getMemoryInfo(memoryInfo);
                lowMemory = memoryInfo.lowMemory;
            }

            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .format(lowMemory ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888);
            if (Build.VERSION.SDK_INT >= 26) {
                options.disallowHardwareConfig();
            }
            builder.setLogLevel(Log.ERROR)
                    .setDefaultRequestOptions(options);
        } catch (Throwable throwable) {

        }

        //内存缓存相关,默认是24m
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        //设置磁盘缓存及其路径
        //
        int MAX_CACHE_SIZE = 100 * 1024 * 1024;
        String CACHE_FILE_NAME = "imgCache";
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context,CACHE_FILE_NAME,MAX_CACHE_SIZE));
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String downloadDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                    CACHE_FILE_NAME;
            //路径---->sdcard/imgCache
            builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, MAX_CACHE_SIZE));
        } else {
            //路径---->/sdcard/Android/data/<application package>/cache/imgCache
            builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, CACHE_FILE_NAME, MAX_CACHE_SIZE));
        }
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //配置glide网络加载框架
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public boolean isManifestParsingEnabled() {
        //不使用清单配置的方式,减少初始化时间
        return false;
    }
}
