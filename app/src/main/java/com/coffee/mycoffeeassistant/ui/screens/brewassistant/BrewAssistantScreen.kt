package com.coffee.mycoffeeassistant.ui.screens.brewassistant

import android.media.MediaPlayer
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.model.BrewAssistantUiState
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrewAssistantScreen(
    viewModel: BrewAssistantViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current

    val brewAssistantUiState by viewModel.uiState.collectAsState()

    viewModel.getCoffees()

    var coffeeExpanded by remember { mutableStateOf(false) }
    var optionSelected by remember { mutableStateOf(0) }

    var isCoffeeAmountWrong by remember { mutableStateOf(false) }
    var isWaterAmountWrong by remember { mutableStateOf(false) }
    var ratioExpanded by remember { mutableStateOf(false) }

    var coffeeAmount by remember { mutableStateOf("") }
    var waterAmount by remember { mutableStateOf("") }
    var coffeeRatio by remember { mutableStateOf(5) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card {
                if (brewAssistantUiState.selectedCoffee?.bitmap != null) {
                    AsyncImage(
                        model = brewAssistantUiState.selectedCoffee!!.bitmap,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_photo_camera),
                            contentDescription = "",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }
        item {
            ExposedDropdownMenuBox(
                expanded = coffeeExpanded,
                onExpandedChange = { /* Change handled by interactionSource */ },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedCoffeeValue(brewAssistantUiState),
                    onValueChange = {},
                    maxLines = 1,
                    label = { Text("Coffee beans") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = coffeeExpanded) },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        coffeeExpanded = !coffeeExpanded
                                    }
                                }
                            }
                        }
                )
                ExposedDropdownMenu(
                    expanded = coffeeExpanded,
                    onDismissRequest = { coffeeExpanded = false },
                ) {
                    viewModel.coffeeUiStateList.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { CoffeeDescription(selectionOption) },
                            onClick = {
                                viewModel.selectCoffee(selectionOption)
                                coffeeExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy((8).dp)) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = if (optionSelected == 0) {
                        ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    onClick = { optionSelected = 0 }
                ) {
                    Text(text = "Amount")
                }
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = if (optionSelected == 1) {
                        ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    onClick = { optionSelected = 1 }
                ) {
                    Text(text = "Ratio")
                }
            }
        }
        item {
            OutlinedTextField(
                value = coffeeAmount,
                onValueChange = {
                    coffeeAmount = it
                    if (coffeeAmount.toFloatOrNull() != null && coffeeAmount.toFloat() >= 0) {
                        isCoffeeAmountWrong = false
                        if (optionSelected == 1 && coffeeAmount.isNotBlank()) {
                            waterAmount =
                                (coffeeAmount.toFloat() * coffeeRatio.toFloat()).toString()
                        }
                        if (optionSelected == 0 && waterAmount.toFloatOrNull() != null) {
                            coffeeRatio = (waterAmount.toFloat() / coffeeAmount.toFloat()).toInt()
                        }
                    } else {
                        isCoffeeAmountWrong = true
                    }
                },
                label = { Text("Coffee amount") },
                singleLine = true,
                isError = isCoffeeAmountWrong,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
        item {
            Crossfade(targetState = optionSelected) {
                if (it == 0) {
                    OutlinedTextField(
                        value = waterAmount,
                        onValueChange = { value ->
                            waterAmount = value
                            if (waterAmount.toFloatOrNull() != null && waterAmount.toFloat() >= 0) {
                                isWaterAmountWrong = false
                                if (coffeeAmount.toFloatOrNull() != null) {
                                    coffeeRatio =
                                        (waterAmount.toFloat() / coffeeAmount.toFloat()).toInt()
                                }
                            } else {
                                isWaterAmountWrong = true
                            }
                        },
                        label = { Text("Water amount") },
                        singleLine = true,
                        isError = isWaterAmountWrong,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )

                } else {
                    ExposedDropdownMenuBox(
                        expanded = ratioExpanded,
                        onExpandedChange = { /* Change handled by interactionSource */ },
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            readOnly = true,
                            value = "1:${coffeeRatio}",
                            onValueChange = {},
                            maxLines = 1,
                            label = { Text("Ratio") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ratioExpanded) },
                            interactionSource = remember { MutableInteractionSource() }
                                .also { interactionSource ->
                                    LaunchedEffect(interactionSource) {
                                        interactionSource.interactions.collect { interaction ->
                                            if (interaction is PressInteraction.Release) {
                                                ratioExpanded = !ratioExpanded
                                            }
                                        }
                                    }
                                }
                        )
                        ExposedDropdownMenu(
                            expanded = ratioExpanded,
                            onDismissRequest = { ratioExpanded = false },
                        ) {
                            (5..30).forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(text = "1:${selectionOption}") },
                                    onClick = {
                                        coffeeRatio = selectionOption
                                        if (coffeeAmount.isNotBlank()) {
                                            waterAmount =
                                                (coffeeAmount.toFloat() * coffeeRatio.toFloat()).toString()
                                        }
                                        ratioExpanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }
            }

        }
        item {
            Crossfade(targetState = optionSelected) {
                if (it == 0) {
                    OutlinedTextField(
                        readOnly = true,
                        value = "1:${coffeeRatio}",
                        onValueChange = { },
                        label = { Text("Ratio") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        readOnly = true,
                        value = waterAmount,
                        onValueChange = { },
                        label = { Text("Water amount") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        item {
            Button(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp).fillMaxWidth(),
                onClick = {
                    if (coffeeAmount.toFloatOrNull() != null) {
                        viewModel.reduceAmount(coffeeAmount.toFloat()) {
                            val mediaPlayer = MediaPlayer.create(context, R.raw.kettle_pour_coffee)
                            mediaPlayer.start()
                        }
                    }
                }
            ) {
                Text(text = "Brew coffee")
            }
        }
    }
}

@Composable
private fun selectedCoffeeValue(brewAssistantUiState: BrewAssistantUiState) =
    if (brewAssistantUiState.selectedCoffee != null) {
        buildDescription(brewAssistantUiState.selectedCoffee)
    } else {
        stringResource(id = R.string.choose_your_coffee_beans)
    }

@Composable
private fun CoffeeDescription(coffeeUiState: CoffeeUiState) {
    val description = buildDescription(coffeeUiState)
    Text(description)
}


private fun buildDescription(coffeeUiState: CoffeeUiState): String {
    return "${coffeeUiState.name}, ${coffeeUiState.brand} (${coffeeUiState.currentAmount} g)"
}