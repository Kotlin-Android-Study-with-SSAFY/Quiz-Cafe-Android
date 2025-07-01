package com.android.quizcafe.feature.quiz.solve.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.usecase.quizbook.GetQuizBookUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetQuizGradeUseCase
import com.android.quizcafe.core.domain.usecase.solving.DeleteQuizBookGradeUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetOrCreateQuizBookGradeUseCase
import com.android.quizcafe.core.domain.usecase.solving.GradeQuizUseCase
import com.android.quizcafe.core.domain.usecase.solving.SolveQuizBookUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class QuizSolveViewModel @Inject constructor(
    private val getQuizBookUseCase: GetQuizBookUseCase,
    private val getOrCreateQuizBookGradeUseCase: GetOrCreateQuizBookGradeUseCase,
    private val gradeQuizUseCase: GradeQuizUseCase,
    private val solveQuizBookUseCase: SolveQuizBookUseCase,
    private val deleteQuizBookGradeUseCase: DeleteQuizBookGradeUseCase,
    private val getQuizGradeUseCase: GetQuizGradeUseCase
) : BaseViewModel<QuizSolveUiState, QuizSolveIntent, QuizSolveEffect>(
    initialState = QuizSolveUiState()
),
    IQuizSolveViewModel {
    init {
        viewModelScope.launch {
            while (true) {
                delay(1_000L)
                if (state.value.common.isTimerActive) {
                    sendIntent(QuizSolveIntent.UpdateTimer)
                }
            }
        }
    }

    override suspend fun handleIntent(intent: QuizSolveIntent) {
        when (intent) {
            is QuizSolveIntent.StartSolving -> {
                val quizBookId = QuizBookId(abs(intent.quizBookId))
                getQuizBook(quizBookId)
                getOrCreateQuizBookGrade(quizBookId)
            }

            is QuizSolveIntent.ResumeSolving -> {
                if (intent.resumeWithNewSolving) {
                    val quizBookId = state.value.quizBook?.id
                    deleteQuizBookGrade()
                    quizBookId?.let {
                        getOrCreateQuizBookGrade(it)
                    }
                }
                emitEffect(QuizSolveEffect.CloseResumeDialog)
                sendIntent(QuizSolveIntent.StartTimer)
            }

            is QuizSolveIntent.SubmitQuizBookSuccess -> {
                emitEffect(NavigateToQuizBookSolvingResult(intent.quizBookGradeServerId))
            }

            is QuizSolveIntent.HandleError -> {
                emitEffect(ShowErrorDialog(intent.message ?: ""))
            }

            QuizSolveIntent.NavigateToNextQuestion -> {
                val currentState = state.value
                Log.d("test", "currentState : $currentState")
                when {
                    currentState.common.playMode == PlayMode.REVIEW_MODE && !currentState.review.showExplanation -> {
                        saveQuizToLocal()
                    }
                    currentState.common.playMode == PlayMode.REVIEW_MODE && currentState.isLastQuestion -> {
                        submitQuizAnswer()
                    }
                    // 리뷰모드 아닐 때
                    else -> {
                        saveQuizToLocal()
                        if (currentState.isLastQuestion) {
                            submitQuizAnswer()
                        }
                    }
                }
            }

            QuizSolveIntent.NavigateToPreviousQuestion -> {
                getQuizAnswer()
            }
            QuizSolveIntent.NavigateToResult -> {
                // This will be handled by SubmitQuizBookSuccess
            }
            QuizSolveIntent.NavigateBack -> {
                emitEffect(QuizSolveEffect.ShowExitDialog)
            }
            is QuizSolveIntent.GradeQuizSuccess -> {
                val currentState = state.value
                when {
                    currentState.common.playMode == PlayMode.REVIEW_MODE -> {
                        getQuizAnswer()
                    }
                    !currentState.isLastQuestion && currentState.common.playMode == PlayMode.DEFAULT -> {
                        getQuizAnswer()
                    }
                    else -> Unit
                }
            }
            is QuizSolveIntent.ExitWithDelete -> {
                deleteQuizBookGrade()
                emitEffect(QuizSolveEffect.NavigateToBack)
            }
            is QuizSolveIntent.ExitWithSave -> {
                emitEffect(QuizSolveEffect.NavigateToBack)
            }

            is QuizSolveIntent.SelectAnswer,
            is QuizSolveIntent.UpdateSubjectiveAnswer,
            is QuizSolveIntent.LoadQuizBookSuccess,
            is QuizSolveIntent.LoadQuizBookGradeSuccess,
            is QuizSolveIntent.GetQuizGradeSuccess,
            is QuizSolveIntent.StartTimer,
            is QuizSolveIntent.UpdateTimer -> Unit
        }
    }

    override fun reduce(currentState: QuizSolveUiState, intent: QuizSolveIntent): QuizSolveUiState {
        return when (intent) {
            is QuizSolveIntent.StartSolving -> {
                if (intent.quizBookId < 0) {
                    currentState.copy(
                        common = currentState.common.copy(
                            playMode = PlayMode.REVIEW_MODE
                        ),
                        isLoading = true,
                        errorMessage = null
                    )
                } else {
                    currentState.copy(isLoading = true, errorMessage = null)
                }
            }

            QuizSolveIntent.UpdateTimer -> {
                val timer = currentState.timer
                currentState.copy(
                    timer = timer.copy(
                        elapsedSeconds = timer.elapsedSeconds + 1
                    )
                )
            }

            is QuizSolveIntent.SelectAnswer ->
                currentState.copy(
                    mcq = currentState.mcq.copy(
                        selectedId = intent.option.id,
                        selectedContent = intent.option.text
                    ),
                )

            is QuizSolveIntent.UpdateSubjectiveAnswer ->
                currentState.copy(
                    subjective = currentState.subjective.copy(
                        answer = intent.answer
                    )
                )

            is QuizSolveIntent.LoadQuizBookSuccess -> {
                currentState.copy(
                    quizBook = intent.quizBook,
                    isLoading = false
                )
            }

            is QuizSolveIntent.LoadQuizBookGradeSuccess -> {
                val index = intent.quizBookGrade.quizGrades.size.let {
                    if (it == currentState.quizBook?.quizList?.size) it - 1 else it
                }
                currentState.copy(
                    quizBookGrade = intent.quizBookGrade,
                    common = currentState.common.copy(
                        currentIndex = index
                    )
                )
            }

            QuizSolveIntent.NavigateToPreviousQuestion -> {
                if (currentState.common.currentIndex > 0) {
                    currentState.previousQuestionReset()
                } else {
                    currentState
                }
            }

            is QuizSolveIntent.GetQuizGradeSuccess ->
                currentState.applyFetchedGrade(intent.quizGrade)

            QuizSolveIntent.GradeQuizSuccess -> {
                currentState.onLocalSaveSuccess()
            }
            QuizSolveIntent.StartTimer -> {
                currentState.copy(
                    common = currentState.common.copy(isTimerActive = true)
                )
            }

            is QuizSolveIntent.HandleError,
            QuizSolveIntent.NavigateBack,
            QuizSolveIntent.NavigateToNextQuestion,
            QuizSolveIntent.NavigateToResult,
            is QuizSolveIntent.SubmitQuizBookSuccess,
            is QuizSolveIntent.ExitWithSave,
            is QuizSolveIntent.ResumeSolving,
            is QuizSolveIntent.ExitWithDelete -> currentState
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
        val userAnswer = when (uiState.questionType) {
            QuestionType.SUBJECTIVE -> uiState.subjective.answer
            else -> uiState.mcq.selectedContent
        }
        if (quiz != null && localId != null && userAnswer != null) {
            gradeQuizUseCase(
                quiz,
                localId,
                userAnswer
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        // 답안 저장 성공 후 상태 업데이트
                        sendIntent(QuizSolveIntent.GradeQuizSuccess)
                        Log.d("getQuizBookGradeUseCase", "Get QuizBookDetail Success")
                    }

                    is Resource.Loading -> {
                        Log.d("getQuizBookGradeUseCase", "Loading")
                    }

                    is Resource.Failure -> {
                        Log.d("getQuizBookGradeUseCase", it.errorMessage)
                        sendIntent(QuizSolveIntent.HandleError(it.errorMessage))
                    }
                }
            }
        } else {
            Log.d("saveQuizToLocal", "문제 발생")
            sendIntent(QuizSolveIntent.HandleError("문제 발생"))
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
                        sendIntent(QuizSolveIntent.SubmitQuizBookSuccess(it.data))
                        Log.d("solveQuizBookUseCase", "채점 Success")
                    }

                    is Resource.Loading -> {
                        Log.d("solveQuizBookUseCase", "Loading")
                    }

                    is Resource.Failure -> {
                        Log.d("solveQuizBookUseCase", it.errorMessage)
                        sendIntent(QuizSolveIntent.HandleError(it.errorMessage))
                    }
                }
            }
        } else {
            sendIntent(QuizSolveIntent.HandleError("문제 발생"))
        }
    }

    private suspend fun getQuizAnswer() {
        val currentState = state.value
        val localId = state.value.quizBookGrade?.localId
        val quizId = currentState.currentQuiz?.id
        if (localId != null && quizId != null) {
            getQuizGradeUseCase(
                quizBookGradeLocalId = localId,
                quizId = quizId
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        sendIntent(QuizSolveIntent.GetQuizGradeSuccess(it.data))
                        Log.d("getQuizGradeUseCase", "Success")
                    }

                    is Resource.Loading -> {
                        Log.d("getQuizGradeUseCase", "Loading")
                    }

                    is Resource.Failure -> {
                        Log.d("getQuizGradeUseCase", it.errorMessage)
                        sendIntent(QuizSolveIntent.HandleError(it.errorMessage))
                    }
                }
            }
        } else {
            sendIntent(QuizSolveIntent.HandleError("문제 발생"))
        }
    }

    private suspend fun getQuizBook(quizBookId: QuizBookId) {
        getQuizBookUseCase(
            quizBookId
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("getQuizBook", "${it.data.quizList}")
                    sendIntent(QuizSolveIntent.LoadQuizBookSuccess(it.data))
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
    private suspend fun getOrCreateQuizBookGrade(
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
                    sendIntent(QuizSolveIntent.LoadQuizBookGradeSuccess(it.data))
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
}
