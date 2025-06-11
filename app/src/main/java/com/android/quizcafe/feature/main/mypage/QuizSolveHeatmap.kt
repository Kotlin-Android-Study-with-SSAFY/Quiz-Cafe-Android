package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val days = remember { makeDayList(joinDate, today) }
    val weekLabels = weekLabelStrings()
    val grid = remember { makeQuizGrid(days) }
    val monthLabels = remember { makeMonthLabels(grid) }
    val yearLabel = remember { getYearLabel(joinDate, today) }
    val (maxStreak, currentStreak) = remember { calcStreakInfo(quizSolvingRecord, joinDate, today, kst) }
    val scrollState = rememberScrollState()

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
            .padding(bottom = 6.dp)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(44.dp))
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
            // 마지막 칸은 Spacer 생략
            if (idx != monthLabels.lastIndex) {
                Spacer(modifier = Modifier.width(6.dp))
            } else {
                Spacer(modifier = Modifier.width(8.dp))
            }
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
    grid: List<List<Calendar?>>,
    scrollState: ScrollState,
    quizSolvingRecord: Map<String, Int>,
    joinDate: Calendar,
    today: Calendar,
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
        for (w in grid.indices) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.End
            ) {
                for (d in 0..6) {
                    val cal = grid[w][d]
                    if (cal == null || cal.before(joinDate) || cal.after(today)) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(Color.Transparent)
                        )
                        continue
                    }
                    val dateKey = sdf.format(cal.time)
                    val count = quizSolvingRecord[dateKey] ?: 0
                    GrassCell(count)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f, fill = true)) // 오른쪽 남는 공간 채움
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
            color = grass3,
        )
        Text(
            text = stringResource(R.string.mypage_record_current_streak, currentStreak),
            style = MaterialTheme.typography.bodySmall,
            color = grass2,
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
@Preview(showBackground = true)
@Composable
fun PreviewQuizGrassGridByCalendar_Week() {
    PreviewQuizGrassGridByCalendar(days = 7)
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizGrassGridByCalendar_Month() {
    PreviewQuizGrassGridByCalendar(days = 30)
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizGrassGridByCalendar_3Month() {
    PreviewQuizGrassGridByCalendar(days = 90)
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizGrassGridByCalendar_6Month() {
    PreviewQuizGrassGridByCalendar(days = 180)
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizGrassGridByCalendar_Year() {
    PreviewQuizGrassGridByCalendar(days = 364)
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizGrassGridByCalendar_2Year() {
    PreviewQuizGrassGridByCalendar(days = 730)
}

@Composable
private fun PreviewQuizGrassGridByCalendar(days: Int) {
    val kst = TimeZone.getTimeZone("Asia/Seoul")
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = kst
    val today = Calendar.getInstance(kst).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val start = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -(days - 1)) }
    val quizHistory = mutableMapOf<String, Int>()
    for (i in 0 until days) {
        val cal = (start.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, i) }
        quizHistory[sdf.format(cal.time)] = (0..4).random()
    }
    Surface(color = Color(0xFFF6F6F6)) {
        Box(modifier = Modifier.padding(16.dp)) {
            QuizGrassGridByCalendar(
                quizSolvingRecord = quizHistory,
                joinDateStr = sdf.format(start.time),
            )
        }
    }
}
