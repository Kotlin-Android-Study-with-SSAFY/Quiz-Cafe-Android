package com.android.quizcafe.feature.quizbooklist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.onPrimaryLight
import com.android.quizcafe.core.designsystem.theme.outlineVariantLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import com.android.quizcafe.core.designsystem.theme.surfaceVariantLight
import com.android.quizcafe.core.designsystem.theme.tertiaryLight
import kotlin.math.roundToInt

@Composable
fun QuizBookFilterContent(
    filterState: FilterState,
    onApplyClick: (FilterState) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentFilterState by remember { mutableStateOf(filterState) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SortOptionContent(
            selected = currentFilterState.sortOption,
            onOptionSelected = { currentFilterState = currentFilterState.copy(sortOption = it) }
        )
        LevelOptionContent(
            selected = currentFilterState.level,
            onOptionSelected = { currentFilterState = currentFilterState.copy(level = it) }
        )
        SetQuizCountContent(
            selected = currentFilterState.quizCountRange,
            onValueChanged = { currentFilterState = currentFilterState.copy(quizCountRange = it) }
        )

        QuizCafeButton(
            onClick = { onApplyClick(currentFilterState) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.apply))
        }
    }
}

@Composable
private fun SetQuizCountContent(
    selected: IntRange = 1..50,
    onValueChanged: (IntRange) -> Unit = {}
) {
    var sliderPosition by remember { mutableStateOf(selected.first.toFloat()..selected.last.toFloat()) }

    val max = 50
    val min = 1

    Column {
        Text(stringResource(R.string.quiz_count), style = MaterialTheme.typography.titleMedium)
        RangeSlider(
            value = sliderPosition,
            onValueChange = { range ->
                sliderPosition = range
                onValueChanged(range.start.toInt()..range.endInclusive.toInt())
            },
            valueRange = min.toFloat()..max.toFloat(),
            steps = max - min - 1,
            colors = SliderDefaults.colors(
                thumbColor = tertiaryLight,
                activeTrackColor = tertiaryLight,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            )
        )
        Text(
            text = "${sliderPosition.start.roundToInt()}~${sliderPosition.endInclusive.roundToInt()} ${
                stringResource(
                    R.string.question
                )
            }",
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun LevelOptionContent(
    levelOptions: List<QuizLevel> = QuizLevel.entries,
    selected: QuizLevel = QuizLevel.ALL,
    onOptionSelected: (QuizLevel) -> Unit = {}
) {
    Column {
        Text(stringResource(R.string.level), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        OptionSelector(
            options = levelOptions,
            selectedOption = selected,
            onOptionSelected = { onOptionSelected(it) },
            optionToText = { it.stringResId }
        )
    }
}

@Composable
private fun SortOptionContent(
    sortOptions: List<SortOption> = SortOption.entries,
    selected: SortOption = SortOption.LATEST,
    onOptionSelected: (SortOption) -> Unit = {}
) {
    Column {
        Text(stringResource(R.string.sort), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        OptionSelector(
            options = sortOptions,
            selectedOption = selected,
            onOptionSelected = { onOptionSelected(it) },
            optionToText = { it.stringResId }
        )
    }
}

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
                        .background(if (isSelected) tertiaryLight else Color.Transparent) // 예시 색상
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

@Preview(showBackground = true)
@Composable
fun PreviewQuizBookFilterContent() {
    QuizCafeTheme {
        QuizBookFilterContent(
            onApplyClick = {},
            filterState = FilterState()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSortOptionContent() {
    QuizCafeTheme {
        SortOptionContent()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLevelOptionContent() {
    QuizCafeTheme {
        LevelOptionContent()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSetQuizCountContent() {
    QuizCafeTheme {
        SetQuizCountContent()
    }
}
