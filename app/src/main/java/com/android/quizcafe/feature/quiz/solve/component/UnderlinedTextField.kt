package com.android.quizcafe.feature.quiz.solve.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.blue_200
import com.android.quizcafe.core.designsystem.theme.error_02
import com.android.quizcafe.core.designsystem.theme.neutral05
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight

@Composable
fun UnderlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxCharCount: Int = 30,
    showCharCount: Boolean = false,
    answerState: AnswerState = AnswerState.DEFAULT
) {
    val bottomLineColor = when (answerState) {
        AnswerState.DEFAULT -> scrimLight
        AnswerState.CORRECT -> blue_200
        AnswerState.INCORRECT -> error_02
        AnswerState.SELECTED -> {}
    } as Color

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxCharCount) onValueChange(it)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    inner()

                    // 우측 아이콘 (정답/오답일 때만)
                    when (answerState) {
                        AnswerState.CORRECT,
                        AnswerState.INCORRECT -> {
                            AnswerResultIcon(answerState,bottomLineColor)
                        }
                        else -> {
                            /* Normal 일 땐 아이콘 없음 */
                        }
                    }

                }
            }
        )
        HorizontalDivider(
            color = bottomLineColor,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        if (showCharCount) {
            LengthCountText(value, maxCharCount)
        }
    }
}

@Composable
fun AnswerResultIcon(answerState: AnswerState, bottomLineColor: Color) {
    Icon(
        painter = painterResource(if (answerState == AnswerState.INCORRECT) R.drawable.ic_subjective_incorrect else R.drawable.ic_subjective_correct),
        contentDescription = "Incorrect",
        tint = bottomLineColor,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
fun LengthCountText(value: String, maxCharCount: Int) {
    Text(
        text = "(${value.length}/$maxCharCount)",
        style = quizCafeTypography().bodyMedium,
        color = if (value.isEmpty()) neutral05 else scrimLight,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun UnderlinedTextFieldPreview() {
    QuizCafeTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            UnderlinedTextField(
                value = "",
                onValueChange = {},
                maxCharCount = 30,
                showCharCount = true,
                answerState = AnswerState.DEFAULT
            )
            UnderlinedTextField(
                value = "정답전 입력",
                onValueChange = {},
                maxCharCount = 30,
                showCharCount = true,
                answerState = AnswerState.DEFAULT
            )

            UnderlinedTextField(
                value = "맞췄다!",
                onValueChange = {},
                maxCharCount = 30,
                showCharCount = true,
                answerState = AnswerState.CORRECT
            )

            UnderlinedTextField(
                value = "틀렸다…",
                onValueChange = {},
                maxCharCount = 30,
                showCharCount = true,
                answerState = AnswerState.INCORRECT
            )
        }
    }
}
