package com.android.quizcafe.core.data.model.auth

import com.android.quizcafe.core.domain.model.auth.VerifyCodeRequest
import kotlinx.serialization.SerialName

data class VerifyCodeRequestDto(
    @SerialName("toMail")
    val email : String,
    val code : String
)

fun VerifyCodeRequest.toDto() =
    VerifyCodeRequestDto(
        email = this.email,
        code = this.code
    )