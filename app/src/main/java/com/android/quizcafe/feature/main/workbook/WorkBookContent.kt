package com.android.quizcafe.feature.main.workbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.onPrimaryLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import toRelativeDate

@Composable
fun NoWorkBookContent(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
fun WorkBookCard(
    category: String,
    title: String,
    count: Int,
    totalQuizzes: Int,
    completedAt: String,
    isSolved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Spacer(modifier.height(8.dp))
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = onPrimaryLight),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = category,
                color = MaterialTheme.colorScheme.outlineVariant,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            WorkBookTitle(title = title)
            Spacer(modifier = Modifier.height(20.dp))
            WorkBookFooter(
                count = count,
                totalQuizzes = totalQuizzes,
                createAt = completedAt,
                isSolved = isSolved
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun WorkBookTitle(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = quizCafeTypography().titleMedium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.go),
            tint = scrimLight,
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
fun WorkBookFooter(
    count: Int,
    totalQuizzes: Int,
    createAt: String,
    isSolved: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isSolved) {
            QuizSolveProgressBar(solvedQuizzes = count, totalQuizzes = totalQuizzes)
        } else {
            Text("내 결과 : $count/$totalQuizzes", style = MaterialTheme.typography.labelMedium)
        }

        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = createAt.toRelativeDate(),
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun QuizSolveProgressBar(
    solvedQuizzes: Int,
    totalQuizzes: Int
) {
    Column {
        Text("$solvedQuizzes/$totalQuizzes 풀이 중", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { if (totalQuizzes > 0) solvedQuizzes.toFloat() / totalQuizzes else 0f },
            modifier = Modifier.padding(end = 20.dp),
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkBookCardPreviewUnsolved() {
    QuizCafeTheme {
        WorkBookCard(
            category = "데이터베이스",
            title = "SQL 기초 문제",
            count = 5,
            totalQuizzes = 10,
            completedAt = "2024-06-26",
            isSolved = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkBookCardPreviewSolved() {
    QuizCafeTheme {
        WorkBookCard(
            category = "운영체제",
            title = "프로세스 관리 심화",
            count = 10,
            totalQuizzes = 10,
            completedAt = "2024-06-25",
            isSolved = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoWorkBookContentPreview() {
    QuizCafeTheme {
        NoWorkBookContent(text = "아직 생성된 문제집이 없어요.")
    }
}
