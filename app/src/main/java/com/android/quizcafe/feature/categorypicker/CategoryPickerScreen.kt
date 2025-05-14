package com.android.quizcafe.feature.categorypicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.TitleWithUnderLine

@Composable
fun CategoryPickerScreen(
    state: CategoryViewState = CategoryViewState(),
    sendIntent: (CategoryIntent) -> Unit
) {
    val categories = state.categories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .navigationBarsPadding()
            .systemBarsPadding()
    ) {
        TitleWithUnderLine(stringResource(R.string.pick_category))
        Spacer(Modifier.height(12.dp))
        CategoryCardList(
            categories = categories,
            onItemClicked = {
                sendIntent(CategoryIntent.ClickCategory)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryPickerScreenPreview() {
    QuizCafeTheme {
        CategoryPickerScreen(state = CategoryViewState()) {}
    }
}
