package com.android.quizcafe.core.data.model.auth.request

import com.android.quizcafe.core.domain.model.auth.request.SignUpRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    val id: Long? = null,
    @SerialName("loginEmail")
    val email: String,
    val password: String,
    val nickName: String
)

fun SignUpRequest.toDto() =
    SignUpRequestDto(
        id = id,
        email = email,
        password = password,
        nickName = nickName
    )