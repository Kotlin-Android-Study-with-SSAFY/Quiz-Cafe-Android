package com.android.quizcafe.core.data.model.auth.request

import com.android.quizcafe.core.domain.model.auth.request.SendCodeRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendCodeRequestDto(
    @SerialName("toMail")
    val email : String,
    val type : String
)

fun SendCodeRequest.toDto() =
    SendCodeRequestDto(
        email = this.email,
        type = this.type
    )
