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
import com.android.quizcafe.core.domain.model.solving.McqOptionSolving
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.domain.model.solving.QuizSolving
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.feature.quiz.solvingResult.component.QuizResultItem

@Composable
fun QuizBookSolvingResultScreen(
    state: QuizBookSolvingResultUiState = QuizBookSolvingResultUiState(),
    sendIntent: (QuizBookSolvingResultIntent) -> Unit = {}
) {
    val quizBookSolving = state.quizBookSolving
    if (quizBookSolving != null) {
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
                    totalScore = quizBookSolving.correctCount,
                    maxScore = quizBookSolving.totalQuizzes,
                    solvingTime = quizBookSolving.getSolvingTimeFormatted()
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
                        itemsIndexed(quizBookSolving.quizSolvingList) { index, quizSolving ->

                            QuizResultItem(
                                quizSolving = quizSolving,
                                questionNumber = index + 1
                            )
                        }
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
                quizBookSolving = QuizBookSolving(
                    id = 1L,
                    userId = 123L,
                    quizBookId = QuizBookId(1L),
                    version = 1L,
                    level = "BEGINNER",
                    category = "KOTLIN",
                    title = "Kotlin 기초 퀴즈",
                    description = "Kotlin의 기본 문법과 개념을 다루는 퀴즈입니다.",
                    totalQuizzes = 3,
                    correctCount = 2,
                    completedAt = "2025-06-13T15:30:00",
                    elapsedTime = kotlin.time.Duration.parse("PT9M2S"),
                    quizSolvingList = listOf(
                        QuizSolving(
                            id = 101L,
                            quizBookSolvingId = 1L,
                            quizId = 1L,
                            questionType = "MCQ",
                            content = "다음 중 Kotlin의 특징이 아닌 것은?",
                            answer = "4",
                            explanation = "Kotlin은 메모리 안전성을 제공하며, 메모리 누수를 방지하는 다양한 기능을 제공합니다.",
                            memo = "확실한 답안이었음",
                            userAnswer = "4",
                            isCorrect = true,
                            completedAt = "2025-06-13T15:25:00",
                            mcqOptionSolvingList = listOf(
                                McqOptionSolving(
                                    id = 1001L,
                                    quizSolvingId = 101L,
                                    optionNumber = 1,
                                    optionContent = "Null Safety",
                                    isCorrect = false
                                ),
                                McqOptionSolving(
                                    id = 1002L,
                                    quizSolvingId = 101L,
                                    optionNumber = 2,
                                    optionContent = "상호 운용성",
                                    isCorrect = false
                                ),
                                McqOptionSolving(
                                    id = 1003L,
                                    quizSolvingId = 101L,
                                    optionNumber = 3,
                                    optionContent = "간결성",
                                    isCorrect = false
                                ),
                                McqOptionSolving(
                                    id = 1004L,
                                    quizSolvingId = 101L,
                                    optionNumber = 4,
                                    optionContent = "메모리 누수",
                                    isCorrect = true
                                )
                            )
                        ),
                        QuizSolving(
                            id = 102L,
                            quizBookSolvingId = 1L,
                            quizId = 2L,
                            questionType = "OX",
                            content = "Room 데이터베이스는 SQLite의 추상화 레이어이다.",
                            answer = "O",
                            explanation = "Room은 SQLite 위에 구축된 추상화 레이어로, 컴파일 타임 검증과 편리한 API를 제공합니다.",
                            memo = "헷갈렸던 문제",
                            userAnswer = "X",
                            isCorrect = false,
                            completedAt = "2025-06-13T15:27:00",
                            mcqOptionSolvingList = emptyList()
                        ),
                        QuizSolving(
                            id = 103L,
                            quizBookSolvingId = 1L,
                            quizId = 3L,
                            questionType = "SHORT_ANSWER",
                            content = "Android에서 비동기 작업을 처리하기 위해 사용하는 Kotlin의 기능은?",
                            answer = "Coroutines",
                            explanation = "Kotlin Coroutines는 비동기 프로그래밍을 위한 라이브러리로, suspend 함수와 함께 사용하여 효율적인 비동기 처리를 가능하게 합니다.",
                            memo = null,
                            userAnswer = "Coroutines",
                            isCorrect = true,
                            completedAt = "2025-06-13T15:30:00",
                            mcqOptionSolvingList = emptyList()
                        )
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookSolvingResultScreenAdvancedPreview() {
    QuizCafeTheme {
        QuizBookSolvingResultScreen(
            state = QuizBookSolvingResultUiState(
                isLoading = false,
                quizBookSolving = QuizBookSolving(
                    id = 2L,
                    userId = 123L,
                    quizBookId = QuizBookId(2L),
                    version = 2L,
                    level = "ADVANCED",
                    category = "ANDROID",
                    title = "Android 심화 퀴즈",
                    description = "Android Architecture Components와 Jetpack Compose에 대한 심화 문제들입니다.",
                    totalQuizzes = 2,
                    correctCount = 1,
                    completedAt = "2025-06-13T17:05:42",
                    elapsedTime = kotlin.time.Duration.parse("PT1H35M42S"),
                    quizSolvingList = listOf(
                        QuizSolving(
                            id = 201L,
                            quizBookSolvingId = 2L,
                            quizId = 4L,
                            questionType = "MCQ",
                            content = "Android에서 UI를 구성하는 최신 방법은?",
                            answer = "1",
                            explanation = "Jetpack Compose는 Android의 최신 선언형 UI 툴킷입니다.",
                            memo = "Compose 관련 문제",
                            userAnswer = "1",
                            isCorrect = true,
                            completedAt = "2025-06-13T16:30:00",
                            mcqOptionSolvingList = listOf(
                                McqOptionSolving(
                                    id = 2001L,
                                    quizSolvingId = 201L,
                                    optionNumber = 1,
                                    optionContent = "Jetpack Compose",
                                    isCorrect = true
                                ),
                                McqOptionSolving(
                                    id = 2002L,
                                    quizSolvingId = 201L,
                                    optionNumber = 2,
                                    optionContent = "XML Layout",
                                    isCorrect = false
                                ),
                                McqOptionSolving(
                                    id = 2003L,
                                    quizSolvingId = 201L,
                                    optionNumber = 3,
                                    optionContent = "React Native",
                                    isCorrect = false
                                ),
                                McqOptionSolving(
                                    id = 2004L,
                                    quizSolvingId = 201L,
                                    optionNumber = 4,
                                    optionContent = "Flutter",
                                    isCorrect = false
                                )
                            )
                        ),
                        QuizSolving(
                            id = 202L,
                            quizBookSolvingId = 2L,
                            quizId = 5L,
                            questionType = "SHORT_ANSWER",
                            content = "ViewModel의 생명주기와 Repository 패턴의 장점을 설명하시오.",
                            answer = "ViewModel은 UI 관련 데이터를 저장하고 관리하는 클래스로, 화면 회전 등의 구성 변경에도 데이터를 유지합니다.",
                            explanation = "ViewModel은 UI 컨트롤러의 생명주기를 고려하여 설계된 클래스입니다. Repository 패턴과 함께 사용하면 데이터 소스의 추상화와 테스트 용이성을 제공합니다.",
                            memo = "좀 더 자세히 써야 했는데...",
                            userAnswer = "ViewModel은 데이터를 관리합니다.",
                            isCorrect = false,
                            completedAt = "2025-06-13T17:05:42",
                            mcqOptionSolvingList = emptyList()
                        )
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookSolvingResultScreenDatabasePreview() {
    QuizCafeTheme {
        QuizBookSolvingResultScreen(
            state = QuizBookSolvingResultUiState(
                isLoading = false,
                quizBookSolving = QuizBookSolving(
                    id = 3L,
                    userId = 123L,
                    quizBookId = QuizBookId(3L),
                    version = 1L,
                    level = "INTERMEDIATE",
                    category = "DATABASE",
                    title = "Room 데이터베이스 퀴즈",
                    description = "Room 데이터베이스의 기본 개념과 사용법을 다루는 퀴즈입니다.",
                    totalQuizzes = 1,
                    correctCount = 1,
                    completedAt = "2025-06-13T14:15:30",
                    elapsedTime = kotlin.time.Duration.parse("PT3M15S"),
                    quizSolvingList = listOf(
                        QuizSolving(
                            id = 301L,
                            quizBookSolvingId = 3L,
                            quizId = 6L,
                            questionType = "OX",
                            content = "@Relation 어노테이션은 Room에서 테이블 간의 관계를 정의할 때 사용됩니다.",
                            answer = "O",
                            explanation = "@Relation은 Room에서 1:N 관계를 정의하는 데 사용되는 어노테이션입니다.",
                            memo = "Room 관련 기본 지식",
                            userAnswer = "O",
                            isCorrect = true,
                            completedAt = "2025-06-13T14:15:30",
                            mcqOptionSolvingList = emptyList()
                        )
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizBookSolvingResultScreenLoadingPreview() {
    QuizCafeTheme {
        QuizBookSolvingResultScreen(
            state = QuizBookSolvingResultUiState(
                isLoading = true,
                quizBookSolving = null
            )
        )
    }
}
