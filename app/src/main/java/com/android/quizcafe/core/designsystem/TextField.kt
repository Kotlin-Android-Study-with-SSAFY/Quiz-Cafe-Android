package com.android.quizcafe.core.designsystem

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.quizcafe.core.designsystem.theme.errorLight
import com.android.quizcafe.core.designsystem.theme.onSurfaceVariantLight
import com.android.quizcafe.core.designsystem.theme.primaryContainerLight
import com.android.quizcafe.core.designsystem.theme.primaryLight
import com.android.quizcafe.core.designsystem.theme.quizCafeTypography
import com.android.quizcafe.core.designsystem.theme.scrimLight
import com.android.quizcafe.core.designsystem.theme.surfaceBrightLight

@Composable
fun QuizCafeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    isPassword: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (label != null) {
            Text(label, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = if (errorMessage != null) {
                modifier.border(
                    1.dp,
                    Color.Red,
                    RoundedCornerShape(10.dp)
                )
            } else {
                modifier
            },
            enabled = enabled,
            colors = TextFieldDefaults.colors(
                focusedTextColor = scrimLight,
                unfocusedTextColor = onSurfaceVariantLight,
                disabledContainerColor = surfaceBrightLight,
                focusedContainerColor = primaryContainerLight,
                unfocusedContainerColor = primaryContainerLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = primaryLight
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(fontSize = 16.sp)
        )

        if (errorMessage != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = errorMessage,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(4.dp),
                color = errorLight,
                style = quizCafeTypography().labelSmall,
            )
        }
    }
}

@Composable
fun LabeledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    focusRequester: FocusRequester? = null
) {
    QuizCafeTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        isPassword = isPassword,
        errorMessage = errorMessage,
        enabled = enabled
    )
}
