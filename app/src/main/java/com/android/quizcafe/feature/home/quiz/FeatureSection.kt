package com.android.quizcafe.feature.home.quiz

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.CreateQuizCardColor
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.SolveQuizCardColor


@Composable
fun FeatureSection() {
    val featureItems = listOf(
        FeatureItem(R.string.feature_title_solve_quiz, R.string.feature_desc_solve_quiz, SolveQuizCardColor),
        FeatureItem(R.string.feature_title_create_quiz, R.string.feature_desc_create_quiz, CreateQuizCardColor),
        FeatureItem(R.string.feature_title_create_quiz, R.string.feature_desc_create_quiz, CreateQuizCardColor) // 테스트용 3번째
    )

    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        val chunks = featureItems.chunked(2)

        chunks.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                rowItems.forEachIndexed { index, item ->
                    FeatureCard(
                        title = stringResource(item.titleResId),
                        description = stringResource(item.descResId),
                        backgroundColor = item.backgroundColor
                    )
                    if (index == 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                // 홀수일 때 오른쪽 빈공간 채우기
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun RowScope.FeatureCard(
    title: String,
    description: String,
    backgroundColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(4.dp)
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
                color = Color.Black
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.quiz_unfill),
                    contentDescription = "$title 이동",
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFeatureSection() {
    QuizCafeTheme {
        FeatureSection()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFeatureCard() {
    QuizCafeTheme {
        Row(modifier = Modifier.padding(8.dp)) {
            FeatureCard(
                title = "문제 풀기",
                description = "원하는 카테고리를 선택해서 학습해요.",
                backgroundColor = Color(185, 234, 217, 255)
            )
        }
    }
}
