package com.android.quizcafe.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: String,
    val code: Int,
    val message: String,
    val data: T? = null
)
