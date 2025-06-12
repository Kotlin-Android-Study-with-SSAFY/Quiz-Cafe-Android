package com.android.quizcafe.feature.quiz.solve.viewmodel

import com.android.quizcafe.core.ui.base.BaseContract
import com.android.quizcafe.feature.quiz.solve.component.AnswerState
import java.util.Locale

enum class QuestionType { OX, MULTIPLE_CHOICE, SUBJECTIVE }
enum class PlayMode { TIME_ATTACK, NO_TIME_ATTACK }
enum class AnswerPhase { ANSWERING, REVIEW }

data class QuizOption(
    val id: Long,
    val text: String
)

data class QuizSolveUiState(
    val currentQuestion: Int = 1,
    val totalQuestions: Int = 10,
    val questionType: QuestionType = QuestionType.OX,
    val phase: AnswerPhase = AnswerPhase.ANSWERING,
    val playMode: PlayMode = PlayMode.NO_TIME_ATTACK,
    val questionText: String = "",
    // MC/OX 전용
    val options: List<QuizOption> = emptyList(),
    val selectedOptionId: Long? = null,
    val correctOptionId: Long? = null,
    // SUBJECTIVE 전용
    val subjectiveAnswer: String = "",
    val correctAnswerText: String? = null,
    val subjectHint: String = "",
    val maxCharCount: Int = 30,
    val showCharCount: Boolean = true,
    val answerState: AnswerState = AnswerState.DEFAULT,
    val showExplanation: Boolean = false,
    val explanation: String = "",
    val isButtonEnabled: Boolean = false,
    /** 남은 시간(TIME_ATTACK 모드) */
    val remainingSeconds: Int = 600,
    /** 경과 시간(NO_TIME_ATTACK 모드) */
    val elapsedSeconds: Int = 0
) : BaseContract.UiState {
    fun optionState(opt: QuizOption): AnswerState = when (phase) {
        AnswerPhase.ANSWERING -> when {
            opt.id == selectedOptionId -> AnswerState.SELECTED
            else -> AnswerState.DEFAULT
        }

        AnswerPhase.REVIEW -> when {
            opt.id == correctOptionId -> AnswerState.CORRECT
            opt.id == selectedOptionId -> AnswerState.INCORRECT
            else -> AnswerState.DEFAULT
        }
    }

    fun getTimeText(): String {
        val seconds = if (playMode == PlayMode.TIME_ATTACK) remainingSeconds else elapsedSeconds
        val m = seconds / 60
        val s = seconds % 60
        return String.format(Locale.KOREA, "%02d:%02d", m, s)
    }
}
