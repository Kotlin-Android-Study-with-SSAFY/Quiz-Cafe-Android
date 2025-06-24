package com.android.quizcafe.feature.main.workbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WorkBookRoute(
    viewModel: WorkBookViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.sendIntent(WorkBookIntent.LoadWorkBookList)
    }

    val workBookState = viewModel.state.collectAsState()
    WorkbookScreen(workBookState.value)
}
