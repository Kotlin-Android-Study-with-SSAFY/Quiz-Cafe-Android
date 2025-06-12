package com.android.quizcafe.feature.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

private fun String.safeToDate(): Date? = try {
    SimpleDateFormat(ISO_PATTERN, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse(this)
} catch (e: Exception) {
    null
}

fun String.safeToRelativeTime(now: Long = System.currentTimeMillis()): String {
    val date = safeToDate() ?: return ""
    val diffMillis = now - date.time
    val diffMinutes = diffMillis / (60 * 1000)
    val diffHours = diffMillis / (60 * 60 * 1000)
    val diffDays = diffMillis / (24 * 60 * 60 * 1000)
    return when {
        diffMinutes < 1 -> "방금 전"
        diffHours < 1 -> "${diffMinutes}분 전"
        diffDays < 1 -> "${diffHours}시간 전"
        else -> "${diffDays}일 전"
    }
}
