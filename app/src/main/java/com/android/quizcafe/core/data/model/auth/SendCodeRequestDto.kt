package com.android.quizcafe.core.data.model.auth

import com.android.quizcafe.core.domain.model.auth.SendCodeRequest

data class SendCodeRequestDto(
    val email : String
)

fun SendCodeRequest.toDto() =
    SendCodeRequestDto(
        email = this.email
    )