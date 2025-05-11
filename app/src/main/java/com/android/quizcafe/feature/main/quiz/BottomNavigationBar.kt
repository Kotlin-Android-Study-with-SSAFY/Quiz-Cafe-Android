package com.android.quizcafe.feature.main.quiz

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.android.quizcafe.R

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf("Main", "Workbook", "MyPage")

    NavigationBar {
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.quiz_unfill),
                        contentDescription = label
                    )
                },
                label = { Text(text = label) }
            )
        }
    }
}
