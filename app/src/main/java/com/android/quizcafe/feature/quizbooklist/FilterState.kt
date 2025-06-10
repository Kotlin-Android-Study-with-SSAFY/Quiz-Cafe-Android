package com.android.quizcafe.feature.quizbooklist

import androidx.annotation.StringRes
import com.android.quizcafe.R

data class FilterState(
    val sortOption: SortOption = SortOption.LATEST,
    val level: QuizLevel = QuizLevel.ALL,
    val quizCountRange: IntRange = 1..50
)

enum class SortOption(@StringRes val stringResId: Int) {
    LATEST(R.string.sort_latest),
    SAVED(R.string.sort_saved),
    RANDOM(R.string.sort_random);

    override fun toString(): String {
        return this.name
    }
}

enum class QuizLevel(@StringRes val stringResId: Int) {
    ALL(R.string.all),
    EASY(R.string.level_easy),
    MEDIUM(R.string.level_medium),
    HARD(R.string.level_hard)
}
