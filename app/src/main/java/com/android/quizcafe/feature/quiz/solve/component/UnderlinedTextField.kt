package com.android.quizcafe.feature.quiz.solve.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
@Composable
fun UnderlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxCharCount: Int = Int.MAX_VALUE,
    showCharCount: Boolean = false,
    answerState: AnswerState = AnswerState.Normal,
    placeholder: String = ""
) {
    val bottomLineColor = when (answerState) {
        AnswerState.Normal -> colorResource(R.color.neutral_13)
        AnswerState.Correct -> colorResource(R.color.secondary_200)
        AnswerState.Incorrect -> colorResource(R.color.error_1)
        AnswerState.Selected -> {}
    }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxCharCount) onValueChange(it)
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            decorationBox = { inner ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        inner()
                    }
                    // 우측 아이콘 (정답/오답일 때만)
                    when (answerState) {
                        AnswerState.Correct ->
                            Icon(
                                painter = painterResource(R.drawable.ic_subjective_correct),
                                contentDescription = "Correct",
                                tint = bottomLineColor as Color,
                                modifier = Modifier.size(20.dp)
                            )

                        AnswerState.Incorrect ->
                            Icon(
                                painter = painterResource(R.drawable.ic_subjective_incorrect),
                                contentDescription = "Incorrect",
                                tint = bottomLineColor as Color,
                                modifier = Modifier.size(20.dp)
                            )

                        else -> {
                            /* Normal 일 땐 아이콘 없음 */
                        }
                    }
                }
            }
        )
        HorizontalDivider(
            color = bottomLineColor as Color,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        if (showCharCount) {
            Text(
                text = "(${value.length}/$maxCharCount)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
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
                value = "정답전 입력",
                onValueChange = {},
                maxCharCount = 30,
                showCharCount = true,
                answerState = AnswerState.Normal,
                placeholder = "답을 입력하세요"
            )

            UnderlinedTextField(
                value = "맞췄다!",
                onValueChange = {},
                maxCharCount = 30,
                showCharCount = true,
                answerState = AnswerState.Correct,
                placeholder = "답을 입력하세요"
            )

            UnderlinedTextField(
                value = "틀렸다…",
                onValueChange = {},
                maxCharCount = 30,
                showCharCount = true,
                answerState = AnswerState.Incorrect,
                placeholder = "답을 입력하세요"
            )
        }
    }
}
