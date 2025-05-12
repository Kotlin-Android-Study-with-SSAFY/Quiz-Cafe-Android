package com.android.quizcafe.feature.home.workbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

@Composable
fun WorkbookScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("워크북페이지 화면입니다.")
    }
}

@Preview(showBackground = true)
@Composable
fun WorkbookScreenPreview() {
    QuizCafeTheme {
        WorkbookScreen()
    }
}

