package com.luczka.mycoffee.ui.screens.assistant.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.textfields.FilteredMyCoffeeOutlinedTextField
import com.luczka.mycoffee.ui.screens.assistant.AmountSelectionUiState
import com.luczka.mycoffee.util.toStringWithOneDecimalPoint

@Composable
fun AssistantAmountSelectionDialog(
    maxAmount: Float = 99.9f,
    amountSelectionUiState: AmountSelectionUiState,
    onNegative: () -> Unit,
    onPositive: (String) -> Unit,
) {
    var amountValue by rememberSaveable { mutableStateOf("") }
    var isAmountError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_select_amount)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(
                        id = R.string.dialog_text_select_amount,
                        maxAmount.toStringWithOneDecimalPoint()
                    )
                )
                FilteredMyCoffeeOutlinedTextField(
                    value = amountValue,
                    onValueChange = { value ->
                        amountValue = value
                        isAmountError = validateValue(
                            amountValue = amountValue,
                            maxAmount = maxAmount
                        )
                    },
                    regex = Regex("([0-9]+([.][0-9]?)?|[.][0-9])"),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = { Text(text = amountSelectionUiState.selectedAmount) },
                    supportingText = {
                        Text(
                            modifier = Modifier.alpha(if (isAmountError) 1f else 0f),
                            text = stringResource(
                                id = R.string.assistant_select_amount_error,
                                "100"
                            )
                        )
                    },
                    isError = isAmountError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.dialog_action_cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onPositive(amountValue) },
                enabled = acceptInput(amountValue = amountValue, isAmountError = isAmountError)
            ) {
                Text(text = stringResource(id = R.string.dialog_action_select))
            }
        }
    )
}

private fun acceptInput(amountValue: String, isAmountError: Boolean): Boolean {
    return amountValue.isNotBlank() && !isAmountError
}

private fun validateValue(amountValue: String, maxAmount: Float): Boolean {
    val amount = amountValue.toFloatOrNull()
    return when {
        amountValue.isBlank() -> false
        amount == null -> false
        else -> amount > maxAmount
    }
}

//@Preview
//@Composable
//fun AmountSelectionDialogPreview() {
//    val selectedCoffee = CoffeeUiState(
//        name = "ethiopia sami",
//        brand = "monko.",
//        amount = "123.4"
//    )
//    MyCoffeeTheme(darkTheme = false) {
//        Box(modifier = Modifier.fillMaxSize()) {
//            AmountSelectionDialog(
//                selectedCoffee = selectedCoffee,
//                amountSelectionUiState = "0.0",
//                onPositive = {
//
//                },
//                onNegative = {
//
//                }
//            )
//        }
//    }
//}