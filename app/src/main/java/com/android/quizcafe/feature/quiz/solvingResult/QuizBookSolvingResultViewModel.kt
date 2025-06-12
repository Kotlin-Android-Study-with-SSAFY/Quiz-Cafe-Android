package com.android.quizcafe.feature.quiz.solvingResult

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.model.solving.QuizBookGrade
import com.android.quizcafe.core.domain.model.value.QuizBookGradeLocalId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.domain.usecase.quiz.GetQuizListByBookIdUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetQuizBookGradeUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizBookSolvingResultViewModel @Inject constructor(
    private val getQuizListByBookIdUseCase: GetQuizListByBookIdUseCase,
    private val getQuizBookGradeUseCase: GetQuizBookGradeUseCase,
) : BaseViewModel<QuizBookSolvingResultUiState, QuizBookSolvingResultIntent, QuizBookSolvingResultEffect>(
    initialState = QuizBookSolvingResultUiState()
) {
    companion object {
        private const val TAG = "QuizBookSolvingResultViewModel"
    }

    override suspend fun handleIntent(intent: QuizBookSolvingResultIntent) {
        when (intent) {
            is QuizBookSolvingResultIntent.InitWithQuizBookGradeId -> {
                initWithCombinedData(QuizBookGradeLocalId(intent.quizBookGradeLocalId))
            }
            QuizBookSolvingResultIntent.ClickFinish -> {
                emitEffect(QuizBookSolvingResultEffect.NavigateToMain)
            }
            is QuizBookSolvingResultIntent.Error -> {
                intent.errorMessage?.let { message ->
                    emitEffect(QuizBookSolvingResultEffect.ShowError(message))
                }
            }
            else -> Unit
        }
    }

    override fun reduce(
        state: QuizBookSolvingResultUiState,
        intent: QuizBookSolvingResultIntent
    ): QuizBookSolvingResultUiState = when (intent) {
        QuizBookSolvingResultIntent.StartLoading -> {
            state.copy(isLoading = true, errorMessage = null)
        }
        is QuizBookSolvingResultIntent.SuccessLoadData -> {
            state.copy(
                quizBookSolvingResult = intent.quizBookGrade,
                quizzes = intent.quizzes,
                isLoading = false,
                errorMessage = null
            )
        }
        is QuizBookSolvingResultIntent.Error -> {
            state.copy(
                isLoading = false,
                errorMessage = intent.errorMessage
            )
        }
        else -> state
    }

    // 먼저 QuizBookGrade를 가져와서 QuizBookId를 얻은 후 QuizList를 가져옴
    private fun initWithCombinedData(quizBookGradeLocalId: QuizBookGradeLocalId) = viewModelScope.launch {
        sendIntent(QuizBookSolvingResultIntent.StartLoading)

        getQuizBookGradeUseCase(quizBookGradeLocalId)
            .collect { gradeResource ->
                when (gradeResource) {
                    is Resource.Success -> {
                        val quizBookId = gradeResource.data.quizBookId
                        if (quizBookId.value != null) {
                            loadQuizListAndCombine(
                                loadedQuizBookGrade = gradeResource.data,
                                quizBookId = quizBookId
                            )
                        } else {
                            sendIntent(QuizBookSolvingResultIntent.Error("QuizBookId를 찾을 수 없습니다"))
                        }
                    }
                    is Resource.Failure -> {
                        sendIntent(QuizBookSolvingResultIntent.Error(gradeResource.errorMessage))
                    }
                    is Resource.Loading -> Unit
                }
            }
    }

    private fun loadQuizListAndCombine(
        loadedQuizBookGrade: QuizBookGrade,
        quizBookId: QuizBookId
    ) = viewModelScope.launch {
        // 퀴즈 리스트만 추가로 가져옴
        getQuizListByBookIdUseCase(quizBookId)
            .collect { quizListResource ->
                when (quizListResource) {
                    is Resource.Success -> {
                        sendIntent(QuizBookSolvingResultIntent.SuccessLoadData(
                            quizBookGrade = loadedQuizBookGrade,
                            quizzes = quizListResource.data
                        ))
                        Log.d(TAG, "Successfully loaded both QuizBookGrade and Quiz list")
                    }
                    is Resource.Failure -> {
                        sendIntent(QuizBookSolvingResultIntent.Error(quizListResource.errorMessage))
                    }
                    is Resource.Loading -> Unit
                }
            }
    }

}
