package com.luczka.mycoffee.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.BrewUiState

@Composable
fun DeleteBrewDialog(
    brewUiState: BrewUiState,
    onNegative: () -> Unit,
    onPositive: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_delete_coffee_title)) },
        text = {
//            Text(
//                text = stringResource(
//                    id = R.string.dialog_text_delete,
//                    coffeeUiState.brand,
//                    coffeeUiState.name
//                )
//            )
        },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.action_delete))
            }
        },
        tonalElevation = 0.dp
    )
}