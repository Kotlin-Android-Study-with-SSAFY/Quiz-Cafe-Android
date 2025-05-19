package com.android.quizcafe.feature.categorypicker

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.quizcafe.R

@Composable
fun CategoryRoute(
    navigateToQuizBookPicker: (String) -> Unit,
    navigateToHome: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(CategoryIntent.LoadCategories) // API 요청
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CategoryEffect.NavigateToHome -> {
                    navigateToHome()
                }

                is CategoryEffect.NavigateToQuizBooks -> {
                    navigateToQuizBookPicker(effect.categoryId)
                }

                is CategoryEffect.ShowError -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    CategoryPickerScreen(
        state = state,
        sendIntent = viewModel::sendIntent
    )
}
