package com.android.quizcafe.core.data.model.user.response

import com.android.quizcafe.core.domain.model.user.response.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseDto(
    val nickname: String,
    val email: String,
    val joinDateStr: String =""
)

fun UserInfoResponseDto.toDomain(
    quizCount: Int,
    quizBookCount: Int,
    joinDateStr: String,
    quizSolvingRecord: Map<String, Int>
) = UserInfo(
    nickname = nickname,
    email = email,
    quizCount = quizCount,
    quizBookCount = quizBookCount,
    joinDateStr = joinDateStr,
    quizSolvingRecord = quizSolvingRecord
)
