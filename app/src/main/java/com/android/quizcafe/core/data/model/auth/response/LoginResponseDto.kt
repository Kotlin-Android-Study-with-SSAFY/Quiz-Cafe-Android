package com.android.quizcafe.core.data.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val accessToken : String
)