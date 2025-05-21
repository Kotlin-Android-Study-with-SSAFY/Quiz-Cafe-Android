package com.android.quizcafe.feature.quizbookdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.domain.model.quizbook.response.Comment
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBookDetail
import com.android.quizcafe.core.domain.model.quizbook.response.QuizSummary

// --- Composable Entry ---
@Composable
fun QuizBookDetailScreen(
    state: QuizBookDetailViewState = QuizBookDetailViewState(),
    sendIntent: (QuizBookDetailIntent) -> Unit
) {
//    val quizBookDetail = state.quizBookDetail
    val quizBookDetail = QuizBookDetail(
        ownerName = "박승준",
        title = "제목",
        description = "설명",
        quizSummaries = listOf(
            QuizSummary(1, "문제1", "객관식"),
            QuizSummary(2, "문제2", "주관식")
        ),
        comments = listOf(
            Comment(1, 1, "박승준", "댓글1"),
        )
    )
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
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuizHeader(
                title = "quizBookDetail.title",
                averageScore = quizBookDetail.averageScore,
                totalSaves = quizBookDetail.totalSaves,
                questionCount = quizBookDetail.quizSummaries.size,
                creatorName = quizBookDetail.ownerName
            )
            QuizDescription(quizBookDetail.description)
            QuizQuestionList(quizBookDetail.quizSummaries)
            CommentList(quizBookDetail.comments)
        }
    }
}

// --- Sub-Components ---
@Composable
fun QuizHeader(
    title: String,
    averageScore: Double,
    totalSaves: Int,
    questionCount: Int,
    creatorName: String
) {
    Text(title, style = MaterialTheme.typography.titleLarge)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("평균 점수: $averageScore", style = MaterialTheme.typography.bodyMedium)
            Text("저장 수: $totalSaves", style = MaterialTheme.typography.bodySmall)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("문제 수: $questionCount", style = MaterialTheme.typography.bodyMedium)
            Row {
                Image(painter = painterResource(id = R.drawable.ic_account), contentDescription = null,)
                Text(creatorName, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun QuizDescription(description: String) {
    Column {
        Text("문제집 설명", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(description, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun QuizQuestionList(quizSummaries: List<QuizSummary>) {
    Column {
        Text("문제 목록", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        quizSummaries.forEach {
            Text(it.quizContent, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun CommentList(comments: List<Comment>) {
    Column {
        ChapterWithDivider("댓글")
        Spacer(modifier = Modifier.height(4.dp))
        comments.forEach {
            Text("${it.userName}: ${it.commentContent}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun QuizSolveButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text("바로 문제 풀기")
    }
}

@Composable
fun ColumnScope.ChapterWithDivider(title: String) {
    HorizontalDivider(modifier = Modifier.fillMaxWidth())
    Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
}
