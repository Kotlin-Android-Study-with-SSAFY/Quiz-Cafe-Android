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

data class QuestionInfo(
    val current: Int,
    val total: Int,
    val text: String,
    val type: QuestionType
)

data class McqState(
    val options: List<QuizOption>,
    val selectedId: Long? = null,
    val correctId: Long? = null
)

data class SubjectiveState(
    val answer: String = "",
    val correctAnswer: String? = null,
    val hint: String = "",
    val showCharCount: Boolean = true,
    val maxCharCount: Int = 30
)

data class ReviewState(
    val phase: AnswerPhase = AnswerPhase.ANSWERING,
    val answerState: AnswerState = AnswerState.DEFAULT,
    val showExplanation: Boolean = false,
    val explanation: String = ""
)

data class TimerState(
    val playMode: PlayMode = PlayMode.NO_TIME_ATTACK,
    val remainingSeconds: Int = 600,
    val elapsedSeconds: Int = 0
)

data class CommonState(
    val isButtonEnabled: Boolean = false
)

data class QuizSolveUiState(
    val question: QuestionInfo = QuestionInfo(1, 10, "", QuestionType.MULTIPLE_CHOICE),
    val mcq: McqState = McqState(
        options = listOf(
            QuizOption(101L, "선택지 1"),
            QuizOption(102L, "선택지 2"),
            QuizOption(103L, "선택지 3"),
            QuizOption(104L, "선택지 4")
        )
    ),
    val subjective: SubjectiveState = SubjectiveState(),
    val review: ReviewState = ReviewState(),
    val timer: TimerState = TimerState(),
    val common: CommonState = CommonState()
) : BaseContract.ViewState {
    val isLastQuestion: Boolean
        get() = question.current == question.total

    val isWrongAnswer: Boolean
        get() = review.answerState == AnswerState.INCORRECT && !review.showExplanation

    fun optionState(opt: QuizOption): AnswerState = when (review.phase) {
        AnswerPhase.ANSWERING -> when {
            opt.id == mcq.selectedId -> AnswerState.SELECTED
            else -> AnswerState.DEFAULT
        }

        AnswerPhase.REVIEW -> when (opt.id) {
            mcq.correctId -> AnswerState.CORRECT
            mcq.selectedId -> AnswerState.INCORRECT
            else -> AnswerState.DEFAULT
        }
    }

    fun getTimeText(): String {
        val seconds = if (timer.playMode == PlayMode.TIME_ATTACK) timer.remainingSeconds else timer.elapsedSeconds
        val m = seconds / 60
        val s = seconds % 60
        return String.format(Locale.KOREA, "%02d:%02d", m, s)
    }
}
