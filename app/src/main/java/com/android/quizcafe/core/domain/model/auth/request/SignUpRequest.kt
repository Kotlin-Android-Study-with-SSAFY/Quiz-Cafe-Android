package com.android.quizcafe.core.domain.model.auth.request

data class SignUpRequest(
    val id: Long? = null,
    val email: String,
    val password: String,
    val nickName: String
)