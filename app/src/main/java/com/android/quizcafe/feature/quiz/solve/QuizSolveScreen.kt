package com.android.quizcafe.feature.quiz.solve

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.theme.neutral08
import com.android.quizcafe.core.designsystem.theme.primaryLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import com.android.quizcafe.core.designsystem.theme.surfaceDimLight
import com.android.quizcafe.core.designsystem.theme.yellow_200
import com.android.quizcafe.feature.quiz.solve.component.ExplanationSection
import com.android.quizcafe.feature.quiz.solve.component.MultipleChoiceOptionButton
import com.android.quizcafe.feature.quiz.solve.component.OxOptionButton
import com.android.quizcafe.feature.quiz.solve.component.QuizTopBar
import com.android.quizcafe.feature.quiz.solve.component.UnderlinedTextField
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuestionType
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizOption
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveIntent
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveUiState

@Composable
fun QuizSolveScreen(
    uiState: QuizSolveUiState,
    onIntent: (QuizSolveIntent) -> Unit
) {
    val textRightRes = when {
        uiState.isWrongAnswer -> R.string.solve_btn_explanation
        uiState.isLastQuestion -> R.string.solve_btn_submit
        else -> R.string.solve_btn_next_text
    }
    Scaffold(
        topBar = {
            QuizTopBar(
                currentQuestion = uiState.common.currentIndex + 1,
                totalQuestions = uiState.quizBook?.totalQuizzes ?: 0,
                timeText = uiState.getTimeText(),
                onBackClick = { onIntent(QuizSolveIntent.NavigateBack) },
                onSideBarClick = { /* 사이드바 보여줘? 말어 */ },
            )
        },
        bottomBar = {
            QuizSolveBottomBar(
                isEnabled = uiState.isButtonEnabled,
                isLastQuestion = uiState.isLastQuestion,
                isFirstQuestion = uiState.isFirstQuestion,
                onClickActionPrev = { onIntent(QuizSolveIntent.NavigateToPreviousQuestion) },
                onClickActionNext = { onIntent(QuizSolveIntent.NavigateToNextQuestion) },
                textRes = textRightRes
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
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
                    QuizTitleSection(questionText = uiState.currentQuiz?.content ?: "")
                }
                item {
                    Spacer(Modifier.height(24.dp))
                }
                item {
                    when (uiState.questionType) {
                        QuestionType.OX -> {
                            SelectOXSection(uiState = uiState, onIntent = onIntent)
                        }

                        QuestionType.MULTIPLE_CHOICE -> {
                            SelectMultipleChoiceSection(uiState = uiState, onIntent = onIntent)
                        }

                        QuestionType.SUBJECTIVE -> {
                            SubjectiveAnswerSection(uiState = uiState, onIntent = onIntent)
                        }
                    }
                }
                if (uiState.review.showExplanation) {
                    item {
                        ExplanationSection(explanation = uiState.review.explanation)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizSolveBottomBar(
    isEnabled: Boolean,
    isLastQuestion: Boolean,
    isFirstQuestion: Boolean,
    onClickActionPrev: () -> Unit,
    onClickActionNext: () -> Unit,
    textRes: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!isFirstQuestion) {
            QuizCafeButton(
                modifier = Modifier
                    .weight(1F)
                    .padding(horizontal = 16.dp),
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellow_200,
                    contentColor = scrimLight,
                    disabledContainerColor = surfaceDimLight
                ),
                onClick = onClickActionPrev,
                text = {
                    Text(
                        text = stringResource(R.string.solve_btn_prev_text),
                        style = quizCafeTypography().titleSmall
                    )
                }
            )
        }
        QuizCafeButton(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 16.dp),
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLastQuestion) yellow_200 else primaryLight,
                contentColor = scrimLight,
                disabledContainerColor = surfaceDimLight
            ),
            onClick = onClickActionNext,
            text = {
                Text(
                    text = stringResource(textRes),
                    style = quizCafeTypography().titleSmall
                )
            }
        )
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
        value = uiState.subjective.answer,
        onValueChange = { onIntent(QuizSolveIntent.UpdateSubjectiveAnswer(it)) },
        maxCharCount = uiState.subjective.maxCharCount,
        showCharCount = uiState.subjective.showCharCount,
        answerState = uiState.review.answerState
    )
    Spacer(Modifier.height(24.dp))
    Text(
        text = "힌트 : ${uiState.subjective.hint}",
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
        uiState.optionList.forEachIndexed { idx, option ->
            MultipleChoiceOptionButton(
                modifier = modifier,
                answerState = uiState.getOptionState(option),
                index = idx + 1,
                content = option.text,
                onClick = { onIntent(QuizSolveIntent.SelectAnswer(option)) }
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
    val oxOptions = listOf(
        QuizOption(id = 0L, text = "O"),
        QuizOption(id = 1L, text = "X")
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        oxOptions.forEach { option ->
            OxOptionButton(
                modifier = modifier.weight(1F),
                answerState = uiState.getOptionState(option),
                iconPaint = if (option.text == "O") R.drawable.ic_ox_option_o else R.drawable.ic_ox_option_x,
                onClick = { onIntent(QuizSolveIntent.SelectAnswer(option)) }
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
