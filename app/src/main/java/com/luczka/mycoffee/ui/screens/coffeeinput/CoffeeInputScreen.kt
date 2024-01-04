package com.luczka.mycoffee.ui.screens.coffeeinput

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.enums.Process
import com.luczka.mycoffee.enums.Roast
import com.luczka.mycoffee.ui.components.ClickableOutlinedTextField
import com.luczka.mycoffee.ui.components.CloseIconButton
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
    onSelectPhoto: (Uri?) -> Unit,
    onDeletePhoto: () -> Unit,
    onUpdateName: (String) -> Unit,
    onNameInputFinished: () -> Unit,
    onUpdateBrand: (String) -> Unit,
    onBrandInputFinished: () -> Unit,
    onUpdateAmount: (String) -> Unit,
    onUpdateScaScore: (String) -> Unit,
    onUpdateProcess: (Process?) -> Unit,
    onUpdateRoast: (Roast?) -> Unit,
    onUpdateRoastingDate: (LocalDate) -> Unit,
    onSave: (Context) -> Unit
) {
    var openDiscardDialog by rememberSaveable { mutableStateOf(false) }
    var openCalendarDialog by rememberSaveable { mutableStateOf(false) }

    val roastListState = rememberLazyListState()
    val processListState = rememberLazyListState()

    val coffeeRoasts = remember { mutableStateOf(Roast.values()) }
    val coffeeProcess = remember { mutableStateOf(Process.values()) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onSelectPhoto
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
            onNegative = { openDiscardDialog = false },
            onPositive = {
                openDiscardDialog = false
                navigateUp()
            }
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
            roastListState = roastListState,
            processListState = processListState,
            coffeeRoasts = coffeeRoasts,
            coffeeProcess = coffeeProcess,
            onAddPhoto = {
                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                singlePhotoPickerLauncher.launch(PickVisualMediaRequest(mediaType))
            },
            onDeletePhoto = onDeletePhoto,
            onUpdateName = onUpdateName,
            onNameInputFinished = onNameInputFinished,
            onUpdateBrand = onUpdateBrand,
            onBrandInputFinished = onBrandInputFinished,
            onUpdateAmount = onUpdateAmount,
            onUpdateScaScore = onUpdateScaScore,
            onUpdateProcess = onUpdateProcess,
            onUpdateRoast = onUpdateRoast,
            onShowCalendarDialog = { openCalendarDialog = true }
        )
    }
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
            TextButton(onClick = { saveCoffee(context) }) {
                Text(text = stringResource(id = R.string.dialog_action_save))
            }
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
    roastListState: LazyListState,
    processListState: LazyListState,
    coffeeRoasts: MutableState<Array<Roast>>,
    coffeeProcess: MutableState<Array<Process>>,
    onAddPhoto: () -> Unit,
    onDeletePhoto: () -> Unit,
    onUpdateName: (String) -> Unit,
    onNameInputFinished: () -> Unit,
    onUpdateBrand: (String) -> Unit,
    onBrandInputFinished: () -> Unit,
    onUpdateAmount: (String) -> Unit,
    onUpdateScaScore: (String) -> Unit,
    onUpdateProcess: (Process?) -> Unit,
    onUpdateRoast: (Roast?) -> Unit,
    onShowCalendarDialog: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ImageSelectionBox(
                    coffeeInputUiState = coffeeInputUiState,
                    coffeeUiState = coffeeInputUiState.coffeeUiState,
                    onAddCoffeePhoto = onAddPhoto,
                    onCoffeePhotoDeleted = onDeletePhoto
                )
            }
            item {
                RequiredOutlinedTextFieldWithErrorMessage(
                    value = coffeeInputUiState.coffeeUiState.name,
                    onValueChange = onUpdateName,
                    label = { Text(text = stringResource(id = R.string.input_required_text_field_label_name)) },
                    isError = coffeeInputUiState.isNameWrong,
                    onInputFinished = onNameInputFinished
                )
            }
            item {
                RequiredOutlinedTextFieldWithErrorMessage(
                    value = coffeeInputUiState.coffeeUiState.brand,
                    onValueChange = onUpdateBrand,
                    label = { Text(text = stringResource(id = R.string.input_required_text_field_label_brand)) },
                    isError = coffeeInputUiState.isBrandWrong,
                    onInputFinished = onBrandInputFinished
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilteredOutlinedTextField(
                        modifier = Modifier.weight(3f),
                        value = coffeeInputUiState.coffeeUiState.amount ?: "",
                        onValueChange = onUpdateAmount,
                        regex = Regex("([0-9]+([.][0-9]?)?|[.][0-9])"),
                        label = { Text(text = stringResource(id = R.string.coffee_parameters_amount)) },
                        placeholder = { Text(text = stringResource(id = R.string.input_text_field_placeholder_amount)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    // TODO: Fix regex
                    FilteredOutlinedTextField(
                        modifier = Modifier.weight(2f),
                        value = coffeeInputUiState.coffeeUiState.scaScore ?: "",
                        onValueChange = onUpdateScaScore,
                        regex = Regex("([0-9]+([.][0-9]?)?|[.][0-9])"),
                        label = { Text(text = stringResource(id = R.string.coffee_parameters_sca_score)) },
                        placeholder = { Text(text = stringResource(id = R.string.input_text_field_placeholder_amount)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
            item {
                CoffeeRoastDropdownMenu(
                    coffeeUiState = coffeeInputUiState.coffeeUiState,
                    roastListState = roastListState,
                    coffeeRoasts = coffeeRoasts,
                    updateCoffeeRoast = onUpdateRoast
                )
            }
            item {
                CoffeeProcessDropdownMenu(
                    coffeeUiState = coffeeInputUiState.coffeeUiState,
                    processListState = processListState,
                    coffeeProcess = coffeeProcess,
                    updateCoffeeProcess = onUpdateProcess
                )
            }
//            item {
//                CoffeeRoastingDateTextField(
//                    coffeeUiState = coffeeInputUiState.coffeeUiState,
//                    onShowCalendarDialog = onShowCalendarDialog
//                )
//            }
        }
    }
}

@Composable
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
            .padding(horizontal = 24.dp)
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
                modifier = Modifier.fillMaxSize(),
                model = model,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_photo_camera),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = onAddCoffeePhoto,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.input_assist_chip_select))
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = onCoffeePhotoDeleted,
                enabled = hasUri || hasImage,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null
                )
                Text(text = stringResource(id = R.string.input_assist_chip_delete))
            }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoffeeProcessDropdownMenu(
    coffeeUiState: CoffeeUiState,
    processListState: LazyListState,
    coffeeProcess: MutableState<Array<Process>>,
    updateCoffeeProcess: (Process?) -> Unit
) {
    Text(
        modifier = Modifier.padding(horizontal = 24.dp),
        text = stringResource(id = R.string.coffee_parameters_process),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
    LazyRow(
        state = processListState,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(coffeeProcess.value, { it.id }) { coffeeProcess ->
            val isSelected = coffeeUiState.process == coffeeProcess
            FilterChip(
                selected = isSelected,
                onClick = {
                    val selectedProcess = if (isSelected) null else coffeeProcess
                    updateCoffeeProcess(selectedProcess)
                },
                label = { Text(text = stringResource(id = coffeeProcess.stringResource)) },
                leadingIcon = {
                    Box(
                        modifier = Modifier.animateContentSize(
                            animationSpec = keyframes { durationMillis = 200 }
                        )
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoffeeRoastDropdownMenu(
    coffeeUiState: CoffeeUiState,
    roastListState: LazyListState,
    coffeeRoasts: MutableState<Array<Roast>>,
    updateCoffeeRoast: (Roast?) -> Unit
) {
    Text(
        modifier = Modifier.padding(horizontal = 24.dp),
        text = stringResource(id = R.string.coffee_parameters_roast),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
    LazyRow(
        state = roastListState,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(coffeeRoasts.value, { it.id }) { coffeeRoast ->
            val isSelected = coffeeUiState.roast == coffeeRoast
            FilterChip(
                selected = isSelected,
                onClick = {
                    val selectedRoast = if (isSelected) null else coffeeRoast
                    updateCoffeeRoast(selectedRoast)
                },
                label = { Text(text = stringResource(id = coffeeRoast.stringResource)) },
                leadingIcon = {
                    Box(
                        modifier = Modifier.animateContentSize(
                            animationSpec = keyframes { durationMillis = 200 }
                        )
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    }
                }
            )
        }
    }
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
            onSelectPhoto = {},
            onDeletePhoto = {},
            onUpdateName = {},
            onNameInputFinished = {},
            onUpdateBrand = {},
            onBrandInputFinished = {},
            onUpdateAmount = {},
            onUpdateScaScore = {},
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
            onSelectPhoto = {},
            onDeletePhoto = {},
            onUpdateName = {},
            onNameInputFinished = {},
            onUpdateBrand = {},
            onBrandInputFinished = {},
            onUpdateAmount = {},
            onUpdateScaScore = {},
            onUpdateProcess = {},
            onUpdateRoast = {},
            onUpdateRoastingDate = {},
            onSave = { _ -> }
        )
    }
}