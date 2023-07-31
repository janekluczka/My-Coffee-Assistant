package com.coffee.mycoffeeassistant.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.components.HomeSectionRow
import com.coffee.mycoffeeassistant.ui.model.screens.HomeUiState
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    navigate: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name_short),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Brew assistant") },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_coffee_maker),
                        contentDescription = null
                    )
                },
                onClick = { navigate(Screen.BrewAssistant.route) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = homeUiState.userGreeting,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            item {
                Card(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .aspectRatio(3f / 2f)
                        .fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "placeholder")
                    }
                }
            }
            if (homeUiState.lowAmountCoffeeUiStateList.isNotEmpty()) {
                item {
                    HomeSectionRow(
                        sectionTitle = stringResource(id = R.string.section_title_low),
                        sectionItems = homeUiState.lowAmountCoffeeUiStateList,
                        navigate = navigate
                    )
                }
            }
            if (homeUiState.oldCoffeeUiStateList.isNotEmpty()) {
                item {
                    HomeSectionRow(
                        sectionTitle = stringResource(id = R.string.section_title_old),
                        sectionItems = homeUiState.oldCoffeeUiStateList,
                        navigate = navigate
                    )
                }
            }
        }
    }
}