package com.android.quizcafe.feature.quiz.solve.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.blue_200
import com.android.quizcafe.core.designsystem.theme.error_01
import com.android.quizcafe.core.designsystem.theme.green_200
import com.android.quizcafe.core.designsystem.theme.neutral07
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight

@Composable
fun MultipleChoiceOptionButton(
    modifier: Modifier = Modifier,
    answerState: AnswerState = AnswerState.DEFAULT,
    index: Number,
    content: String,
    onClick: () -> Unit,
) {
    val (containerColor, contentColor) = when (answerState) {
        AnswerState.DEFAULT -> Pair(
            neutral07,
            scrimLight
        )

        AnswerState.SELECTED -> Pair(
            blue_200,
            scrimLight
        )

        AnswerState.CORRECT -> Pair(
            green_200,
            scrimLight
        )

        AnswerState.INCORRECT -> Pair(
            error_01,
            scrimLight
        )
    }

    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "$index.",
                style = quizCafeTypography().bodyMedium,
                modifier = Modifier.padding(top = 3.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = content,
                style = quizCafeTypography().bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultipleChoiceOptionPreview() {
    QuizCafeTheme {
        Column {
            MultipleChoiceOptionButton(
                answerState = AnswerState.DEFAULT,
                onClick = { /* 선택 처리 */ },
                modifier = Modifier,
                index = 1,
                content = "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다. "
            )
            Spacer(modifier = Modifier.height(8.dp))
            MultipleChoiceOptionButton(
                answerState = AnswerState.CORRECT,
                onClick = { /* 선택 처리 */ },
                modifier = Modifier,
                index = 1,
                content = "코루틴은 자바에서만 사용할 수 있다. "
            )
            Spacer(modifier = Modifier.height(8.dp))
            MultipleChoiceOptionButton(
                answerState = AnswerState.SELECTED,
                onClick = { /* 선택 처리 */ },
                modifier = Modifier,
                index = 1,
                content = "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다. "
            )
            Spacer(modifier = Modifier.height(8.dp))
            MultipleChoiceOptionButton(
                answerState = AnswerState.INCORRECT,
                onClick = { /* 선택 처리 */ },
                modifier = Modifier,
                index = 1,
                content = "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다. "
            )
        }
    }
}
