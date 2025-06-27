package com.android.quizcafe.feature.quiz.solve.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.usecase.quizbook.GetQuizBookUseCase
import com.android.quizcafe.core.domain.usecase.solving.DeleteQuizBookGradeUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetOrCreateQuizBookGradeUseCase
import com.android.quizcafe.core.domain.usecase.solving.GradeQuizUseCase
import com.android.quizcafe.core.domain.usecase.solving.SolveQuizBookUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizSolveViewModel @Inject constructor(
    private val getQuizBookUseCase: GetQuizBookUseCase,
    private val getOrCreateQuizBookGradeUseCase: GetOrCreateQuizBookGradeUseCase,
    private val gradeQuizUseCase: GradeQuizUseCase,
    private val solveQuizBookUseCase: SolveQuizBookUseCase,
    private val deleteQuizBookGradeUseCase: DeleteQuizBookGradeUseCase,
) : BaseViewModel<QuizSolveUiState, QuizSolveIntent, QuizSolveEffect>(
    initialState = QuizSolveUiState()
),
    IQuizSolveViewModel {
    init {
        viewModelScope.launch {
            while (true) {
                delay(1_000L)
                if (state.value.common.isTimerActive) {
                    sendIntent(QuizSolveIntent.TickTime)
                }
            }
        }
    }

    override suspend fun handleIntent(intent: QuizSolveIntent) {
        when (intent) {
            is QuizSolveIntent.StartSolving -> {
                val quizBookId = QuizBookId(intent.quizBookId)
                getQuizBook(quizBookId)
                getQuizBookGrade(quizBookId)
            }
            is QuizSolveIntent.ResumeSolving -> {
                if (intent.resumeWithNewSolving) {
                    val quizBookId = state.value.quizBook?.id
                    deleteQuizBookGrade()
                    quizBookId?.let {
                        getQuizBookGrade(it)
                    }
                }
                emitEffect(QuizSolveEffect.CloseResumeDialog)
                sendIntent(QuizSolveIntent.StartTimer)
            }

            QuizSolveIntent.OnBackClick -> {
                emitEffect(QuizSolveEffect.ShowExitDialog)
            }

            is QuizSolveIntent.SubmitNext -> {
                saveQuizToLocal()
            }
            is QuizSolveIntent.SubmitAnswer -> {
                submitQuizAnswer()
            }

            is QuizSolveIntent.ExitWithDelete -> {
                deleteQuizBookGrade()
                emitEffect(QuizSolveEffect.NavigateToBack)
            }
            is QuizSolveIntent.ExitWithSave -> {
                emitEffect(QuizSolveEffect.NavigateToBack)
            }
            is QuizSolveIntent.SolveQuizSuccess -> {
                emitEffect(QuizSolveEffect.NavigateToQuizBookSolvingResult(intent.quizBookGradeServerId))
            }

            is QuizSolveIntent.GradeQuizError -> {
                emitEffect(QuizSolveEffect.ShowErrorDialog(intent.message ?: ""))
            }
            else -> Unit
        }
    }

    override fun reduce(currentState: QuizSolveUiState, intent: QuizSolveIntent): QuizSolveUiState {
        return when (intent) {
            QuizSolveIntent.StartTimer -> {
                currentState.copy(
                    common = currentState.common.copy(isTimerActive = true)
                )
            }
            QuizSolveIntent.TickTime -> {
                val timer = currentState.timer
                when (timer.playMode) {
                    PlayMode.TIME_ATTACK ->
                        currentState.copy(
                            timer = timer.copy(
                                remainingSeconds = (timer.remainingSeconds - 1).coerceAtLeast(0)
                            )
                        )

                    PlayMode.NO_TIME_ATTACK ->
                        currentState.copy(
                            timer = timer.copy(
                                elapsedSeconds = timer.elapsedSeconds + 1
                            )
                        )
                }
            }

            is QuizSolveIntent.SelectOption ->
                currentState.copy(
                    mcq = currentState.mcq.copy(
                        selectedId = intent.option.id,
                        selectedContent = intent.option.text
                    ),
                    common = currentState.common.copy(
                        isButtonEnabled = true
                    )
                )

            is QuizSolveIntent.UpdatedSubjectiveAnswer ->
                currentState.copy(
                    subjective = currentState.subjective.copy(
                        answer = intent.answer
                    ),
                    common = currentState.common.copy(
                        isButtonEnabled = intent.answer.isNotBlank()
                    )
                )

            // ─── 4) 해설보기 토글 ─────────────────────────────────────────
            QuizSolveIntent.ShowExplanation ->
                currentState.copy(
                    review = currentState.review.copy(
                        showExplanation = true
                    )
                )

            is QuizSolveIntent.SuccessGetQuizBook -> {
                currentState.copy(
                    quizBook = intent.data,
                    isLoading = false
                )
            }

            is QuizSolveIntent.SuccessGetQuizBookGrade -> {
                currentState.copy(
                    quizBookGrade = intent.quizBookGrade,
                    currentIndex = intent.quizBookGrade.quizGrades.size
                )
            }
            QuizSolveIntent.GradeQuizSuccess -> {
                if (!currentState.isLastQuestion) {
                    currentState.copy(
                        currentIndex = currentState.currentIndex + 1,
                        common = currentState.common.copy(
                            isButtonEnabled = false
                        )
                    )
                } else {
                    currentState
                }
            }
            else -> currentState
        }
    }

    private suspend fun getQuizBook(quizBookId: QuizBookId) {
        getQuizBookUseCase(
            quizBookId
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("getQuizBook", "${it.data.quizList}")
                    sendIntent(QuizSolveIntent.SuccessGetQuizBook(it.data))
                }

                is Resource.Loading -> {
                    Log.d("getQuizBook", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("getQuizBook", it.errorMessage)
                }
            }
        }
    }

    // 여기서 QuizGrade 정보들이 있냐없냐로 ResumeDialog 띄울지 말지 결정
    private suspend fun getQuizBookGrade(
        quizBookId: QuizBookId,
    ) {
        getOrCreateQuizBookGradeUseCase(quizBookId = quizBookId).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("getQuizBookGradeUseCase", "Get QuizBookDetail Success")
                    Log.d("getOrCreateQuizBookGradeUseCase", "quizBookGrade : ${it.data}")
                    val isResume = it.data.quizGrades.isNotEmpty()
                    if (isResume) {
                        emitEffect(QuizSolveEffect.ShowResumeDialog)
                    } else {
                        sendIntent(QuizSolveIntent.StartTimer)
                    }
                    sendIntent(QuizSolveIntent.SuccessGetQuizBookGrade(it.data))
                }

                is Resource.Loading -> {
                    Log.d("getQuizBookGradeUseCase", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("getQuizBookGradeUseCase", it.errorMessage)
                }
            }
        }
    }
    private suspend fun deleteQuizBookGrade() {
        val quizBookLocalId = state.value.quizBookGrade?.localId
        quizBookLocalId?.let {
            deleteQuizBookGradeUseCase(it).collect {
                when (it) {
                    is Resource.Success -> {
                        Log.d("deleteQuizBookGradeUseCase", "Get QuizBookDetail Success")
                    }
                    is Resource.Failure -> {
                        Log.d("deleteQuizBookGradeUseCase", it.errorMessage)
                    }
                    else -> Unit
                }
            }
        } ?: Log.d("deleteQuizBookGrade", "quizBookLocalId is null")
    }
    private suspend fun saveQuizToLocal() {
        val uiState = state.value
        val quiz = uiState.currentQuiz
        val localId = uiState.quizBookGrade?.localId
        val userAnswer = when (uiState.questionInfo.type) {
            QuestionType.SUBJECTIVE -> uiState.subjective.answer
            else -> uiState.mcq.selectedContent
        }
        if (quiz != null && localId != null && userAnswer != null) {
            println(localId)
            gradeQuizUseCase(
                quiz,
                localId,
                userAnswer
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        sendIntent(QuizSolveIntent.GradeQuizSuccess)
                        if (uiState.isLastQuestion) {
                            sendIntent(QuizSolveIntent.SubmitAnswer)
                        }
                        Log.d("getQuizBookGradeUseCase", "Get QuizBookDetail Success")
                    }

                    is Resource.Loading -> {
                        Log.d("getQuizBookGradeUseCase", "Loading")
                    }

                    is Resource.Failure -> {
                        Log.d("getQuizBookGradeUseCase", it.errorMessage)
                        sendIntent(QuizSolveIntent.GradeQuizError(it.errorMessage))
                    }
                }
            }
        } else {
            Log.d(
                "saveQuizToLocal",
                "quiz: $quiz, localId: $localId, userAnswer: $userAnswer"
            )
            sendIntent(QuizSolveIntent.GradeQuizError("문제 발생"))
        }
    }
    private suspend fun submitQuizAnswer() {
        val localId = state.value.quizBookGrade?.localId
        val elapsedTimeInSeconds: Long = state.value.timer.elapsedSeconds.toLong()
        if (localId != null) {
            solveQuizBookUseCase(
                quizBookGradeLocalId = localId,
                elapsedTimeInSeconds = elapsedTimeInSeconds
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        sendIntent(QuizSolveIntent.SolveQuizSuccess(it.data))
                        Log.d("solveQuizBookUseCase", "채점 Success")
                    }

                    is Resource.Loading -> {
                        Log.d("solveQuizBookUseCase", "Loading")
                    }

                    is Resource.Failure -> {
                        Log.d("solveQuizBookUseCase", it.errorMessage)
                        sendIntent(QuizSolveIntent.GradeQuizError(it.errorMessage))
                    }
                }
            }
        } else {
            sendIntent(QuizSolveIntent.GradeQuizError("문제 발생"))
        }
    }
}
