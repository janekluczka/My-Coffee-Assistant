package com.coffee.mycoffeeassistant.ui.screens.addcoffee

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.screens.AddCoffeeUiState
import com.coffee.mycoffeeassistant.util.createSquareBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class AddCoffeeViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCoffeeUiState())
    val uiState: StateFlow<AddCoffeeUiState> = _uiState.asStateFlow()

    var coffeeUiState by mutableStateOf(CoffeeUiState())
        private set

    fun updateCoffeeUiState(newCoffeeUiState: CoffeeUiState) {
        coffeeUiState = newCoffeeUiState.copy()
    }

    fun updateUiState(newAddCoffeeUiState: AddCoffeeUiState) {
        _uiState.update { newAddCoffeeUiState.copy() }
    }

    fun saveCoffee(context: Context, navigateUp: () -> Unit) {
        val isNameWrong = coffeeUiState.isNameWrong()
        val isBrandWrong = coffeeUiState.isBrandWrong()
        val isAmountWrong = coffeeUiState.isAmountWrong()

        _uiState.update {
            it.copy(
                isNameWrong = isNameWrong,
                isBrandWrong = isBrandWrong,
                isAmountWrong = isAmountWrong
            )
        }

        val isCoffeeValid = !(isNameWrong || isBrandWrong || isAmountWrong)
        if (isCoffeeValid) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    saveImageInMultipleSizes(context)
                    coffeeRepository.insertCoffee(coffeeUiState.toCoffee())
                }
                navigateUp()
            }
        }
    }

    private fun saveImageInMultipleSizes(context: Context) {
        val uri = _uiState.value.imageUri ?: return

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        @Suppress("SpellCheckingInspection")
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(calendar.time)
        val baseFileName = "MCA_IMG_$timeStamp"
        val inputStream = context.contentResolver.openInputStream(uri)

        inputStream?.use { input ->
            val bitmap = BitmapFactory.decodeStream(input)
            val squareBitmap = bitmap.createSquareBitmap()
            bitmap.recycle()

            val imageSizes = listOf(
                Pair(240, 240), // MDPI
                Pair(360, 360), // HDPI
                Pair(480, 480), // XHDPI
                Pair(720, 720), // XXHDPI
                Pair(960, 960)  // XXXHDPI
            )

            for ((width, height) in imageSizes) {
                val filename = "${baseFileName}_${width}x${height}.jpeg"
                val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)

                val compressionFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    Bitmap.CompressFormat.JPEG
                }
                val compressionQuality = 90

                val scaledBitmap = Bitmap.createScaledBitmap(squareBitmap, width, height, true)
                scaledBitmap.compress(compressionFormat, compressionQuality, outputStream)
                scaledBitmap.recycle()

                outputStream.close()

                // Get the size of the saved file and log it
                val file = File(context.filesDir, filename)
                val fileSizeBytes = file.length()
                val fileSizeKB =
                    (fileSizeBytes / 1024.0 * 100).roundToInt() / 100 // Size in KB, rounded to two decimal places

                Log.d("File Size", "Saved file $filename size: $fileSizeKB KB")
            }

            squareBitmap.recycle()
        }

        val imageFile240x240 = "${baseFileName}_240x240.jpeg"
        val imageFile360x360 = "${baseFileName}_360x360.jpeg"
        val imageFile480x480 = "${baseFileName}_480x480.jpeg"
        val imageFile720x720 = "${baseFileName}_720x720.jpeg"
        val imageFile960x960 = "${baseFileName}_960x960.jpeg"

        updateCoffeeUiState(
            coffeeUiState.copy(
                hasImage = true,
                imageFile240x240 = imageFile240x240,
                imageFile360x360 = imageFile360x360,
                imageFile480x480 = imageFile480x480,
                imageFile720x720 = imageFile720x720,
                imageFile960x960 = imageFile960x960
            )
        )

        inputStream?.close()
    }

}