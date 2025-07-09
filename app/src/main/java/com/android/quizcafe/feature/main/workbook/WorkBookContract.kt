package com.android.quizcafe.feature.main.workbook

import androidx.annotation.StringRes
import com.android.quizcafe.R
import com.android.quizcafe.core.domain.model.solving.QuizBookGradeWithQuizBook
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.domain.model.value.QuizBookGradeServerId
import com.android.quizcafe.core.domain.model.value.QuizBookId
import com.android.quizcafe.core.ui.base.BaseContract

enum class WorkBookState(@StringRes val resId: Int) {
    SOLVING(R.string.solving_description),
    SOLVED(R.string.solved_description)
}

data class WorkBookUiState(
    val solvingQuizBooks: List<QuizBookGradeWithQuizBook> = emptyList(),
    val solvedQuizBooks: List<QuizBookSolving> = emptyList(),
    val currentWorkBookState: WorkBookState = WorkBookState.SOLVING,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
) : BaseContract.UiState

sealed class WorkBookIntent : BaseContract.ViewIntent {
    data class ClickWorkBookFilter(val workBookState: WorkBookState) : WorkBookIntent()
    data class ClickSolvingCard(val id: QuizBookId) : WorkBookIntent()
    data class ClickSolvedCard(val id: QuizBookGradeServerId) : WorkBookIntent()

    data class SuccessSolvingQuizBookList(val qbg: List<QuizBookGradeWithQuizBook>) : WorkBookIntent()
    data class SuccessSolvedQuizBookList(val qbs: List<QuizBookSolving>) : WorkBookIntent()

    data class FailLoadWorkBookList(val errorMessage: String?) : WorkBookIntent()
}

sealed class WorkBookEffect : BaseContract.ViewEffect {
    data class NavigateToSolveQuiz(val id: QuizBookId) : WorkBookEffect()
    data class NavigateToGradeResult(val id: QuizBookGradeServerId) : WorkBookEffect()
}
