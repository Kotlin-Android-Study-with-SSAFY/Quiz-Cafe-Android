package com.android.quizcafe.feature.quiz.solve.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTopBar(
    modifier: Modifier = Modifier,
    currentQuestion: Int,
    totalQuestions: Int,
    timeText: String?,
    onBackClick: () -> Unit,
    onSideBarClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painterResource(R.drawable.ic_arrow_back),
                    contentDescription = "뒤로가기"
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.quiz_topbar_title, currentQuestion, totalQuestions),
                style = quizCafeTypography().titleMedium
            )
        },
        actions = {
            TopBarActionSection(timeText, onSideBarClick)
        }
    )
}

@Composable
fun TopBarActionSection(timeText: String?, onSideBarClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        if (timeText != null) {
            TimeRemainSection(timeText)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(R.drawable.ic_sidebar),
            contentDescription = "사이드 바",
            modifier = Modifier.clickable {
                onSideBarClick()
            }
        )
    }
}

@Composable
fun TimeRemainSection(timeText: String) {
    Icon(
        painter = painterResource(R.drawable.ic_timer),
        contentDescription = "타이머"
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = timeText,
        style = quizCafeTypography().titleSmall
    )
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTopBarPreview() {
    MaterialTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            QuizTopBar(
                modifier = Modifier,
                currentQuestion = 1,
                totalQuestions = 10,
                timeText = "09:58",
                onBackClick = { },
                onSideBarClick = { }
            )

            QuizTopBar(
                modifier = Modifier,
                currentQuestion = 33,
                totalQuestions = 99,
                timeText = null,
                onBackClick = { },
                onSideBarClick = { }
            )
        }
    }
}
