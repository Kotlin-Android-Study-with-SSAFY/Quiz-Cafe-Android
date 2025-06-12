package com.android.quizcafe.feature.main.home

import androidx.compose.ui.graphics.Color
import com.android.quizcafe.core.domain.model.quizsolvingrecord.response.QuizSolvingRecord
import com.android.quizcafe.core.ui.base.BaseContract

data class ModeItem(
    val titleResId: Int,
    val descResId: Int,
    val backgroundColor: Color,
    val iconResId: Int
)

data class HomeViewState(
    val quizSolvingRecords: List<QuizSolvingRecord> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : BaseContract.ViewState

sealed class HomeIntent : BaseContract.ViewIntent {
    data object FetchRecord : HomeIntent()
    data object LoadingFetchRecord : HomeIntent()
    data class SuccessFetchRecord(val quizSolvingRecords: List<QuizSolvingRecord>) : HomeIntent()
    data class FailFetchRecord(val errorMessage: String? = null) : HomeIntent()

    data class ClickHomeCard(val quizType: String) : HomeIntent()
}

sealed class HomeEffect : BaseContract.ViewEffect {
    data class ShowErrorDialog(val message: String) : HomeEffect()

    data class NavigateToCategory(val quizType: String) : HomeEffect()
}
