package com.android.quizcafe.feature.quizbookpicker

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.QuizCafeButton
import com.android.quizcafe.core.designsystem.theme.onPrimaryLight
import com.android.quizcafe.core.designsystem.theme.outlineVariantLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import com.android.quizcafe.core.designsystem.theme.surfaceVariantLight
import com.android.quizcafe.core.designsystem.theme.tertiaryLight
import kotlin.math.roundToInt

@Composable
fun QuizBookFilterContent(
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sortOptions = listOf(
        stringResource(R.string.sort_latest),
        stringResource(R.string.sort_saved),
        stringResource(R.string.sort_random)
    )
    val levelOptions = listOf(
        stringResource(R.string.all),
        stringResource(R.string.level_easy),
        stringResource(R.string.level_medium),
        stringResource(R.string.level_hard),
        stringResource(R.string.level_very_hard)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SortOptionContent(sortOptions)
        Spacer(Modifier.height(8.dp))
        LevelOptionContent(levelOptions)
        Spacer(Modifier.height(8.dp))
        SetQuizCountContent()

        QuizCafeButton(onClick = onApplyClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.apply))
        }
    }
}

@Composable
private fun SetQuizCountContent() {
    val minCount by remember { mutableIntStateOf(5) }
    val maxCount by remember { mutableIntStateOf(50) }
    var sliderPosition by remember { mutableStateOf(minCount.toFloat()..maxCount.toFloat()) }

    Text(stringResource(R.string.quiz_count), style = MaterialTheme.typography.titleMedium)
    Column {
        RangeSlider(
            value = sliderPosition,
            onValueChange = { range -> sliderPosition = range },
            valueRange = minCount.toFloat()..maxCount.toFloat(),
            steps = maxCount - minCount - 1,
            colors = SliderDefaults.colors(thumbColor = tertiaryLight, activeTrackColor = tertiaryLight, activeTickColor = Color.Transparent, inactiveTickColor = Color.Transparent)
        )
        Text(
            text = "${sliderPosition.start.roundToInt()}~${sliderPosition.endInclusive.roundToInt()} ${stringResource(R.string.question)}",
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun LevelOptionContent(
    levelOptions: List<String>
) {
    val defaultSortOption = stringResource(R.string.all)
    var selected by remember { mutableStateOf(defaultSortOption) }

    Text(stringResource(R.string.level), style = MaterialTheme.typography.titleMedium)
    OptionSelector(
        options = levelOptions,
        selectedOption = selected,
        onOptionSelected = { selected = it }
    )
}

@Composable
private fun SortOptionContent(
    sortOptions: List<String>
) {
    val defaultSortOption = stringResource(R.string.sort_latest)
    var selected by remember { mutableStateOf(defaultSortOption) }

    Text(stringResource(R.string.sort), style = MaterialTheme.typography.titleMedium)
    OptionSelector(
        options = sortOptions,
        selectedOption = selected,
        onOptionSelected = { selected = it }
    )
}

@Composable
fun OptionSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
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
                        text = option,
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
