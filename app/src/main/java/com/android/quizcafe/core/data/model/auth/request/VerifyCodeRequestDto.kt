package com.android.quizcafe.core.data.model.auth.request

import com.android.quizcafe.core.domain.model.auth.request.VerifyCodeRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
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