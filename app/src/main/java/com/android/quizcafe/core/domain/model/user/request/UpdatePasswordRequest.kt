package com.android.quizcafe.core.domain.model.user.request

data class UpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
