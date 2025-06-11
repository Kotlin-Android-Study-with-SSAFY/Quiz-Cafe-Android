package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun MyPageScreen(
    userName: String = "빵빠야",
    solvedCount: Int = 1205,
    myQuizSetCount: Int = 5,
    quizSolvingRecord: Map<String, Int> = emptyMap(),
    startDateStr: String = "",
    onClickStats: () -> Unit = {},
    onClickAlarm: () -> Unit = {},
    onClickChangePw: () -> Unit = {},
    onClickMyQuizSet: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 18.dp, end = 18.dp)
    ) {
        MyPageUserName(userName)
        MyPageSummary(solvedCount, myQuizSetCount)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(
            modifier = Modifier.height(1.dp),
            color = Color(0xFFE0E0E0)
        )
        MyPageMenu(
            onClickStats,
            onClickAlarm,
            onClickChangePw,
            onClickMyQuizSet
        )
        Spacer(Modifier.height(28.dp))
        QuizGrassGridByCalendar(
            quizSolvingRecord = quizSolvingRecord,
            joinDateStr = startDateStr
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFDFDFD)
@Composable
fun PreviewMyPageScreen() {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val today = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val start = addDaysToCalendar(today, -364)
    val quizHistory = mutableMapOf<String, Int>()
    for (i in 0..364) {
        val cal = addDaysToCalendar(start, i)
        quizHistory[sdf.format(cal.time)] = (0..4).random()
    }
    MyPageScreen(
        userName = "빵빠야",
        solvedCount = 1205,
        myQuizSetCount = 5,
        quizSolvingRecord = quizHistory,
        startDateStr = sdf.format(start.time),
    )
}
