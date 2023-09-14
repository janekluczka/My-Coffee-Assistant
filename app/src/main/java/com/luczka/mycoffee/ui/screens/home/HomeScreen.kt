package com.luczka.mycoffee.ui.screens.home

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
import androidx.compose.ui.unit.dp
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.HomeSectionRow
import com.luczka.mycoffee.ui.components.TopAppBarTitle
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    navigateToAssistant: () -> Unit,
    navigate: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TopAppBarTitle(text = stringResource(R.string.app_name_short)) }
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
                onClick = navigateToAssistant
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
                    text = getUserGreeting(),
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
            if (uiState.lowAmountCoffees.isNotEmpty()) {
                item {
                    HomeSectionRow(
                        sectionTitle = stringResource(id = R.string.section_title_low),
                        sectionItems = uiState.lowAmountCoffees,
                        navigate = navigate
                    )
                }
            }
            if (uiState.oldCoffees.isNotEmpty()) {
                item {
                    HomeSectionRow(
                        sectionTitle = stringResource(id = R.string.section_title_old),
                        sectionItems = uiState.oldCoffees,
                        navigate = navigate
                    )
                }
            }
        }
    }
}

@Composable
fun getUserGreeting(): String {
    val currentTime = LocalTime.now(ZoneId.systemDefault())
    return when {
        currentTime.isBefore(LocalTime.of(4, 0)) -> "It's late. Get some sleep"
        currentTime.isBefore(LocalTime.of(12, 0)) -> "Good morning"
        currentTime.isBefore(LocalTime.of(17, 0)) -> "Good afternoon"
        else -> "Good evening"
    }
}