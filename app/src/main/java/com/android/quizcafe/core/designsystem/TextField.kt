package com.android.quizcafe.core.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme

enum class TextFieldType {
    Text,
    Email,
    Password
}

@Composable
fun QuizCafeTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    isOutlined: Boolean = true,
    isClearable: Boolean = false,
    type: TextFieldType = TextFieldType.Text,
    trailingIcon: (@Composable () -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = when (type) {
        TextFieldType.Email -> KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction)
        TextFieldType.Password -> KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = imeAction)
        TextFieldType.Text -> KeyboardOptions.Default.copy(imeAction = imeAction)
    }
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation = remember(type, passwordVisible) {
        when (type) {
            TextFieldType.Password -> if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            else -> VisualTransformation.None
        }
    }

    val textFieldModifier = modifier.fillMaxWidth()

    val effectiveTrailingIcon: @Composable (() -> Unit) = remember(value, passwordVisible, isError) {
        {
            if (isError) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = stringResource(R.string.error_icon),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else if (type == TextFieldType.Password) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = stringResource(R.string.password_visibility_toggle),
                    modifier = Modifier
                        .clickable { passwordVisible = !passwordVisible }
                        .padding(end = 8.dp)
                )
            } else if (isClearable && value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.clear_text),
                    modifier = Modifier
                        .clickable { onValueChange("") }
                        .padding(end = 8.dp)
                )
            }
            trailingIcon?.invoke()
        }
    }

    val supportingText: @Composable (() -> Unit)? = errorText?.let {
        { Text(text = it, color = MaterialTheme.colorScheme.error) }
    }

    if (isOutlined) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            trailingIcon = effectiveTrailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            modifier = textFieldModifier,
            isError = isError,
            supportingText = supportingText
        )
    } else {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            trailingIcon = effectiveTrailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            modifier = textFieldModifier,
            isError = isError,
            supportingText = supportingText
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizCafeTextFieldPreview() {
    var text by remember { mutableStateOf("입력하세요") }

    QuizCafeTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Outlined Text
            QuizCafeTextField(
                value = text,
                onValueChange = { text = it },
                label = "일반 텍스트 (Outlined)",
                type = TextFieldType.Text,
                isClearable = true,
                isOutlined = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Underline Text
            QuizCafeTextField(
                value = text,
                onValueChange = { text = it },
                label = "일반 텍스트 (Underline)",
                type = TextFieldType.Text,
                isClearable = true,
                isOutlined = false
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Email (Outlined)
            QuizCafeTextField(
                value = text,
                onValueChange = { text = it },
                label = "이메일 (Outlined)",
                type = TextFieldType.Email,
                isClearable = true,
                isOutlined = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Email (Underline)
            QuizCafeTextField(
                value = text,
                onValueChange = { text = it },
                label = "이메일 (Underline)",
                type = TextFieldType.Email,
                isClearable = true,
                isOutlined = false
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password (Outlined)
            QuizCafeTextField(
                value = text,
                onValueChange = { text = it },
                label = "비밀번호 (Outlined)",
                type = TextFieldType.Password,
                isClearable = true,
                isOutlined = true,
                isError = true,
                errorText = "비밀번호가 너무 짧습니다."
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Password (Underline)
            QuizCafeTextField(
                value = text,
                onValueChange = { text = it },
                label = "비밀번호 (Underline)",
                type = TextFieldType.Password,
                isClearable = true,
                isOutlined = false,
                isError = false
            )
        }
    }
}