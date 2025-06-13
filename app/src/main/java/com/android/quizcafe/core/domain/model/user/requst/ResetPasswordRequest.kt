package com.android.quizcafe.core.domain.model.user.requst

data class ResetPasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
