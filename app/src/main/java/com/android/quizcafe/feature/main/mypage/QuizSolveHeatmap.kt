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

// TODO : Merge 되면 quizSolvingRecord Data Class 연결하기
@Composable
fun QuizGrassGridByCalendar(
    quizSolvingRecord: Map<String, Int>,
    joinDateStr: String,
    modifier: Modifier = Modifier
) {
    // 날짜 계산
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") } }
    val joinDate = remember { Calendar.getInstance().apply { time = sdf.parse(joinDateStr)!! } }
    val today = remember { Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }}

    // (1) 1년 미만이면 회원가입일부터 1년 뒤(365일)까지, 1년 이상이면 오늘 기준 1년 전부터 오늘까지
    val (startDate, endDate) = remember(joinDate, today) {
        val oneYear = 364
        val dayDiff = getDaysBetween(joinDate, today)
        if (dayDiff < oneYear) {
            val end = (joinDate.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, oneYear) }
            Pair(joinDate, end)
        } else {
            val start = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -oneYear) }
            Pair(start, today)
        }
    }

    // (2) 잔디(그래스) 데이터 구성 (365칸)
    val totalDays = getDaysBetween(startDate, endDate) + 1
    val days = remember(startDate, endDate) {
        List(totalDays) { addDaysToCalendar(startDate, it) }
    }

    // (3) 요일, 주차 계산
    val firstDayOfWeek = (days.first().get(Calendar.DAY_OF_WEEK) + 5) % 7 // 월~일: 0~6
    val weekCount = ((firstDayOfWeek + days.size + 6) / 7)
    val grid = remember(days) {
        MutableList(weekCount) { MutableList<Calendar?>(7) { null } }.apply {
            var week = 0
            var dayOfWeek = firstDayOfWeek
            for (i in days.indices) {
                this[week][dayOfWeek] = days[i]
                dayOfWeek++
                if (dayOfWeek == 7) {
                    dayOfWeek = 0
                    week++
                }
            }
        }
    }

    // (4) 월 라벨
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

    // (5) 연도 라벨(상단은 고정)
    val yearLabel = remember(startDate, endDate) {
        val startYear = startDate.get(Calendar.YEAR)
        val endYear = endDate.get(Calendar.YEAR)
        if (startYear == endYear) "$startYear" else "$startYear ~ $endYear"
    }

    val (maxStreak, currentStreak) = remember(quizSolvingRecord, startDate, endDate) {
        calcStreakInfo(quizSolvingRecord, startDate, endDate)
    }

    Column(modifier) {
        // --- 상단은 항상 고정 ---
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

        // --- 잔디/그래스 구간은 좌우 스크롤 ---
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

            // 잔디 영역
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
                                // ------ QuizCafe 전용 컬러 팔레트 ------
                                count == 0 -> Color(0xFFF1F6FB)
                                count == 1 -> Color(0xFFAFCAFF)   // primaryLight
                                count in 2..3 -> Color(0xFF6E9DD8) // 메인 대비 진한톤
                                count >= 4 -> Color(0xFF3853A4)    // 가장 진함
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
        // --- streak info ---
        Column (
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
            joinDateStr = sdf.format(start.time),
        )
    }
}
