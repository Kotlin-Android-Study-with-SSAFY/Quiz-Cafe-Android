package com.android.quizcafe.feature.main.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.onSurfaceLight
import com.android.quizcafe.core.designsystem.theme.outlineLight
import com.android.quizcafe.core.designsystem.theme.surfaceContainerHighestLight

@Composable
fun QuizHistorySection(historyList: List<QuizHistory>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "풀이기록",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "앞으로 이동"
            )
        }
        // LazyColumn만 높이 제한
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(288.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historyList) { history ->
                QuizHistoryCard(history = history)
            }
        }
    }
}

@Composable
fun QuizHistoryCard(
    history: QuizHistory,
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
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            Text(history.time, style = MaterialTheme.typography.labelSmall, color = outlineLight)
            Text(
                text = history.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = onSurfaceLight
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Text("결과 : ${history.result}/${history.totalProblems}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizHistorySection() {
    QuizCafeTheme {
        val mockHistoryList = listOf(
            QuizHistory("30분 전", "성준이의 운영체제", 16, 20),
            QuizHistory("16시간 전", "성민이의 네트워크", 18, 20),
            QuizHistory("04/01", "재용이의 안드로이드", 19, 20)
        )
        QuizHistorySection(historyList = mockHistoryList)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizHistoryCard() {
    QuizCafeTheme {
        QuizHistoryCard(
            history = QuizHistory(
                time = "1시간 전", title = "오인성의 컴파일러 퀴즈", result = 15, totalProblems = 20
            )
        )
    }
}
