package com.android.quizcafe.core.data.model.auth.request

import com.android.quizcafe.core.domain.model.auth.request.SignInRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestDto(
    @SerialName("loginEmail")
    val email : String,
    val password : String
)

fun SignInRequest.toDto() =
    SignInRequestDto(
        email = this.email,
        password = this.password
    )
