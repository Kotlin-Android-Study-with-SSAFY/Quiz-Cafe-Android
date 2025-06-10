package com.android.quizcafe.feature.quiz.solve.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.feature.quiz.solve.QuizSolveScreen
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuestionType
import com.android.quizcafe.feature.quiz.solve.viewmodel.QuizSolveUiState

@Composable
fun ExplanationSection(
    modifier: Modifier = Modifier,
    explanation: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.explanation),
            style = quizCafeTypography().titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = explanation,
            style = quizCafeTypography().bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizSolveExplanationPreview() {
    QuizCafeTheme {
        ExplanationSection(
            explanation = "뭘 봐 인마"
        )
    }
}
