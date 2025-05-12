package com.android.quizcafe.feature.home

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
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { onItemSelected(0) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.quiz_unfill),
                    contentDescription = "Main"
                )
            },
            label = { Text(text = "Main") }
        )

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { onItemSelected(1) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.quiz_unfill),
                    contentDescription = "Workbook"
                )
            },
            label = { Text(text = "Workbook") }
        )

        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { onItemSelected(2) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.quiz_unfill),
                    contentDescription = "MyPage"
                )
            },
            label = { Text(text = "MyPage") }
        )
    }
}
