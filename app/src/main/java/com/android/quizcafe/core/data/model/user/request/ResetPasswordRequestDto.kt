package com.android.quizcafe.core.data.model.user.request

import com.android.quizcafe.core.domain.model.user.requst.ResetPasswordRequest
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestDto(
    val oldPassword: String,
    val newPassword: String
)

fun ResetPasswordRequest.toDto() =
    ResetPasswordRequestDto(
        oldPassword = this.oldPassword,
        newPassword = this.newPassword
    )
