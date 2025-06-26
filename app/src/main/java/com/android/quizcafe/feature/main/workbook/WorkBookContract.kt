package com.android.quizcafe.feature.main.workbook

import androidx.annotation.StringRes
import com.android.quizcafe.R
import com.android.quizcafe.core.domain.model.solving.QuizBookGradeWithQuizBook
import com.android.quizcafe.core.domain.model.solving.QuizBookSolving
import com.android.quizcafe.core.ui.base.BaseContract

enum class WorkBookState(@StringRes val resId: Int) {
    SOLVING(R.string.solving),
    SOLVED(R.string.solved)
}

data class WorkBookUiState(
    val solvingQuizBooks: List<QuizBookGradeWithQuizBook> = emptyList(),
    val solvedQuizBooks: List<QuizBookSolving> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
) : BaseContract.UiState

sealed class WorkBookIntent : BaseContract.ViewIntent {
    data object LoadWorkBookList : WorkBookIntent()
    data class ClickWorkBookCard(val id: Long) : WorkBookIntent()

    data class SuccessSolvingQuizBookList(val qbg: List<QuizBookGradeWithQuizBook>) : WorkBookIntent()
    data class SuccessSolvedQuizBookList(val qbs: List<QuizBookSolving>) : WorkBookIntent()
}

sealed class WorkBookEffect : BaseContract.ViewEffect {
    data class NavigateToGradeResult(val id: Long) : WorkBookEffect()
}
