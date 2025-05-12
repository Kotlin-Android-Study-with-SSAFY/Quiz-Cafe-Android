package com.android.quizcafe.feature.quiz.solve.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme




@Composable
fun MultipleChoiceOptionButton(
    modifier: Modifier = Modifier,
    answerState: AnswerState = AnswerState.Normal,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val (containerColor, contentColor) = when (answerState) {
        AnswerState.Normal -> Pair(
            colorResource(R.color.neutral_5),
            colorResource(R.color.black)
        )

        AnswerState.Selected -> Pair(
            colorResource(R.color.secondary_200),
            colorResource(R.color.black)
        )

        AnswerState.Correct -> Pair(
            colorResource(R.color.green_2),
            colorResource(R.color.black)
        )

        AnswerState.Incorrect -> Pair(
            colorResource(R.color.error_0),
            colorResource(R.color.black)
        )
    }

    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun MultipleChoiceOptionPreview() {
    QuizCafeTheme {
        Column {
            MultipleChoiceOptionButton(
                answerState = AnswerState.Normal,
                onClick = { /* 선택 처리 */ },
                modifier = Modifier
            ) {
                Row {
                    Text(
                        text = "2. ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다. ",
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            MultipleChoiceOptionButton(
                answerState = AnswerState.Correct,
                onClick = { /* 선택 처리 */ },
                modifier = Modifier
            ) {
                Row {
                    Text(
                        text = "2. ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다. ",
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            MultipleChoiceOptionButton(
                answerState = AnswerState.Selected,
                onClick = { /* 선택 처리 */ },
                modifier = Modifier
            ) {
                Row {
                    Text(
                        text = "2. ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다. ",
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            MultipleChoiceOptionButton(
                answerState = AnswerState.Incorrect,
                onClick = { /* 선택 처리 */ },
                modifier = Modifier
            ) {
                Row {
                    Text(
                        text = "2. ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "코루틴은 자바에서만 사용할 수 있다. 코루틴은 자바에서만 사용할 수 있다. ",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
