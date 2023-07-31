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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.enums.getProcessStringResource
import com.coffee.mycoffeeassistant.enums.getRoastStringResource
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.screens.CoffeeDetailsUiState
import com.coffee.mycoffeeassistant.util.isPositiveFloat
import java.io.File
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailsScreen(
    coffeeDetailsUiState: CoffeeDetailsUiState,
    navigateUp: () -> Unit,
    updateCoffeeDetailsUiState: (CoffeeDetailsUiState) -> Unit,
    updateIsFavourite: () -> Unit,
    deleteCoffee: (File, () -> Unit) -> Unit,
) {
    val context = LocalContext.current

    val coffeeUiState = coffeeDetailsUiState.coffeeUiState

    if (coffeeDetailsUiState.openRemoveFromFavouritesDialog) {
        RemoveFromFavouritesDialog(
            coffeeUiState = coffeeUiState,
            updateCoffeeDetailsUiState = updateCoffeeDetailsUiState,
            coffeeDetailsUiState = coffeeDetailsUiState,
            deleteCoffee = deleteCoffee,
            navigateUp = navigateUp
        )
    }

    if (coffeeDetailsUiState.openDeleteDialog) {
        DeleteDialog(
            coffeeUiState = coffeeUiState,
            updateCoffeeDetailsUiState = updateCoffeeDetailsUiState,
            coffeeDetailsUiState = coffeeDetailsUiState,
            deleteCoffee = deleteCoffee,
            navigateUp = navigateUp
        )
    }

    Scaffold(
        topBar = {
            CoffeeDetailsTopBar(
                navigateUp,
                coffeeUiState,
                updateCoffeeDetailsUiState,
                coffeeDetailsUiState,
                updateIsFavourite
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Card {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f / 1f)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            val imageFile = coffeeUiState.imageFile960x960
                            if (imageFile != "") {
                                val cacheFile = File(context.filesDir, imageFile)
                                val model = ImageRequest.Builder(LocalContext.current)
                                    .data(cacheFile)
                                    .build()
                                AsyncImage(
                                    model = model,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
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
                item {
                    CoffeeDetailCard(
                        detailName = stringResource(id = R.string.coffee_parameters_name),
                        detailValue = coffeeUiState.name
                    )
                }
                item {
                    CoffeeDetailCard(
                        detailName = stringResource(id = R.string.coffee_parameters_brand),
                        detailValue = coffeeUiState.brand
                    )
                }
                if (coffeeUiState.amount.isPositiveFloat()) {
                    item {
                        CoffeeDetailCard(
                            detailName = stringResource(id = R.string.coffee_parameters_amount),
                            detailValue = coffeeUiState.amount + " " + stringResource(id = R.string.unit_gram_short)
                        )
                    }
                }
                if (coffeeUiState.roast != null) {
                    val roastStringResource = getRoastStringResource(id = coffeeUiState.roast!!)
                    item {
                        CoffeeDetailCard(
                            detailName = stringResource(id = R.string.coffee_parameters_roast),
                            detailValue = roastStringResource?.let { stringResource(id = it) } ?: ""
                        )
                    }
                }
                if (coffeeUiState.process != null) {
                    val processStringResource = getProcessStringResource(id = coffeeUiState.process!!)
                    item {
                        CoffeeDetailCard(
                            detailName = stringResource(id = R.string.coffee_parameters_process),
                            detailValue = processStringResource?.let { stringResource(id = it) } ?: ""
                        )
                    }
                }
                if (coffeeUiState.roastingDate != null) {
                    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    val date = coffeeUiState.roastingDate?.format(formatter)!!
                    item {
                        CoffeeDetailCard(
                            detailName = stringResource(id = R.string.coffee_parameters_roasting_date),
                            detailValue = date
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CoffeeDetailsTopBar(
    navigateUp: () -> Unit,
    coffeeUiState: CoffeeUiState,
    updateCoffeeDetailsUiState: (CoffeeDetailsUiState) -> Unit,
    coffeeDetailsUiState: CoffeeDetailsUiState,
    updateIsFavourite: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { navigateUp() },
                content = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            )
        },
        title = {
            Text(
                coffeeUiState.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconToggleButton(
                checked = coffeeUiState.isFavourite,
                onCheckedChange = {
                    if (coffeeUiState.isOnlyInFavourites()) {
                        updateCoffeeDetailsUiState(
                            coffeeDetailsUiState.copy(
                                openRemoveFromFavouritesDialog = true
                            )
                        )
                    } else {
                        updateIsFavourite()
                    }
                },
                content = {
                    if (coffeeUiState.isFavourite) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            )
            IconButton(
                onClick = { /*TODO: Add Edit screen*/ },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                }
            )
            IconButton(
                onClick = {
                    updateCoffeeDetailsUiState(
                        coffeeDetailsUiState.copy(
                            openDeleteDialog = true
                        )
                    )
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                }
            )
        }
    )
}

@Composable
private fun RemoveFromFavouritesDialog(
    coffeeUiState: CoffeeUiState,
    updateCoffeeDetailsUiState: (CoffeeDetailsUiState) -> Unit,
    coffeeDetailsUiState: CoffeeDetailsUiState,
    deleteCoffee: (File, () -> Unit) -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_remove)) },
        text = { Text(text = "Removing ${coffeeUiState.brand} ${coffeeUiState.name} from favourites will delete it as it is not in stock") },
        confirmButton = {
            TextButton(
                onClick = {
                    updateCoffeeDetailsUiState(
                        coffeeDetailsUiState.copy(
                            openRemoveFromFavouritesDialog = false
                        )
                    )
                    deleteCoffee(context.filesDir) {
                        navigateUp()
                    }
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_remove_and_delete)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                    updateCoffeeDetailsUiState(
                        coffeeDetailsUiState.copy(
                            openRemoveFromFavouritesDialog = false
                        )
                    )
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_cancel)) }
            )
        },
        onDismissRequest = {
            updateCoffeeDetailsUiState(
                coffeeDetailsUiState.copy(
                    openRemoveFromFavouritesDialog = false
                )
            )
        }
    )
}

@Composable
private fun DeleteDialog(
    coffeeUiState: CoffeeUiState,
    updateCoffeeDetailsUiState: (CoffeeDetailsUiState) -> Unit,
    coffeeDetailsUiState: CoffeeDetailsUiState,
    deleteCoffee: (File, () -> Unit) -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_delete)) },
        text = { Text(text = "${coffeeUiState.brand} ${coffeeUiState.name} will be deleted and gone forever") },
        confirmButton = {
            TextButton(
                onClick = {
                    updateCoffeeDetailsUiState(coffeeDetailsUiState.copy(openDeleteDialog = false))
                    deleteCoffee(context.filesDir) {
                        navigateUp()
                    }
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_delete)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = {
                    updateCoffeeDetailsUiState(
                        coffeeDetailsUiState.copy(
                            openDeleteDialog = false
                        )
                    )
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_cancel)) }
            )
        },
        onDismissRequest = {
            updateCoffeeDetailsUiState(
                coffeeDetailsUiState.copy(
                    openDeleteDialog = false
                )
            )
        }
    )
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