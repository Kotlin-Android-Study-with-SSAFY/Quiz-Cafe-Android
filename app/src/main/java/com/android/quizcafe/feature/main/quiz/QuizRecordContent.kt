package com.android.quizcafe.feature.main.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.onSurfaceLight
import com.android.quizcafe.core.designsystem.theme.outlineLight
import com.android.quizcafe.core.designsystem.theme.surfaceContainerHighestLight
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord
import com.android.quizcafe.feature.util.safeToRelativeTime

@Composable
fun QuizRecordContent(
    quizSolvingRecords: List<QuizSolvingRecord>,
    onQuizRecordClick: (QuizSolvingRecord) -> Unit = {},
    onHeaderClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        QuizRecordHeader(onClick = onHeaderClick)
        if (quizSolvingRecords.isEmpty()) {
            QuizRecordEmpty()
        } else {
            QuizRecordList(quizSolvingRecords, onQuizRecordClick)
        }
    }
}

@Composable
fun QuizRecordHeader(onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.quiz_record),
            style = MaterialTheme.typography.titleMedium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(id = R.string.navigate_forward)
        )
    }
}

@Composable
fun QuizRecordEmpty() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(288.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.quiz_record_empty),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun QuizRecordList(
    quizSolvingRecords: List<QuizSolvingRecord>,
    onQuizRecordClick: (QuizSolvingRecord) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 288.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(quizSolvingRecords) { record ->
            QuizRecordCard(
                quizSolvingRecord = record,
                onClick = { onQuizRecordClick(record) }
            )
        }
    }
}

@Composable
fun QuizRecordCard(
    quizSolvingRecord: QuizSolvingRecord,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceContainerHighestLight,
            contentColor = onSurfaceLight
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(
                    id = R.string.quiz_solved_date,
                    quizSolvingRecord.completedAt.safeToRelativeTime()
                ),
                style = MaterialTheme.typography.labelSmall,
                color = outlineLight
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = quizSolvingRecord.title,
                style = MaterialTheme.typography.titleSmall,
                color = onSurfaceLight
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(
                        R.string.quiz_result,
                        quizSolvingRecord.totalQuizzes,
                        quizSolvingRecord.correctCount
                    ),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "QuizRecordContent - 기록 있음")
@Composable
fun Preview_QuizRecordContent() {
    QuizCafeTheme {
        QuizRecordContent(
            quizSolvingRecords = sampleQuizSolvingRecords()
        )
    }
}

@Preview(showBackground = true, name = "QuizRecordContent - 텅 비어 있음")
@Composable
fun Preview_QuizRecordContent_Empty() {
    QuizCafeTheme {
        QuizRecordContent(quizSolvingRecords = emptyList())
    }
}

@Preview(showBackground = true, name = "QuizRecordHeader")
@Composable
fun Preview_QuizRecordHeader() {
    QuizCafeTheme {
        QuizRecordHeader()
    }
}

@Preview(showBackground = true, name = "QuizRecordEmpty")
@Composable
fun Preview_QuizRecordEmpty() {
    QuizCafeTheme {
        QuizRecordEmpty()
    }
}

@Preview(showBackground = true, name = "QuizRecordCard")
@Composable
fun Preview_QuizRecordCard() {
    QuizCafeTheme {
        QuizRecordCard(
            quizSolvingRecord = QuizSolvingRecord(
                id = 1,
                userId = 1,
                quizBookId = 1,
                version = 1,
                level = "EASY",
                category = "운영체제",
                title = "성준이의 운영체제",
                description = "",
                totalQuizzes = 20,
                correctCount = 16,
                completedAt = "2025-06-09T05:14:05.986Z",
                quizzes = emptyList()
            )
        )
    }
}

// 샘플 데이터 함수
fun sampleQuizSolvingRecords() = listOf(
    QuizSolvingRecord(
        id = 1,
        userId = 1,
        quizBookId = 1,
        version = 1,
        level = "EASY",
        category = "운영체제",
        title = "성준이의 운영체제",
        description = "",
        totalQuizzes = 20,
        correctCount = 16,
        completedAt = "2025-06-09T05:14:05.986Z",
        quizzes = emptyList()
    )
)
