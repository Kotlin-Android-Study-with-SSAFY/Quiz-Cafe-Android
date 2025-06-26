package com.android.quizcafe.feature.main.workbook

import android.util.Log
import com.android.quizcafe.core.domain.model.Resource
import com.android.quizcafe.core.domain.usecase.solving.GetAllQuizBookGradeWithQuizBookUseCase
import com.android.quizcafe.core.domain.usecase.solving.GetAllQuizBookSolvingUseCase
import com.android.quizcafe.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkBookViewModel @Inject constructor(
    private val getAllQuizBookGradeWithQuizBookUseCase: GetAllQuizBookGradeWithQuizBookUseCase,
    private val getAllQuizBookSolvingUseCase: GetAllQuizBookSolvingUseCase
) : BaseViewModel<WorkBookUiState, WorkBookIntent, WorkBookEffect>(
    initialState = WorkBookUiState()
) {

    override suspend fun handleIntent(intent: WorkBookIntent) {
        when (intent) {
            WorkBookIntent.LoadWorkBookList -> loadWorkBookData()
            is WorkBookIntent.ClickWorkBookCard -> { }
            is WorkBookIntent.SuccessSolvedQuizBookList -> Unit
            is WorkBookIntent.SuccessSolvingQuizBookList -> Unit
        }
    }

    override fun reduce(currentState: WorkBookUiState, intent: WorkBookIntent): WorkBookUiState {
        return when (intent) {
            is WorkBookIntent.ClickWorkBookCard -> currentState.copy(isLoading = false)

            WorkBookIntent.LoadWorkBookList -> currentState.copy(isLoading = true)

            is WorkBookIntent.SuccessSolvedQuizBookList -> currentState.copy(solvedQuizBooks = intent.qbs, isLoading = false)
            is WorkBookIntent.SuccessSolvingQuizBookList -> currentState.copy(solvingQuizBooks = intent.qbg, isLoading = false)
        }
    }

    private suspend fun loadWorkBookData() {
        getAllQuizBookGradeWithQuizBookUseCase().collect {
            when (it) {
                is Resource.Failure -> {}
                Resource.Loading -> {}
                is Resource.Success -> {
                    Log.d("loadWorkBook", it.data.get(1).quizBookGrade.toString())
                    sendIntent(WorkBookIntent.SuccessSolvingQuizBookList(it.data))
                }
            }
        }

        // 서버로부터 풀어본 퀴즈북 목록 가져오기
        getAllQuizBookSolvingUseCase().collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("LoadWorkBookData", it.data.toString())
                    sendIntent(WorkBookIntent.SuccessSolvedQuizBookList(it.data.sortedByDescending { it.completedAt }))
                }
                is Resource.Failure -> {
                    Log.d("LoadWorkBookData", "실패")
                }
                Resource.Loading -> {
                    Log.d("LoadWorkBookData", "로딩")
                }
            }
        }
    }
}
