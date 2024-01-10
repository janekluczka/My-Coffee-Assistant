package com.luczka.mycoffee.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    widthSizeClass: WindowWidthSizeClass,
    uiState: HomeUiState,
    navigateToAssistant: () -> Unit,
    navigate: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            when (widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    CenterAlignedTopAppBar(
                        title = { TopAppBarTitle(text = stringResource(R.string.app_name_short)) }
                    )
                }
            }
        }
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
                OutlinedCard(
                    onClick = { },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
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
                                onClick = navigateToAssistant
                            ) {
                                Text(text = "Use assistant")
                            }
                        }
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
        currentTime.isBefore(LocalTime.of(6, 0)) -> "It's late. Get some sleep"
        currentTime.isBefore(LocalTime.of(12, 0)) -> "Good morning"
        currentTime.isBefore(LocalTime.of(17, 0)) -> "Good afternoon"
        currentTime.isBefore(LocalTime.of(22, 0)) -> "It's late. Get some sleep"
        else -> "Good evening"
    }
}