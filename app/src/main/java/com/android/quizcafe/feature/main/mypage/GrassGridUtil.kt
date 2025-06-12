package com.android.quizcafe.feature.main.mypage

import androidx.compose.ui.graphics.Color
import com.android.quizcafe.core.designsystem.theme.grass1
import com.android.quizcafe.core.designsystem.theme.grass2
import com.android.quizcafe.core.designsystem.theme.grass3
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun getGrassColor(count: Int): Color = when {
    count == 0 -> Color.White
    count == 1 -> grass1
    count in 2..3 -> grass2
    count >= 4 -> grass3
    else -> Color.LightGray
}

fun rememberSdf(timeZone: TimeZone): SimpleDateFormat =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
        this.timeZone = timeZone
    }

fun rememberTodayCalendar(timeZone: TimeZone): Calendar =
    Calendar.getInstance(timeZone).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

fun rememberJoinDate(
    joinDateStr: String,
    sdf: SimpleDateFormat,
    today: Calendar,
    kst: TimeZone
): Calendar {
    val cal = Calendar.getInstance(kst)
    val parsed = sdf.runCatching { parse(joinDateStr) }.getOrNull()
    return if (parsed != null) {
        cal.time = parsed
        cal
    } else {
        (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -6) }
    }
}

fun makeDayList(start: Calendar, end: Calendar): List<Calendar> {
    val list = mutableListOf<Calendar>()
    val cal = (start.clone() as Calendar)
    while (!cal.after(end)) {
        list.add(cal.clone() as Calendar)
        cal.add(Calendar.DAY_OF_YEAR, 1)
    }
    return list
}

fun makeQuizGrid(days: List<Calendar>): List<List<Calendar?>> {
    val firstDayOfWeek = (days.first().get(Calendar.DAY_OF_WEEK) + 5) % 7
    val totalGridCount = ((firstDayOfWeek + days.size + 6) / 7) * 7
    val weekCount = totalGridCount / 7
    return MutableList(weekCount) { MutableList<Calendar?>(7) { null } }.apply {
        var week = 0
        var dayOfWeek = firstDayOfWeek
        for (day in days) {
            this[week][dayOfWeek] = day
            dayOfWeek++
            if (dayOfWeek == 7) {
                dayOfWeek = 0
                week++
            }
        }
    }
}

fun makeMonthLabels(grid: List<List<Calendar?>>): List<String> {
    val weekCount = grid.size
    return MutableList(weekCount) { "" }.apply {
        var prevMonth: Int? = null
        for (w in grid.indices) {
            val firstNotNull = grid[w].firstOrNull { it != null }
            val month = firstNotNull?.get(Calendar.MONTH)
            if (month != null && month != prevMonth) {
                this[w] = (month + 1).toString()
                prevMonth = month
            }
        }
    }
}

fun getYearLabel(joinDate: Calendar, today: Calendar): String {
    val startYear = joinDate.get(Calendar.YEAR)
    val endYear = today.get(Calendar.YEAR)
    return if (startYear == endYear) "$startYear" else "$startYear ~ $endYear"
}

fun calcStreakInfo(
    record: Map<String, Int>,
    start: Calendar,
    end: Calendar,
    timeZone: TimeZone = TimeZone.getTimeZone("Asia/Seoul")
): Pair<Int, Int> {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = timeZone
    var maxStreak = 0
    var curStreak = 0
    var day = start.clone() as Calendar
    while (!day.after(end)) {
        val dateKey = sdf.format(day.time)
        val solved = (record[dateKey] ?: 0) > 0
        if (solved) {
            curStreak++
        } else {
            if (curStreak > maxStreak) maxStreak = curStreak
            curStreak = 0
        }
        day.add(Calendar.DAY_OF_YEAR, 1)
    }
    if (curStreak > maxStreak) maxStreak = curStreak
    var currentStreak = 0
    day = end.clone() as Calendar
    while (!day.before(start)) {
        val dateKey = sdf.format(day.time)
        val solved = (record[dateKey] ?: 0) > 0
        if (solved) {
            currentStreak++
        } else {
            break
        }
        day.add(Calendar.DAY_OF_YEAR, -1)
    }
    return Pair(maxStreak, currentStreak)
}
