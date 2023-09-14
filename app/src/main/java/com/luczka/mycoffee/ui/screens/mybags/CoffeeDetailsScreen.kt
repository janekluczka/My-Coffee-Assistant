package com.luczka.mycoffee.ui.screens.mybags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.enums.Process
import com.luczka.mycoffee.enums.Roast
import com.luczka.mycoffee.ui.components.BackIconButton
import com.luczka.mycoffee.ui.components.DeleteIconButton
import com.luczka.mycoffee.ui.components.EditIconButton
import com.luczka.mycoffee.ui.components.FavouriteToggleButton
import com.luczka.mycoffee.ui.components.TopAppBarTitle
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import com.luczka.mycoffee.util.isPositiveFloat
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailsScreen(
    widthSizeClass: WindowWidthSizeClass,
    coffeeUiState: CoffeeUiState?,
    navigateUp: () -> Unit,
    onUpdateFavourite: () -> Unit,
    onEdit: (Int) -> Unit,
    onDelete: (File) -> Unit,
) {
    if (coffeeUiState == null) return

    key(coffeeUiState.id) {
        var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

        if (openDeleteDialog) {
            DeleteDialog(
                coffeeUiState = coffeeUiState,
                onHideDeleteDialog = { openDeleteDialog = false },
                deleteCoffee = onDelete
            )
        }

        when (widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                Scaffold(
                    topBar = {
                        CoffeeDetailsTopBar(
                            navigateUp = navigateUp,
                            coffeeUiState = coffeeUiState,
                            updateIsFavourite = onUpdateFavourite,
                            navigateToEditCoffee = onEdit,
                            onShowDeleteDialog = { openDeleteDialog = true }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        CoffeeDetailsList(coffeeUiState = coffeeUiState)
                    }
                }
            }

            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                    CoffeeDetailsList(coffeeUiState = coffeeUiState)
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        OutlinedCard(border = CardDefaults.outlinedCardBorder(enabled = false)) {
                            Surface {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    FavouriteToggleButton(
                                        checked = coffeeUiState.isFavourite,
                                        onCheckedChange = onUpdateFavourite
                                    )
                                    EditIconButton(onClick = { onEdit(coffeeUiState.id) })
                                    DeleteIconButton(onClick = { openDeleteDialog = true })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CoffeeDetailsList(coffeeUiState: CoffeeUiState) {
    val context = LocalContext.current
    LazyColumn {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 1f)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (coffeeUiState.imageFile960x960 != null) {
                    val cacheFile = coffeeUiState.imageFile960x960?.let {
                        File(context.filesDir, it)
                    }
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
        item {
            CoffeeDetailListItem(
                detailName = stringResource(id = R.string.coffee_parameters_name),
                detailValue = coffeeUiState.name
            )
        }
        item {
            CoffeeDetailListItem(
                detailName = stringResource(id = R.string.coffee_parameters_brand),
                detailValue = coffeeUiState.brand
            )
        }
        if (coffeeUiState.amount?.isPositiveFloat() == true) {
            item {
                CoffeeDetailListItem(
                    detailName = stringResource(id = R.string.coffee_parameters_amount),
                    detailValue = coffeeUiState.amount + " " + stringResource(id = R.string.unit_gram_short)
                )
            }
        }
        if (coffeeUiState.roast != null) {
            val roastStringResource = coffeeUiState.roast!!.stringResource
            item {
                CoffeeDetailListItem(
                    detailName = stringResource(id = R.string.coffee_parameters_roast),
                    detailValue = stringResource(id = roastStringResource)
                )
            }
        }
        if (coffeeUiState.process != null) {
            val processStringResource = coffeeUiState.process!!.stringResource
            item {
                CoffeeDetailListItem(
                    detailName = stringResource(id = R.string.coffee_parameters_process),
                    detailValue = stringResource(id = processStringResource)
                )
            }
        }
        if (coffeeUiState.roastingDate != null) {
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            val date = coffeeUiState.roastingDate?.format(formatter)!!
            item {
                CoffeeDetailListItem(
                    detailName = stringResource(id = R.string.coffee_parameters_roasting_date),
                    detailValue = date
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CoffeeDetailsTopBar(
    navigateUp: () -> Unit,
    coffeeUiState: CoffeeUiState,
    updateIsFavourite: () -> Unit,
    navigateToEditCoffee: (Int) -> Unit,
    onShowDeleteDialog: () -> Unit
) {
    TopAppBar(
        navigationIcon = { BackIconButton(onClick = navigateUp) },
        title = { TopAppBarTitle(text = "") },
        actions = {
            FavouriteToggleButton(
                checked = coffeeUiState.isFavourite,
                onCheckedChange = updateIsFavourite
            )
            EditIconButton(onClick = { navigateToEditCoffee(coffeeUiState.id) })
            DeleteIconButton(onClick = onShowDeleteDialog)
        }
    )
}

@Composable
private fun DeleteDialog(
    coffeeUiState: CoffeeUiState,
    onHideDeleteDialog: () -> Unit,
    deleteCoffee: (File) -> Unit,
) {
    val context = LocalContext.current
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_delete)) },
        text = {
            Text(
                text = stringResource(
                    id = R.string.dialog_text_delete,
                    coffeeUiState.brand,
                    coffeeUiState.name
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onHideDeleteDialog()
                    deleteCoffee(context.filesDir)
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_delete)) }
            )
        },
        dismissButton = {
            TextButton(
                onClick = onHideDeleteDialog,
                content = { Text(text = stringResource(id = R.string.dialog_action_cancel)) }
            )
        },
        onDismissRequest = onHideDeleteDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoffeeDetailListItem(
    detailName: String,
    detailValue: String
) {
    ListItem(
        headlineText = { Text(text = detailValue, style = MaterialTheme.typography.titleMedium) },
        supportingText = { Text(text = detailName, style = MaterialTheme.typography.bodyMedium) }
    )
}

@Preview
@Composable
private fun CoffeeDetailListItemPreview() {
    CoffeeDetailListItem(detailName = "Name", detailValue = "Ethiopia")
}

@Preview
@Composable
fun CoffeeDetailsPreview() {
    val coffeeUiState = CoffeeUiState(
        name = "ethiopia sami",
        brand = "monko.",
        amount = "240.0",
        roast = Roast.MediumDark,
        roastingDate = LocalDate.now(),
        process = Process.Natural
    )
    MyCoffeeTheme {
        CoffeeDetailsScreen(
            widthSizeClass = WindowWidthSizeClass.Compact,
            coffeeUiState = coffeeUiState,
            navigateUp = {},
            onUpdateFavourite = {},
            onEdit = {},
            onDelete = { _ -> }
        )
    }
}