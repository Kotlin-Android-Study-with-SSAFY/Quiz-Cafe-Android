package com.android.quizcafe.feature.quiz.solve.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.usecase.quizbook.GetQuizBookUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetQuizBookGradeUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetQuizBookLocalIdUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetQuizGradeUseCase
import com.android.quizcafe.core.domain.usecase.solving.GradeQuizUseCase
import com.android.quizcafe.core.domain.usecase.solving.SolveQuizBookUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import com.android.quizcafe.feature.quiz.solve.component.AnswerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class QuizSolveViewModel @Inject constructor(
    private val getQuizBookUseCase: GetQuizBookUseCase,
    private val getQuizBookLocalIdUseCase: GetQuizBookLocalIdUseCase,
    private val getQuizBookGradeUseCase: GetQuizBookGradeUseCase,
    private val gradeQuizUseCase: GradeQuizUseCase,
    private val solveQuizBookUseCase: SolveQuizBookUseCase,
    private val getQuizGradeUseCase: GetQuizGradeUseCase
) : BaseViewModel<QuizSolveUiState, QuizSolveIntent, QuizSolveEffect>(
    initialState = QuizSolveUiState()
) {
    init {
        viewModelScope.launch {
            while (true) {
                delay(1_000L)
                sendIntent(QuizSolveIntent.UpdateTimer)
            }
        }
    }

    override suspend fun handleIntent(intent: QuizSolveIntent) {
        when (intent) {
            QuizSolveIntent.NavigateBack -> {
                emitEffect(QuizSolveEffect.NavigatePopBack)
            }

            QuizSolveIntent.NavigateToResult -> {
                // This will be handled by SubmitQuizBookSuccess
            }

            // 초기화
            is QuizSolveIntent.Initialize -> {
                loadQuizBook(abs(intent.quizBookId))
                getQuizBookLocalId(intent.quizBookId)
            }

            is QuizSolveIntent.SetQuizBookLocalId -> {
                getQuizBookGradeResult(intent.quizBookLocalId)
            }


            is QuizSolveIntent.SubmitQuizBookSuccess -> {
                emitEffect(QuizSolveEffect.NavigateToQuizBookSolvingResult(intent.quizBookGradeServerId))
            }

            is QuizSolveIntent.HandleError -> {
                emitEffect(QuizSolveEffect.ShowErrorDialog(intent.message ?: ""))
            }

            QuizSolveIntent.NavigateToNextQuestion -> {
                val currentState = state.value
                saveQuizToLocal()
                if (currentState.isLastQuestion) {
                    submitQuizAnswer()
                }
            }

            QuizSolveIntent.NavigateToPreviousQuestion -> {
               getQuizAnswer()
            }
            is QuizSolveIntent.GradeQuizSuccess -> {
                val currentState = state.value
                if(!currentState.isLastQuestion){
                    getQuizAnswer()
                }
            }
            is QuizSolveIntent.SelectAnswer,
            is QuizSolveIntent.UpdateSubjectiveAnswer,
            is QuizSolveIntent.LoadQuizBookSuccess,
            is QuizSolveIntent.LoadQuizBookGradeSuccess,
            QuizSolveIntent.UpdateTimer -> {

            }

            is QuizSolveIntent.GetQuizGradeSuccess -> {
            }
        }
    }

    override fun reduce(currentState: QuizSolveUiState, intent: QuizSolveIntent): QuizSolveUiState {
        return when (intent) {
            // 타이머
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
                    common = currentState.common.copy(
                        isButtonEnabled = true
                    )
                )

            is QuizSolveIntent.UpdateSubjectiveAnswer ->
                currentState.copy(
                    subjective = currentState.subjective.copy(
                        answer = intent.answer
                    ),
                    common = currentState.common.copy(
                        isButtonEnabled = intent.answer.isNotBlank()
                    )
                )

            is QuizSolveIntent.Initialize -> {
                if (intent.quizBookId < 0) {
                    currentState.copy(
                        playMode = PlayMode.REVIEW_MODE,
                        isLoading = true,
                        errorMessage = null
                    )
                } else
                    currentState.copy(isLoading = true, errorMessage = null)
            }

            is QuizSolveIntent.LoadQuizBookSuccess -> {
                currentState.copy(
                    quizBook = intent.quizBook,
                    isLoading = false
                )
            }

            is QuizSolveIntent.SetQuizBookLocalId ->
                currentState.copy(
                    quizBookLocalId = intent.quizBookLocalId
                )

            is QuizSolveIntent.LoadQuizBookGradeSuccess ->
                currentState.copy(
                    quizGrades = intent.quizBookGrade.quizGrades
                )

            QuizSolveIntent.NavigateToPreviousQuestion -> {
                if (currentState.currentIndex > 0) {
                    if (currentState.playMode == PlayMode.DEFAULT) {
                        // DEFAULT_MODE에서는 이전 문제로 이동하고 상태 초기화
                        // TODO: 확장함수로 분리
                        currentState.copy(
                            currentIndex = currentState.currentIndex - 1,
                            common = CommonState(false),
                            subjective = currentState.subjective.copy(
                                answer = ""
                            ),
                            mcq = currentState.mcq.copy(
                                selectedContent = ""
                            ),
                            review = ReviewState()
                        )
                    } else {
                        currentState.copy(
                            currentIndex = currentState.currentIndex - 1,
                        )
                    }
                } else {
                    // 첫 번째 문제에서는 이동하지 않음
                    currentState
                }
            }


            is QuizSolveIntent.GetQuizGradeSuccess -> {
                val quizGrade = intent.quizGrade
                if (quizGrade == null) {
                    currentState
                } else {
                    when (currentState.playMode) {
                        PlayMode.DEFAULT -> {
                            currentState.copy(
                                common = CommonState(true),
                                subjective = currentState.subjective.copy(
                                    answer = quizGrade.userAnswer
                                ),
                                mcq = currentState.mcq.copy(
                                    selectedContent = quizGrade.userAnswer
                                )
                            )
                        }

                        PlayMode.REVIEW_MODE -> {
                            if (currentState.review.showExplanation) {
                                // 다음 문제 클릭했을때 reduce 로직
                                if(currentState.currentGrade != null) {
                                    currentState.copy(
                                        currentGrade = null,
                                        currentIndex = currentState.currentIndex + 1,
                                        subjective = currentState.subjective.copy(
                                            answer = ""
                                        ),
                                        mcq = currentState.mcq.copy(
                                            selectedContent = ""
                                        ),
                                        review = ReviewState()
                                    )
                                }else{
                                    // 이전 문제 클릭했을때 reduce 로직
                                    currentState.copy(
                                        currentGrade = quizGrade,
                                        subjective = currentState.subjective.copy(
                                            answer = quizGrade.userAnswer
                                        ),
                                        mcq = currentState.mcq.copy(
                                            selectedContent = quizGrade.userAnswer
                                        ),
                                        review = currentState.review.copy(
                                            answerState = if (quizGrade.isCorrect) AnswerState.CORRECT else AnswerState.INCORRECT,
                                            showExplanation = true,
                                            explanation = currentState.currentQuiz?.explanation ?: ""
                                        )
                                    )
                                }
                            } else {
                                currentState.copy(
                                    currentGrade = quizGrade,
                                    subjective = currentState.subjective.copy(
                                        answer = quizGrade.userAnswer,
                                        correctAnswer = currentState.currentQuiz?.answer
                                    ),
                                    mcq = currentState.mcq.copy(
                                        selectedContent = quizGrade.userAnswer,
                                        correctContent = if(currentState.questionInfo.type == QuestionType.MULTIPLE_CHOICE)currentState.optionList[(currentState.currentQuiz?.answer ?: "0").toInt()-1].text else currentState.currentQuiz?.answer
                                    ),
                                    review = currentState.review.copy(
                                        answerState = if (quizGrade.isCorrect) AnswerState.CORRECT else AnswerState.INCORRECT,
                                        showExplanation = true,
                                        explanation = currentState.currentQuiz?.explanation ?: ""
                                    )
                                )
                            }
                        }
                    }
                }
            }


            QuizSolveIntent.GradeQuizSuccess -> {
                if (!currentState.isLastQuestion && currentState.playMode == PlayMode.DEFAULT) {
                    currentState.copy(
                        currentIndex = currentState.currentIndex + 1,
                        common = CommonState(false),
                        subjective = currentState.subjective.copy(
                            answer = ""
                        ),
                        mcq = currentState.mcq.copy(
                            selectedContent = ""
                        )
                    )
                } else
                    currentState
            }
            is QuizSolveIntent.HandleError,
            QuizSolveIntent.NavigateBack,
            QuizSolveIntent.NavigateToNextQuestion,
            QuizSolveIntent.NavigateToResult,
            is QuizSolveIntent.SubmitQuizBookSuccess -> currentState
        }
    }

    // MARK: - Private Methods
    private suspend fun loadQuizBook(id: Long) {
        getQuizBookUseCase(
            QuizBookId(id)
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

    private suspend fun getQuizBookLocalId(id: Long) {
        getQuizBookLocalIdUseCase(
            QuizBookId(abs(id))
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("getQuizBookLocalId", "Get QuizBookDetail Success")
                    sendIntent(QuizSolveIntent.SetQuizBookLocalId(it.data))
                }

                is Resource.Loading -> {
                    Log.d("getQuizBookLocalId", "Loading")
                }

                is Resource.Failure -> {
                    Log.d("getQuizBookLocalId", it.errorMessage)
                }
            }
        }
    }

    private suspend fun getQuizBookGradeResult(id: QuizBookGradeLocalId) {
        getQuizBookGradeUseCase(
            id
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("getQuizBookGradeUseCase", "Get QuizBookDetail Success")
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

    private suspend fun saveQuizToLocal() {
        val uiState = state.value
        val quiz = uiState.currentQuiz
        val localId = uiState.quizBookLocalId
        val userAnswer = when (uiState.questionInfo.type) {
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
            sendIntent(QuizSolveIntent.HandleError("문제 발생"))
        }
    }

    private suspend fun submitQuizAnswer() {
        val localId = state.value.quizBookLocalId
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
        val localId = currentState.quizBookLocalId
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
}
