package com.android.quizcafe.core.data.model.auth.request

import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import kotlinx.serialization.Serializable

@Serializable
data class SendCodeRequestDto(
    val email : String
)

fun SendCodeRequest.toDto() =
    SendCodeRequestDto(
        email = this.email
    )