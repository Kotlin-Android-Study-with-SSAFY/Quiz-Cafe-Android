package com.android.quizcafe.core.data.model.user.request

data class ChangePasswordRequestDto(
    val oldPassword: String,
    val newPassword: String
)
