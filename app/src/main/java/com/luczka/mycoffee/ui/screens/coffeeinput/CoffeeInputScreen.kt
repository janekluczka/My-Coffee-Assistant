package com.luczka.mycoffee.ui.screens.coffeeinput

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.chips.MyCoffeeFilterChip
import com.luczka.mycoffee.ui.components.chips.MyCoffeeInputChip
import com.luczka.mycoffee.ui.components.icons.AddIcon
import com.luczka.mycoffee.ui.components.icons.CloseIcon
import com.luczka.mycoffee.ui.components.icons.InfoIcon
import com.luczka.mycoffee.ui.components.icons.PhotoCameraIcon
import com.luczka.mycoffee.ui.components.icons.PhotoLibraryIcon
import com.luczka.mycoffee.ui.components.textfields.FilteredMyCoffeeOutlinedTextField
import com.luczka.mycoffee.ui.components.textfields.MyCoffeeOutlinedTextField
import com.luczka.mycoffee.ui.models.CoffeeImageUiState
import com.luczka.mycoffee.ui.screens.coffeeinput.dialogs.CoffeeInputDiscardDialog
import com.luczka.mycoffee.ui.screens.coffeeinput.dialogs.CoffeeInputScaInfoDialog
import com.luczka.mycoffee.ui.theme.MyCoffeeTheme
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeInputScreen(
    uiState: CoffeeInputUiState,
    onAction: (CoffeeInputAction) -> Unit,
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val sheetState = rememberModalBottomSheetState()

    val roastListState = rememberLazyListState()
    val processListState = rememberLazyListState()

    // TODO: Remove
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {

        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                photoUri = createImageFile(context)
                cameraLauncher.launch(photoUri)
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Camera permission denied")
                }
            }
        }
    )

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            val action = CoffeeInputAction.OnImagesSelected(uris)
            onAction(action)
        },
    )

    val photoPickedPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val allPermissionsGranted = permissions.all { it.value }
            if (allPermissionsGranted) {
                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                photoPickerLauncher.launch(PickVisualMediaRequest(mediaType))
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Photo picker permissions denied")
                }
            }
        }
    )

    BackHandler {
        val action = CoffeeInputAction.OnBackClicked
        onAction(action)
    }

    if (uiState.openDiscardDialog) {
        CoffeeInputDiscardDialog(
            onNegative = {
                val action = CoffeeInputAction.OnHideDiscardDialog
                onAction(action)
            },
            onPositive = {
                val action = CoffeeInputAction.NavigateUp
                onAction(action)
            }
        )
    }

    if (uiState.openScaInfoDialog) {
        CoffeeInputScaInfoDialog(
            onDismiss = {
                val action = CoffeeInputAction.OnHideScaInfoDialog
                onAction(action)
            }
        )
    }

    Scaffold(
        topBar = {
            AddCoffeeTopBar(
                uiState = uiState,
                onAction = onAction
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        if (uiState.openBottomSheet) {
            val bottomSystemBarHeight = with(LocalDensity.current) {
                WindowInsets.systemBars.getBottom(this).toDp()
            }

            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    val action = CoffeeInputAction.HideBottomSheet
                    onAction(action)
                },
                tonalElevation = 0.dp
            ) {
                Column(modifier = Modifier.padding(bottom = bottomSystemBarHeight)) {
                    ListItem(
                        modifier = Modifier.clickable {
                            val action = CoffeeInputAction.HideBottomSheet
                            onAction(action)

                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
                                photoUri = createImageFile(context)
                                cameraLauncher.launch(photoUri)
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        leadingContent = {
                            PhotoCameraIcon()
                        },
                        headlineContent = {
                            Text(text = "Take photo")
                        }
                    )
                    ListItem(
                        modifier = Modifier.clickable {
                            val action = CoffeeInputAction.HideBottomSheet
                            onAction(action)

                            val requiredPermissions = when {
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(
                                    READ_MEDIA_IMAGES,
                                    READ_MEDIA_VIDEO,
                                    READ_MEDIA_VISUAL_USER_SELECTED
                                )

                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
                                    READ_MEDIA_IMAGES,
                                    READ_MEDIA_VIDEO
                                )

                                else -> arrayOf(READ_EXTERNAL_STORAGE)
                            }

                            val neededPermissions = requiredPermissions.filter { permission ->
                                ContextCompat.checkSelfPermission(context, permission) != PERMISSION_GRANTED
                            }.toTypedArray()

                            if (neededPermissions.isEmpty()) {
                                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                photoPickerLauncher.launch(PickVisualMediaRequest(mediaType))
                            } else {
                                photoPickedPermissionsLauncher.launch(neededPermissions)
                            }
                        },
                        leadingContent = {
                            PhotoLibraryIcon()
                        },
                        headlineContent = {
                            Text(text = "Select photos")
                        }
                    )
                }
            }
        }
        AddCoffeeContent(
            innerPadding = innerPadding,
            uiState = uiState,
            roastListState = roastListState,
            processListState = processListState,
            onAction = onAction
        )
    }
}

fun createImageFile(context: Context): Uri? {
    val imageFile = File(context.filesDir, "temp_image.jpg")
    return FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", imageFile
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddCoffeeTopBar(
    uiState: CoffeeInputUiState,
    onAction: (CoffeeInputAction) -> Unit,
) {
    val title = if (uiState.isEdit) {
        stringResource(id = R.string.coffee_input_top_bar_title_edit)
    } else {
        stringResource(id = R.string.coffee_input_top_bar_title_add)
    }
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    val action = CoffeeInputAction.OnBackClicked
                    onAction(action)
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
                enabled = !uiState.isLoading,
                onClick = {
                    val action = CoffeeInputAction.OnSaveClicked
                    onAction(action)
                }
            ) {
                Text(text = stringResource(id = R.string.action_save))
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AddCoffeeContent(
    innerPadding: PaddingValues,
    uiState: CoffeeInputUiState,
    roastListState: LazyListState,
    processListState: LazyListState,
    onAction: (CoffeeInputAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current

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
                Text(
                    modifier = Modifier.padding(
                        start = 24.dp, end = 24.dp,
                        bottom = 8.dp
                    ),
                    text = "Images",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .height(160.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val coffeeImages = uiState.newOrUpdatedCoffeeUiState.coffeeImages
                    if (coffeeImages.isEmpty()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = RoundedCornerShape(12.dp),
                            onClick = {
                                val action = CoffeeInputAction.OnAddImageClicked
                                onAction(action)
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    space = 8.dp,
                                    alignment = Alignment.CenterHorizontally
                                )
                            ) {
                                AddIcon()
                                Text(
                                    text = "Add",
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        }
                    } else {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(coffeeImages) { index, coffeeImageUiState ->
                                val isFirstItem = index == 0
                                CoffeeInputImageCard(
                                    isFirstItem = isFirstItem,
                                    coffeeImageUiState = coffeeImageUiState,
                                )
                            }
                            item {
                                Surface(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .fillMaxHeight(),
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    shape = RoundedCornerShape(12.dp),
                                    onClick = {
                                        val action = CoffeeInputAction.OnAddImageClicked
                                        onAction(action)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(
                                            space = 8.dp,
                                            alignment = Alignment.CenterHorizontally
                                        )
                                    ) {
                                        AddIcon()
                                        Text(
                                            text = "Add more",
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                val supportingText = if (uiState.isRoasterOrBrandError) {
                    stringResource(id = R.string.error_text_field_can_not_be_empty)
                } else {
                    stringResource(id = R.string.supporting_text_required)
                }
                MyCoffeeOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = uiState.newOrUpdatedCoffeeUiState.roasterOrBrand,
                    onValueChange = {
                        val action = CoffeeInputAction.OnRoasterOrBrandValueChanged(it)
                        onAction(action)
                    },
                    label = {
                        Text(text = stringResource(id = R.string.label_text_roaster_brand_required))
                    },
                    isError = uiState.isRoasterOrBrandError,
                    supportingText = {
                        Text(text = supportingText)
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )
                )
            }
            item {
                val supportingText = if (uiState.isOriginOrNameError) {
                    stringResource(id = R.string.error_text_field_can_not_be_empty)
                } else {
                    stringResource(id = R.string.supporting_text_required)
                }
                MyCoffeeOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = uiState.newOrUpdatedCoffeeUiState.originOrName,
                    onValueChange = {
                        val action = CoffeeInputAction.OnOriginOrNameValueChanged(it)
                        onAction(action)
                    },
                    label = {
                        Text(text = stringResource(id = R.string.label_text_origin_name_required))
                    },
                    isError = uiState.isOriginOrNameError,
                    supportingText = {
                        Text(text = supportingText)
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(id = R.string.roast),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        val isSelected = uiState.newOrUpdatedCoffeeUiState.roast == coffeeRoast
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
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(id = R.string.process),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        val isSelected = uiState.newOrUpdatedCoffeeUiState.process == process
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
            item {
                // TODO: Add tasting notes
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "Tasting notes",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                FlowRow(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MyCoffeeInputChip(
                        selected = false,
                        onClick = {

                        },
                        leadingIcon = {
                            AddIcon()
                        },
                        label = {
                            Text("Add")
                        }
                    )
                }
            }
            item {
                MyCoffeeOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = uiState.newOrUpdatedCoffeeUiState.plantation ?: "",
                    onValueChange = {
                        val action = CoffeeInputAction.OnPlantationValueChanged(it)
                        onAction(action)
                    },
                    label = {
                        Text(text = "Plantation")
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )
                )
            }
            item {
                MyCoffeeOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = uiState.newOrUpdatedCoffeeUiState.altitude ?: "",
                    onValueChange = {
                        val action = CoffeeInputAction.OnAltitudeValueChanged(it)
                        onAction(action)
                    },
                    label = {
                        Text(text = "Altitude")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )
                )
            }
            item {
                // TODO: Fix regex
                FilteredMyCoffeeOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = uiState.newOrUpdatedCoffeeUiState.scaScore ?: "",
                    onValueChange = {
                        val action = CoffeeInputAction.OnScaScoreValueChanged(it)
                        onAction(action)
                    },
                    regex = Regex("([0-9]+([.][0-9]?)?|[.][0-9])"),
                    label = {
                        Text(text = stringResource(id = R.string.sca_score))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.placeholder_text_amount))
                    },
                    supportingText = {
                        Text(
                            text = "Value between 0.00 and 100.00",
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                val action = CoffeeInputAction.OnShowScaInfoDialog
                                onAction(action)
                            }
                        ) {
                            InfoIcon()
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )
                )
            }
            item {
                val additionalInformationLength = uiState.newOrUpdatedCoffeeUiState.additionalInformation?.length ?: 0
                val additionalInformationMaxLength = uiState.additionalInformationMaxLength
                MyCoffeeOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = uiState.newOrUpdatedCoffeeUiState.additionalInformation ?: "",
                    onValueChange = {
                        val action = CoffeeInputAction.OnAdditionalInformationValueChanged(it)
                        onAction(action)
                    },
                    label = {
                        Text(text = "Additional information")
                    },
                    supportingText = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "$additionalInformationLength/$additionalInformationMaxLength",
                            textAlign = TextAlign.End
                        )
                    },
                    isError = uiState.isAdditionalInformationError,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
            }
        }
        Divider()
    }
}

@Composable
private fun CoffeeInputImageCard(
    modifier: Modifier = Modifier,
    isFirstItem: Boolean,
    coffeeImageUiState: CoffeeImageUiState
) {
    val context = LocalContext.current

    val data = when {
        coffeeImageUiState.filename != null -> File(context.filesDir, coffeeImageUiState.filename)
        else -> coffeeImageUiState.uri
    }

    val model = ImageRequest.Builder(context)
        .data(data)
        .build()

    val border = if (isFirstItem) {
        BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
        )
    } else {
        BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        )
    }
    Surface(
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        border = border
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxHeight(),
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(device = "id:pixel_6")
@Composable
private fun AddCoffeeScreenLightThemePreview() {
    val coffeeInputUiState = CoffeeInputUiState()
    MyCoffeeTheme {
        CoffeeInputScreen(uiState = coffeeInputUiState, onAction = {})
    }
}

@Preview(device = "id:pixel_6")
@Composable
private fun AddCoffeeScreenDarkThemePreview() {
    val coffeeInputUiState = CoffeeInputUiState()
    MyCoffeeTheme(darkTheme = true) {
        CoffeeInputScreen(uiState = coffeeInputUiState, onAction = {})
    }
}