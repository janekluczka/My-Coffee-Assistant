package com.luczka.mycoffee.ui.screens.coffeeinput

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.CoffeeRepository
import com.luczka.mycoffee.enums.Process
import com.luczka.mycoffee.enums.Roast
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.util.CustomBitmapFactory
import com.luczka.mycoffee.util.compressBitmap
import com.luczka.mycoffee.util.createScaledBitmap
import com.luczka.mycoffee.util.createSquareBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

data class CoffeeInputUiState(
    val isEdit: Boolean = false,
    val coffeeUiState: CoffeeUiState = CoffeeUiState(),
    val isNameWrong: Boolean = false,
    val isBrandWrong: Boolean = false,
    val imageUri: Uri? = null,
    val deleteImage: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class CoffeeInputViewModel(
    private val coffeeId: Int? = null,
    private val coffeeRepository: CoffeeRepository
) : ViewModel() {

    companion object {
        private val COMPRESSION_FORMAT = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.JPEG
        }
        private const val COMPRESSION_QUALITY = 90
    }

    private val _uiState = MutableStateFlow(CoffeeInputUiState())
    val uiState: StateFlow<CoffeeInputUiState> = _uiState.asStateFlow()

    init {
        if (coffeeId != null) {
            viewModelScope.launch {
                coffeeRepository.getCoffeeStream(id = coffeeId).filterNotNull().collect { coffee ->
                    _uiState.update {
                        it.copy(
                            isEdit = true,
                            coffeeUiState = coffee.toCoffeeUiState()
                        )
                    }
                }
            }
        }
    }

    fun updateImageUri(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun updateDeleteImage(deleteImage: Boolean) {
        _uiState.update { it.copy(deleteImage = deleteImage) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(name = name)) }
    }

    fun nameInputFinished() {
        _uiState.update { it.copy(isNameWrong = it.coffeeUiState.isNameWrong()) }
    }

    fun updateBrand(brand: String) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(brand = brand)) }
    }

    fun brandInputFinished() {
        _uiState.update { it.copy(isBrandWrong = it.coffeeUiState.isBrandWrong()) }
    }

    fun updateAmount(amount: String) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(amount = amount)) }
    }

    fun updateProcess(process: Process?) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(process = process)) }
    }

    fun updateRoast(roast: Roast?) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(roast = roast)) }
    }

    fun updateRoastingDate(date: LocalDate) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(roastingDate = date)) }
    }

    fun saveCoffee(context: Context) {
        if (_uiState.value.isSaving) return

        val isNameWrong = _uiState.value.coffeeUiState.isNameWrong()
        val isBrandWrong = _uiState.value.coffeeUiState.isBrandWrong()

        _uiState.update { it.copy(isNameWrong = isNameWrong, isBrandWrong = isBrandWrong) }

        if (isNameWrong || isBrandWrong) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            withContext(Dispatchers.IO) {
                deleteCurrentPhotos(context = context)
                saveImageInMultipleSizes(context = context)

                if (_uiState.value.isEdit) {
                    coffeeRepository.updateCoffee(coffee = _uiState.value.coffeeUiState.toCoffee())
                } else {
                    coffeeRepository.insertCoffee(coffee = _uiState.value.coffeeUiState.toCoffee())
                }
            }

            _uiState.update { it.copy(isSaved = true) }
        }
    }

    private fun deleteCurrentPhotos(context: Context) {
        val hasUri = _uiState.value.imageUri != null
        val deleteImage = _uiState.value.deleteImage

        if (!hasUri && !deleteImage) return

        val coffeeUiState = _uiState.value.coffeeUiState

        val imageFile240x240 = coffeeUiState.imageFile240x240
        val imageFile360x360 = coffeeUiState.imageFile360x360
        val imageFile480x480 = coffeeUiState.imageFile480x480
        val imageFile720x720 = coffeeUiState.imageFile720x720
        val imageFile960x960 = coffeeUiState.imageFile960x960

        val filesDir = context.filesDir
        val file240x240 = imageFile240x240?.let { File(filesDir, it) }
        val file360x360 = imageFile360x360?.let { File(filesDir, it) }
        val file480x480 = imageFile480x480?.let { File(filesDir, it) }
        val file720x720 = imageFile720x720?.let { File(filesDir, it) }
        val file960x960 = imageFile960x960?.let { File(filesDir, it) }

        file240x240?.delete()
        file360x360?.delete()
        file480x480?.delete()
        file720x720?.delete()
        file960x960?.delete()

        _uiState.update {
            it.copy(
                coffeeUiState = it.coffeeUiState.copy(
                    imageFile240x240 = null,
                    imageFile360x360 = null,
                    imageFile480x480 = null,
                    imageFile720x720 = null,
                    imageFile960x960 = null
                )
            )
        }
    }

    private fun saveImageInMultipleSizes(context: Context) {
        val uri = _uiState.value.imageUri ?: return

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        @Suppress("SpellCheckingInspection")
        val pattern = "yyyyMMdd_HHmmss"
        val locale = Locale.getDefault()
        val timeStamp = SimpleDateFormat(pattern, locale).format(calendar.time)
        val baseFileName = "MCA_IMG_$timeStamp"
        val inputStream = context.contentResolver.openInputStream(uri)

        inputStream?.use { input ->
            val bitmap = CustomBitmapFactory.decodeStreamWithOrientation(inputStream = input)
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

                val squareBitmap = bitmap.createSquareBitmap()

                val scaledBitmap = squareBitmap.createScaledBitmap(
                    width = width,
                    height = height,
                    filter = true
                )

                scaledBitmap.compressBitmap(
                    compressionFormat = COMPRESSION_FORMAT,
                    quality = COMPRESSION_QUALITY,
                    outputStream = outputStream
                )

                outputStream.close()

                // Get the size of the saved file and log it
                val file = File(context.filesDir, filename)
                val fileSizeBytes = file.length()
                val fileSizeKB = (fileSizeBytes / 1024.0 * 100).roundToInt() / 100 // Size in KB, rounded to two decimal places

                Log.d("File Size", "Saved file $filename size: $fileSizeKB KB")
            }
        }

        val imageFile240x240 = "${baseFileName}_240x240.jpeg"
        val imageFile360x360 = "${baseFileName}_360x360.jpeg"
        val imageFile480x480 = "${baseFileName}_480x480.jpeg"
        val imageFile720x720 = "${baseFileName}_720x720.jpeg"
        val imageFile960x960 = "${baseFileName}_960x960.jpeg"

        _uiState.update {
            it.copy(
                coffeeUiState = it.coffeeUiState.copy(
                    imageFile240x240 = imageFile240x240,
                    imageFile360x360 = imageFile360x360,
                    imageFile480x480 = imageFile480x480,
                    imageFile720x720 = imageFile720x720,
                    imageFile960x960 = imageFile960x960
                )
            )
        }

        inputStream?.close()
    }

}