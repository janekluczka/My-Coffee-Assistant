package com.luczka.mycoffee.ui.screens.coffeeinput.dialogs

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
fun CoffeeInputDiscardDialog(
    onPositive: () -> Unit,
    onNegative: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_discard_coffee_changes_title)) },
        text = { Text(text = stringResource(id = R.string.dialog_discard_coffee_changes_text)) },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.action_discard))
            }
        },
        tonalElevation = 0.dp
    )
}

@Preview
@Composable
private fun DiscardDialogPreview() {
    MyCoffeeTheme {
        CoffeeInputDiscardDialog(onPositive = {}, onNegative = {})
    }
}