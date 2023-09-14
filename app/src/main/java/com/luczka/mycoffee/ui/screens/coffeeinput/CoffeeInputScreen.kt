package com.luczka.mycoffee.ui.screens.coffeeinput

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.enums.Process
import com.luczka.mycoffee.enums.Roast
import com.luczka.mycoffee.enums.toDropdownMenuUiState
import com.luczka.mycoffee.ui.components.ClickableOutlinedTextField
import com.luczka.mycoffee.ui.components.CloseIconButton
import com.luczka.mycoffee.ui.components.CustomExposedDropdownMenu
import com.luczka.mycoffee.ui.components.FilteredOutlinedTextField
import com.luczka.mycoffee.ui.components.TopAppBarTitle
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeInputScreen(
    uiState: CoffeeInputUiState,
    navigateUp: () -> Unit,
    onPhotoSelected: (Uri?) -> Unit,
    onPhotoDeleted: () -> Unit,
    onUpdateName: (String) -> Unit,
    onNameInputFinished: () -> Unit,
    onUpdateBrand: (String) -> Unit,
    onBrandInputFinished: () -> Unit,
    onUpdateAmount: (String) -> Unit,
    onUpdateProcess: (Process?) -> Unit,
    onUpdateRoast: (Roast?) -> Unit,
    onUpdateRoastingDate: (LocalDate) -> Unit,
    onSave: (Context) -> Unit
) {
    var openDiscardDialog by rememberSaveable { mutableStateOf(false) }
    var openCalendarDialog by rememberSaveable { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onPhotoSelected
    )

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            navigateUp()
        }
    }

    BackHandler {
        onBackPressed(
            coffeeInputUiState = uiState,
            navigateUp = navigateUp,
            onShowDiscardDialog = { openDiscardDialog = true }
        )
    }

    if (openDiscardDialog) {
        DiscardDialog(
            navigateUp = navigateUp,
            onHideDialog = { openDiscardDialog = false }
        )
    }

    if (openCalendarDialog) {
        RoastingCalendarDialog(
            onHideDialog = { openCalendarDialog = false },
            updateCoffeeRoastingDate = onUpdateRoastingDate
        )
    }

    Scaffold(
        topBar = {
            AddCoffeeTopBar(
                coffeeInputUiState = uiState,
                navigateUp = navigateUp,
                saveCoffee = onSave,
                onShowDiscardDialog = { openDiscardDialog = true }
            )
        }
    ) { innerPadding ->
        AddCoffeeContent(
            innerPadding = innerPadding,
            coffeeInputUiState = uiState,
            onAddPhoto = {
                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                singlePhotoPickerLauncher.launch(PickVisualMediaRequest(mediaType))
            },
            onDeletePhoto = onPhotoDeleted,
            updateName = onUpdateName,
            onNameInputFinished = onNameInputFinished,
            updateBrand = onUpdateBrand,
            onBrandInputFinished = onBrandInputFinished,
            updateAmount = onUpdateAmount,
            updateProcess = onUpdateProcess,
            updateRoast = onUpdateRoast,
            onShowCalendarDialog = { openCalendarDialog = true }
        )
    }
}

@Composable
private fun DiscardDialog(
    navigateUp: () -> Unit,
    onHideDialog: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.dialog_title_discard)) },
        text = { Text(text = stringResource(id = R.string.dialog_text_discard)) },
        confirmButton = {
            TextButton(
                onClick = {
                    onHideDialog()
                    navigateUp()
                },
                content = { Text(text = stringResource(id = R.string.dialog_action_discard)) })
        },
        dismissButton = {
            TextButton(
                onClick = onHideDialog,
                content = { Text(text = stringResource(id = R.string.dialog_action_cancel)) }
            )
        },
        onDismissRequest = onHideDialog
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RoastingCalendarDialog(
    updateCoffeeRoastingDate: (LocalDate) -> Unit,
    onHideDialog: () -> Unit,
) {
    CalendarDialog(
        state = rememberUseCaseState(
            visible = true,
            onCloseRequest = { onHideDialog() },
            onFinishedRequest = { onHideDialog() },
            onDismissRequest = { onHideDialog() }
        ),
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            boundary = LocalDate.now().minusYears(100)..LocalDate.now()
        ),
        selection = CalendarSelection.Date {
            updateCoffeeRoastingDate(it)
            onHideDialog()
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddCoffeeTopBar(
    coffeeInputUiState: CoffeeInputUiState,
    navigateUp: () -> Unit,
    saveCoffee: (Context) -> Unit,
    onShowDiscardDialog: () -> Unit
) {
    val context = LocalContext.current
    val title = if (coffeeInputUiState.isEdit) {
        stringResource(id = R.string.input_top_bar_title_edit)
    } else {
        stringResource(id = R.string.input_top_bar_title_add)
    }
    TopAppBar(
        navigationIcon = {
            CloseIconButton(
                onClick = {
                    onBackPressed(
                        coffeeInputUiState = coffeeInputUiState,
                        navigateUp = navigateUp,
                        onShowDiscardDialog = onShowDiscardDialog
                    )
                }
            )
        },
        title = { TopAppBarTitle(text = title) },
        actions = {
            TextButton(
                onClick = { saveCoffee(context) },
                content = { Text(text = stringResource(id = R.string.dialog_action_save)) }
            )
        }
    )
}

private fun onBackPressed(
    coffeeInputUiState: CoffeeInputUiState,
    navigateUp: () -> Unit,
    onShowDiscardDialog: () -> Unit,
) {
    if (coffeeInputUiState.coffeeUiState.isBlank()) {
        navigateUp()
    } else {
        onShowDiscardDialog()
    }
}

@Composable
private fun AddCoffeeContent(
    innerPadding: PaddingValues,
    coffeeInputUiState: CoffeeInputUiState,
    onAddPhoto: () -> Unit,
    onDeletePhoto: () -> Unit,
    updateName: (String) -> Unit,
    onNameInputFinished: () -> Unit,
    updateBrand: (String) -> Unit,
    onBrandInputFinished: () -> Unit,
    updateAmount: (String) -> Unit,
    updateProcess: (Process?) -> Unit,
    updateRoast: (Roast?) -> Unit,
    onShowCalendarDialog: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ImageSelectionBox(
                    coffeeInputUiState = coffeeInputUiState,
                    coffeeUiState = coffeeInputUiState.coffeeUiState,
                    onAddCoffeePhoto = onAddPhoto,
                    onCoffeePhotoDeleted = onDeletePhoto
                )
            }
            item {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RequiredOutlinedTextFieldWithErrorMessage(
                        value = coffeeInputUiState.coffeeUiState.name,
                        onValueChange = updateName,
                        label = { Text(text = stringResource(id = R.string.input_required_text_field_label_name)) },
                        isError = coffeeInputUiState.isNameWrong,
                        onInputFinished = onNameInputFinished
                    )
                    RequiredOutlinedTextFieldWithErrorMessage(
                        value = coffeeInputUiState.coffeeUiState.brand,
                        onValueChange = updateBrand,
                        label = { Text(text = stringResource(id = R.string.input_required_text_field_label_brand)) },
                        isError = coffeeInputUiState.isBrandWrong,
                        onInputFinished = onBrandInputFinished
                    )
                    FilteredOutlinedTextField(
                        value = coffeeInputUiState.coffeeUiState.amount ?: "",
                        onValueChange = updateAmount,
                        regex = Regex("([0-9]+([.][0-9]?)?|[.][0-9])"),
                        label = { Text(text = stringResource(id = R.string.coffee_parameters_amount)) },
                        placeholder = { Text(text = stringResource(id = R.string.input_text_field_placeholder_amount)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    CoffeeRoastDropdownMenu(
                        coffeeUiState = coffeeInputUiState.coffeeUiState,
                        updateCoffeeRoast = updateRoast
                    )
                    CoffeeRoastingDateTextField(
                        coffeeUiState = coffeeInputUiState.coffeeUiState,
                        onShowCalendarDialog = onShowCalendarDialog
                    )
                    CoffeeProcessDropdownMenu(
                        coffeeUiState = coffeeInputUiState.coffeeUiState,
                        updateCoffeeProcess = updateProcess
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ImageSelectionBox(
    coffeeInputUiState: CoffeeInputUiState,
    coffeeUiState: CoffeeUiState,
    onAddCoffeePhoto: () -> Unit,
    onCoffeePhotoDeleted: () -> Unit
) {
    val context = LocalContext.current

    val hasUri = coffeeInputUiState.imageUri != null
    val hasImage = coffeeUiState.imageFile960x960 != null
    val imageToDelete = coffeeInputUiState.deleteImage

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f / 1f)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
        val model: Any? = when {
            hasUri -> coffeeInputUiState.imageUri
            hasImage && !imageToDelete -> ImageRequest.Builder(LocalContext.current)
                .data(coffeeUiState.imageFile960x960?.let { File(context.filesDir, it) })
                .build()

            else -> null
        }

        if (model != null) {
            AsyncImage(
                model = model,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
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
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(
                onClick = onAddCoffeePhoto,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = R.string.input_assist_chip_select)) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
            AssistChip(
                onClick = onCoffeePhotoDeleted,
                enabled = hasUri || hasImage,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = R.string.input_assist_chip_delete)) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
private fun RequiredOutlinedTextFieldWithErrorMessage(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    isError: Boolean,
    onInputFinished: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val supportingText = if (isError) {
        stringResource(id = R.string.input_required_text_field_supporting_text_error)
    } else {
        stringResource(id = R.string.input_required_text_field_supporting_text_neutral)
    }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        singleLine = true,
        isError = isError,
        supportingText = { Text(text = supportingText) },
        keyboardActions = KeyboardActions(
            onDone = {
                onInputFinished()
                keyboardController?.hide()
            },
        ),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CoffeeProcessDropdownMenu(
    coffeeUiState: CoffeeUiState,
    updateCoffeeProcess: (Process?) -> Unit
) {
    val menuItems = remember { Process.values().map { it.toDropdownMenuUiState() } }
    val processStringResource = coffeeUiState.process?.stringResource
    CustomExposedDropdownMenu(
        value = processStringResource?.let { stringResource(id = it) } ?: "",
        label = { Text(text = stringResource(id = R.string.coffee_parameters_process)) },
        menuItems = menuItems,
        onItemSelected = updateCoffeeProcess
    )
}

@Composable
private fun CoffeeRoastDropdownMenu(
    coffeeUiState: CoffeeUiState,
    updateCoffeeRoast: (Roast?) -> Unit
) {
    val menuItems = remember { Roast.values().map { it.toDropdownMenuUiState() } }
    val roastStringResource = coffeeUiState.roast?.stringResource
    CustomExposedDropdownMenu(
        value = roastStringResource?.let { stringResource(id = it) } ?: "",
        label = { Text(text = stringResource(id = R.string.coffee_parameters_roast)) },
        menuItems = menuItems,
        onItemSelected = updateCoffeeRoast
    )
}

@Composable
private fun CoffeeRoastingDateTextField(
    coffeeUiState: CoffeeUiState,
    onShowCalendarDialog: () -> Unit,
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
        onClick = onShowCalendarDialog
    )
}


@Preview(device = "id:pixel_6")
@Composable
fun AddCoffeeScreenLightThemePreview() {
    val coffeeInputUiState = CoffeeInputUiState()
    MyCoffeeTheme {
        CoffeeInputScreen(
            uiState = coffeeInputUiState,
            navigateUp = { },
            onPhotoSelected = {},
            onPhotoDeleted = {},
            onUpdateName = {},
            onNameInputFinished = {},
            onUpdateBrand = {},
            onBrandInputFinished = {},
            onUpdateAmount = {},
            onUpdateProcess = {},
            onUpdateRoast = {},
            onUpdateRoastingDate = {},
            onSave = { _ -> }
        )
    }
}

@Preview(device = "id:pixel_6")
@Composable
fun AddCoffeeScreenDarkThemePreview() {
    val coffeeInputUiState = CoffeeInputUiState()
    MyCoffeeTheme(darkTheme = true) {
        CoffeeInputScreen(
            uiState = coffeeInputUiState,
            navigateUp = { },
            onPhotoSelected = {},
            onPhotoDeleted = {},
            onUpdateName = {},
            onNameInputFinished = {},
            onUpdateBrand = {},
            onBrandInputFinished = {},
            onUpdateAmount = {},
            onUpdateProcess = {},
            onUpdateRoast = {},
            onUpdateRoastingDate = {},
            onSave = { _ -> }
        )
    }
}