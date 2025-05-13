package com.android.quizcafe.feature.categorypicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.TitleWithUnderLine

@Composable
fun CategoryPickerScreen() {
    // 임시 데이터
    val categories = listOf(
        Category("Computer Science", "네트워크"),
        Category("Computer Science", "운영체제"),
        Category("Computer Science", "알고리즘"),
        Category("Android", "안드로이드"),
        Category("Computer Science", "네트워크"),
        Category("Computer Science", "운영체제"),
        Category("Computer Science", "알고리즘"),
        Category("Android", "안드로이드"),
        Category("Programming Language", "코틀린")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TitleWithUnderLine(stringResource(R.string.pick_category))
        Spacer(Modifier.height(12.dp))
        CategoryCardList(categories)
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryPickerScreenPreview() {
    QuizCafeTheme {
        CategoryPickerScreen()
    }
}
