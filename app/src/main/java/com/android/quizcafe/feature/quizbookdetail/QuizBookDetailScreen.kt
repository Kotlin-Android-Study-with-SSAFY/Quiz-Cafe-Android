package com.android.quizcafe.feature.quizbookdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.IconText
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.QuizCafeOutlinedButton
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.domain.model.quizbook.response.Comment
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.response.QuizSummary

// --- Composable Entry ---
@Composable
fun QuizBookDetailScreen(
    state: QuizBookDetailViewState = QuizBookDetailViewState(),
    sendIntent: (QuizBookDetailIntent) -> Unit
) {
    val quizBookDetail = state.quizBookDetail

    Scaffold(
        bottomBar = {
            QuizSolveButton(
                onClick = { sendIntent(QuizBookDetailIntent.ClickQuizSolve) }
            )
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
                creatorName = quizBookDetail.ownerName
            )
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            QuizDescription(quizBookDetail.description)
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            QuizQuestionList(quizBookDetail.quizSummaries)
            CommentList(quizBookDetail.comments)
        }
    }
}

// --- Sub-Components ---
@Composable
fun QuizHeader(
    title: String,
    averageScore: String,
    totalSaves: Int,
    views: Int,
    questionCount: Int,
    creatorName: String
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            QuizCafeOutlinedButton(
                onClick = {
                    // TODO: 문제집 저장
                },
                contentPadding = PaddingValues(8.dp)
            ) {
                IconText("저장 ", R.drawable.ic_add, MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("평균 점수 : $averageScore", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            Row {
                IconText(totalSaves.toString(), R.drawable.ic_bookmark)
                Spacer(modifier = Modifier.width(12.dp))
                IconText(views.toString(), R.drawable.ic_view)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("문제 수 : ${questionCount}문제", style = MaterialTheme.typography.bodySmall)
            IconText(creatorName, R.drawable.ic_account)
        }
    }
}

@Composable
fun QuizDescription(description: String) {
    Column {
        Text("문제집 설명", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun QuizQuestionList(quizSummaries: List<QuizSummary>) {
    Column {
        Text("문제 목록", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        quizSummaries.forEach {
            Text(it.quizContent, style = MaterialTheme.typography.bodySmall)
            Text(it.quizType, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CommentList(comments: List<Comment>) {
    Column {
        IconText("댓글 ${comments.size}", R.drawable.ic_comment)
        Spacer(modifier = Modifier.height(4.dp))
        comments.forEach {
            Text(it.commentContent, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun QuizSolveButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier.navigationBarsPadding().padding(16.dp)
    ) {
        QuizCafeButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("바로 문제 풀기", style = MaterialTheme.typography.bodyLarge)
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
