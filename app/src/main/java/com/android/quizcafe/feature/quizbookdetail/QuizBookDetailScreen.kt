package com.android.quizcafe.feature.quizbookdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.selectedColor
import com.android.quizcafe.core.domain.model.quizbook.response.Comment
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.response.QuizSummary

@Composable
fun QuizBookDetailScreen(
    state: QuizBookDetailViewState = QuizBookDetailViewState(),
    sendIntent: (QuizBookDetailIntent) -> Unit
) {
    val quizBookDetail = state.quizBookDetail

    var isChecked by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Column(
                Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.clickable {
                        isChecked = !isChecked
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(if (isChecked) R.drawable.ic_check_circle_fill else R.drawable.ic_check_circle_unfill),
                        contentDescription = null,
                        tint = if (isChecked) selectedColor else MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("즉시 채점 모드")
                }
                Spacer(Modifier.height(8.dp))
                QuizSolveButton(
                    onClick = { sendIntent(QuizBookDetailIntent.ClickQuizSolve) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuizHeader(
                title = quizBookDetail.title,
                averageScore = quizBookDetail.averageScore,
                totalSaves = quizBookDetail.totalSaves,
                views = quizBookDetail.views,
                questionCount = quizBookDetail.quizSummaries.size,
                level = quizBookDetail.level,
                creatorName = quizBookDetail.ownerName,
                createdAt = quizBookDetail.createdAt,
                isMarked = quizBookDetail.isMarked
            ) {
                if (quizBookDetail.isMarked) {
                    sendIntent(QuizBookDetailIntent.ClickUnmarkQuizBook)
                } else {
                    sendIntent(QuizBookDetailIntent.ClickMarkQuizBook)
                }
            }
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            QuizDescription(quizBookDetail.description)
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            QuizSummaryList(quizBookDetail.quizSummaries)
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            CommentContent(quizBookDetail.comments)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookDetailScreenPreview() {
    QuizCafeTheme {
        QuizBookDetailScreen(
            QuizBookDetailViewState(
                quizBookDetail = QuizBookDetail(
                    id = 1,
                    ownerId = 1,
                    ownerName = "박승준",
                    category = "운영체제",
                    title = "승준이의 운영체제",
                    description = "잘풀어봐라",
                    difficulty = "어려움",
                    averageScore = "20.5/30",
                    totalSaves = 15,
                    quizSummaries = listOf(
                        QuizSummary(
                            quizId = 0,
                            quizContent = "!23123",
                            quizType = "객관식"
                        )
                    ),
                    comments = listOf(
                        Comment(
                            commentId = 0,
                            userId = 0,
                            userName = "박승준",
                            commentContent = "도전!"
                        )
                    ),
                    createdAt = ""
                )
            )
        ) {}
    }
}
