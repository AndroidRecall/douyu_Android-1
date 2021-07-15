package github.leavesc.reactivehttp.viewmodel

import kotlinx.coroutines.Job

/**
 * @Author: leavesC
 * @Date: 2020/6/26 21:19
 * @Desc:
 * @GitHub：https://github.com/leavesC
 */
open class BaseActionEvent

class ShowLoadingEvent(val job: Boolean) : BaseActionEvent()

object DismissLoadingEvent : BaseActionEvent()

object FinishViewEvent : BaseActionEvent()

class ShowToastEvent(val message: String) : BaseActionEvent()

class ShowErrorEvent(val t: Throwable?) : BaseActionEvent()