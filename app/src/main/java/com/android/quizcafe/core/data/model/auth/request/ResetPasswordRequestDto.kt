package com.android.quizcafe.core.data.model.auth.request

import com.android.quizcafe.core.domain.model.auth.request.ResetPasswordRequest
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
