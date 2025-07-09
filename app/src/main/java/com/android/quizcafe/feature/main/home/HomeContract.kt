package com.android.quizcafe.feature.main.home

import androidx.compose.ui.graphics.Color
import com.android.quizcafe.core.domain.model.quizbook.response.QuizBook
import com.android.quizcafe.core.ui.base.BaseContract

data class ModeItem(
    val titleResId: Int,
    val descResId: Int,
    val backgroundColor: Color,
    val iconResId: Int
)

data class HomeUiState(
    val latestQuizBooks: List<QuizBook> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : BaseContract.UiState

sealed class HomeIntent : BaseContract.ViewIntent {
    data object FetchLatestQuizBook : HomeIntent()
    data object LoadingLatestQuizBook : HomeIntent()
    data class SuccessFetchLatestQuizBook(val latestQuizBooks: List<QuizBook>) : HomeIntent()
    data class FailFetchLatestQuizBook(val errorMessage: String? = null) : HomeIntent()

    data class ClickHomeCard(val quizType: String) : HomeIntent()
}

sealed class HomeEffect : BaseContract.ViewEffect {
    data class ShowErrorDialog(val message: String) : HomeEffect()

    data class NavigateToCategory(val quizType: String) : HomeEffect()
}
