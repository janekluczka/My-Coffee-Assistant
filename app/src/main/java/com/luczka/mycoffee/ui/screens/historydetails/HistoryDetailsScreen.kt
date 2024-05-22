package com.luczka.mycoffee.ui.screens.historydetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.BackIconButton
import com.luczka.mycoffee.ui.components.DeleteIconButton
import com.luczka.mycoffee.ui.components.HistoryDetailsCoffeeListItem
import com.luczka.mycoffee.ui.components.TopAppBarTitle
import com.luczka.mycoffee.ui.screens.assistant.SummaryParametersListItem
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailsScreen(
    historyDetailsUiState: HistoryDetailsUiState,
    navigateUp: () -> Unit,
    onDelete: () -> Unit
) {
    historyDetailsUiState.brew ?: return

    var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (openDeleteDialog) {
        DeleteBrewDialog(
            brewUiState = historyDetailsUiState.brew,
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
                    val date = historyDetailsUiState.brew.date.format(formatter)
                    TopAppBarTitle(text = date)
                },
                actions = {
                    DeleteIconButton(onClick = { openDeleteDialog = true })
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Divider()
            LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
                historyDetailsUiState.brew.let { brewUiState ->

                    item {
                        Text(
                            modifier = Modifier.padding(
                                top = 0.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            ),
                            text = if (brewUiState.brewedCoffees.size > 1) {
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
                        Column {
                            if (brewUiState.brewedCoffees.isEmpty()) {
                                HistoryDetailsCoffeeListItem(coffeeUiState = null)
                            } else {
                                brewUiState.brewedCoffees.forEach { brewedCoffeeUiState ->
                                    HistoryDetailsCoffeeListItem(coffeeUiState = brewedCoffeeUiState.coffee)
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item {
                        Text(
                            modifier = Modifier.padding(
                                top = 0.dp,
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
                        SummaryParametersListItem(
                            index = 0,
                            headlineText = stringResource(id = R.string.assistant_ratio),
                            trailingText = stringResource(
                                id = R.string.assistant_ratio_format,
                                brewUiState.coffeeRatio,
                                brewUiState.waterRatio
                            )
                        )
                        SummaryParametersListItem(
                            index = 1,
                            headlineText = stringResource(id = R.string.assistant_coffee),
                            trailingText = stringResource(
                                id = R.string.coffee_parameters_amount_with_unit,
                                brewUiState.coffeeAmount
                            )
                        )
                        SummaryParametersListItem(
                            index = 2,
                            headlineText = stringResource(id = R.string.assistant_water),
                            trailingText = stringResource(
                                id = R.string.coffee_parameters_amount_with_unit,
                                brewUiState.waterAmount
                            )
                        )
                    }
                }
            }
        }
    }
}