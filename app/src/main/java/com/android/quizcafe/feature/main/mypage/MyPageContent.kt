package com.android.quizcafe.feature.main.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R

@Composable
fun MyPageUserName(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
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
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    "$solvedCount",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }
        }
        Box(
            Modifier
                .width(1.dp)
                .fillMaxHeight()
        )
        Box(
            Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(id = R.string.mypage_my_quiz_set),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    "$myQuizSetCount",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun MyPageMenu(
    onMenuClick: (Int) -> Unit = {}
) {
    val menuItems = listOf(
        R.string.mypage_menu_stats,
        R.string.mypage_menu_alarm,
        R.string.mypage_menu_change_pw,
        R.string.mypage_menu_my_quiz_set
    )

    Column(Modifier.fillMaxWidth()) {
        menuItems.forEachIndexed { idx, labelRes ->
            MyPageMenuItem(
                title = stringResource(labelRes),
                onClick = { onMenuClick(idx) },
                isLast = idx == menuItems.lastIndex
            )
        }
    }
}

@Composable
fun MyPageMenuItem(
    title: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 8.dp)
                .height(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
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
                modifier = Modifier.height(1.dp)
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
        MyPageMenuItem(title = "학습 통계", onClick = {})
        MyPageMenuItem(title = "알림 설정", onClick = {})
        MyPageMenuItem(title = "비밀번호 변경", onClick = {})
        MyPageMenuItem(title = "내가 만든 문제집 보기", onClick = {}, isLast = true)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyPageMenuItem() {
    MyPageMenuItem(title = "학습 통계", onClick = {})
}
