package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.quizcafe.R

@Composable
fun MyPageUserName(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = userName,
            fontSize = 28.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MyPageSummary(solvedCount: Int, myQuizSetCount: Int) {
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
                Text(
                    stringResource(id = R.string.mypage_solved_problem),
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    "$solvedCount",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
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
                Text(
                    stringResource(id = R.string.mypage_my_quiz_set),
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    "$myQuizSetCount",
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MyPageMenu(
    onClickStats: () -> Unit = {},
    onClickAlarm: () -> Unit = {},
    onClickChangePw: () -> Unit = {},
    onClickMyQuizSet: () -> Unit = {}
) {
    Column(Modifier.fillMaxWidth()) {
        MyPageMenuItem(stringResource(R.string.mypage_menu_stats), onClickStats)
        MyPageMenuItem(stringResource(R.string.mypage_menu_alarm), onClickAlarm)
        MyPageMenuItem(stringResource(R.string.mypage_menu_change_pw), onClickChangePw)
        MyPageMenuItem(stringResource(R.string.mypage_menu_my_quiz_set), onClickMyQuizSet, isLast = true)
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

@Preview(showBackground = true)
@Composable
fun PreviewMyPageSummary() {
    MyPageSummary(solvedCount = 1205, myQuizSetCount = 5)
}

@Preview(showBackground = true)
@Composable
fun PreviewMyPageUserName() {
    MyPageUserName("빵빠야")
}

@Preview(showBackground = true)
@Composable
fun PreviewMyPageMenu() {
    Column {
        MyPageMenuItem("학습 통계", onClick = {})
        MyPageMenuItem("알림 설정", onClick = {})
        MyPageMenuItem("비밀번호 변경", onClick = {})
        MyPageMenuItem("내가 만든 문제집 보기", onClick = {}, isLast = true)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyPageMenuItem() {
    MyPageMenuItem(title = "학습 통계", onClick = {})
}
