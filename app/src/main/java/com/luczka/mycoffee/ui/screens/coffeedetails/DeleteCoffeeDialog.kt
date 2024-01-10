package com.luczka.mycoffee.ui.screens.coffeedetails

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.model.CoffeeUiState

@Composable
fun DeleteCoffeeDialog(
    coffeeUiState: CoffeeUiState,
    onNegative: () -> Unit,
    onPositive: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_delete)) },
        text = {
            Text(
                text = stringResource(
                    id = R.string.dialog_text_delete,
                    coffeeUiState.brand,
                    coffeeUiState.name
                )
            )
        },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.dialog_action_delete))
            }
        },
    )
}