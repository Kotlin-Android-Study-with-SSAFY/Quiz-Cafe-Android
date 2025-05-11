package com.android.quizcafe.core.domain.model.auth.request

data class SendCodeRequest(
    val email: String,
    val type: String
)
