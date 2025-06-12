package com.android.quizcafe.feature.quiz.solvingResult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.primaryLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import com.android.quizcafe.core.designsystem.theme.surfaceDimLight
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId
import com.android.quizcafe.feature.quiz.solvingResult.component.QuizResultItem

@Composable
fun QuizBookSolvingResultScreen(
    state: QuizBookSolvingResultUiState = QuizBookSolvingResultUiState(),
    sendIntent: (QuizBookSolvingResultIntent) -> Unit = {}
) {
    val quizBookGrade = state.quizBookSolvingResult
    val quizzes = state.quizzes

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            QuizResultBottomBar(
                onClick = { sendIntent(QuizBookSolvingResultIntent.ClickFinish) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            QuizResultHeader(
                totalScore = quizBookGrade?.getTotalScore() ?: 0,
                maxScore = quizzes.size,
                solvingTime = quizBookGrade?.getSolvingTimeFormatted() ?: "00:00"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    itemsIndexed(quizzes) { index, quiz ->
                        val quizGrade = quizBookGrade?.quizGrades?.find { it.quizId == quiz.id }

                        QuizResultItem(
                            quiz = quiz,
                            quizGrade = quizGrade,
                            questionNumber = index + 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizResultHeader(
    totalScore: Int,
    maxScore: Int,
    solvingTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "채점 결과 : $totalScore/$maxScore",
            style = quizCafeTypography().headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "걸린 시간 : $solvingTime",
            style = quizCafeTypography().bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun QuizResultBottomBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuizCafeButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryLight,
                contentColor = scrimLight,
                disabledContainerColor = surfaceDimLight
            ),
            text = {
                Text(
                    text = "종료하기",
                    style = quizCafeTypography().titleSmall
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookSolvingResultScreenPreview() {
    QuizCafeTheme {
        QuizBookSolvingResultScreen(
            state = QuizBookSolvingResultUiState(
                isLoading = false,
                quizBookSolvingResult = QuizBookGrade(
                    localId = QuizBookGradeLocalId(1L),
                    serverId = null,
                    quizBookId = QuizBookId(1L),
                    quizGrades = listOf(
                        QuizGrade(
                            localId = 1L,
                            quizId = QuizId(1L),
                            quizBookGradeLocalId = QuizBookGradeLocalId(1L),
                            userAnswer = "O",
                            memo = null,
                            isCorrect = true,
                            completedAt = "2025-06-11T15:30:00"
                        ),
                        QuizGrade(
                            localId = 2L,
                            quizId = QuizId(2L),
                            quizBookGradeLocalId = QuizBookGradeLocalId(1L),
                            userAnswer = "async",
                            memo = null,
                            isCorrect = false,
                            completedAt = "2025-06-11T15:32:00"
                        ),
                        QuizGrade(
                            localId = 3L,
                            quizId = QuizId(3L),
                            quizBookGradeLocalId = QuizBookGradeLocalId(1L),
                            userAnswer = "val",
                            memo = null,
                            isCorrect = true,
                            completedAt = "2025-06-11T15:35:00"
                        )
                    ),
                    elapsedTime = kotlin.time.Duration.parse("PT9M2S")
                ),
                quizzes = listOf(
                    Quiz(
                        id = QuizId(1L),
                        quizBookId = QuizBookId(1L),
                        questionType = "MCQ",
                        content = "스택은 어시기어시기",
                        answer = "O",
                        explanation = "스택은 LIFO(Last In First Out) 구조입니다.",
                        version = 1L
                    ),
                    Quiz(
                        id = QuizId(2L),
                        quizBookId = QuizBookId(1L),
                        questionType = "SHORT_ANSWER",
                        content = "launch()와 async는 동일하다.",
                        answer = "launch",
                        explanation = "launch와 async는 코루틴을 실행시키기 위한 빌더이지만, 반환 결과에 따른 차이가 존재합니다. launch는 결과를 반환하지 않으며 Job 객체를 반환합니다. 반면, async는 Deferred로 감싸진 결과를 반환하며, 이를 await()메서드를 통해 결과를 얻을 수 있습니다. 따라서, launch는 결과가 필요없이 실행만 하는 경우 사용되고 async는 결과가 필요한 비동기 작업에 사용됩니다.",
                        version = 1L
                    ),
                    Quiz(
                        id = QuizId(3L),
                        quizBookId = QuizBookId(1L),
                        questionType = "SHORT_ANSWER",
                        content = "Android Architecture Components에서 ViewModel의 역할과 생명주기에 대해 설명하고, Repository 패턴과 함께 사용할 때의 장점을 서술하시오.",
                        answer = "val",
                        explanation = "val은 불변 변수를 선언할 때 사용합니다.",
                        version = 1L
                    )
                )
            )
        )
    }
}
