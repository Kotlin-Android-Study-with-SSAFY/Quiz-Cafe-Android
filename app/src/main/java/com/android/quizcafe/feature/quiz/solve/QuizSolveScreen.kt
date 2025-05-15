package com.android.quizcafe.feature.quiz.solve

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.neutral08
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.feature.quiz.solve.component.MultipleChoiceOptionButton
import com.android.quizcafe.feature.quiz.solve.component.OxOptionButton
import com.android.quizcafe.feature.quiz.solve.component.QuizTopBar
import com.android.quizcafe.feature.quiz.solve.component.UnderlinedTextField
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuestionType
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveIntent
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSolveScreen(
    uiState: QuizSolveUiState,
    onIntent: (QuizSolveIntent) -> Unit
) {
    Scaffold(
        topBar = {
            QuizTopBar(
                currentQuestion = uiState.currentQuestion,
                totalQuestions = uiState.totalQuestions,
                timeText = uiState.getTimeRemainText(uiState.remainingSeconds),
                onBackClick = { onIntent(QuizSolveIntent.OnBackClick) },
                onSideBarClick = { /* 사이드바 보여줘? 말어 */ },
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(Modifier.height(36.dp))
                }
                item {
                    QuizTitleSection(questionText = uiState.questionText)
                }
                item {
                    Spacer(Modifier.height(24.dp))
                }
                item {
                    when (uiState.questionType) {
                        QuestionType.OX -> {
                            SelectOXSection(uiState = uiState) {
                                onIntent
                            }
                        }

                        QuestionType.MULTIPLE_CHOICE -> {
                            SelectMultipleChoiceSection(uiState = uiState) {
                                onIntent
                            }
                        }

                        QuestionType.SUBJECTIVE -> {
                            SubjectiveAnswerSection(uiState = uiState) {
                                onIntent
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1F))
            QuizCafeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = uiState.isButtonEnabled,
                onClick = {},
                text = {
                    Text(
                        text = stringResource(R.string.solve_btn_next_text),
                        style = quizCafeTypography().titleSmall
                    )
                }
            )
        }
    }
}

@Composable
fun SubjectiveAnswerSection(
    modifier: Modifier = Modifier,
    uiState: QuizSolveUiState,
    onIntent: (QuizSolveIntent) -> Unit
) {
    UnderlinedTextField(
        modifier = modifier,
        value = uiState.selectedOption,
        onValueChange = { onIntent(QuizSolveIntent.UpdatedSubjectiveAnswer(it)) },
        maxCharCount = uiState.maxCharCount,
        showCharCount = uiState.showCharCount,
        answerState = uiState.answerState
    )
    Spacer(Modifier.height(24.dp))
    Text(
        text = "힌트 : ${uiState.subjectHint}",
        style = quizCafeTypography().labelLarge,
        modifier = Modifier.fillMaxWidth(),
        color = neutral08
    )
}

@Composable
fun SelectMultipleChoiceSection(
    modifier: Modifier = Modifier,
    uiState: QuizSolveUiState,
    onIntent: (QuizSolveIntent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        uiState.options.forEachIndexed { idx, option ->
            MultipleChoiceOptionButton(
                modifier = modifier,
                answerState = uiState.answerState,
                index = idx + 1,
                content = option,
                onClick = { onIntent(QuizSolveIntent.SelectOXOption(option)) }
            )
        }
    }
}

@Composable
fun SelectOXSection(
    modifier: Modifier = Modifier,
    uiState: QuizSolveUiState,
    onIntent: (QuizSolveIntent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("O", "X").forEach { option ->
            OxOptionButton(
                modifier = modifier.weight(1F),
                answerState = uiState.optionState(option),
                iconPaint = if (option == "O") R.drawable.ic_ox_option_o else R.drawable.ic_ox_option_x,
                onClick = { onIntent(QuizSolveIntent.SelectOXOption(option)) }
            )
        }
    }
}

@Composable
fun QuizTitleSection(modifier: Modifier = Modifier, questionText: String) {
    Text(
        text = questionText,
        style = quizCafeTypography().titleMedium,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun QuizSolveScreenOXPreview() {
    QuizCafeTheme {
        QuizSolveScreen(
            QuizSolveUiState(
                questionText = "Q1. 박승준의 나이는?",
                questionType = QuestionType.OX
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun QuizSolveScreenMultiplePreview() {
    QuizCafeTheme {
        QuizSolveScreen(
            QuizSolveUiState(
                questionText = "Q1. 박승준의 나이는?",
                questionType = QuestionType.MULTIPLE_CHOICE,
                options = listOf(
                    "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                    "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                    "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                    "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다."
                )
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun QuizSolveScreenSubjectivePreview() {
    QuizCafeTheme {
        QuizSolveScreen(
            QuizSolveUiState(
                questionText = "Q1. 박승준의 나이는?",
                questionType = QuestionType.SUBJECTIVE,
                options = listOf(
                    "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                    "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                    "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다.",
                    "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다."
                ),
                subjectHint = "니가 알아서 처 맞춰라"
            )
        ) {}
    }
}
