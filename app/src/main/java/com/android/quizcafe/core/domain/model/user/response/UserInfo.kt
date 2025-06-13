package com.android.quizcafe.core.domain.model.user.response

data class UserInfo(
    val nickname: String,
    val email: String = "",
    val quizCount: Int,
    val quizBookCount: Int,
    val joinDateStr: String,
    val quizSolvingRecord: Map<String, Int>,
)
