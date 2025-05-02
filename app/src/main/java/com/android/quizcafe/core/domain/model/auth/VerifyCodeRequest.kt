package com.android.quizcafe.core.domain.model.auth

data class VerifyCodeRequest(
    val email : String,
    val code : String

)
