package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun QuizGrassGridByCalendar(
    quizSolvingRecord: Map<String, Int>,
    startDateStr: String,
    modifier: Modifier = Modifier
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    val startDate = Calendar.getInstance().apply { time = sdf.parse(startDateStr)!! }
    val endDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val totalDays = getDaysBetween(startDate, endDate) + 1

    val days = mutableListOf<Calendar>()
    for (i in 0 until totalDays) {
        days.add(addDaysToCalendar(startDate, i))
    }

    val firstDayOfWeek = (days.first().get(Calendar.DAY_OF_WEEK) + 5) % 7
    val weekCount = ((firstDayOfWeek + days.size + 6) / 7)
    val grid = MutableList(weekCount) { MutableList<Calendar?>(7) { null } }
    var week = 0
    var dayOfWeek = firstDayOfWeek
    for (i in days.indices) {
        grid[week][dayOfWeek] = days[i]
        dayOfWeek++
        if (dayOfWeek == 7) {
            dayOfWeek = 0
            week++
        }
    }

    val monthLabels = MutableList(weekCount) { "" }
    var prevMonth: Int? = null
    for (w in grid.indices) {
        val firstNotNull = grid[w].firstOrNull { it != null }
        val month = firstNotNull?.get(Calendar.MONTH)
        if (month != null && month != prevMonth) {
            monthLabels[w] = getMonthShort(month)
            prevMonth = month
        }
    }

    val weekLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val scrollState = rememberScrollState()

    val (maxStreak, currentStreak) = remember(quizSolvingRecord, startDate, endDate) {
        calcStreakInfo(quizSolvingRecord, startDate, endDate)
    }

    Column(modifier) {
        Row(
            modifier = Modifier
                .padding(start = 36.dp, bottom = 4.dp)
                .horizontalScroll(scrollState)
        ) {
            monthLabels.forEach { label ->
                Box(
                    modifier = Modifier
                        .width(22.dp)
                        .height(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }
        }
        Row(verticalAlignment = Alignment.Top) {
            Column(
                modifier = Modifier.padding(end = 4.dp, top = 0.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                weekLabels.forEach { label ->
                    Box(
                        Modifier
                            .height(20.dp)
                            .width(32.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = label,
                            color = Color.Gray,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .border(1.dp, Color.Black.copy(alpha = 0.13f), RoundedCornerShape(4.dp))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                for (w in grid.indices) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        for (d in 0..6) {
                            val cal = grid[w][d]
                            val dateKey = cal?.let { sdf.format(it.time) }
                            val count = quizSolvingRecord[dateKey] ?: 0
                            val color = when {
                                cal == null -> Color.Transparent
                                count == 0 -> Color(0xFFEEEEEE)
                                count == 1 -> Color(0xFFB5E48C)
                                count in 2..3 -> Color(0xFF52B788)
                                count >= 4 -> Color(0xFF1B4332)
                                else -> Color.LightGray
                            }
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(color, RoundedCornerShape(3.dp))
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "한 번도 쉬지 않고 푼 최장 기록: ${maxStreak}일",
                color = Color(0xFF1B4332),
                fontSize = 13.sp,
            )
            Text(
                text = "연속 풀이 챌린지 달성: ${currentStreak}일",
                color = Color(0xFF388e3c),
                fontSize = 13.sp,
            )
        }
    }
}

fun calcStreakInfo(
    record: Map<String, Int>,
    start: Calendar,
    end: Calendar
): Pair<Int, Int> {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    var maxStreak = 0
    var curStreak = 0
    var currentStreak = 0

    var streakCounting = false
    var day = start.clone() as Calendar

    while (!day.after(end)) {
        val dateKey = sdf.format(day.time)
        val solved = (record[dateKey] ?: 0) > 0
        if (solved) {
            curStreak++
            if (!streakCounting) {
                streakCounting = true
            }
        } else {
            if (curStreak > maxStreak) maxStreak = curStreak
            curStreak = 0
            streakCounting = false
        }
        day.add(Calendar.DAY_OF_YEAR, 1)
    }

    if (curStreak > maxStreak) maxStreak = curStreak

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

fun getMonthShort(month: Int) = when (month) {
    0 -> "Jan"
    1 -> "Feb"
    2 -> "Mar"
    3 -> "Apr"
    4 -> "May"
    5 -> "Jun"
    6 -> "Jul"
    7 -> "Aug"
    8 -> "Sep"
    9 -> "Oct"
    10 -> "Nov"
    11 -> "Dec"
    else -> ""
}

fun getDaysBetween(start: Calendar, end: Calendar): Int {
    val startMillis = start.timeInMillis
    val endMillis = end.timeInMillis
    return ((endMillis - startMillis) / (24 * 60 * 60 * 1000)).toInt()
}

fun addDaysToCalendar(calendar: Calendar, days: Int): Calendar {
    val cal = calendar.clone() as Calendar
    cal.add(Calendar.DAY_OF_YEAR, days)
    return cal
}

@Preview(showBackground = true, backgroundColor = 0xFFF6F6F6)
@Composable
fun QuizGrassGridByCalendarPreview() {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val today = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val start = addDaysToCalendar(today, -364)

    val quizHistory = mutableMapOf<String, Int>()
    for (i in 0..364) {
        val cal = addDaysToCalendar(start, i)
        quizHistory[sdf.format(cal.time)] = (0..4).random()
    }

    Box(modifier = Modifier.padding(16.dp)) {
        QuizGrassGridByCalendar(
            quizSolvingRecord = quizHistory,
            startDateStr = sdf.format(start.time),
        )
    }
}
