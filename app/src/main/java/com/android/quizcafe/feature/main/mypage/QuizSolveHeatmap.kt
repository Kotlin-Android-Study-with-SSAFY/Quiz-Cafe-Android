@file:RequiresApi(Build.VERSION_CODES.O)

package com.android.quizcafe.feature.main.mypage

import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuizGrassGridByCalendar(
    quizSolvingRecord: Map<String, Int>,
    joinDateStr: String,
    modifier: Modifier = Modifier
) {
    // 1. parse join & today
    val joinDate = parseJoinDate(joinDateStr)
    val today = todayKst()

    // 2. days, grid, labels
    val days = generateDayList(joinDate, today)
    val grid = buildQuizGrid(days)
    val monthLabels = createMonthLabels(grid)
    val yearLabel = formatYearLabel(joinDate, today)
    val weekLabels = weekLabelStrings()

    // 3. streaks
    val (maxStreak, currentStreak) = calculateStreak(
        record = quizSolvingRecord,
        start = joinDate,
        end = today
    )

    val scrollState = rememberScrollState()
    LaunchedEffect(grid) { scrollState.scrollTo(scrollState.maxValue) }

    Column(modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.mypage_record_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 2.dp)
        )
        Text(
            text = yearLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        )
        MonthRow(monthLabels, scrollState)
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            WeekLabelColumn(weekLabels)
            GrassGridContent(grid, scrollState, quizSolvingRecord, joinDate, today)
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
                Modifier
                    .size(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
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
                    .size(width = 32.dp, height = 18.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(label, style = MaterialTheme.typography.labelMedium, color = weekLabelColor)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun GrassGridContent(
    grid: List<List<LocalDate?>>,
    scrollState: ScrollState,
    quizSolvingRecord: Map<String, Int>,
    joinDate: LocalDate,
    today: LocalDate
) {
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .border(1.dp, gridBorder, RoundedCornerShape(4.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        grid.forEach { week ->
            Column(verticalArrangement = Arrangement.spacedBy(6.dp), horizontalAlignment = Alignment.End) {
                week.forEach { date ->
                    if (date == null || date.isBefore(joinDate) || date.isAfter(today)) {
                        Box(
                            Modifier
                                .size(18.dp)
                                .background(Color.Transparent)
                        )
                    } else {
                        val key = date.format(fmt)
                        val count = quizSolvingRecord[key] ?: 0
                        GrassCell(count)
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun GrassCell(count: Int) {
    val color = getGrassColor(count)
    val border = if (count == 0) Modifier.border(1.dp, gridBorder, RoundedCornerShape(3.dp)) else Modifier
    Box(
        border
            .then(Modifier.size(18.dp))
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

// --- Previews ---
@Preview(showBackground = true, name = "OneDayStreak")
@Composable
fun PreviewOneDay() = PreviewQuizGrass(days = 1)

@Preview(showBackground = true, name = "OneWeekStreak")
@Composable
fun PreviewOneWeek() = PreviewQuizGrass(days = 7)

@Preview(showBackground = true, name = "OneMonthStreak")
@Composable
fun PreviewOneMonth() = PreviewQuizGrass(days = 30)

@Composable
private fun PreviewQuizGrass(days: Int) {
    // generate mock record
    val today = LocalDate.now(ZoneId.of("Asia/Seoul"))
    val start = today.minusDays((days - 1).toLong())
    val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val history = (0 until days).associate { i ->
        val d = start.plusDays(i.toLong())
        d.format(fmt) to (0..5).random()
    }
    Surface(color = Color(0xFFF6F6F6)) {
        Box(Modifier.padding(16.dp)) {
            QuizGrassGridByCalendar(history, start.format(fmt))
        }
    }
}
