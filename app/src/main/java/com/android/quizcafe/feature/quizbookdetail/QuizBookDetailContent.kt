package com.android.quizcafe.feature.quizbookdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.IconText
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.QuizCafeOutlinedButton
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.domain.model.quizbook.response.Comment
import com.android.quizcafe.core.domain.model.quizbook.response.QuizSummary

// --- Sub-Components ---
@Composable
fun QuizHeader(
    title: String,
    averageScore: String,
    totalSaves: Int,
    views: Int,
    questionCount: Int,
    level: String,
    creatorName: String,
    createdAt: String,
    isSaved: Boolean,
    onSaveClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            QuizCafeOutlinedButton(
                onClick = onSaveClick,
                contentPadding = PaddingValues(8.dp)
            ) {
                IconText(
                    text = if (isSaved) stringResource(R.string.saved) else stringResource(R.string.save),
                    iconResId = if (isSaved) R.drawable.ic_check else R.drawable.ic_add,
                    MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                "${stringResource(R.string.average_score)} : $averageScore",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
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
            Text("${stringResource(R.string.quiz_count)} : ${stringResource(R.string.quiz_count_description, questionCount)}", style = MaterialTheme.typography.bodyMedium)
            IconText(creatorName, R.drawable.ic_account)
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("${stringResource(R.string.level)} : $level", style = MaterialTheme.typography.bodyMedium)
            Text(createdAt, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun QuizDescription(description: String) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun QuizSummaryList(quizSummaries: List<QuizSummary>) {
    Column {
        Text(stringResource(R.string.quiz_list), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(12.dp))
        quizSummaries.forEach { quizSummary ->
            QuizSummary(quizSummary)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun QuizSummary(
    quizSummary: QuizSummary
) {
    Row(verticalAlignment = Alignment.Top) {
        Text("- ", style = MaterialTheme.typography.bodyMedium)
        Column {
            Text(quizSummary.quizContent, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(quizSummary.quizType, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun CommentContent(comments: List<Comment>, modifier: Modifier = Modifier) {
    Column {
        IconText("${stringResource(R.string.comment)} ${comments.size}", R.drawable.ic_comment)
        Spacer(modifier = modifier.height(4.dp))
        comments.forEach {
            Text(it.commentContent, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun QuizSolveButton(onClick: () -> Unit) {
    Column {
        QuizCafeButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.scrim,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.scrim,
            )
        ) {
            Text(stringResource(R.string.solve_now), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizHeaderSavedPreview() {
    QuizCafeTheme {
        QuizHeader(
            title = "코틀린 마스터 퀴즈",
            averageScore = "85.5점",
            totalSaves = 128,
            views = 1024,
            questionCount = 15,
            level = "어려움",
            creatorName = "개발자A",
            createdAt = "2일 전",
            isSaved = true,
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizHeaderNotSavedPreview() {
    QuizCafeTheme {
        QuizHeader(
            title = "자바스크립트 완전 정복",
            averageScore = "72.0점",
            totalSaves = 99,
            views = 2560,
            questionCount = 20,
            level = "어려움",
            creatorName = "프론트엔드 마스터",
            createdAt = "2일 전",
            isSaved = false,
            onSaveClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Quiz Description Preview")
@Composable
fun QuizDescriptionPreview() {
    QuizCafeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            QuizDescription(
                description = "이 퀴즈는 코틀린의 기본 문법부터 고급 기능까지 모두 다루고 있습니다. " +
                    "코루틴, 고차 함수, 제네릭 등 다양한 주제를 통해 코틀린 실력을 점검하고 향상시켜 보세요!"
            )
        }
    }
}

@Preview(showBackground = true, name = "Quiz Question List Preview")
@Composable
fun QuizQuestionListPreview() {
    QuizCafeTheme {
        val sampleQuizzes = listOf(
            QuizSummary(
                quizId = 1L,
                quizContent = "코틀린에서 'val'과 'var'의 차이점은 무엇인가요?",
                quizType = "객관식"
            ),
            QuizSummary(
                quizId = 2L,
                quizContent = "고차 함수(Higher-Order Function)에 대해 설명하세요.",
                quizType = "주관식"
            ),
            QuizSummary(
                quizId = 3L,
                quizContent = "코틀린의 데이터 클래스(Data Class)의 특징으로 옳지 않은 것은?",
                quizType = "객관식"
            )
        )
        Column(modifier = Modifier.padding(16.dp)) {
            QuizSummaryList(quizSummaries = sampleQuizzes)
        }
    }
}

@Preview(showBackground = true, name = "Comment List Preview")
@Composable
fun CommentListPreview() {
    QuizCafeTheme {
        val sampleComments = listOf(
            Comment(
                commentId = 101L,
                userId = 2001L,
                userName = "퀴즈매니아",
                commentContent = "문제가 너무 좋네요! 많이 배워갑니다."
            ),
            Comment(
                commentId = 102L,
                userId = 2002L,
                userName = "개발꿈나무",
                commentContent = "해설도 같이 있었으면 좋겠습니다. 오답노트 기능도 추가해주세요!"
            ),
            Comment(
                commentId = 103L,
                userId = 2003L,
                userName = "수강생A",
                commentContent = "친구에게도 추천했습니다!"
            )
        )
        Column(modifier = Modifier.padding(16.dp)) {
            CommentContent(comments = sampleComments)
        }
    }
}

@Preview(showBackground = true, name = "Quiz Solve Button Preview")
@Composable
fun QuizSolveButtonPreview() {
    QuizCafeTheme {
        QuizSolveButton(onClick = {})
    }
}
