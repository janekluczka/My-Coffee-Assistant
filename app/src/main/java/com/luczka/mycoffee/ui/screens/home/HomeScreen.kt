package com.luczka.mycoffee.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.cards.BrewCard
import com.luczka.mycoffee.ui.components.cards.CoffeeCard
import com.luczka.mycoffee.ui.components.icons.MenuIcon
import com.luczka.mycoffee.ui.models.BrewUiState
import com.luczka.mycoffee.ui.models.BrewedCoffeeUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    widthSizeClass: WindowWidthSizeClass,
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit,
) {
    val configuration = LocalConfiguration.current

    val horizontalPadding = 16.dp

    val screenWidth = configuration.screenWidthDp

    val homeListState = rememberLazyListState()

    val showDivider by remember {
        derivedStateOf { homeListState.firstVisibleItemScrollOffset != 0 }
    }
    
    Scaffold(
        topBar = {
            when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    val action = HomeAction.OnMenuClicked
                                    onAction(action)
                                }
                            ) {
                                MenuIcon()
                            }
                        },
                        title = {
                            Icon(
                                modifier = Modifier.height(24.dp),
                                painter = painterResource(R.drawable.ic_logo_my_coffee_assistant),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Divider(color = if (showDivider) MaterialTheme.colorScheme.outlineVariant else Color.Transparent)
            LazyColumn(
                state = homeListState,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
//            item {
//                Text(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    text = getUserGreeting(),
//                    style = MaterialTheme.typography.headlineLarge
//                )
//            }
                item {
                    OutlinedCard(
                        onClick = { },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Brew coffee with assistant",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        val action = HomeAction.NavigateToAssistant
                                        onAction(action)
                                    }
                                ) {
                                    Text(text = "Use assistant")
                                }
                            }
                        }
                    }
                }
                if (uiState.recentBrews.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Text(
                                    text = "Recent brews",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.recentBrews) { brewUiState ->
                                    BrewCard(
                                        modifier = Modifier.widthIn(min = 0.dp, max = screenWidth.dp - 2 * horizontalPadding),
                                        brewUiState = brewUiState,
                                        onClick = {
                                            val action = HomeAction.NavigateToBrewDetails(brewUiState.brewId)
                                            onAction(action)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                if (uiState.recentlyAddedCoffees.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Text(
                                    text = "Recently added coffees",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.recentlyAddedCoffees) { coffeeUiState ->
                                    CoffeeCard(
                                        modifier = Modifier.width(200.dp),
                                        coffeeUiState = coffeeUiState,
                                        onClick = {
                                            val action = HomeAction.NavigateToCoffeeDetails(coffeeUiState.coffeeId)
                                            onAction(action)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                // Coffees - recent, low amount??
                // Equipment maintanance?
                // Recipes?
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        widthSizeClass = WindowWidthSizeClass.Compact,
        uiState = HomeUiState.HasCoffees(
            recentBrews = listOf(
                BrewUiState(
                    addedOn = LocalDate.now(),
                    coffeeAmount = 18.0f,
                    coffeeRatio = 1,
                    waterRatio = 2,
                    waterAmount = 36.0f,
                    rating = null,
                    notes = "",
                    brewedCoffees = listOf(
                        BrewedCoffeeUiState(
                            coffeeAmount = 18.0f,
                            coffee = CoffeeUiState(
                                roasterOrBrand = "Brand",
                                originOrName = "Name"
                            )
                        )
                    )
                )
            ),
            recentlyAddedCoffees = listOf(
                CoffeeUiState(
                    roasterOrBrand = "Brand",
                    originOrName = "Name"
                )
            )
        ),
        onAction = {}
    )
}