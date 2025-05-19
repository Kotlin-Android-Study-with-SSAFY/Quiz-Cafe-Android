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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.blue_300
import com.android.quizcafe.core.designsystem.theme.error_02
import com.android.quizcafe.core.designsystem.theme.neutral06
import com.android.quizcafe.core.designsystem.theme.onPrimaryLight

@Composable
fun OxOptionButton(
    modifier: Modifier = Modifier,
    answerState: AnswerState = AnswerState.DEFAULT,
    iconPaint: Int,
    onClick: () -> Unit,
) {
    val (containerColor, contentColor) = when (answerState) {
        AnswerState.DEFAULT -> Pair(
            neutral06,
            onPrimaryLight,
        )

        AnswerState.SELECTED -> Pair(
            blue_300,
            onPrimaryLight,
        )

        AnswerState.CORRECT -> Pair(
            blue_300,
            onPrimaryLight,
        )

        AnswerState.INCORRECT -> Pair(
            neutral06,
            onPrimaryLight,
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
        border = if (answerState == AnswerState.INCORRECT) BorderStroke(8.dp, color = error_02) else null
    ) {
        Icon(
            painter = painterResource(iconPaint),
            contentDescription = "O",
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OxOptionButtonPreview() {
    QuizCafeTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OxOptionButton(
                answerState = AnswerState.DEFAULT,
                onClick = {},
                iconPaint = R.drawable.ic_ox_option_o,
                modifier = Modifier
            )
            OxOptionButton(
                answerState = AnswerState.CORRECT,
                onClick = {},
                iconPaint = R.drawable.ic_ox_option_x,
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OxOptionButtonErrorPreview() {
    QuizCafeTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OxOptionButton(
                answerState = AnswerState.INCORRECT,
                onClick = {},
                iconPaint = R.drawable.ic_ox_option_o,
                modifier = Modifier
            )
            OxOptionButton(
                answerState = AnswerState.SELECTED,
                onClick = {},
                iconPaint = R.drawable.ic_ox_option_x,
                modifier = Modifier
            )
        }
    }
}
