package com.swbg.mlivestreaming.base;

import android.os.Environment;


import com.swbg.mlivestreaming.BuildConfig;

import java.io.File;


public interface Constant {

    //根目录
    String DIR_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    //存放的长期文件夹
    String SAVE_DIR = BuildConfig.APP_NAME_;
    //存放的长期文件夹完整路径
    String SAVE_PATH_DIR = DIR_ROOT + File.separator + SAVE_DIR;
}
