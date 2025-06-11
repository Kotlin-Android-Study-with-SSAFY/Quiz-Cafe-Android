package com.android.quizcafe.core.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

@Composable
fun IconText(
    text: String,
    iconResId: Int,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = iconResId), contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = style, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun IconTextPreview() {
    QuizCafeTheme {
        IconText("아이콘 텍스트", R.drawable.ic_filter)
    }
}
