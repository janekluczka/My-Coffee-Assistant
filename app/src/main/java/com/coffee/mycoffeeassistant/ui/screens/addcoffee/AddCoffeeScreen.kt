package com.coffee.mycoffeeassistant.ui.screens.addcoffee

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.enums.getProcessStringResource
import com.coffee.mycoffeeassistant.enums.getRoastStringResource
import com.coffee.mycoffeeassistant.ui.components.ClickableOutlinedTextField
import com.coffee.mycoffeeassistant.ui.components.CustomExposedDropdownMenu
import com.coffee.mycoffeeassistant.ui.components.FilteredOutlinedTextField
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.screens.AddCoffeeUiState
import com.coffee.mycoffeeassistant.ui.theme.MyCoffeeAssistantTheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCoffeeScreen(
    addCoffeeUiState: AddCoffeeUiState,
    coffeeUiState: CoffeeUiState,
    navigateUp: () -> Unit,
    updateUiState: (AddCoffeeUiState) -> Unit,
    updateCoffeeUiState: (CoffeeUiState) -> Unit,
    saveCoffee: (Context, () -> Unit) -> Unit
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                updateUiState(addCoffeeUiState.copy(imageUri = it))
                updateCoffeeUiState(coffeeUiState.copy(hasImage = true))
            }
        })

    BackHandler {
        onBackPressed(
            coffeeUiState = coffeeUiState,
            addCoffeeUiState = addCoffeeUiState,
            navigateUp = navigateUp,
            updateUiState = updateUiState
        )
    }

    if (addCoffeeUiState.openDiscardDialog) {
        DiscardDialog(
            addCoffeeUiState = addCoffeeUiState,
            navigateUp = navigateUp,
            updateUiState = updateUiState
        )
    }

    if (addCoffeeUiState.openCalendarDialog) {
        RoastingCalendarDialog(
            addCoffeeUiState = addCoffeeUiState,
            coffeeUiState = coffeeUiState,
            updateUiState = updateUiState,
            updateCoffeeUiState = updateCoffeeUiState
        )
    }

    Scaffold(topBar = {
        AddCoffeeTopBar(
            addCoffeeUiState = addCoffeeUiState,
            coffeeUiState = coffeeUiState,
            navigateUp = navigateUp,
            updateUiState = updateUiState,
            saveCoffee = saveCoffee
        )
    }) { innerPadding ->
        AddCoffeeContent(
            innerPadding = innerPadding,
            singlePhotoPickerLauncher = singlePhotoPickerLauncher,
            addCoffeeUiState = addCoffeeUiState,
            coffeeUiState = coffeeUiState,
            updateUiState = updateUiState,
            updateCoffeeUiState = updateCoffeeUiState
        )
    }
}

@Composable
private fun DiscardDialog(
    addCoffeeUiState: AddCoffeeUiState,
    navigateUp: () -> Unit,
    updateUiState: (AddCoffeeUiState) -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_discard)) },
        text = { Text(text = stringResource(id = R.string.dialog_text_discard)) },
        confirmButton = {
            TextButton(
                onClick = {
                    updateUiState(addCoffeeUiState.copy(openDiscardDialog = false))
                    navigateUp()
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_discard)) })
        },
        dismissButton = {
            TextButton(
                onClick = { updateUiState(addCoffeeUiState.copy(openDiscardDialog = false)) },
                content = { Text(text = stringResource(id = R.string.dialog_action_cancel)) }
            )
        },
        onDismissRequest = {
            updateUiState(addCoffeeUiState.copy(openDiscardDialog = false))
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RoastingCalendarDialog(
    addCoffeeUiState: AddCoffeeUiState,
    coffeeUiState: CoffeeUiState,
    updateUiState: (AddCoffeeUiState) -> Unit,
    updateCoffeeUiState: (CoffeeUiState) -> Unit
) {
    CalendarDialog(
        state = rememberUseCaseState(
            visible = true,
            onCloseRequest = { updateUiState(addCoffeeUiState.copy(openCalendarDialog = false)) },
            onFinishedRequest = { updateUiState(addCoffeeUiState.copy(openCalendarDialog = false)) },
            onDismissRequest = { updateUiState(addCoffeeUiState.copy(openCalendarDialog = false)) }
        ),
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            boundary = LocalDate.now().minusYears(100)..LocalDate.now()
        ),
        selection = CalendarSelection.Date {
            updateCoffeeUiState(coffeeUiState.copy(roastingDate = it))
            updateUiState(addCoffeeUiState.copy(openCalendarDialog = false))
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddCoffeeTopBar(
    addCoffeeUiState: AddCoffeeUiState,
    coffeeUiState: CoffeeUiState,
    navigateUp: () -> Unit,
    updateUiState: (AddCoffeeUiState) -> Unit,
    saveCoffee: (Context, () -> Unit) -> Unit
) {
    val context = LocalContext.current
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackPressed(coffeeUiState, addCoffeeUiState, navigateUp, updateUiState)
                },
                content = { Icon(imageVector = Icons.Filled.Close, contentDescription = null) }
            )
        },
        title = { Text(text = "Add coffee", maxLines = 1, overflow = TextOverflow.Ellipsis) },
        actions = {
            TextButton(
                onClick = { saveCoffee(context) { navigateUp() } },
                content = { Text(text = "Save") }
            )
        }
    )
}

private fun onBackPressed(
    coffeeUiState: CoffeeUiState,
    addCoffeeUiState: AddCoffeeUiState,
    navigateUp: () -> Unit,
    updateUiState: (AddCoffeeUiState) -> Unit
) {
    if (coffeeUiState.isBlank()) {
        navigateUp()
    } else {
        updateUiState(addCoffeeUiState.copy(openDiscardDialog = true))
    }
}

@Composable
private fun AddCoffeeContent(
    innerPadding: PaddingValues,
    singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    addCoffeeUiState: AddCoffeeUiState,
    coffeeUiState: CoffeeUiState,
    updateUiState: (AddCoffeeUiState) -> Unit,
    updateCoffeeUiState: (CoffeeUiState) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (addCoffeeUiState.imageUri != null) {
                                AsyncImage(
                                    model = addCoffeeUiState.imageUri,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.background(
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                                    )
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
                        Button(
                            onClick = {
                                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                singlePhotoPickerLauncher.launch(PickVisualMediaRequest(mediaType))
                            },
                            content = { Text(text = "Select photo") }
                        )
                    }
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CoffeeNameTextField(coffeeUiState, addCoffeeUiState, updateCoffeeUiState)
                    CoffeeBrandTextField(coffeeUiState, addCoffeeUiState, updateCoffeeUiState)
                    CoffeeAmountTextField(coffeeUiState, addCoffeeUiState, updateCoffeeUiState)
                    CoffeeProcessDropdownMenu(coffeeUiState, addCoffeeUiState, updateCoffeeUiState)
                    CoffeeRoastDropdownMenu(coffeeUiState, addCoffeeUiState, updateCoffeeUiState)
                    CoffeeRoastingDateTextField(coffeeUiState, addCoffeeUiState, updateUiState)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CoffeeNameTextField(
    coffeeUiState: CoffeeUiState,
    addCoffeeUiState: AddCoffeeUiState,
    updateCoffeeUiState: (CoffeeUiState) -> Unit
) {
    OutlinedTextField(
        value = coffeeUiState.name,
        onValueChange = { updateCoffeeUiState(coffeeUiState.copy(name = it)) },
        label = { Text(text = stringResource(id = R.string.coffee_parameters_name)) },
        singleLine = true,
        isError = addCoffeeUiState.isNameWrong,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CoffeeBrandTextField(
    coffeeUiState: CoffeeUiState,
    addCoffeeUiState: AddCoffeeUiState,
    updateCoffeeUiState: (CoffeeUiState) -> Unit
) {
    OutlinedTextField(
        value = coffeeUiState.brand,
        onValueChange = { updateCoffeeUiState(coffeeUiState.copy(brand = it)) },
        label = { Text(text = stringResource(id = R.string.coffee_parameters_brand)) },
        singleLine = true,
        isError = addCoffeeUiState.isBrandWrong,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CoffeeAmountTextField(
    coffeeUiState: CoffeeUiState,
    addCoffeeUiState: AddCoffeeUiState,
    updateCoffeeUiState: (CoffeeUiState) -> Unit
) {
    FilteredOutlinedTextField(
        value = coffeeUiState.amount,
        onValueChange = { updateCoffeeUiState(coffeeUiState.copy(amount = it)) },
        regex = Regex("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)"),
        label = { Text(text = stringResource(id = R.string.coffee_parameters_amount)) },
        trailingIcon = { Text(text = stringResource(id = R.string.unit_gram_short)) },
        singleLine = true,
        isError = addCoffeeUiState.isAmountWrong,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun CoffeeProcessDropdownMenu(
    coffeeUiState: CoffeeUiState,
    addCoffeeUiState: AddCoffeeUiState,
    updateCoffeeUiState: (CoffeeUiState) -> Unit
) {
    val processStringResource = coffeeUiState.process?.let { getProcessStringResource(id = it) }
    CustomExposedDropdownMenu(
        value = processStringResource?.let { stringResource(id = it) } ?: "",
        label = { Text(text = stringResource(id = R.string.coffee_parameters_process)) },
        menuItems = addCoffeeUiState.processMenuUiStateList,
        onItemSelected = { updateCoffeeUiState(coffeeUiState.copy(process = it)) }
    )
}

@Composable
private fun CoffeeRoastDropdownMenu(
    coffeeUiState: CoffeeUiState,
    addCoffeeUiState: AddCoffeeUiState,
    updateCoffeeUiState: (CoffeeUiState) -> Unit
) {
    val roastStringResource = coffeeUiState.roast?.let { getRoastStringResource(id = it) }
    CustomExposedDropdownMenu(
        value = roastStringResource?.let { stringResource(id = it) } ?: "",
        label = { Text(text = stringResource(id = R.string.coffee_parameters_roast)) },
        menuItems = addCoffeeUiState.roastMenuUiStateList,
        onItemSelected = { updateCoffeeUiState(coffeeUiState.copy(roast = it)) }
    )
}

@Composable
private fun CoffeeRoastingDateTextField(
    coffeeUiState: CoffeeUiState,
    addCoffeeUiState: AddCoffeeUiState,
    updateUiState: (AddCoffeeUiState) -> Unit
) {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    val date = coffeeUiState.roastingDate?.format(formatter) ?: ""
    ClickableOutlinedTextField(
        value = date,
        label = { Text(text = stringResource(id = R.string.coffee_parameters_roasting_date)) },
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = { Icon(imageVector = Icons.Filled.DateRange, contentDescription = null) },
        readOnly = true,
        onClick = { updateUiState(addCoffeeUiState.copy(openCalendarDialog = true)) }
    )
}


@Preview(device = "id:pixel_6")
@Composable
fun AddCoffeeScreenLightThemePreview() {
    val addCoffeeUiState = AddCoffeeUiState()
    val coffeeUiState = CoffeeUiState()
    MyCoffeeAssistantTheme {
        AddCoffeeScreen(
            addCoffeeUiState = addCoffeeUiState,
            coffeeUiState = coffeeUiState,
            navigateUp = {},
            updateUiState = {},
            updateCoffeeUiState = {},
            saveCoffee = { _, _ -> }
        )
    }
}

@Preview(device = "id:pixel_6")
@Composable
fun AddCoffeeScreenDarkThemePreview() {
    val addCoffeeUiState = AddCoffeeUiState()
    val coffeeUiState = CoffeeUiState()
    MyCoffeeAssistantTheme(darkTheme = true) {
        AddCoffeeScreen(
            addCoffeeUiState = addCoffeeUiState,
            coffeeUiState = coffeeUiState,
            navigateUp = {},
            updateUiState = {},
            updateCoffeeUiState = {},
            saveCoffee = { _, _ -> }
        )
    }
}