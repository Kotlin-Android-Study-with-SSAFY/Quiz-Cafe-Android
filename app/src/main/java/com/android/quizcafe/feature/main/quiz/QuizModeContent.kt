package com.android.quizcafe.feature.main.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.CreateQuizCardColor
import com.android.quizcafe.core.designsystem.theme.OxQuizCardColor
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.QuizCardColorMultipleChoice
import com.android.quizcafe.core.designsystem.theme.QuizCardColorShortAnswer

@Composable
fun QuizModeContent() {
    val modeItems = listOf(
        ModeItem(R.string.feature_title_ox_quiz, R.string.feature_desc_ox_quiz, OxQuizCardColor),
        ModeItem(R.string.feature_title_multiple_choice_quiz, R.string.feature_desc_multiple_choice_quiz, QuizCardColorMultipleChoice),
        ModeItem(R.string.feature_title_short_answer_quiz, R.string.feature_desc_short_answer_quiz, QuizCardColorShortAnswer),
        ModeItem(R.string.feature_title_create_quiz, R.string.feature_desc_create_quiz, CreateQuizCardColor)
    )

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        modeItems.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    QuizModeCard(
                        title = stringResource(item.titleResId),
                        description = stringResource(item.descResId),
                        backgroundColor = item.backgroundColor
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun RowScope.QuizModeCard(
    title: String,
    description: String,
    backgroundColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                painter = painterResource(R.drawable.quiz_unfill),
                contentDescription = "$title 이동",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizModeContent() {
    QuizCafeTheme {
        QuizModeContent()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQuizModeCard() {
    QuizCafeTheme {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(160.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuizModeCard(
                title = "문제 풀기",
                description = "원하는 카테고리를 선택해서 학습해요.",
                backgroundColor = Color(185, 234, 217, 255)
            )
            QuizModeCard(
                title = "문제 만들기",
                description = "직접 문제를 만들어보세요.",
                backgroundColor = Color(255, 240, 200, 255)
            )
        }
    }
}
