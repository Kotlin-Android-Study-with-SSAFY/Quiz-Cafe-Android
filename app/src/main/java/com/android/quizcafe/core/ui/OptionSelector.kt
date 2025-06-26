package com.android.quizcafe.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.android.quizcafe.core.designsystem.theme.onPrimaryLight
import com.android.quizcafe.core.designsystem.theme.outlineVariantLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import com.android.quizcafe.core.designsystem.theme.surfaceVariantLight
import com.android.quizcafe.core.designsystem.theme.tertiaryLight

@Composable
fun <T> OptionSelector(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionToText: (T) -> Int
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = surfaceVariantLight
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == option

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) tertiaryLight else Color.Transparent)
                        .clickable {
                            onOptionSelected(option)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = stringResource(optionToText(option)),
                        color = if (isSelected) onPrimaryLight else scrimLight,
                        style = quizCafeTypography().labelSmall
                    )
                }

                if (index < options.lastIndex) {
                    VerticalDivider(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .height(16.dp)
                            .width(1.dp),
                        color = outlineVariantLight
                    )
                }
            }
        }
    }
}

@Composable
fun <T> OutLinedOptionSelector(
    modifier: Modifier = Modifier,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionToText: (T) -> Int,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedOption == option

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Transparent)
                    .clickable { onOptionSelected(option) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stringResource(optionToText(option)),
                    color = if (isSelected) MaterialTheme.colorScheme.scrim else MaterialTheme.colorScheme.outlineVariant,
                    style = textStyle
                )
            }

            if (index < options.lastIndex) {
                VerticalDivider(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .height(16.dp)
                        .width(1.dp),
                    color = outlineVariantLight
                )
            }
        }
    }
}
