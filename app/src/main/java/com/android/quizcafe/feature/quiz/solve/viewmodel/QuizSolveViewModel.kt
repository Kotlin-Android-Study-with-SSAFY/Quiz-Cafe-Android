package com.android.quizcafe.feature.quiz.solve.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.usecase.quizbook.GetQuizBookUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetQuizBookGradeUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetQuizBookLocalIdUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizSolveViewModel @Inject constructor(
    private val getQuizBookUseCase: GetQuizBookUseCase,
    private val getQuizBookLocalIdUseCase: GetQuizBookLocalIdUseCase,
    private val getQuizBookGradeUseCase: GetQuizBookGradeUseCase
) : BaseViewModel<QuizSolveUiState, QuizSolveIntent, QuizSolveEffect>(
    initialState = QuizSolveUiState()
) {
    init {
        viewModelScope.launch {
            while (true) {
                delay(1_000L)
                sendIntent(QuizSolveIntent.TickTime)
            }
        }
    }

    override suspend fun handleIntent(intent: QuizSolveIntent) {
        when (intent) {
            QuizSolveIntent.OnBackClick -> {
                emitEffect(QuizSolveEffect.NavigatePopBack)
            }

            is QuizSolveIntent.LoadQuizBookDetail -> {
                getQuizBook(intent.quizBookId)
            }

            is QuizSolveIntent.GetQuizBookLocalId -> {
                getQuizBookLocalId(intent.quizBookId)
            }

            is QuizSolveIntent.GetQuizBookGradeResult -> {
                getQuizBookGradeResult(intent.quizBookLocalId)
            }

            else -> Unit
        }
    }

    override fun reduce(currentState: QuizSolveUiState, intent: QuizSolveIntent): QuizSolveUiState {
        return when (intent) {
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
                        selectedId = intent.option
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

            is QuizSolveIntent.LoadQuizBookDetail -> currentState.copy(isLoading = true, errorMessage = null)
            is QuizSolveIntent.SuccessGetQuizBookDetail -> {
                Log.d("test1234",intent.data.toString())
                currentState.copy(
                    quizInfos = intent.data,
                    isLoading = false
                )
            }

            is QuizSolveIntent.GetQuizBookGradeResult ->
                currentState.copy(
                    quizBookLocalId = intent.quizBookLocalId
                )
            is QuizSolveIntent.SuccessGetQuizBookGradeResult ->
                currentState.copy(
                    quizGrades =  intent.quizBookGrade.quizGrades
                )
            else -> currentState
        }
    }

    private suspend fun getQuizBook(id: Long) {
        getQuizBookUseCase(
            QuizBookId(id)
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("getQuizBook", "${it.data.quizList}")
                    sendIntent(QuizSolveIntent.SuccessGetQuizBookDetail(it.data.quizList))
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
            QuizBookId(id)
        ).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("getQuizBookLocalId", "Get QuizBookDetail Success")
                    sendIntent(QuizSolveIntent.GetQuizBookGradeResult(it.data))
                    sendIntent(QuizSolveIntent.SuccessGetQuizBookLocalId(it.data))
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
                    sendIntent(QuizSolveIntent.SuccessGetQuizBookGradeResult(it.data))
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
