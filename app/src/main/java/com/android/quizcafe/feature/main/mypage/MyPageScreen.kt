package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun MyPageScreen(
    state: MyPageViewState,
    onClick: (MyPageIntent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 18.dp, end = 18.dp)
    ) {
        MyPageUserName(state.nickname)
        MyPageSummary(state.solvedCount, state.myQuizSetCount)
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(modifier = Modifier.height(1.dp))
        MyPageMenu { menuId ->
            when (menuId) {
                0 -> onClick(MyPageIntent.ClickStats(state.solvedCount))
                1 -> onClick(MyPageIntent.ClickAlarm)
                2 -> onClick(MyPageIntent.ClickChangePw)
                3 -> onClick(MyPageIntent.ClickMyQuizSet)
            }
        }
        Spacer(Modifier.height(28.dp))
        QuizGrassGridByCalendar(
            quizSolvingRecord = state.quizSolvingRecord,
            joinDateStr = state.joinDateStr
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFDFDFD)
@Composable
fun PreviewMyPageScreen() {
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
    for (i in 0 until 364) {
        val cal = (start.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, i) }
        quizHistory[sdf.format(cal.time)] = (0..4).random()
    }
    MyPageScreen(
        state = MyPageViewState(
            nickname = "빵빠야",
            solvedCount = 1205,
            myQuizSetCount = 5,
            quizSolvingRecord = quizHistory,
            joinDateStr = sdf.format(start.time)
        ),
        onClick = {}
    )
}
