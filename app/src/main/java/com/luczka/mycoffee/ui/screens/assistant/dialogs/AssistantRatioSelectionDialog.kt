package com.luczka.mycoffee.ui.screens.assistant.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.textfield.FilteredOutlinedTextField
import com.luczka.mycoffee.ui.screens.assistant.RatioSelectionUiState

// TODO: Fix error changing box size

@Composable
fun AssistantRatioSelectionDialog(
    ratioSelectionUiState: RatioSelectionUiState,
    onNegative: () -> Unit,
    onPositive: (String, String) -> Unit
) {
    var coffeeRatioValue by rememberSaveable { mutableStateOf("") }
    var waterRatioValue by rememberSaveable { mutableStateOf("") }

    var isCoffeeRatioError by rememberSaveable { mutableStateOf(false) }
    var isWaterRatioError by rememberSaveable { mutableStateOf(false) }

    val minCoffeeRatio = ratioSelectionUiState.coffeeRatios.first()
    val minWaterRatio = ratioSelectionUiState.waterRatios.first()

    val maxCoffeeRatio = ratioSelectionUiState.coffeeRatios.last()
    val maxWaterRatio = ratioSelectionUiState.waterRatios.last()

    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_select_ratio)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = stringResource(
                        id = R.string.dialog_text_select_ratio,
                        minCoffeeRatio, minWaterRatio,
                        maxCoffeeRatio, maxWaterRatio
                    )
                )
                RatioInput(
                    ratioSelectionUiState = ratioSelectionUiState,
                    coffeeRatioValue = coffeeRatioValue,
                    waterRatioValue = waterRatioValue,
                    isCoffeeRatioError = isCoffeeRatioError,
                    isWaterRatioError = isWaterRatioError,
                    maxCoffeeRatio = maxCoffeeRatio,
                    maxWaterRatio = maxWaterRatio,
                    onCoffeeRatioValueChange = { value ->
                        coffeeRatioValue = value
                        isCoffeeRatioError = validateValue(
                            ratioValue = coffeeRatioValue,
                            maxRatio = maxCoffeeRatio,
                            minRatio = minCoffeeRatio
                        )
                    },
                    onWaterRatioValueChange = { value ->
                        waterRatioValue = value
                        isWaterRatioError = validateValue(
                            ratioValue = waterRatioValue,
                            maxRatio = maxWaterRatio,
                            minRatio = minWaterRatio
                        )
                    }
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
                onClick = { onPositive(coffeeRatioValue, waterRatioValue) },
                enabled = acceptInput(
                    coffeeRatioValue = coffeeRatioValue,
                    waterRatioValue = waterRatioValue,
                    isCoffeeRatioError = isCoffeeRatioError,
                    isWaterRatioError = isWaterRatioError
                )
            ) {
                Text(text = stringResource(id = R.string.dialog_action_select))
            }
        }
    )
}

@Composable
private fun RatioInput(
    ratioSelectionUiState: RatioSelectionUiState,
    coffeeRatioValue: String,
    waterRatioValue: String,
    isCoffeeRatioError: Boolean,
    isWaterRatioError: Boolean,
    maxCoffeeRatio: Int,
    maxWaterRatio: Int,
    onCoffeeRatioValueChange: (String) -> Unit,
    onWaterRatioValueChange: (String) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilteredOutlinedTextField(
            modifier = Modifier.weight(1f),
            value = coffeeRatioValue,
            onValueChange = onCoffeeRatioValueChange,
            regex = Regex("[0-9]+"),
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${ratioSelectionUiState.selectedCoffeeRatio}",
                    style = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                )
            },
            supportingText = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .alpha(if (isCoffeeRatioError) 1f else 0f),
                    text = stringResource(
                        id = R.string.assistant_select_ratio_range_error,
                        maxCoffeeRatio
                    )
                )
            },
            isError = isCoffeeRatioError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Box(
            modifier = Modifier.heightIn(min = 56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = ":",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        FilteredOutlinedTextField(
            modifier = Modifier.weight(1f),
            value = waterRatioValue,
            onValueChange = onWaterRatioValueChange,
            regex = Regex("[0-9]+"),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(text = "${ratioSelectionUiState.selectedWaterRatio}")
            },
            supportingText = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .alpha(if (isWaterRatioError) 1f else 0f),
                    text = stringResource(
                        id = R.string.assistant_select_ratio_range_error,
                        maxWaterRatio
                    )
                )
            },
            isError = isWaterRatioError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

private fun validateValue(ratioValue: String, maxRatio: Int, minRatio: Int): Boolean {
    val ratio = ratioValue.toIntOrNull()
    return when {
        ratioValue.isBlank() -> false
        ratio == null -> false
        else -> ratio > maxRatio || minRatio > ratio
    }
}

private fun acceptInput(
    coffeeRatioValue: String,
    waterRatioValue: String,
    isCoffeeRatioError: Boolean,
    isWaterRatioError: Boolean
): Boolean {
    val hasValue = coffeeRatioValue.isNotBlank() || waterRatioValue.isNotBlank()
    val hasNoError = !isCoffeeRatioError && !isWaterRatioError
    return hasValue && hasNoError
}

//@Preview
//@Composable
//fun RatioSelectionDialogPreview() {
//    MyCoffeeTheme {
//        Box(modifier = Modifier.fillMaxSize()) {
//            RatioSelectionDialog(
//                selectedCoffeeRatio = 1,
//                selectedWaterRatio = 1,
//                onPositive = { _, _ -> },
//                onNegative = {}
//            )
//        }
//    }
//}