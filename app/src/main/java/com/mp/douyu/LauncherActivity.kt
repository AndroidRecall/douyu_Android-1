package com.mp.douyu

import android.annotation.SuppressLint
import android.content.Intent
import com.mp.douyu.base.MBaseActivity
import com.mp.douyu.provider.StoredUserSources
import com.mp.douyu.ui.GuideUserActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Thread.sleep

class LauncherActivity : MBaseActivity(){
    override val contentViewLayoutId: Int
        get() = R.layout.activity_launcher

    @SuppressLint("CheckResult")
    override fun initView() {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        matchWaterDropScreen(this)
        setActivityImmersion(this)

        Observable.just{}
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map {
                sleep(3 * 1000)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (StoredUserSources.getIsFirstLoad().isFirstLoad.equals("1")) {
                    startActivityWithTransition(GuideUserActivity.open(this))
                } else{
                    startActivityWithTransition(MainActivity.open(this))
                }
                finishView()
            }

    }
}