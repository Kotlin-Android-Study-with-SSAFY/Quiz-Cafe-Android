package com.android.quizcafe.feature.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.primaryLight

private data class BottomNavItem(
    @DrawableRes val iconResId: Int,
    @StringRes val labelResId: Int
)

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem(
            iconResId = R.drawable.quiz_unfill,
            labelResId = R.string.bottom_nav_quiz
        ),
        BottomNavItem(
            iconResId = R.drawable.star_unfill,
            labelResId = R.string.bottom_nav_workbook
        ),
        BottomNavItem(
            iconResId = R.drawable.ic_person_outline,
            labelResId = R.string.bottom_nav_mypage
        )
    )

    NavigationBar(containerColor = primaryLight) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = stringResource(id = item.labelResId)
                    )
                },
                label = {
                    if (selectedIndex == index) {
                        Text(text = stringResource(id = item.labelResId))
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}
