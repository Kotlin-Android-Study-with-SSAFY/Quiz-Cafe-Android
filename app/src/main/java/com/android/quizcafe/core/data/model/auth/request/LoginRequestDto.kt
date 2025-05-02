package com.android.quizcafe.core.data.model.auth.request

import com.android.quizcafe.core.domain.model.auth.request.LoginRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    @SerialName("loginEmail")
    val email : String,
    val password : String
)

fun LoginRequest.toDto() =
    LoginRequestDto(
        email = this.email,
        password = this.password
    )
