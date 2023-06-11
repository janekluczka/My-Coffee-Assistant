package com.coffee.mycoffeeassistant.ui.screens.addcoffee

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.AppViewModelProvider
import com.coffee.mycoffeeassistant.ui.components.CustomExposedDropdownMenuWithBox
import com.coffee.mycoffeeassistant.ui.components.FilteredOutlinedTextField
import com.coffee.mycoffeeassistant.ui.components.ClickableOutlinedTextField
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
    navController: NavController,
    viewModel: AddCoffeeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current

    val addCoffeeUiState by viewModel.uiState.collectAsState()

    val coffeeUiState = viewModel.coffeeUiState

    var calendarOpened by remember { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { viewModel.addImage(contentResolver = context.contentResolver, uri = it) }
        }
    )

    if (calendarOpened) {
        CalendarDialog(
            state = rememberUseCaseState(
                visible = true,
                onCloseRequest = { calendarOpened = false },
                onFinishedRequest = { calendarOpened = false },
                onDismissRequest = { calendarOpened = false }
            ),
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH,
                boundary = LocalDate.now().minusYears(100)..LocalDate.now()
            ),
            selection = CalendarSelection.Date {
                viewModel.updateCoffeeUiState(coffeeUiState.copy(roastingDate = it))
                calendarOpened = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        if (coffeeUiState.image.isNotEmpty()) {
                            AsyncImage(
                                model = coffeeUiState.imageUri,
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
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        content = { Text(text = "Select photo") }
                    )
                }
            }
            OutlinedTextField(
                value = coffeeUiState.name,
                onValueChange = { viewModel.updateCoffeeUiState(coffeeUiState.copy(name = it)) },
                label = { Text("Name") },
                singleLine = true,
                isError = addCoffeeUiState.isNameWrong,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = coffeeUiState.brand,
                onValueChange = { viewModel.updateCoffeeUiState(coffeeUiState.copy(brand = it)) },
                label = { Text("Brand") },
                singleLine = true,
                isError = addCoffeeUiState.isBrandWrong,
                modifier = Modifier.fillMaxWidth()
            )
            FilteredOutlinedTextField(
                value = coffeeUiState.currentAmount,
                onValueChange = {
                    viewModel.updateCoffeeUiState(
                        coffeeUiState.copy(
                            currentAmount = it,
                            startAmount = it
                        )
                    )
                },
                regex = Regex("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)"),
                label = { Text("Amount") },
                singleLine = true,
                isError = addCoffeeUiState.isAmountWrong,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            CustomExposedDropdownMenuWithBox(
                value = coffeeUiState.roast,
                label = { Text("Roast") },
                menuItems = coffeeUiState.roastOptions,
                onItemSelected = {
                    viewModel.updateCoffeeUiState(coffeeUiState.copy(roast = it.toString()))
                }
            )
            CustomExposedDropdownMenuWithBox(
                value = coffeeUiState.process,
                label = { Text("Process") },
                menuItems = coffeeUiState.processOptions,
                onItemSelected = {
                    viewModel.updateCoffeeUiState(coffeeUiState.copy(process = it.toString()))
                }
            )
            ClickableOutlinedTextField(
                value = coffeeUiState.roastingDate.format(
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.MEDIUM
                    )
                ),
                label = { Text("Roasting date") },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_date_range),
                        contentDescription = null
                    )
                },
                readOnly = true,
                onClick = { calendarOpened = true }
            )
            Button(
                onClick = { viewModel.saveCoffee { navController.navigateUp() } },
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 24.dp)
                    .fillMaxWidth(),
                content = { Text(text = "Save") }
            )
        }
    }
}

@Preview
@Composable
fun AddCoffeeScreenPreview() {
    AddCoffeeScreen(rememberNavController())
}