package com.android.quizcafe.feature.quiz.solve.viewmodel

import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
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
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val quizBookLocalId: QuizBookGradeLocalId? = null,
    val quizBook: QuizBook? = null,
    val quizGrades: List<QuizGrade> = emptyList(),
    val currentIndex: Int = 0,
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
) : BaseContract.UiState {
    val currentQuiz: Quiz?
        get() = quizBook?.quizList?.getOrNull(currentIndex)

    val questionInfo: QuestionInfo
        get() = QuestionInfo(
            current = currentIndex + 1,
            total = quizBook?.totalQuizzes ?: 0,
            text = currentQuiz?.content.orEmpty(),
            type = QuestionType.MULTIPLE_CHOICE
        )
    val optionList: List<QuizOption>
        get() = currentQuiz?.mcqOption
            ?.map { QuizOption(id = it.quizId.value, text = it.optionContent) }
            ?: listOf(
                QuizOption(0L, "O"),
                QuizOption(1L, "X")
            )
    val correctAnswerText: String?
        get() = currentQuiz?.answer

    val explanationText: String?
        get() = currentQuiz?.explanation

    private val currentGrade: QuizGrade?
        get() = quizGrades.getOrNull(currentIndex)

    val currentPhase: AnswerPhase
        get() = if (currentGrade != null) AnswerPhase.REVIEW else AnswerPhase.ANSWERING

    fun optionState(opt: QuizOption): AnswerState = when (currentPhase) {
        AnswerPhase.ANSWERING ->
            if (opt.id == mcq.selectedId) {
                AnswerState.SELECTED
            } else {
                AnswerState.DEFAULT
            }

        AnswerPhase.REVIEW -> currentGrade?.let { gr ->
            when {
                gr.isCorrect && opt.id.toString() == gr.userAnswer -> AnswerState.CORRECT
                !gr.isCorrect && opt.id.toString() == gr.userAnswer -> AnswerState.INCORRECT
                else -> AnswerState.DEFAULT
            }
        } ?: AnswerState.DEFAULT
    }

    val subjectiveAnswerState: AnswerState
        get() = when (currentPhase) {
            AnswerPhase.ANSWERING ->
                if (subjective.answer.isNotBlank()) AnswerState.SELECTED else AnswerState.DEFAULT

            AnswerPhase.REVIEW ->
                if (currentGrade?.isCorrect == true) AnswerState.CORRECT else AnswerState.INCORRECT
        }

    val isLastQuestion: Boolean
        get() = questionInfo.current == questionInfo.total

    val isWrongAnswer: Boolean
        get() = subjectiveAnswerState == AnswerState.INCORRECT && !review.showExplanation ||
            optionList.any { optionState(it) == AnswerState.INCORRECT } && !review.showExplanation

    fun getTimeText(): String {
        val seconds = if (timer.playMode == PlayMode.TIME_ATTACK) timer.remainingSeconds else timer.elapsedSeconds
        val m = seconds / 60
        val s = seconds % 60
        return String.format(Locale.KOREA, "%02d:%02d", m, s)
    }
}
