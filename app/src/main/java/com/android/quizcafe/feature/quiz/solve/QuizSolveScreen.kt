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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.wrongButtonColor
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
import com.android.quizcafe.feature.quiz.solve.viewmodel.CommonState
import com.android.quizcafe.feature.quiz.solve.viewmodel.McqState
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuestionInfo
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuestionType
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizOption
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveIntent
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveUiState
import com.android.quizcafe.feature.quiz.solve.viewmodel.ReviewState
import com.android.quizcafe.feature.quiz.solve.viewmodel.SubjectiveState

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
                currentQuestion = uiState.question.current,
                totalQuestions = uiState.question.total,
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
                    QuizTitleSection(questionText = uiState.question.text)
                }
                item {
                    Spacer(Modifier.height(24.dp))
                }
                item {
                    when (uiState.question.type) {
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
        uiState.mcq.options.forEachIndexed { idx, option ->
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

// ——————————————————————————————————————
// OX 풀이 모드
@Preview(showBackground = true, name = "OX 풀이 모드")
@Composable
fun Preview_OX_Answering() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                question = QuestionInfo(
                    current = 3,
                    total = 10,
                    text = "OX 문제입니다",
                    type = QuestionType.OX
                ),
                mcq = McqState(
                    options = emptyList(),
                    selectedId = 0L
                ),
                review = ReviewState(
                    phase = AnswerPhase.ANSWERING,
                    answerState = AnswerState.SELECTED,
                    showExplanation = false
                ),
                common = CommonState(isButtonEnabled = true)
            ),
            onIntent = {}
        )
    }
}

// OX 리뷰 모드
@Preview(showBackground = true, name = "OX 리뷰 모드")
@Composable
fun Preview_OX_Review() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                question = QuestionInfo(
                    current = 3,
                    total = 10,
                    text = "OX 문제입니다",
                    type = QuestionType.OX
                ),
                mcq = McqState(
                    options = emptyList(),
                    selectedId = 0L,
                    correctId = 1L
                ),
                review = ReviewState(
                    phase = AnswerPhase.REVIEW,
                    answerState = AnswerState.INCORRECT,
                    showExplanation = true,
                    explanation = "O/X 해설: launch와 async 차이"
                ),
                common = CommonState(isButtonEnabled = true)
            ),
            onIntent = {}
        )
    }
}

// ——————————————————————————————————————
// 객관식 풀이 모드
@Preview(showBackground = true, name = "객관식 풀이 모드")
@Composable
fun Preview_MC_Answering() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                question = QuestionInfo(
                    current = 2,
                    total = 10,
                    text = "객관식 문제입니다",
                    type = QuestionType.MULTIPLE_CHOICE
                ),
                mcq = McqState(
                    options = listOf(
                        QuizOption(101L, "선택지 1"),
                        QuizOption(102L, "선택지 2"),
                        QuizOption(103L, "선택지 3"),
                        QuizOption(104L, "선택지 4")
                    ),
                    selectedId = 101L
                ),
                review = ReviewState(
                    phase = AnswerPhase.ANSWERING,
                    answerState = AnswerState.SELECTED
                ),
                common = CommonState(isButtonEnabled = true)
            ),
            onIntent = {}
        )
    }
}

// 객관식 리뷰 모드
@Preview(showBackground = true, name = "객관식 리뷰 모드")
@Composable
fun Preview_MC_Review() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                question = QuestionInfo(
                    current = 2,
                    total = 10,
                    text = "객관식 문제입니다",
                    type = QuestionType.MULTIPLE_CHOICE
                ),
                mcq = McqState(
                    options = listOf(
                        QuizOption(101L, "선택지 1"),
                        QuizOption(102L, "선택지 2"),
                        QuizOption(103L, "선택지 3"),
                        QuizOption(104L, "선택지 4")
                    ),
                    selectedId = 103L,
                    correctId = 102L
                ),
                review = ReviewState(
                    phase = AnswerPhase.REVIEW,
                    answerState = AnswerState.INCORRECT,
                    showExplanation = true,
                    explanation = "객관식 해설: 2번이 정답입니다."
                ),
                common = CommonState(isButtonEnabled = true)
            ),
            onIntent = {}
        )
    }
}

// ——————————————————————————————————————
// 주관식 풀이 모드
@Preview(showBackground = true, name = "주관식 풀이 모드")
@Composable
fun Preview_Subjective_Answering() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                question = QuestionInfo(
                    current = 5,
                    total = 10,
                    text = "주관식 문제입니다",
                    type = QuestionType.SUBJECTIVE
                ),
                subjective = SubjectiveState(
                    answer = "",
                    hint = "힌트를 확인하세요",
                    showCharCount = true,
                    maxCharCount = 30
                ),
                review = ReviewState(
                    phase = AnswerPhase.ANSWERING,
                    answerState = AnswerState.DEFAULT
                ),
                common = CommonState(isButtonEnabled = false)
            ),
            onIntent = {}
        )
    }
}

// 주관식 리뷰 모드
@Preview(showBackground = true, name = "주관식 리뷰 모드")
@Composable
fun Preview_Subjective_Review() {
    QuizCafeTheme {
        QuizSolveScreen(
            uiState = QuizSolveUiState(
                question = QuestionInfo(
                    current = 5,
                    total = 10,
                    text = "주관식 문제입니다",
                    type = QuestionType.SUBJECTIVE
                ),
                subjective = SubjectiveState(
                    answer = "내 답안",
                    correctAnswer = "모범 답안",
                    hint = "힌트를 확인하세요",
                    showCharCount = true,
                    maxCharCount = 30
                ),
                review = ReviewState(
                    phase = AnswerPhase.REVIEW,
                    answerState = AnswerState.INCORRECT,
                    showExplanation = true,
                    explanation = "주관식 해설입니다."
                ),
                common = CommonState(isButtonEnabled = true)
            ),
            onIntent = {}
        )
    }
}
