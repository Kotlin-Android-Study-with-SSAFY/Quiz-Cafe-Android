package com.android.quizcafe.feature.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val ISO_PATTERN_FULL = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private const val ISO_PATTERN_SHORT = "yyyy-MM-dd'T'HH:mm:ss"

private fun String.safeToDate(): Date? {
    val utc = TimeZone.getTimeZone("UTC")
    val fullInput = if (this.endsWith("Z")) this else this + "Z"
    val fullFormat = SimpleDateFormat(ISO_PATTERN_FULL, Locale.getDefault()).apply { timeZone = utc }
    return try {
        fullFormat.parse(fullInput)
    } catch (_: Exception) {
        try {
            SimpleDateFormat(ISO_PATTERN_SHORT, Locale.getDefault()).apply { timeZone = utc }.parse(this)
        } catch (_: Exception) {
            null
        }
    }
}

/**
 * 상대 시간 문자열로 변환:
 * - 1분 미만 → "방금 전"
 * - 1시간 미만 → "n분 전"
 * - 1일 미만 → "n시간 전"
 * - 그 외 → "n일 전"
 */
fun String.safeToRelativeTime(now: Long = System.currentTimeMillis()): String {
    val date = safeToDate() ?: return ""
    val diff = now - date.time
    val minutes = diff / (60 * 1000)
    val hours = diff / (60 * 60 * 1000)
    val days = diff / (24 * 60 * 60 * 1000)
    return when {
        minutes < 1 -> "방금 전"
        hours < 1 -> "${minutes}분 전"
        days < 1 -> "${hours}시간 전"
        else -> "${days}일 전"
    }
}
