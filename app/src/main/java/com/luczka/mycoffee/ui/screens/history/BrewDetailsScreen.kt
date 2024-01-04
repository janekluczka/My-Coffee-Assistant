package com.luczka.mycoffee.ui.screens.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.BackIconButton
import com.luczka.mycoffee.ui.components.DeleteIconButton
import com.luczka.mycoffee.ui.components.SelectedCoffeeCard
import com.luczka.mycoffee.ui.components.TopAppBarTitle
import com.luczka.mycoffee.util.toStringWithOneDecimalPoint
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrewDetailsScreen(
    brewDetailsUiState: BrewDetailsUiState,
    navigateUp: () -> Unit,
    onDelete: () -> Unit
) {
    brewDetailsUiState.brew ?: return

    var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (openDeleteDialog) {
        DeleteBrewDialog(
            brewUiState = brewDetailsUiState.brew,
            onNegative = {
                openDeleteDialog = false
            },
            onPositive = {
                openDeleteDialog = false
                onDelete()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { BackIconButton(onClick = navigateUp) },
                title = {
                    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                    val date = brewDetailsUiState.brew.date.format(formatter)
                    TopAppBarTitle(text = date)
                },
                actions = {
                    DeleteIconButton(onClick = { openDeleteDialog = true })
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(
                            top = 0.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        ),
                        text = if (brewDetailsUiState.brew.brewedCoffees.size > 1) {
                            stringResource(id = R.string.assistant_selected_coffees)
                        } else {
                            stringResource(id = R.string.assistant_selected_coffee)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        brewDetailsUiState.brew.brewedCoffees.forEach {brewedCoffeeUiState ->
                            SelectedCoffeeCard(coffeeUiState = brewedCoffeeUiState.coffee)
                        }
                    }
                }
                item {
                    Text(
                        modifier = Modifier.padding(
                            top = 24.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        ),
                        text = stringResource(id = R.string.assistant_selected_parameters),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BrewParameterCard(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            parameterName = stringResource(id = R.string.assistant_coffee),
                            parameterValue = stringResource(
                                id = R.string.coffee_parameters_amount_with_unit,
                                brewDetailsUiState.brew.coffeeAmount.toStringWithOneDecimalPoint()
                            )
                        )
                        BrewParameterCard(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            parameterName = stringResource(id = R.string.assistant_ratio),
                            parameterValue = brewDetailsUiState.brew.ratio
                        )
                        BrewParameterCard(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            parameterName = stringResource(id = R.string.assistant_water),
                            parameterValue = stringResource(
                                id = R.string.coffee_parameters_amount_with_unit,
                                brewDetailsUiState.brew.waterAmount.toStringWithOneDecimalPoint()
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BrewParameterCard(
    modifier: Modifier,
    parameterName: String,
    parameterValue: String
) {
    OutlinedCard(
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = parameterValue,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = parameterName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}