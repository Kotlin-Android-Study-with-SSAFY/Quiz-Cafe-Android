package com.android.quizcafe.core.domain.model.auth.request

data class VerifyCodeRequest(
    val email: String,
    val code: String
)
