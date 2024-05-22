package com.luczka.mycoffee.ui.screens.coffeedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.BackIconButton
import com.luczka.mycoffee.ui.components.DeleteIconButton
import com.luczka.mycoffee.ui.components.EditIconButton
import com.luczka.mycoffee.ui.components.FavouriteToggleButton
import com.luczka.mycoffee.ui.components.TopAppBarTitle
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.util.isPositiveFloat
import java.io.File
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailsScreen(
    widthSizeClass: WindowWidthSizeClass,
    uiState: CoffeeDetailsUiState,
    navigateUp: () -> Unit,
    onUpdateFavourite: () -> Unit,
    onEdit: (Int) -> Unit,
    onDelete: (File) -> Unit,
) {
    when (uiState) {
        is CoffeeDetailsUiState.NoCoffee -> {
            LaunchedEffect(uiState.isDeleted) {
                if (uiState.isDeleted) {
                    navigateUp()
                }
            }
        }

        is CoffeeDetailsUiState.HasCoffee -> {
            val filesDir = LocalContext.current.filesDir

            var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

            if (openDeleteDialog) {
                DeleteCoffeeDialog(
                    coffeeUiState = uiState.coffee,
                    onNegative = {
                        openDeleteDialog = false
                    },
                    onPositive = {
                        openDeleteDialog = false
                        onDelete(filesDir)
                    }
                )
            }

            Scaffold(
                topBar = {
                    CoffeeDetailsTopBar(
                        navigateUp = navigateUp,
                        coffeeUiState = uiState.coffee,
                        updateIsFavourite = onUpdateFavourite,
                        navigateToEditCoffee = onEdit,
                        onShowDeleteDialog = { openDeleteDialog = true }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    Divider()
                    CoffeeDetailsList(coffeeUiState = uiState.coffee)
                }
            }
        }
    }
}

@Composable
private fun CoffeeDetailsList(coffeeUiState: CoffeeUiState) {
    val filesDir = LocalContext.current.filesDir

    LazyColumn(contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)) {
        item {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f / 1f)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (coffeeUiState.imageFile960x960 != null) {
                    val cacheFile = File(filesDir, coffeeUiState.imageFile960x960)
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
            val roastStringResource = coffeeUiState.roast.stringResource
            item {
                CoffeeDetailListItem(
                    detailName = stringResource(id = R.string.coffee_parameters_roast),
                    detailValue = stringResource(id = roastStringResource)
                )
            }
        }
        if (coffeeUiState.process != null) {
            val processStringResource = coffeeUiState.process.stringResource
            item {
                CoffeeDetailListItem(
                    detailName = stringResource(id = R.string.coffee_parameters_process),
                    detailValue = stringResource(id = processStringResource)
                )
            }
        }
        if (coffeeUiState.roastingDate != null) {
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            val date = coffeeUiState.roastingDate.format(formatter)!!
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
        title = { TopAppBarTitle(text = coffeeUiState.name) },
        actions = {
            FavouriteToggleButton(
                checked = coffeeUiState.isFavourite,
                onCheckedChange = updateIsFavourite
            )
            EditIconButton(onClick = { navigateToEditCoffee(coffeeUiState.coffeeId) })
            DeleteIconButton(onClick = onShowDeleteDialog)
        }
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

//@Preview
//@Composable
//fun CoffeeDetailsPreview() {
//    val coffeeUiState = CoffeeUiState(
//        name = "ethiopia sami",
//        brand = "monko.",
//        amount = "240.0",
//        roast = Roast.MediumDark,
//        roastingDate = LocalDate.now(),
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