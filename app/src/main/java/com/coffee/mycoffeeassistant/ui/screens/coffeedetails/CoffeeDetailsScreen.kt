package com.coffee.mycoffeeassistant.ui.screens.coffeedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CoffeeDetailsScreen(
    navController: NavController,
    coffeeId: Int,
    viewModel: CoffeeDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // TODO: Fix refreshing data
    val coffeeUiState = viewModel.coffeeUiState

    viewModel.getCoffeeUiState(coffeeId)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card {
                if (coffeeUiState.bitmap != null) {
                    AsyncImage(
                        model = coffeeUiState.bitmap,
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
        item { CoffeeDetailCard(detailName = "Name", detailValue = coffeeUiState.name) }
        item { CoffeeDetailCard(detailName = "Brand", detailValue = coffeeUiState.brand) }
        item {
            CoffeeDetailCard(
                detailName = "Amount",
                detailValue = coffeeUiState.currentAmount + " / " + coffeeUiState.startAmount
            )
        }
        item { CoffeeDetailCard(detailName = "Roast", detailValue = coffeeUiState.roast) }
        item { CoffeeDetailCard(detailName = "Process", detailValue = coffeeUiState.process) }
        item {
            CoffeeDetailCard(
                detailName = "Roasting date", detailValue = coffeeUiState.roastingDate.format(
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.MEDIUM
                    )
                )
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                if (coffeeUiState.isFavourite) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.updateIsFavourite()
                        }
                    ) { Text(text = "Remove from favourites") }
                } else {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.updateIsFavourite()
                        }
                    ) { Text(text = "Add to favourites") }
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    onClick = {
                        viewModel.deleteCoffee {
                            navController.navigateUp()
                        }
                    }
                ) { Text(text = "Delete") }
            }
        }
    }
}

@Composable
private fun CoffeeDetailCard(
    detailName: String,
    detailValue: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = detailName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = detailValue,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}