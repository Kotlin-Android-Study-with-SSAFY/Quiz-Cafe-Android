package com.android.quizcafe.core.data.model.user.request

import com.android.quizcafe.core.domain.model.user.request.UpdateNicknameRequest
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNicknameRequestDto(
    val nickname: String
)

fun UpdateNicknameRequest.toDto() =
    UpdateNicknameRequestDto(
        nickname = this.nickname
    )
