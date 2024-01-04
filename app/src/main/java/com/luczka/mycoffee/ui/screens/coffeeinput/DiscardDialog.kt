package com.luczka.mycoffee.ui.screens.coffeeinput

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.luczka.mycoffee.R

@Composable
fun DiscardDialog(
    onPositive: () -> Unit,
    onNegative: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_discard)) },
        text = { Text(text = stringResource(id = R.string.dialog_text_discard)) },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.dialog_action_discard))
            }
        }
    )
}