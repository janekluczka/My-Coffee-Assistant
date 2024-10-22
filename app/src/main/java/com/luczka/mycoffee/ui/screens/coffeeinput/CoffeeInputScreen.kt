package com.luczka.mycoffee.ui.screens.coffeeinput

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.buttons.MyCoffeeOutlinedButton
import com.luczka.mycoffee.ui.components.chips.MyCoffeeFilterChip
import com.luczka.mycoffee.ui.components.icons.CloseIcon
import com.luczka.mycoffee.ui.components.textfields.FilteredMyCoffeeOutlinedTextField
import com.luczka.mycoffee.ui.components.textfields.MyCoffeeOutlinedTextField
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeInputScreen(
    uiState: CoffeeInputUiState,
    onAction: (CoffeeInputAction) -> Unit,
) {
    var openDiscardDialog by rememberSaveable { mutableStateOf(false) }

    val roastListState = rememberLazyListState()
    val processListState = rememberLazyListState()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val action = CoffeeInputAction.OnImageUriChanged(uri)
            onAction(action)
        }
    )

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            val action = CoffeeInputAction.NavigateUp
            onAction(action)
        }
    }

    BackHandler {
        onBackPressed(
            coffeeInputUiState = uiState,
            navigateUp = {
                val action = CoffeeInputAction.NavigateUp
                onAction(action)
            },
            onShowDiscardDialog = { openDiscardDialog = true }
        )
    }

    if (openDiscardDialog) {
        CoffeeInputDiscardDialog(
            onNegative = {
                openDiscardDialog = false
            },
            onPositive = {
                openDiscardDialog = false
                val action = CoffeeInputAction.NavigateUp
                onAction(action)
            }
        )
    }

    Scaffold(
        topBar = {
            AddCoffeeTopBar(
                uiState = uiState,
                onAction = onAction,
                onShowDiscardDialog = { openDiscardDialog = true }
            )
        }
    ) { innerPadding ->
        AddCoffeeContent(
            innerPadding = innerPadding,
            uiState = uiState,
            roastListState = roastListState,
            processListState = processListState,
            onSelectPhoto = {
                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                singlePhotoPickerLauncher.launch(PickVisualMediaRequest(mediaType))
            },
            onAction = onAction,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddCoffeeTopBar(
    uiState: CoffeeInputUiState,
    onAction: (CoffeeInputAction) -> Unit,
    onShowDiscardDialog: () -> Unit
) {
    val title = if (uiState.isEdit) {
        stringResource(id = R.string.input_top_bar_title_edit)
    } else {
        stringResource(id = R.string.input_top_bar_title_add)
    }
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackPressed(
                        coffeeInputUiState = uiState,
                        navigateUp = {
                            val action = CoffeeInputAction.NavigateUp
                            onAction(action)
                        },
                        onShowDiscardDialog = onShowDiscardDialog
                    )
                }
            ) {
                CloseIcon()
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            TextButton(
                onClick = {
                    val action = CoffeeInputAction.OnSaveClicked
                    onAction(action)
                }
            ) {
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
    uiState: CoffeeInputUiState,
    roastListState: LazyListState,
    processListState: LazyListState,
    onAction: (CoffeeInputAction) -> Unit,
    onSelectPhoto: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Divider()
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ImageSelectionBox(
                    coffeeInputUiState = uiState,
                    coffeeUiState = uiState.coffeeUiState,
                    onAddCoffeePhoto = onSelectPhoto,
                    onCoffeePhotoDeleted = {
                        val action = CoffeeInputAction.OnDeleteImageClicked
                        onAction(action)
                    }
                )
            }
            item {
                RequiredOutlinedTextFieldWithErrorMessage(
                    value = uiState.coffeeUiState.name,
                    onValueChange = {
                        val action = CoffeeInputAction.OnNameValueChanged(it)
                        onAction(action)
                    },
                    label = { Text(text = stringResource(id = R.string.input_required_text_field_label_name)) },
                    isError = uiState.isNameWrong,
                    onInputFinished = {
                        val action = CoffeeInputAction.OnNameInputFinished
                        onAction(action)
                    }
                )
            }
            item {
                RequiredOutlinedTextFieldWithErrorMessage(
                    value = uiState.coffeeUiState.brand,
                    onValueChange = {
                        val action = CoffeeInputAction.OnBrandValueChanged(it)
                        onAction(action)
                    },
                    label = { Text(text = stringResource(id = R.string.input_required_text_field_label_brand)) },
                    isError = uiState.isBrandWrong,
                    onInputFinished = {
                        val action = CoffeeInputAction.OnBrandInputFinished
                        onAction(action)
                    }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilteredMyCoffeeOutlinedTextField(
                        modifier = Modifier.weight(3f),
                        value = uiState.coffeeUiState.amount ?: "",
                        onValueChange = {
                            val action = CoffeeInputAction.OnAmountValueChanged(it)
                            onAction(action)
                        },
                        regex = Regex("([0-9]+([.][0-9]?)?|[.][0-9])"),
                        label = { Text(text = stringResource(id = R.string.coffee_parameters_amount)) },
                        placeholder = { Text(text = stringResource(id = R.string.input_text_field_placeholder_amount)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    // TODO: Fix regex
                    FilteredMyCoffeeOutlinedTextField(
                        modifier = Modifier.weight(2f),
                        value = uiState.coffeeUiState.scaScore ?: "",
                        onValueChange = {
                            val action = CoffeeInputAction.OnScaScoreValueChanged(it)
                            onAction(action)
                        },
                        regex = Regex("([0-9]+([.][0-9]?)?|[.][0-9])"),
                        label = { Text(text = stringResource(id = R.string.coffee_parameters_sca_score)) },
                        placeholder = { Text(text = stringResource(id = R.string.input_text_field_placeholder_amount)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
            item {
                CoffeeRoastSelectionRow(
                    roastListState = roastListState,
                    uiState = uiState,
                    onAction = onAction
                )
            }
            item {
                CoffeeProcessSelectionRow(
                    processListState = processListState,
                    uiState = uiState,
                    onAction = onAction
                )
            }
        }
        Divider()
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
            MyCoffeeOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = onAddCoffeePhoto,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Text(text = stringResource(id = R.string.input_assist_chip_select))
            }
            MyCoffeeOutlinedButton(
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
                Text(text = stringResource(id = R.string.input_assist_chip_delete))
            }
        }
    }
}

@Composable
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
    MyCoffeeOutlinedTextField(
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
private fun CoffeeProcessSelectionRow(
    processListState: LazyListState,
    uiState: CoffeeInputUiState,
    onAction: (CoffeeInputAction) -> Unit
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
        items(
            items = uiState.processes,
            key = { it.name }
        ) { process ->
            val isSelected = uiState.coffeeUiState.process == process
            MyCoffeeFilterChip(
                selected = isSelected,
                onClick = {
                    val action = CoffeeInputAction.OnProcessClicked(process)
                    onAction(action)
                },
                label = {
                    Text(text = stringResource(id = process.stringRes))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoffeeRoastSelectionRow(
    roastListState: LazyListState,
    uiState: CoffeeInputUiState,
    onAction: (CoffeeInputAction) -> Unit
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
        items(
            items = uiState.roasts,
            key = { roast -> roast.name }
        ) { coffeeRoast ->
            val isSelected = uiState.coffeeUiState.roast == coffeeRoast
            MyCoffeeFilterChip(
                selected = isSelected,
                onClick = {
                    val action = CoffeeInputAction.OnRoastClicked(coffeeRoast)
                    onAction(action)
                },
                label = {
                    Text(text = stringResource(id = coffeeRoast.stringRes))
                }
            )
        }
    }
}


@Preview(device = "id:pixel_6")
@Composable
fun AddCoffeeScreenLightThemePreview() {
    val coffeeInputUiState = CoffeeInputUiState()
    MyCoffeeTheme {
        CoffeeInputScreen(
            uiState = coffeeInputUiState,
            onAction = {}
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
            onAction = {}
        )
    }
}