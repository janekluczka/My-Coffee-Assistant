package com.coffee.mycoffeeassistant.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.components.UserGreeting
import com.coffee.mycoffeeassistant.ui.components.HomeSection

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn {
        item { UserGreeting() }
        item {
            HomeSection(
                navController = navController,
                sectionTitle = stringResource(id = R.string.section_title_low)
            )
        }
        item {
            HomeSection(
                navController = navController,
                sectionTitle = stringResource(id = R.string.section_title_old)
            )
        }
    }
}