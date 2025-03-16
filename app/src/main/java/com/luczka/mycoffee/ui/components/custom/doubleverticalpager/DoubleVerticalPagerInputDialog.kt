package com.luczka.mycoffee.ui.components.custom.doubleverticalpager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.textfields.FilteredMyCoffeeOutlinedTextField

@Composable
fun DoubleVerticalPagerInputDialog(
    doubleVerticalPagerState: DoubleVerticalPagerState,
    titleText: String,
    onNegative: () -> Unit,
    onPositive: (leftInputValue: String, rightInputValue: String) -> Unit
) {
    var leftInputValue by rememberSaveable { mutableStateOf("") }
    var rightInputValue by rememberSaveable { mutableStateOf("") }

    var isLeftInputError by rememberSaveable { mutableStateOf(false) }
    var isRightInputError by rememberSaveable { mutableStateOf(false) }

    val leftPagerMinValue = doubleVerticalPagerState.leftPagerItems.first()
    val leftPagerMaxValue = doubleVerticalPagerState.leftPagerItems.last()

    val rightPagerMinValue = doubleVerticalPagerState.rightPagerItems.first()
    val rightPagerMaxValue = doubleVerticalPagerState.rightPagerItems.last()

    val separator = stringResource(id = doubleVerticalPagerState.separatorRes)
    val descriptionText = stringResource(
        id = R.string.dialog_double_vertical_pager_input_description,
        leftPagerMinValue,
        rightPagerMinValue,
        leftPagerMaxValue,
        rightPagerMaxValue,
        separator
    )

    AlertDialog(
        title = {
            Text(text = titleText)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = descriptionText)
                Input(
                    doubleVerticalPagerState = doubleVerticalPagerState,
                    leftInputValue = leftInputValue,
                    rightInputValue = rightInputValue,
                    isLeftInputError = isLeftInputError,
                    isRightInputError = isRightInputError,
                    onLeftInputValueChange = { value ->
                        leftInputValue = value
                        isLeftInputError = validateValue(
                            inputValue = leftInputValue,
                            maxValue = leftPagerMaxValue,
                            minValue = leftPagerMinValue
                        )
                    },
                    onRightInputValueChange = { value ->
                        rightInputValue = value
                        isRightInputError = validateValue(
                            inputValue = rightInputValue,
                            maxValue = rightPagerMaxValue,
                            minValue = rightPagerMinValue
                        )
                    }
                )
            }
        },
        onDismissRequest = onNegative,
        dismissButton = {
            TextButton(onClick = onNegative) {
                Text(text = stringResource(id = R.string.action_cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositive(leftInputValue, rightInputValue)
                },
                enabled = acceptInput(
                    coffeeRatioValue = leftInputValue,
                    waterRatioValue = rightInputValue,
                    isCoffeeRatioError = isLeftInputError,
                    isWaterRatioError = isRightInputError
                )
            ) {
                Text(text = stringResource(id = R.string.action_select))
            }
        },
        tonalElevation = 0.dp
    )
}

@Composable
private fun Input(
    doubleVerticalPagerState: DoubleVerticalPagerState,
    leftInputValue: String,
    rightInputValue: String,
    isLeftInputError: Boolean,
    isRightInputError: Boolean,
    onLeftInputValueChange: (String) -> Unit,
    onRightInputValueChange: (String) -> Unit,
) {
    val currentCoffeeRatio = doubleVerticalPagerState.currentLeftPagerItem()
    val currentWaterRatio = doubleVerticalPagerState.currentRightPagerItem()

    val focusManager = LocalFocusManager.current

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilteredMyCoffeeOutlinedTextField(
            modifier = Modifier.weight(1f),
            value = leftInputValue,
            onValueChange = onLeftInputValueChange,
            regex = Regex("[0-9]+"),
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "$currentCoffeeRatio",
                    style = LocalTextStyle.current.copy(textAlign = TextAlign.End)
                )
            },
            supportingText = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .alpha(if (isLeftInputError) 1f else 0f),
                    text = stringResource(id = R.string.dialog_double_vertical_pager_input_error)
                )
            },
            isError = isLeftInputError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            )
        )
        Box(
            modifier = Modifier.heightIn(min = 56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(doubleVerticalPagerState.separatorRes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        FilteredMyCoffeeOutlinedTextField(
            modifier = Modifier.weight(1f),
            value = rightInputValue,
            onValueChange = onRightInputValueChange,
            regex = Regex("[0-9]+"),
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = {
                Text(text = "$currentWaterRatio")
            },
            supportingText = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .alpha(if (isRightInputError) 1f else 0f),
                    text = stringResource(id = R.string.dialog_double_vertical_pager_input_error)
                )
            },
            isError = isRightInputError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

private fun validateValue(
    inputValue: String,
    maxValue: Int,
    minValue: Int
): Boolean {
    if (inputValue.isBlank()) return false
    val intValue = inputValue.toIntOrNull() ?: return false
    return intValue > maxValue || minValue > intValue
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