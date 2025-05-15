package com.android.quizcafe.feature.categorypicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.designsystem.theme.onPrimaryLight
import com.android.quizcafe.core.designsystem.theme.outlineLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import com.android.quizcafe.core.domain.model.quizbook.response.Category

@Composable
fun CategoryCardList(categories: List<Category>, onItemClicked: () -> Unit = {}) {
    LazyColumn {
        items(categories) { category ->
            CategoryCard(
                category = category,
                onCategoryItemClick = {
                    onItemClicked()
                }
            )
        }
    }
}

@Composable
fun CategoryCard(category: Category, onCategoryItemClick: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = onPrimaryLight),
        onClick = {
            onCategoryItemClick()
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryGroupText(category.group)
            Spacer(Modifier.height(8.dp))
            CategoryCardTitle(category.name)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun CategoryCardTitle(categoryName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = categoryName,
            style = quizCafeTypography().titleMedium
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.go),
            tint = scrimLight,
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
private fun CategoryGroupText(group: String) {
    Text(
        text = group,
        color = outlineLight,
        style = quizCafeTypography().bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryCardPreview() {
    QuizCafeTheme {
        CategoryCardList(listOf(Category("0", "그룹", "카테고리 이름"), Category("1", "그룹", "카테고리 이름")))
    }
}
