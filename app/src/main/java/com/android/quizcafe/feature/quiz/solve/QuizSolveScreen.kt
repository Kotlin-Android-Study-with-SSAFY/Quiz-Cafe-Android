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
import com.android.quizcafe.core.designsystem.theme.wrongButtonColor
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
    val textRes = when {
        uiState.isWrongAnswer -> R.string.solve_btn_explanation
        uiState.isLastQuestion -> R.string.solve_btn_submit
        else -> R.string.solve_btn_next_text
    }
    val onClickAction = {
        when {
            uiState.isWrongAnswer -> onIntent(QuizSolveIntent.ShowExplanation)
            uiState.isLastQuestion -> onIntent(QuizSolveIntent.SubmitAnswer)
            else -> onIntent(QuizSolveIntent.SubmitNext)
        }
    }

    val containerColor = when {
        uiState.isWrongAnswer -> wrongButtonColor
        uiState.isLastQuestion -> wrongButtonColor
        else -> primaryLight
    }
    Scaffold(
        topBar = {
            QuizTopBar(
                currentQuestion = uiState.questionInfo.current,
                totalQuestions = uiState.questionInfo.total,
                timeText = uiState.getTimeText(),
                onBackClick = { onIntent(QuizSolveIntent.OnBackClick) },
                onSideBarClick = { /* 사이드바 보여줘? 말어 */ },
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
                    QuizTitleSection(questionText = uiState.questionInfo.text)
                }
                item {
                    Spacer(Modifier.height(24.dp))
                }
                item {
                    when (uiState.questionInfo.type) {
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

            Spacer(modifier = Modifier.weight(1F))
            QuizCafeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = uiState.common.isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    contentColor = scrimLight,
                    disabledContainerColor = surfaceDimLight
                ),
                onClick = onClickAction,
                text = {
                    Text(
                        text = stringResource(textRes),
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
        value = uiState.subjective.answer,
        onValueChange = { onIntent(QuizSolveIntent.UpdatedSubjectiveAnswer(it)) },
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
                answerState = uiState.optionState(option),
                index = idx + 1,
                content = option.text,
                onClick = { onIntent(QuizSolveIntent.SelectOption(option.id)) }
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
    val oxOptions = uiState.mcq.options.ifEmpty {
        listOf(
            QuizOption(id = 0L, text = "O"),
            QuizOption(id = 1L, text = "X")
        )
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        oxOptions.forEach { option ->
            OxOptionButton(
                modifier = modifier.weight(1F),
                answerState = uiState.optionState(option),
                iconPaint = if (option.text == "O") R.drawable.ic_ox_option_o else R.drawable.ic_ox_option_x,
                onClick = { onIntent(QuizSolveIntent.SelectOption(option.id)) }
            )
        }
    }
}

@Composable
fun QuizTitleSection(modifier: Modifier = Modifier, questionText: String) {
    Text(
        text = "Q.$questionText",
        style = quizCafeTypography().titleMedium,
        modifier = modifier.fillMaxWidth()
    )
}

