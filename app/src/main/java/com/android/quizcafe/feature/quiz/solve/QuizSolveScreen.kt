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
import com.android.quizcafe.core.designsystem.theme.isWrongButton
import com.android.quizcafe.core.designsystem.theme.neutral08
import com.android.quizcafe.core.designsystem.theme.primaryLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import com.android.quizcafe.core.designsystem.theme.surfaceDimLight
import com.android.quizcafe.feature.quiz.solve.component.AnswerState
import com.android.quizcafe.feature.quiz.solve.component.ExplanationSection
import com.android.quizcafe.feature.quiz.solve.component.MultipleChoiceOptionButton
import com.android.quizcafe.feature.quiz.solve.component.OxOptionButton
import com.android.quizcafe.feature.quiz.solve.component.QuizTopBar
import com.android.quizcafe.feature.quiz.solve.component.UnderlinedTextField
import com.android.quizcafe.feature.quiz.solve.viewmodel.AnswerPhase
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuestionType
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizOption
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveIntent
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSolveScreen(
    uiState: QuizSolveUiState,
    onIntent: (QuizSolveIntent) -> Unit
) {
    val isLast = uiState.currentQuestion == uiState.totalQuestions
    val isWrong = uiState.answerState == AnswerState.INCORRECT && !uiState.showExplanation

    val textRes = when {
        isWrong -> R.string.solve_btn_explanation
        isLast -> R.string.solve_btn_submit
        else -> R.string.solve_btn_next_text
    }

    val onClickAction = {
        when {
            isWrong -> onIntent(QuizSolveIntent.ShowExplanation)
            isLast -> onIntent(QuizSolveIntent.SubmitAnswer)
            else -> onIntent(QuizSolveIntent.SubmitNext)
        }
    }

    val containerColor = when {
        isWrong -> isWrongButton
        isLast -> isWrongButton
        else -> primaryLight
    }
    Scaffold(
        topBar = {
            QuizTopBar(
                currentQuestion = uiState.currentQuestion,
                totalQuestions = uiState.totalQuestions,
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
                    QuizTitleSection(questionText = uiState.questionText)
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
                if (uiState.showExplanation) {
                    item {
                        ExplanationSection(explanation = uiState.explanation)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1F))
            QuizCafeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = uiState.isButtonEnabled,
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
        value = uiState.subjectiveAnswer,
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
    val oxOptions = if (uiState.options.isNotEmpty()) {
        uiState.options
    } else {
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

@Preview(showBackground = true, name = "OX 풀이 모드")
@Composable
fun Preview_OX_Answering() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                currentQuestion = 3,
                totalQuestions = 10,
                questionType = QuestionType.OX,
                phase = AnswerPhase.ANSWERING,
                selectedOptionId = 0L,
                isButtonEnabled = true,
            ),
            onIntent = {}
        )
    }
}

// 프리뷰: OX 리뷰 모드
@Preview(showBackground = true, name = "OX 리뷰 모드")
@Composable
fun Preview_OX_Review() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                currentQuestion = 3,
                totalQuestions = 10,
                questionType = QuestionType.OX,
                phase = AnswerPhase.REVIEW,
                selectedOptionId = 0L,
                correctOptionId = 1L,
                showExplanation = true,
                explanation = "O/X 코루틴 해설: launch vs async 차이입니다.",
                isButtonEnabled = true
            ),
            onIntent = {}
        )
    }
}

// ——————————————————————————————————————
// 프리뷰: 객관식 풀이 모드
@Preview(showBackground = true, name = "객관식 풀이 모드")
@Composable
fun Preview_MC_Answering() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                currentQuestion = 2,
                totalQuestions = 10,
                questionType = QuestionType.MULTIPLE_CHOICE,
                phase = AnswerPhase.ANSWERING,
                options = listOf(
                    QuizOption(101L, "코루틴은 자바에서만 사용할 수 있다."),
                    QuizOption(102L, "코루틴은 안드로이드에서도 사용할 수 있다."),
                    QuizOption(103L, "코루틴은 메모리 낭비가 크다."),
                    QuizOption(104L, "코루틴은 비동기 작업에 최적화되어 있다.")
                ),
                selectedOptionId = 101L,
                isButtonEnabled = true
            ),
            onIntent = {}
        )
    }
}

// 프리뷰: 객관식 리뷰 모드
@Preview(showBackground = true, name = "객관식 리뷰 모드")
@Composable
fun Preview_MC_Review() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                currentQuestion = 2,
                totalQuestions = 10,
                questionType = QuestionType.MULTIPLE_CHOICE,
                phase = AnswerPhase.REVIEW,
                questionText = "끝?",
                options = listOf(
                    QuizOption(101L, "코루틴은 자바에서만 사용할 수 있다."),
                    QuizOption(102L, "코루틴은 안드로이드에서도 사용할 수 있다."),
                    QuizOption(103L, "코루틴은 메모리 낭비가 크다."),
                    QuizOption(104L, "코루틴은 비동기 작업에 최적화되어 있다.")
                ),
                selectedOptionId = 103L,
                correctOptionId = 102L,
                showExplanation = true,
                explanation = "정답은 안드로이드에서도… 입니다.",
                isButtonEnabled = true
            ),
            onIntent = {}
        )
    }
}

// ——————————————————————————————————————
// 프리뷰: 주관식 풀이 모드
@Preview(showBackground = true, name = "주관식 풀이 모드")
@Composable
fun Preview_Subjective_Answering() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                currentQuestion = 5,
                totalQuestions = 10,
                questionType = QuestionType.SUBJECTIVE,
                phase = AnswerPhase.ANSWERING,
                subjectiveAnswer = "",
                isButtonEnabled = false
            ),
            onIntent = {}
        )
    }
}

// 프리뷰: 주관식 리뷰 모드
@Preview(showBackground = true, name = "주관식 리뷰 모드")
@Composable
fun Preview_Subjective_Review() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                currentQuestion = 5,
                totalQuestions = 10,
                questionType = QuestionType.SUBJECTIVE,
                phase = AnswerPhase.REVIEW,
                answerState = AnswerState.INCORRECT,
                questionText = "Q. 끝인가요?",
                subjectiveAnswer = "제가 생각한 답",
                correctAnswerText = "모범 답안입니다",
                showExplanation = true,
                explanation = "이 주관식 문제의 해설입니다.",
                isButtonEnabled = true
            ),
            onIntent = {}
        )
    }
}
