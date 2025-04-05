package com.luczka.mycoffee.ui.screens.brewassistant.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantUiState

@Composable
fun SaveBrewDialog(
    modifier: Modifier = Modifier,
    uiState: BrewAssistantUiState,
    onNegative: () -> Unit,
    onPositive: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = stringResource(id = R.string.dialog_title_save_brew))
        },
        text = {
            AssistantSaveDialogText(uiState = uiState)
        },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.action_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onPositive) {
                Text(text = stringResource(id = R.string.action_save))
            }
        },
        tonalElevation = 0.dp
    )
}

@Composable
private fun AssistantSaveDialogText(uiState: BrewAssistantUiState) {
    when (uiState) {
        is BrewAssistantUiState.NoneSelected -> {

        }

        is BrewAssistantUiState.CoffeeSelected -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.selectedCoffees.keys.forEachIndexed { index, selectedCoffee ->
                    val brewAssistantCoffeeAmountItemUiState = uiState.selectedCoffees[selectedCoffee]!!
                    val integerPart = brewAssistantCoffeeAmountItemUiState.selectedIntegerPart()
                    val fractionalPart = brewAssistantCoffeeAmountItemUiState.selectedFractionalPart()
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "${index + 1}.")
                        Text(
                            text = stringResource(
                                id = R.string.dialog_text_finish,
                                integerPart,
                                fractionalPart,
                                selectedCoffee.originOrName,
                                selectedCoffee.roasterOrBrand,
                            )
                        )
                    }
                }
            }
        }
    }
}