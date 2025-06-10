package com.android.quizcafe.feature.quiz.solve.viewmodel

import com.android.quizcafe.core.ui.base.BaseContract
import com.android.quizcafe.feature.quiz.solve.component.AnswerState
import java.util.Locale

enum class QuestionType { OX, MULTIPLE_CHOICE, SUBJECTIVE }
enum class PlayMode { TIME_ATTACK, NO_TIME_ATTACK }

data class QuizSolveUiState(
    val currentQuestion: Int = 1,
    val totalQuestions: Int = 10,
    val questionType: QuestionType = QuestionType.OX,
    val playMode: PlayMode = PlayMode.NO_TIME_ATTACK,
    val questionText: String = "",
    val selectedOption: String = "",
    val subjectHint: String = "",
    val maxCharCount: Int = 30,
    val showCharCount: Boolean = true,
    val answerState: AnswerState = AnswerState.DEFAULT,
    val options: List<String> = emptyList(),
    val isButtonEnabled: Boolean = true,
    /** 남은 시간(TIME_ATTACK 모드) */
    val remainingSeconds: Int = 600,
    /** 경과 시간(NO_TIME_ATTACK 모드) */
    val elapsedSeconds: Int = 0
) : BaseContract.ViewState {
    fun optionState(option: String): AnswerState = when {
        answerState == AnswerState.DEFAULT -> AnswerState.DEFAULT
        selectedOption == option && answerState == AnswerState.SELECTED -> AnswerState.SELECTED
        selectedOption == option && answerState == AnswerState.CORRECT -> AnswerState.CORRECT
        selectedOption == option && answerState == AnswerState.INCORRECT -> AnswerState.INCORRECT
        else -> AnswerState.DEFAULT
    }

    fun getTimeText(): String {
        val seconds = if (playMode == PlayMode.TIME_ATTACK) remainingSeconds else elapsedSeconds
        val m = seconds / 60
        val s = seconds % 60
        return String.format(Locale.KOREA, "%02d:%02d", m, s)
    }
}
