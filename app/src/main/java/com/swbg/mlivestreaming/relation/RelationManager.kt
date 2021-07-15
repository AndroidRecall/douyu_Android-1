package com.swbg.mlivestreaming.relation

import com.swbg.mlivestreaming.bean.*

class RelationManager private constructor() {
    var shortVideoLikes :MutableList<LikeShortVideoBean> = arrayListOf()
    var postLikes :MutableList<DynamicBean> = arrayListOf()
    var follows :MutableList<CommonUserBean> = arrayListOf()
    var followLives :MutableList<LiveBean> = arrayListOf()
    var advBeans :MutableList<AdvBean?> = arrayListOf()
    companion object {
        val instance: RelationManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RelationManager()
        }
    }

}