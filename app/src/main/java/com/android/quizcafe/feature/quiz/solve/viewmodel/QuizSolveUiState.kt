package com.android.quizcafe.feature.quiz.solve.viewmodel

import android.util.Log
import com.android.quizcafe.core.domain.model.quiz.Quiz
import com.android.quizcafe.core.domain.model.quiz.QuizGrade
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.ui.base.BaseContract
import com.android.quizcafe.feature.quiz.solve.component.AnswerState
import java.util.Locale

enum class QuestionType { OX, MULTIPLE_CHOICE, SUBJECTIVE }
enum class PlayMode { DEFAULT, REVIEW_MODE }
enum class AnswerPhase { ANSWERING, REVIEW }

data class QuizOption(
    val id: Long,
    val text: String
)

data class McqState(
    val options: List<QuizOption> = emptyList(),
    val selectedId: Long? = null,
    val selectedContent: String? = null,
    val correctId: Long? = null,
    val correctContent: String? = null
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
    val remainingSeconds: Int = 600,
    val elapsedSeconds: Int = 0
)

data class CommonState(
    val playMode: PlayMode = PlayMode.DEFAULT,
    val currentIndex: Int = 0
)

data class QuizSolveUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val quizBookLocalId: QuizBookGradeLocalId? = null,
    val quizBook: QuizBook? = null,
    val quizGrades: List<QuizGrade>? = null,
    val subjective: SubjectiveState = SubjectiveState(),
    val mcq: McqState = McqState(),
    val review: ReviewState = ReviewState(),
    val timer: TimerState = TimerState(),
    val common: CommonState = CommonState(),
    val currentGrade: QuizGrade? = null,
) : BaseContract.UiState {
    val isButtonEnabled: Boolean
        get() = (subjective.answer != "" || mcq.selectedContent != null)

    val currentQuiz: Quiz?
        get() = quizBook?.quizList?.getOrNull(common.currentIndex)

    val questionType: QuestionType
        get() = when (currentQuiz?.questionType) {
            "MCQ" -> QuestionType.MULTIPLE_CHOICE
            "OX" -> QuestionType.OX
            else -> QuestionType.SUBJECTIVE
        }
    val optionList: List<QuizOption>
        get() = currentQuiz?.mcqOption
            ?.map { QuizOption(id = it.quizId.value, text = it.optionContent) }
            ?: listOf(
                QuizOption(0L, "O"),
                QuizOption(1L, "X")
            )
    val isFirstQuestion: Boolean
        get() = common.currentIndex == 0

    val isLastQuestion: Boolean
        get() = (common.currentIndex + 1) == (quizBook?.totalQuizzes)

    val isWrongAnswer: Boolean
        get() = !review.showExplanation && (currentGrade?.isCorrect == false || optionList.any { getOptionState(it) == AnswerState.INCORRECT })

    fun getTimeText(): String {
        val seconds = timer.elapsedSeconds
        val m = seconds / 60
        val s = seconds % 60
        return String.format(Locale.KOREA, "%02d:%02d", m, s)
    }

    private val currentPhase: AnswerPhase
        get() = if (currentGrade != null) AnswerPhase.REVIEW else AnswerPhase.ANSWERING

    fun getOptionState(opt: QuizOption): AnswerState = when (currentPhase) {
        AnswerPhase.ANSWERING -> {
            if (opt.text == mcq.selectedContent) {
                AnswerState.SELECTED
            } else {
                AnswerState.DEFAULT
            }
        }

        AnswerPhase.REVIEW -> currentGrade?.let { gr ->
            Log.d("test1234", mcq.correctContent.toString())
            when {
                gr.isCorrect && opt.text == gr.userAnswer -> AnswerState.CORRECT
                !gr.isCorrect && opt.text == gr.userAnswer -> AnswerState.INCORRECT
                opt.text == mcq.correctContent -> {
                    AnswerState.CORRECT
                }

                else -> AnswerState.DEFAULT
            }
        } ?: AnswerState.DEFAULT
    }
}

fun QuizSolveUiState.previousQuestionReset(): QuizSolveUiState {
    val newIndex = (common.currentIndex - 1).coerceAtLeast(0)
    return copy(
        common = common.copy(
            currentIndex = newIndex
        ),
        mcq = McqState(),
        subjective = SubjectiveState(),
        review = ReviewState()
    )
}

fun QuizSolveUiState.applyFetchedGrade(grade: QuizGrade): QuizSolveUiState =
    when (common.playMode) {
        PlayMode.DEFAULT -> copy(
            subjective = subjective.copy(answer = grade.userAnswer),
            mcq = mcq.copy(selectedContent = grade.userAnswer)
        )

        PlayMode.REVIEW_MODE -> if (review.showExplanation) {
            copy(
                currentGrade = null,
                common = common.copy(
                    currentIndex = common.currentIndex + 1
                ),
                subjective = SubjectiveState(),
                mcq = McqState(),
                review = ReviewState()
            )
        } else {
            val correctContent = if (questionType == QuestionType.MULTIPLE_CHOICE) {
                optionList[(currentQuiz?.answer ?: "0").toInt() - 1].text
            } else {
                currentQuiz?.answer
            }
            copy(
                currentGrade = grade,
                subjective = subjective.copy(
                    answer = grade.userAnswer,
                    correctAnswer = currentQuiz?.answer
                ),
                mcq = mcq.copy(
                    selectedContent = grade.userAnswer,
                    correctContent = correctContent
                ),
                review = review.copy(
                    answerState = if (grade.isCorrect) AnswerState.CORRECT else AnswerState.INCORRECT,
                    showExplanation = true,
                    explanation = currentQuiz?.explanation.orEmpty()
                )
            )
        }
    }

fun QuizSolveUiState.onLocalSaveSuccess(): QuizSolveUiState =
    if (!isLastQuestion && common.playMode == PlayMode.DEFAULT) {
        copy(
            common = common.copy(
                currentIndex = common.currentIndex + 1
            ),
            subjective = SubjectiveState(),
            mcq = McqState()
        )
    } else {
        this
    }
