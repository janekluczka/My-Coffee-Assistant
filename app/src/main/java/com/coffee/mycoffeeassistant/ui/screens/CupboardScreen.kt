@file:OptIn(ExperimentalMaterial3Api::class)

package com.coffee.mycoffeeassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.components.CoffeeCardVertical
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@Composable
fun CupboardScreen(navController: NavController) {
    var state by remember { mutableStateOf(0) }
    val titles = listOf(
        stringResource(id = R.string.tab_cupboard_my_stock),
        stringResource(id = R.string.tab_cupboard_my_favourites)
    )
    Column {
        TabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                )
            }
        }
        when (state) {
            0 -> LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(24.dp)
            ) {
                items(10) { index ->
//                    Card(
//                        onClick = {
//                            navController.navigate(Screen.CoffeeDetails.route + "/$index")
//                        },
//                        modifier = Modifier
//                            .height(height = 200.dp)
//                            .fillMaxWidth()
//                    ) {
//                        Box(Modifier.fillMaxSize()) {
//                            Text("Coffee $index", Modifier.align(Alignment.Center))
//                        }
//                    }
                    CoffeeCardVertical(
                        navController = navController,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        index = index
                    )
                }
            }
            1 -> LazyVerticalGrid(
                columns = GridCells.Adaptive(250.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(24.dp)
            ) {
                items(5) { index ->
                    Card(
                        onClick = {
                            navController.navigate(Screen.CoffeeDetails.route + "/$index")
                        },
                        modifier = Modifier
                            .height(height = 100.dp)
                            .fillMaxWidth()

                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Text("Coffee", Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}