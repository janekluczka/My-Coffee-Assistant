package com.luczka.mycoffee.ui.screens.coffeedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.chips.MyCoffeeFilterChip
import com.luczka.mycoffee.ui.components.dialogs.DeleteCoffeeDialog
import com.luczka.mycoffee.ui.components.icons.ArrowBackIcon
import com.luczka.mycoffee.ui.components.icons.DeleteIcon
import com.luczka.mycoffee.ui.components.icons.EditIcon
import com.luczka.mycoffee.ui.components.icons.FavoriteIcon
import com.luczka.mycoffee.ui.components.icons.FavoriteOutlinedIcon
import java.io.File

@Composable
fun CoffeeDetailsScreen(
    widthSizeClass: WindowWidthSizeClass,
    uiState: CoffeeDetailsUiState,
    onAction: (CoffeeDetailsAction) -> Unit
) {
    var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            val action = CoffeeDetailsAction.NavigateUp
            onAction(action)
        }
    }

    if (openDeleteDialog) {
        when (uiState) {
            is CoffeeDetailsUiState.NoCoffee -> {

            }

            is CoffeeDetailsUiState.HasCoffee -> {
                DeleteCoffeeDialog(
                    coffeeUiState = uiState.coffee,
                    onNegative = {
                        openDeleteDialog = false
                    },
                    onPositive = {
                        openDeleteDialog = false
                        val action = CoffeeDetailsAction.OnDeleteClicked
                        onAction(action)
                    }
                )
            }
        }

    }

    Scaffold(
        topBar = {
            CoffeeDetailsTopBar(
                uiState = uiState,
                onAction = { action ->
                    when(action) {
                        CoffeeDetailsAction.ShowDeleteDialog -> openDeleteDialog = true
                        else -> {}
                    }
                    onAction(action)
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Divider()
            if (uiState is CoffeeDetailsUiState.HasCoffee) {
                CoffeeDetailsList(uiState)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CoffeeDetailsList(uiState: CoffeeDetailsUiState.HasCoffee) {
    val context = LocalContext.current

    LazyColumn(contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)) {
        item {
            val images = uiState.coffee.coffeeImages
            if (images.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    images.forEach { coffeeImageUiState ->
                        coffeeImageUiState.filename?.let { filename ->
                            item {
                                val imageFile = File(context.filesDir, filename)
                                val model = ImageRequest.Builder(context)
                                    .data(imageFile)
                                    .build()
                                AsyncImage(
                                    modifier = Modifier
                                        .height(256.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    model = model,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.height(160.dp),
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                ) {

                }
            }
        }
        item {
            CoffeeDetailListItem(
                supportingText = stringResource(id = R.string.coffee_parameters_name),
                headlineText = uiState.coffee.originOrName
            )
        }
        item {
            CoffeeDetailListItem(
                supportingText = stringResource(id = R.string.coffee_parameters_brand),
                headlineText = uiState.coffee.roasterOrBrand
            )
        }
        item {
            CoffeeDetailListItem(
                supportingText = stringResource(id = R.string.coffee_parameters_amount),
                headlineText = uiState.coffee.amount + " " + stringResource(id = R.string.unit_gram_short)
            )
        }
        item {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.coffee_parameters_roast),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy((-8).dp)
                ) {
                    uiState.roasts.forEach { roastUiState ->
                        val isSelected = uiState.coffee.roast == roastUiState
                        MyCoffeeFilterChip(
                            selected = isSelected,
                            onClick = {

                            },
                            label = {
                                Text(text = stringResource(id = roastUiState.stringRes))
                            },
                            enabled = false
                        )
                    }
                }
            }
        }
        item {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.coffee_parameters_process),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy((-8).dp)
                ) {
                    uiState.processes.forEach { processUiState ->
                        val isSelected = uiState.coffee.process == processUiState
                        MyCoffeeFilterChip(
                            selected = isSelected,
                            onClick = {

                            },
                            label = {
                                Text(text = stringResource(id = processUiState.stringRes))
                            },
                            enabled = false
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
    uiState: CoffeeDetailsUiState,
    onAction: (CoffeeDetailsAction) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    val action = CoffeeDetailsAction.NavigateUp
                    onAction(action)
                }
            ) {
                ArrowBackIcon()
            }
        },
        title = {
            when (uiState) {
                is CoffeeDetailsUiState.NoCoffee -> {

                }

                is CoffeeDetailsUiState.HasCoffee -> {
                    Text(
                        text = uiState.coffee.originOrName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        actions = {
            when (uiState) {
                is CoffeeDetailsUiState.NoCoffee -> {

                }

                is CoffeeDetailsUiState.HasCoffee -> {
                    IconButton(
                        onClick = {
                            val action = CoffeeDetailsAction.OnFavouriteClicked
                            onAction(action)
                        },
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.coffee.isFavourite) {
                            FavoriteIcon()
                        } else {
                            FavoriteOutlinedIcon()
                        }
                    }
                    IconButton(
                        onClick = {
                            val action = CoffeeDetailsAction.OnEditClicked(coffeeId = uiState.coffee.coffeeId)
                            onAction(action)
                        },
                        enabled = !uiState.isLoading
                    ) {
                        EditIcon()
                    }
                    IconButton(
                        onClick = {
                            val action = CoffeeDetailsAction.ShowDeleteDialog
                            onAction(action)
                        },
                        enabled = !uiState.isLoading
                    ) {
                        DeleteIcon()
                    }
                }
            }
        }
    )
}

@Composable
private fun CoffeeDetailListItem(
    supportingText: String,
    headlineText: String
) {
    ListItem(
        headlineContent = {
            Text(
                text = headlineText,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}

//@Preview
//@Composable
//fun CoffeeDetailsPreview() {
//    val coffeeUiState = CoffeeUiState(
//        name = "ethiopia sami",
//        brand = "monko.",
//        amount = "240.0",
//        roast = Roast.MediumDark,
//        process = Process.Natural
//    )
//    MyCoffeeTheme {
//        CoffeeDetailsScreen(
//            widthSizeClass = WindowWidthSizeClass.Compact,
//            coffeeUiState = coffeeUiState,
//            navigateUp = {},
//            onUpdateFavourite = {},
//            onEdit = {},
//            onDelete = { _ -> }
//        )
//    }
//}