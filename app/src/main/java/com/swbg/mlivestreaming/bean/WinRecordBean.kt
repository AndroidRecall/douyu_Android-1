package com.swbg.mlivestreaming.bean

data class WinRecordBean(
    var name:String?=null,
    var gameBean: GameTypeBean?=null,
    var monery:String? =null
) {

}
data class GameTypeBean( var gameType:Int,var gameName:String )