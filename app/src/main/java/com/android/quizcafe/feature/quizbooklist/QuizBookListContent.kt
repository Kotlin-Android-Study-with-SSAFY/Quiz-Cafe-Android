package com.android.quizcafe.feature.quizbooklist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.onPrimaryLight
import com.android.quizcafe.core.designsystem.theme.outlineLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook

@Composable
fun QuizBookCardList(quizBooks: List<QuizBook>) {
    LazyColumn {
        items(quizBooks) { quizBook ->
            QuizBookCard(quizBook = quizBook)
        }
    }
}

@Composable
fun QuizBookCard(quizBook: QuizBook) {
    Spacer(Modifier.height(8.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = onPrimaryLight),
        onClick = {
            // TODO: 문제집 상세 화면으로 이동
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            QuizBookHeader(difficulty = quizBook.difficulty, totalQuizzes = quizBook.totalQuizzes)
            Spacer(modifier = Modifier.height(8.dp))
            QuizBookTitle(title = quizBook.title)
            Spacer(modifier = Modifier.height(16.dp))
            QuizBookFooter(
                ownerName = quizBook.ownerName,
                totalSaves = quizBook.totalSaves,
                totalComments = quizBook.totalComments
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun QuizBookListHeader(size: Int, onFilterClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(R.string.quiz_book_count_description, size),
            style = quizCafeTypography().labelLarge
        )
        QuizBookFilterButton(stringResource(R.string.filter)) { onFilterClick() }
    }
}

@Composable
fun QuizBookHeader(difficulty: String, totalQuizzes: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${stringResource(R.string.difficulty)} : $difficulty", color = outlineLight)
        Text("$totalQuizzes ${stringResource(R.string.question)}")
    }
}

@Composable
fun QuizBookTitle(title: String) {
    Text(
        text = title,
        style = quizCafeTypography().titleMedium
    )
}

@Composable
fun QuizBookFooter(
    ownerName: String,
    totalSaves: Int,
    totalComments: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(ownerName, color = outlineLight)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_bookmark_fill),
                contentDescription = stringResource(R.string.is_saved),
                modifier = Modifier.size(16.dp)
            )
            Text(text = totalSaves.toString(), modifier = Modifier.padding(start = 4.dp))

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_comment),
                contentDescription = stringResource(R.string.comment),
                modifier = Modifier.size(16.dp)
            )
            Text(text = totalComments.toString(), modifier = Modifier.padding(start = 4.dp))
        }
    }
}

@Composable
fun QuizBookFilterButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = onPrimaryLight,
        border = BorderStroke(1.dp, outlineLight)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = stringResource(R.string.filter),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(16.dp)
            )
            // padding top 1dp : Jua 폰트의 살짝 위로 배치되는 특징 보정
            Text(
                text = text,
                modifier = Modifier.padding(top = 1.dp),
                style = quizCafeTypography().labelLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookCardListPreview() {
    val sampleQuizBooks = listOf(
        QuizBook(
            id = 1L,
            ownerId = 123456L,
            ownerName = "시스템 관리자",
            category = "운영체제",
            title = "모두의 운영체제",
            difficulty = "???",
            totalQuizzes = 231,
            totalComments = 4,
            totalSaves = 32,
            createdAt = "2025-05-11T15:30:00Z",
            version = TODO(),
            description = TODO(),
            level = TODO()
        ),
        QuizBook(
            id = 2L,
            ownerId = 654321L,
            ownerName = "싸피_박성준",
            category = "운영체제",
            title = "성준이의 운영체제",
            difficulty = "상",
            totalQuizzes = 30,
            totalComments = 4,
            totalSaves = 32,
            createdAt = "2025-05-11T15:30:00Z",
            version = TODO(),
            description = TODO(),
            level = TODO()
        )
    )

    QuizCafeTheme {
        QuizBookCardList(
            quizBooks = sampleQuizBooks
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookListHeaderPreview() {
    QuizCafeTheme {
        QuizBookListHeader(2) {}
    }
}
