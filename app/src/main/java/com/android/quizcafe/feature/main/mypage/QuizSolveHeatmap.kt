package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.grass2
import com.android.quizcafe.core.designsystem.theme.grass3
import com.android.quizcafe.core.designsystem.theme.gridBorder
import com.android.quizcafe.core.designsystem.theme.weekLabelColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun QuizGrassGridByCalendar(
    quizSolvingRecord: Map<String, Int>,
    joinDateStr: String,
    modifier: Modifier = Modifier
) {
    val kst = TimeZone.getTimeZone("Asia/Seoul")
    val sdf = rememberSdf(kst)
    val today = rememberTodayCalendar(kst)
    val joinDate = rememberJoinDate(joinDateStr, sdf, today, kst)
    val days = makeDayList(joinDate, today)
    val weekLabels = weekLabelStrings()
    val grid = makeQuizGrid(days)
    val monthLabels = makeMonthLabels(grid)
    val yearLabel = getYearLabel(joinDate, today)

    val (maxStreak, currentStreak) = calcStreakInfo(
        record = quizSolvingRecord,
        start = joinDate,
        end = today,
        timeZone = kst
    )
    val scrollState = rememberScrollState()

    LaunchedEffect(grid) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.mypage_record_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(bottom = 2.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = yearLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(Alignment.CenterHorizontally)
        )
        MonthRow(monthLabels, scrollState)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            WeekLabelColumn(weekLabels)
            GrassGridContent(
                grid = grid,
                scrollState = scrollState,
                quizSolvingRecord = quizSolvingRecord,
                joinDate = joinDate,
                today = today,
                sdf = sdf
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            StreakSummary(maxStreak, currentStreak)
        }
    }
}

@Composable
private fun MonthRow(monthLabels: List<String>, scrollState: ScrollState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 44.dp, bottom = 6.dp)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        monthLabels.forEachIndexed { idx, label ->
            Box(
                modifier = Modifier
                    .width(18.dp)
                    .height(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(if (idx != monthLabels.lastIndex) 6.dp else 14.dp))
        }
    }
}

@Composable
private fun WeekLabelColumn(weekLabels: List<String>) {
    Column(
        modifier = Modifier.padding(end = 4.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        weekLabels.forEach { label ->
            Box(
                Modifier
                    .width(32.dp)
                    .height(18.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = weekLabelColor,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun GrassGridContent(
    grid: List<List<Calendar?>>, scrollState: ScrollState,
    quizSolvingRecord: Map<String, Int>,
    joinDate: Calendar, today: Calendar,
    sdf: SimpleDateFormat
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .border(1.dp, gridBorder, RoundedCornerShape(4.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        grid.forEach { week ->
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.End
            ) {
                week.forEach { cal ->
                    if (cal == null || cal.before(joinDate) || cal.after(today)) {
                        Box(
                            Modifier
                                .size(18.dp)
                                .background(Color.Transparent)
                        )
                    } else {
                        val dateKey = sdf.format(cal.time)
                        val count = quizSolvingRecord[dateKey] ?: 0
                        GrassCell(count)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f, fill = true))
    }
}

@Composable
private fun GrassCell(count: Int) {
    val color = getGrassColor(count)
    val border = if (count == 0) Modifier.border(1.dp, gridBorder, RoundedCornerShape(3.dp)) else Modifier
    Box(
        modifier = Modifier
            .size(18.dp)
            .then(border)
            .background(color, RoundedCornerShape(3.dp))
    )
}

@Composable
private fun StreakSummary(maxStreak: Int, currentStreak: Int) {
    Column {
        Text(
            text = stringResource(R.string.mypage_record_max_streak, maxStreak),
            style = MaterialTheme.typography.bodySmall,
            color = grass3
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.mypage_record_current_streak, currentStreak),
            style = MaterialTheme.typography.bodySmall,
            color = grass2
        )
    }
}

@Composable
private fun weekLabelStrings(): List<String> = listOf(
    stringResource(R.string.week_mon),
    stringResource(R.string.week_tue),
    stringResource(R.string.week_wed),
    stringResource(R.string.week_thu),
    stringResource(R.string.week_fri),
    stringResource(R.string.week_sat),
    stringResource(R.string.week_sun)
)

// --- Preview: 다양한 기간 프리셋 보여주기 ---

@Preview(showBackground = true, name = "OneDayStreak")
@Composable
fun PreviewQuizGrassGridByCalendar_OneDay() {
    PreviewQuizGrassGridByCalendar(days = 1)
}

@Preview(showBackground = true, name = "OneWeekStreak")
@Composable
fun PreviewQuizGrassGridByCalendar_Week() {
    PreviewQuizGrassGridByCalendar(days = 7)
}

@Preview(showBackground = true, name = "OneMonthStreak")
@Composable
fun PreviewQuizGrassGridByCalendar_Month() {
    PreviewQuizGrassGridByCalendar(days = 30)
}

@Preview(showBackground = true, name = "ThreeMonthStreak")
@Composable
fun PreviewQuizGrassGridByCalendar_3Month() {
    PreviewQuizGrassGridByCalendar(days = 90)
}

@Composable
private fun PreviewQuizGrassGridByCalendar(days: Int) {
    val kst = TimeZone.getTimeZone("Asia/Seoul")
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply { timeZone = kst }
    val today = Calendar.getInstance(kst).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val start = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -(days - 1)) }
    val quizHistory = mutableMapOf<String, Int>().apply {
        repeat(days) { i ->
            val cal = (start.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, i) }
            put(sdf.format(cal.time), (0..90).random())
        }
    }
    Surface(color = Color(0xFFF6F6F6)) {
        Box(modifier = Modifier.padding(16.dp)) {
            QuizGrassGridByCalendar(
                quizSolvingRecord = quizHistory,
                joinDateStr = sdf.format(start.time)
            )
        }
    }
}
