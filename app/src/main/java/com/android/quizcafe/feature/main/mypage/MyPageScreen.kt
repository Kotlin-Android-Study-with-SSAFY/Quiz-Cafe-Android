package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        // 유저 이름(가운데 정렬)
        Text(
            text = userName,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 24.dp),
            fontSize = 28.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        // 푼 문제/문제집 통계
        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Box(
                Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("푼 문제", fontSize = 16.sp, color = Color.Black)
                    Text("$solvedCount", fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
            // 세로 구분선
            Box(
                Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFE0E0E0))
            )
            Box(
                Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("문제집", fontSize = 16.sp, color = Color.Black)
                    Text("$myQuizSetCount", fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider(
            modifier = Modifier.height(1.dp),
            color = Color(0xFFE0E0E0)
        )

        // 메뉴 리스트
        Column(Modifier.fillMaxWidth()) {
            MyPageMenuItem("학습 통계", onClickStats)
            MyPageMenuItem("알림 설정", onClickAlarm)
            MyPageMenuItem("비밀번호 변경", onClickChangePw)
            MyPageMenuItem("내가 만든 문제집 보기", onClickMyQuizSet, isLast = true)
        }

        Spacer(Modifier.height(28.dp))
        QuizGrassGridByCalendar(
            quizSolvingRecord = quizSolvingRecord,
            joinDateStr = startDateStr
        )
    }
}

@Composable
fun MyPageMenuItem(
    title: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(22.dp)
            )
        }
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.height(1.dp),
                color = Color(0xFFE0E0E0)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFDFDFD)
@Composable
fun PreviewMyPageScreen() {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val today = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val start = addDaysToCalendar(today, -364) // 364일 전이 1년(365개)

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
