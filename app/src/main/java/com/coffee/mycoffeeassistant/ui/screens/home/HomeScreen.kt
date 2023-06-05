package com.coffee.mycoffeeassistant.ui.screens.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.components.UserGreeting
import com.coffee.mycoffeeassistant.ui.components.HomeSectionRow

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    viewModel.getLowAmountCoffees()
    viewModel.getOldCoffees()
    LazyColumn {
        item { UserGreeting() }
        if(viewModel.lowAmountCoffeeUiStateList.isNotEmpty()) {
            item {
                HomeSectionRow(
                    navController = navController,
                    list = viewModel.lowAmountCoffeeUiStateList,
                    sectionTitle = stringResource(id = R.string.section_title_low)
                )
            }
        }
        if (viewModel.oldCoffeeUiStateList.isNotEmpty()) {
            item {
                HomeSectionRow(
                    navController = navController,
                    list = viewModel.oldCoffeeUiStateList,
                    sectionTitle = stringResource(id = R.string.section_title_old)
                )
            }
        }
    }
}