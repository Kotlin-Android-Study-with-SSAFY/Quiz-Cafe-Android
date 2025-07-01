package com.android.quizcafe.feature.quiz.solve.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.android.quizcafe.R
import com.android.quizcafe.core.designsystem.theme.QuizCafeTheme
import com.android.quizcafe.core.ui.util.TestTags

@Composable
fun ExitSolvingDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onExitWithDelete: () -> Unit,
    onExitWithSave: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .testTag(TestTags.QuizSolve.EXIT_DIALOG),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(
                    modifier = Modifier.testTag(TestTags.QuizSolve.EXIT_DIALOG_CLOSE_BUTTON),
                    onClick = onDismissRequest
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.dialog_close)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.dialog_exit_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag(TestTags.QuizSolve.EXIT_DIALOG_TITLE)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.dialog_exit_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag(TestTags.QuizSolve.EXIT_DIALOG_DESCRIPTION)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onExitWithDelete,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.testTag(TestTags.QuizSolve.EXIT_DIALOG_EXIT_DELETE_BUTTON)
                        ) {
                            Text(text = stringResource(R.string.dialog_exit_with_delete))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TextButton(
                            onClick = onExitWithSave,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.testTag(TestTags.QuizSolve.EXIT_DIALOG_EXIT_SAVE_BUTTON)
                        ) {
                            Text(text = stringResource(R.string.dialog_exit_with_save))
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "ExitSolvingDialog", showBackground = true)
@Composable
fun ExitSolvingDialogPreview() {
    QuizCafeTheme {
        ExitSolvingDialog(
            onDismissRequest = {},
            onExitWithDelete = {},
            onExitWithSave = {}
        )
    }
}
