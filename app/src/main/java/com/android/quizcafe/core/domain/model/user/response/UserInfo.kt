package com.android.quizcafe.core.domain.model.user.response

import java.util.SortedMap

data class UserInfo(
    val nickname: String,
    val email: String = "",
    val quizCount: Int,
    val quizBookCount: Int,
    val joinDateStr: String,
    val quizCountByDate: SortedMap<String, Int>,
)
