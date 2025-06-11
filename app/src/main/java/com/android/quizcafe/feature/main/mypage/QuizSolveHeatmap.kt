package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * 날짜별 퀴즈 풀이 기록을 1년 단위로 "잔디(Grass) 형태"로 시각화하는 컴포저블.
 * - 좌측 상단부터 유저 가입일~오늘까지 365칸(혹은 1년 이내면 그만큼)을 7xN 격자(주차별)로 표현.
 * - 월별 레이블, 요일 레이블 표시.
 * - 각 셀 색상은 해당 날짜 푼 횟수에 따라 다름(0~4회+ 구간).
 * - 오늘이 반드시 마지막 칸(오른쪽 하단)에 오며, 진입 시 자동 스크롤로 오늘이 바로 보임.
 * - 최장 연속 기록/연속 풀이 기록도 함께 표시.
 */
@Composable
fun quizGrassGridByCalendar(
    quizSolvingRecord: Map<String, Int>,
    joinDateStr: String,
    modifier: Modifier = Modifier
) {
    val kst = TimeZone.getTimeZone("Asia/Seoul")
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply { timeZone = kst } }
    val today = remember {
        Calendar.getInstance(kst).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
    val joinDate = remember(joinDateStr) {
        if (joinDateStr.isBlank()) {
            (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -364) }
        } else {
            val cal = Calendar.getInstance(kst)
            val parsed = sdf.runCatching { parse(joinDateStr) }.getOrNull()
            if (parsed != null) {
                cal.time = parsed
                cal
            } else {
                (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -364) }
            }
        }
    }
    val days = remember(joinDate, today) {
        val list = mutableListOf<Calendar>()
        val cal = (joinDate.clone() as Calendar)
        while (!cal.after(today)) {
            list.add(cal.clone() as Calendar)
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        if (list.isEmpty() || list.last().before(today)) {
            list.add(today.clone() as Calendar)
        }
        list
    }
    val firstDayOfWeek = (days.first().get(Calendar.DAY_OF_WEEK) + 5) % 7
    val totalGridCount = ((firstDayOfWeek + days.size + 6) / 7) * 7
    val weekCount = totalGridCount / 7
    val grid = remember(days) {
        MutableList(weekCount) { MutableList<Calendar?>(7) { null } }.apply {
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
    val monthLabels = remember(grid) {
        MutableList(weekCount) { "" }.apply {
            var prevMonth: Int? = null
            for (w in grid.indices) {
                val firstNotNull = grid[w].firstOrNull { it != null }
                val month = firstNotNull?.get(Calendar.MONTH)
                if (month != null && month != prevMonth) {
                    this[w] = getMonthShort(month)
                    prevMonth = month
                }
            }
        }
    }
    val weekLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val scrollState = rememberScrollState()
    LaunchedEffect(weekCount) {
        scrollState.scrollTo(Int.MAX_VALUE)
    }
    val yearLabel = remember(joinDate, today) {
        val startYear = joinDate.get(Calendar.YEAR)
        val endYear = today.get(Calendar.YEAR)
        if (startYear == endYear) "$startYear" else "$startYear ~ $endYear"
    }
    val (maxStreak, currentStreak) = remember(quizSolvingRecord, joinDate, today) {
        calcStreakInfo(quizSolvingRecord, joinDate, today, kst)
    }
    Column(modifier) {
        Text(
            text = "나의 기록",
            fontSize = 20.sp,
            color = Color(0xFF263238),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 2.dp).align(Alignment.CenterHorizontally)
        )
        Text(
            text = yearLabel,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 10.dp).align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier.padding(start = 36.dp, bottom = 4.dp).horizontalScroll(scrollState)
        ) {
            monthLabels.forEach { label ->
                Box(
                    modifier = Modifier.width(22.dp).height(14.dp),
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
                        Modifier.height(20.dp).width(32.dp),
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
                                count == 0 -> Color(0xFFF1F6FB)
                                count == 1 -> Color(0xFFAFCAFF)
                                count in 2..3 -> Color(0xFF6E9DD8)
                                count >= 4 -> Color(0xFF3853A4)
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
        Column(
            modifier = Modifier.padding(top = 12.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "한 번도 쉬지 않고 푼 최장 기록: ${maxStreak}일",
                color = Color(0xFF3853A4),
                fontSize = 13.sp,
            )
            Text(
                text = "연속 풀이 챌린지 달성: ${currentStreak}일",
                color = Color(0xFF4278C9),
                fontSize = 13.sp,
            )
        }
    }
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
    var currentStreak = 0
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
    currentStreak = 0
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

@Preview(showBackground = true, backgroundColor = 0xFFF6F6F6)
@Composable
fun quizGrassGridByCalendarPreview() {
    val kst = TimeZone.getTimeZone("Asia/Seoul")
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = kst
    val today = Calendar.getInstance(kst).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val start = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -364) }
    val quizHistory = mutableMapOf<String, Int>()
    for (i in 0..364) {
        val cal = (start.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, i) }
        quizHistory[sdf.format(cal.time)] = (0..4).random()
    }
    Box(modifier = Modifier.padding(16.dp)) {
        quizGrassGridByCalendar(
            quizSolvingRecord = quizHistory,
            joinDateStr = sdf.format(start.time),
        )
    }
}
