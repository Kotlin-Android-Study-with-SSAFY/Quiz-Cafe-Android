package com.android.quizcafe.core.data.model.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequestDto(
    @SerialName("id_token")
    val idToken: String
)
