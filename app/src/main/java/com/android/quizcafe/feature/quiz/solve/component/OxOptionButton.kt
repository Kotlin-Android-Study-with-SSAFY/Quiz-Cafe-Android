package com.android.quizcafe.feature.quiz.solve.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
@Composable
fun OxOptionButton(
    modifier: Modifier = Modifier,
    answerState: AnswerState = AnswerState.Normal,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val (containerColor, contentColor, border) = when (answerState) {
        AnswerState.Normal -> Triple(
            colorResource(R.color.component_4),
            colorResource(R.color.neutral_1),
            null
        )

        AnswerState.Selected -> Triple(
            colorResource(R.color.secondary_400),
            colorResource(R.color.neutral_1),
            null
        )

        AnswerState.Correct -> Triple(
            colorResource(R.color.secondary_400),
            colorResource(R.color.neutral_1),
            null
        )

        AnswerState.Incorrect -> Triple(
            colorResource(R.color.component_4),
            colorResource(R.color.neutral_1),
            BorderStroke(8.dp, colorResource(R.color.error_1))
        )
    }

    FilledTonalButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .size(width = 184.dp, height = 184.dp),
        shape = RoundedCornerShape(10.dp),
        border = border
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun OxOptionButtonPreview() {
    QuizCafeTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OxOptionButton(
                answerState = AnswerState.Normal,
                onClick = {},
                modifier = Modifier,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_ox_option_o),
                    contentDescription = "O",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                )
            }
            OxOptionButton(
                answerState = AnswerState.Correct,
                onClick = {},
                modifier = Modifier,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_ox_option_x),
                    contentDescription = "X",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OxOptionButtonErrorPreview() {
    QuizCafeTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OxOptionButton(
                answerState = AnswerState.Selected,
                onClick = {},
                modifier = Modifier,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_ox_option_o),
                    contentDescription = "O",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                )
            }
            OxOptionButton(
                answerState = AnswerState.Incorrect,
                onClick = {},
                modifier = Modifier,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_ox_option_x),
                    contentDescription = "X",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                )
            }
        }
    }
}
