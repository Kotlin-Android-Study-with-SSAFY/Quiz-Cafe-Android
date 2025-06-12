package com.android.quizcafe.core.data.model.user.response

import com.android.quizcafe.core.domain.model.user.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseDto(
    val nickname: String,
    val email: String
)

fun UserInfoResponseDto.toDomain() = UserInfo(
    nickname = nickname
)
