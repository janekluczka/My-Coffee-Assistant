package com.luczka.mycoffee.ui.screens.coffeeinput

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme

@Composable
fun CoffeeInputDiscardDialog(
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

@Preview
@Composable
private fun DiscardDialogPreview() {
    MyCoffeeTheme {
        CoffeeInputDiscardDialog(onPositive = {}, onNegative = {})
    }
}