package com.swbg.mlivestreaming

import android.annotation.SuppressLint
import android.content.Intent
import com.swbg.mlivestreaming.base.MBaseActivity
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.ui.GuideUserActivity
import io.reactivex.Observable
import io.reactivex.Scheduler
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