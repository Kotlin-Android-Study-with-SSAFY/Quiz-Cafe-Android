package com.android.quizcafe.feature.main.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
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
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.feature.util.safeToRelativeTime

@Composable
fun LatestQuizBooksContent(
    quizBooks: List<QuizBook>,
    onQuizBookClick: (QuizBook) -> Unit = {},
    onSeeAllClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSeeAllClick)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.latest_quiz_books),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = outlineLight
            )
        }

        val itemHeight = 88.dp
        val spacing = 8.dp
        val paddingV = 8.dp
        val bottomPadding = 8.dp
        val maxListHeight = itemHeight * 3 + spacing * 2 + paddingV * 2

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxListHeight),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = paddingV,
                bottom = paddingV + bottomPadding
            ),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            items(quizBooks) { book ->
                LatestQuizBookCard(
                    quizBook = book,
                    onClick = { onQuizBookClick(book) }
                )
            }
        }
    }
}

@Composable
fun LatestQuizBookCard(
    quizBook: QuizBook,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceContainerHighestLight,
            contentColor = onSurfaceLight
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quizBook.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        id = R.string.created_at,
                        quizBook.createdAt.safeToRelativeTime()
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = outlineLight
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = outlineLight
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${quizBook.totalComments}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = outlineLight
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${quizBook.totalSaves}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// 샘플
private fun sampleLatestQuizBooks() = listOf(
    QuizBook(
        id = 1L, version = 1L, category = "OS", title = "운영체제 기초",
        description = "OS 개념 정리", level = "EASY", ownerName = "홍길동",
        totalQuizzes = 20, totalComments = 5, totalSaves = 10,
        createdAt = "2025-06-09T05:14:05Z"
    ),
    QuizBook(
        id = 2L, version = 1L, category = "DB", title = "데이터베이스 심화",
        description = "ER 모델링", level = "MEDIUM", ownerName = "김철수",
        totalQuizzes = 15, totalComments = 8, totalSaves = 12,
        createdAt = "2025-06-10T08:00:00Z"
    ),
    QuizBook(
        id = 3L, version = 1L, category = "알고리즘", title = "그리디 알고리즘",
        description = "최적화 패턴", level = "HARD", ownerName = "이영희",
        totalQuizzes = 25, totalComments = 3, totalSaves = 7,
        createdAt = "2025-06-11T10:30:00Z"
    ),
    QuizBook(
        id = 4L, version = 1L, category = "네트워크", title = "OSI 7계층",
        description = "통신 프로토콜", level = "EASY", ownerName = "박민호",
        totalQuizzes = 10, totalComments = 0, totalSaves = 5,
        createdAt = "2025-06-12T12:45:00Z"
    )
)

@Preview(showBackground = true)
@Composable
fun Preview_LatestSection() {
    QuizCafeTheme {
        LatestQuizBooksContent(
            quizBooks = sampleLatestQuizBooks(),
            onQuizBookClick = {},
            onSeeAllClick = {}
        )
    }
}
