package com.android.quizcafe.core.domain.model.auth

data class SignUpRequest(
    val id: Long? = null,
    val email: String,
    val password: String,
    val nickName: String
)