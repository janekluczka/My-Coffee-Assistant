package com.luczka.mycoffee.ui.screens.brewassistant.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme

@Composable
fun AssistantAbortDialog(
    onNegative: () -> Unit,
    onPositive: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_abort)) },
        text = { Text(text = stringResource(id = R.string.dialog_text_abort)) },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.dialog_action_abort))
            }
        },
        tonalElevation = 0.dp
    )
}

@Preview
@Composable
private fun AssistantAbortDialogPreview() {
    MyCoffeeTheme {
        AssistantAbortDialog(onNegative = {}, onPositive = {})
    }
}