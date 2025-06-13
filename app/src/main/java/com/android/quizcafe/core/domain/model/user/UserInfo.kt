package com.android.quizcafe.core.domain.model.user

data class UserInfo(
    val nickname: String,
    val email : String = "",
    val quizCount: Int,
    val quizBookCount: Int,
    val quizSolvingRecord: Map<String, Int>,
)
