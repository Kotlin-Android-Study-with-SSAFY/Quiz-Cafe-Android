package com.android.quizcafe.core.data.model.user.request

import com.android.quizcafe.core.domain.model.user.request.UpdatePasswordRequest

data class UpdatePasswordRequestDto(
    val oldPassword: String,
    val newPassword: String
)

fun UpdatePasswordRequest.toDto() =
    UpdatePasswordRequestDto(
        oldPassword = this.oldPassword,
        newPassword = this.newPassword
    )

