package com.android.quizcafe.feature.main.workbook

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.solving.GetAllQuizBookGradeWithQuizBookUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetAllQuizBookSolvingUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkBookViewModel @Inject constructor(
    private val getAllQuizBookGradeWithQuizBookUseCase: GetAllQuizBookGradeWithQuizBookUseCase,
    private val getAllQuizBookSolvingUseCase: GetAllQuizBookSolvingUseCase
) : BaseViewModel<WorkBookUiState, WorkBookIntent, WorkBookEffect>(
    initialState = WorkBookUiState()
) {

    init {
        viewModelScope.launch {
            loadWorkBookData(state.value.currentWorkBookState)
        }
    }

    override suspend fun handleIntent(intent: WorkBookIntent) {
        when (intent) {
            is WorkBookIntent.ClickWorkBookFilter -> loadWorkBookData(intent.workBookState)
            is WorkBookIntent.ClickSolvingCard -> emitEffect(WorkBookEffect.NavigateToSolveQuiz(intent.id))
            is WorkBookIntent.ClickSolvedCard -> emitEffect(WorkBookEffect.NavigateToGradeResult(intent.id))

            is WorkBookIntent.SuccessSolvedQuizBookList -> Unit
            is WorkBookIntent.SuccessSolvingQuizBookList -> Unit

            is WorkBookIntent.FailLoadWorkBookList -> Unit
        }
    }

    override fun reduce(currentState: WorkBookUiState, intent: WorkBookIntent): WorkBookUiState {
        return when (intent) {
            is WorkBookIntent.ClickWorkBookFilter -> currentState.copy(isLoading = true, currentWorkBookState = intent.workBookState)
            is WorkBookIntent.ClickSolvingCard -> currentState.copy(isLoading = true)
            is WorkBookIntent.ClickSolvedCard -> currentState.copy(isLoading = true)

            is WorkBookIntent.SuccessSolvedQuizBookList -> currentState.copy(solvedQuizBooks = intent.qbs, isLoading = false)
            is WorkBookIntent.SuccessSolvingQuizBookList -> currentState.copy(solvingQuizBooks = intent.qbg, isLoading = false)

            is WorkBookIntent.FailLoadWorkBookList -> currentState.copy(errorMessage = intent.errorMessage, isLoading = false)
        }
    }

    private suspend fun loadWorkBookData(workBookState: WorkBookState) {
        when (workBookState) {
            WorkBookState.SOLVING -> {
                // 로컬에서 풀고있는 퀴즈북 목록 가져오기
                getAllQuizBookGradeWithQuizBookUseCase().collect {
                    when (it) {
                        is Resource.Success -> {
                            Log.d("loadWorkBook", "성공")
                            sendIntent(WorkBookIntent.SuccessSolvingQuizBookList(it.data))
                        }
                        is Resource.Failure -> {
                            Log.d("loadWorkBook", "실패")
                            sendIntent(WorkBookIntent.FailLoadWorkBookList(it.errorMessage))
                        }
                        Resource.Loading -> {
                            Log.d("loadWorkBook", "로딩")
                        }
                    }
                }
            }

            WorkBookState.SOLVED -> {
                // 서버로부터 풀어본 퀴즈북 목록 가져오기
                getAllQuizBookSolvingUseCase().collect {
                    when (it) {
                        is Resource.Success -> {
                            Log.d("LoadWorkBookFromServer", it.data.toString())
                            sendIntent(WorkBookIntent.SuccessSolvedQuizBookList(it.data.sortedByDescending { it.completedAt }))
                        }
                        is Resource.Failure -> {
                            Log.d("LoadWorkBookFromServer", "실패")
                            sendIntent(WorkBookIntent.FailLoadWorkBookList(it.errorMessage))
                        }
                        Resource.Loading -> {
                            Log.d("LoadWorkBookFromServer", "로딩")
                        }
                    }
                }
            }
        }
    }
}
