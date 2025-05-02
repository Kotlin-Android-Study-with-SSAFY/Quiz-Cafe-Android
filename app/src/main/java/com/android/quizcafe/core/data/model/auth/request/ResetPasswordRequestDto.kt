package com.android.quizcafe.core.data.model.auth.request

import com.android.quizcafe.core.domain.model.auth.request.ResetPasswordRequest

data class ResetPasswordRequestDto(
    val email : String
)

fun ResetPasswordRequest.toDto() =
    ResetPasswordRequestDto(
        email = this.email
    )