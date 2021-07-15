package com.mp.douyu.bean

data class TaskCenterBean(
    val daily_task: List<DailyTask>? = listOf(),
    val newcomer_task: List<NewcomerTask>? = listOf(),
    val promote_task: List<PromoteTask>? = listOf(),
    val limit: List<LimitBean>? = listOf(),
    val sign_reward: List<String>? = listOf(),
    val user: TaskUserInfo? = TaskUserInfo(),
    val text: String? = ""
)
data class LimitBean(
    val download_times: Int? = 0,
    val id: Int? = 0,
    val play_times: Int? = 0,
    val title: String? = ""
)


data class SignInBean(
    val message: String? = "",
    val points: Int? = 0
)

data class DailyTask(
    val id: Int? = 0,
    val is_complete: Int? = 0,
    val points: String? = ""
)

data class Limit(
    val download_times: Int? = 0,
    val id: Int? = 0,
    val play_times: Int? = 0,
    val title: String? = ""
)

data class NewcomerTask(
    val id: Int? = 0,
    val is_complete: Int? = 0,
    val points: String? = ""
)

data class PromoteTask(
    val download: Int? = 0,
    val id: Int? = 0,
    val play: Int? = 0,
    val user: Int? = 0
)

data class TaskUserInfo(
    val points: Int? = 0,
    val sign_days: Int? = 0,
    val sign_today: Boolean? = false
)