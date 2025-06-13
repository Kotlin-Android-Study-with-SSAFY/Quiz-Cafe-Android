package com.android.quizcafe.feature.quiz.solvingResult.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.checkedColor
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.domain.model.solving.McqOptionSolving
import com.android.quizcafe.core.domain.model.solving.QuizSolving
import com.android.quizcafe.feature.quiz.solve.component.ExplanationSection

@Composable
fun QuizResultItem(
    quizSolving: QuizSolving,
    questionNumber: Int,
    initiallyExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }
    var hasTextOverflow by remember { mutableStateOf(false) }
    val isCorrect = quizSolving.isCorrect

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        if (isCorrect) {
                            R.drawable.ic_subjective_correct
                        } else {
                            R.drawable.ic_subjective_incorrect
                        }
                    ),
                    contentDescription = null,
                    tint = if (isCorrect) checkedColor else MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$questionNumber. " + quizSolving.content,
                    style = quizCafeTypography().titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        hasTextOverflow = textLayoutResult.hasVisualOverflow
                    }
                )
            }
            Text(
                text = "${getQuestionTypeDisplayName(quizSolving.questionType)}",
                style = quizCafeTypography().labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Icon(
                painter = painterResource(
                    if (isExpanded) {
                        R.drawable.ic_keyboard_arrow_down
                    } else {
                        R.drawable.ic_keyboard_arrow_right
                    }
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(16.dp))

            QuizExpandedContent(
                quizSolving = quizSolving,
                hasTextOverflow = hasTextOverflow,
                isCorrect = isCorrect
            )
        }
    }
}

@Composable
fun QuizExpandedContent(
    quizSolving: QuizSolving,
    hasTextOverflow: Boolean,
    isCorrect: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 36.dp)
    ) {
        // 전체 문제 내용 표시
        if (hasTextOverflow) {
            Text(
                text = quizSolving.content,
                style = quizCafeTypography().bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = "제출: ${quizSolving.userAnswer}",
            style = quizCafeTypography().bodySmall,
            color = if (isCorrect) checkedColor else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "정답: ${quizSolving.answer}",
            style = quizCafeTypography().bodySmall,
            color = checkedColor
        )

        // 해설
        if (!quizSolving.explanation.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            ExplanationSection(
                explanation = quizSolving.explanation
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun getQuestionTypeDisplayName(questionType: String): String {
    return when (questionType) {
        "MCQ" -> "객관식"
        "OX" -> "OX문제"
        "SHORT_ANSWER" -> "주관식"
        else -> questionType
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultItemCorrectMCQPreview() {
    QuizCafeTheme {
        QuizResultItem(
            quizSolving = QuizSolving(
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
            questionNumber = 1,
            initiallyExpanded = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultItemIncorrectOXPreviewExpanded() {
    QuizCafeTheme {
        QuizResultItem(
            quizSolving = QuizSolving(
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
            questionNumber = 2,
            initiallyExpanded = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultItemCorrectShortAnswerPreview() {
    QuizCafeTheme {
        QuizResultItem(
            quizSolving = QuizSolving(
                id = 103L,
                quizBookSolvingId = 1L,
                quizId = 3L,
                questionType = "SHORT_ANSWER",
                content = "Android에서 비동기 작업을 처리하기 위해 사용하는 Kotlin의 기능은?",
                answer = "Coroutines",
                explanation = "Kotlin Coroutines는 비동기 프로그래밍을 위한 라이브러리로, suspend 함수와 함께 사용하여 효율적인 비동기 처리를 가능하게 합니다. launch, async, withContext 등의 빌더를 통해 다양한 비동기 작업을 수행할 수 있습니다.",
                memo = null,
                userAnswer = "Coroutines",
                isCorrect = true,
                completedAt = "2025-06-13T15:30:00",
                mcqOptionSolvingList = emptyList()
            ),
            questionNumber = 3,
            initiallyExpanded = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultItemLongContentPreview() {
    QuizCafeTheme {
        QuizResultItem(
            quizSolving = QuizSolving(
                id = 104L,
                quizBookSolvingId = 2L,
                quizId = 5L,
                questionType = "SHORT_ANSWER",
                content = "Android Architecture Components에서 ViewModel의 역할과 생명주기에 대해 설명하고, Repository 패턴과 함께 사용할 때의 장점을 서술하시오. 또한 LiveData와 DataBinding을 함께 사용하는 방법에 대해서도 설명하시오.",
                answer = "ViewModel은 UI 관련 데이터를 저장하고 관리하는 클래스로, 화면 회전 등의 구성 변경에도 데이터를 유지합니다.",
                explanation = "ViewModel은 UI 컨트롤러(Activity/Fragment)의 생명주기를 고려하여 설계된 클래스입니다. 화면 회전이나 기타 구성 변경 시에도 데이터를 유지하며, Repository 패턴과 함께 사용하면 데이터 소스의 추상화와 테스트 용이성을 제공합니다. 또한 메모리 누수를 방지하고 UI와 비즈니스 로직을 분리하는 데 도움이 됩니다.",
                memo = "좀 더 자세히 써야 했는데...",
                userAnswer = "ViewModel은 데이터를 관리하는 클래스입니다.",
                isCorrect = false,
                completedAt = "2025-06-13T17:05:42",
                mcqOptionSolvingList = emptyList()
            ),
            questionNumber = 4,
            initiallyExpanded = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultItemLongContentExpandedPreview() {
    QuizCafeTheme {
        QuizResultItem(
            quizSolving = QuizSolving(
                id = 105L,
                quizBookSolvingId = 2L,
                quizId = 6L,
                questionType = "MCQ",
                content = "Jetpack Compose에서 상태 관리를 위해 사용하는 것 중 올바른 것은? 이 문제는 Compose의 핵심 개념인 상태 관리에 대한 이해를 묻는 문제입니다.",
                answer = "1",
                explanation = "remember는 Compose에서 상태를 기억하고 리컴포지션 간에 값을 유지하는 데 사용됩니다. mutableStateOf와 함께 사용하여 상태 변경 시 UI를 자동으로 업데이트할 수 있습니다.",
                memo = "Compose 상태 관리 기본",
                userAnswer = "1",
                isCorrect = true,
                completedAt = "2025-06-13T16:45:00",
                mcqOptionSolvingList = listOf(
                    McqOptionSolving(
                        id = 2001L,
                        quizSolvingId = 105L,
                        optionNumber = 1,
                        optionContent = "remember",
                        isCorrect = true
                    ),
                    McqOptionSolving(
                        id = 2002L,
                        quizSolvingId = 105L,
                        optionNumber = 2,
                        optionContent = "useState",
                        isCorrect = false
                    ),
                    McqOptionSolving(
                        id = 2003L,
                        quizSolvingId = 105L,
                        optionNumber = 3,
                        optionContent = "observable",
                        isCorrect = false
                    ),
                    McqOptionSolving(
                        id = 2004L,
                        quizSolvingId = 105L,
                        optionNumber = 4,
                        optionContent = "reactive",
                        isCorrect = false
                    )
                )
            ),
            questionNumber = 5,
            initiallyExpanded = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultItemWithoutExplanationPreview() {
    QuizCafeTheme {
        QuizResultItem(
            quizSolving = QuizSolving(
                id = 106L,
                quizBookSolvingId = 3L,
                quizId = 7L,
                questionType = "OX",
                content = "Kotlin은 JVM에서만 실행됩니다.",
                answer = "X",
                explanation = null,
                memo = "간단한 문제",
                userAnswer = "X",
                isCorrect = true,
                completedAt = "2025-06-13T14:10:00",
                mcqOptionSolvingList = emptyList()
            ),
            questionNumber = 6,
            initiallyExpanded = true
        )
    }
}
