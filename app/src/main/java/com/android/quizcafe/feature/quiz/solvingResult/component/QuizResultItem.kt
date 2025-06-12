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
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.model.value.QuizId
import com.android.quizcafe.feature.quiz.solve.component.ExplanationSection

@Composable
fun QuizResultItem(
    quiz: Quiz,
    quizGrade: QuizGrade?,
    questionNumber: Int,
    initiallyExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }
    var hasTextOverflow by remember { mutableStateOf(false) }
    val isCorrect = quizGrade?.isCorrect ?: false

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
                    text = "$questionNumber. " + quiz.content,
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
                text = "${getQuestionTypeDisplayName(quiz.questionType)}",
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
                quiz = quiz,
                quizGrade = quizGrade,
                hasTextOverflow = hasTextOverflow,
                isCorrect = isCorrect
            )
        }
    }
}

@Composable
fun QuizExpandedContent(
    quiz: Quiz,
    quizGrade: QuizGrade?,
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
                text = quiz.content,
                style = quizCafeTypography().bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        // 사용자 답안과 정답
        if (quizGrade != null) {
            Text(
                text = "제출: ${quizGrade.userAnswer}",
                style = quizCafeTypography().bodySmall,
                color = if (isCorrect) checkedColor else MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "정답: ${quiz.answer}",
                style = quizCafeTypography().bodySmall,
                color = checkedColor
            )
        }

        // 해설
        if (!quiz.explanation.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            ExplanationSection(
                explanation = quiz.explanation
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
fun QuizResultItemNewStylePreview() {
    QuizCafeTheme {
        QuizResultItem(
            quiz = Quiz(
                id = QuizId(1L),
                quizBookId = QuizBookId(1L),
                questionType = "SHORT_ANSWER",
                content = "Android Architecture Components에서 ViewModel의 역할과 생명주기에 대해 설명하고, Repository 패턴과 함께 사용할 때의 장점을 서술하시오.",
                answer = "ViewModel은 UI 관련 데이터를 저장하고 관리하는 클래스",
                explanation = "ViewModel은 UI 컨트롤러의 생명주기를 고려하여 설계된 클래스입니다.",
                version = 1L
            ),
            quizGrade = QuizGrade(
                localId = 1L,
                quizId = QuizId(1L),
                quizBookGradeLocalId = QuizBookGradeLocalId(1L),
                userAnswer = "ViewModel",
                memo = null,
                isCorrect = true,
                completedAt = "2025-06-11T15:30:00"
            ),
            questionNumber = 1,
            initiallyExpanded = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultItemNewStyleExpandedPreview() {
    QuizCafeTheme {
        QuizResultItem(
            quiz = Quiz(
                id = QuizId(2L),
                quizBookId = QuizBookId(1L),
                questionType = "MCQ",
                content = "다음 중 Kotlin의 특징이 아닌 것은?\n1. Null Safety\n2. 상호 운용성\n3. 간결성\n4. 메모리 누수",
                answer = "4",
                explanation = "Kotlin은 메모리 안전성을 제공하며, 메모리 누수를 방지하는 다양한 기능을 제공합니다.",
                version = 1L
            ),
            quizGrade = QuizGrade(
                localId = 2L,
                quizId = QuizId(2L),
                quizBookGradeLocalId = QuizBookGradeLocalId(1L),
                userAnswer = "3",
                memo = null,
                isCorrect = false,
                completedAt = "2025-06-11T15:32:00"
            ),
            questionNumber = 2,
            initiallyExpanded = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultItemOXTypePreview() {
    QuizCafeTheme {
        QuizResultItem(
            quiz = Quiz(
                id = QuizId(3L),
                quizBookId = QuizBookId(1L),
                questionType = "OX",
                content = "Room 데이터베이스는 SQLite의 추상화 레이어이다.",
                answer = "O",
                explanation = "Room은 SQLite 위에 구축된 추상화 레이어입니다.",
                version = 1L
            ),
            quizGrade = QuizGrade(
                localId = 3L,
                quizId = QuizId(3L),
                quizBookGradeLocalId = QuizBookGradeLocalId(1L),
                userAnswer = "O",
                memo = null,
                isCorrect = true,
                completedAt = "2025-06-11T15:35:00"
            ),
            questionNumber = 3,
            initiallyExpanded = false
        )
    }
}
