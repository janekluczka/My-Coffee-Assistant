package com.luczka.mycoffee.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.CategoryUiState

@Composable
fun MethodInfoDialog(
    method: CategoryUiState,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.dialog_method_info_title, method.name))
        },
        text = {
            Text(text = method.description)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_ok))
            }
        },
        tonalElevation = 0.dp
    )
}