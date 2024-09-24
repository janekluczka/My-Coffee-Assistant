package com.luczka.mycoffee.ui.screens.coffeeinput

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.models.Process
import com.luczka.mycoffee.domain.models.Roast
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.util.CustomBitmapFactory
import com.luczka.mycoffee.util.compressBitmap
import com.luczka.mycoffee.util.createScaledBitmap
import com.luczka.mycoffee.util.createSquareBitmap
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

data class CoffeeInputUiState(
    val roasts: List<Roast> = Roast.values().toList(),
    val processes: List<Process> = Process.values().toList(),
    val isEdit: Boolean = false,
    val coffeeUiState: CoffeeUiState = CoffeeUiState(),
    val isNameWrong: Boolean = false,
    val isBrandWrong: Boolean = false,
    val imageUri: Uri? = null,
    val deleteImage: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

@AssistedFactory
interface CoffeeInputViewModelFactory {
    fun create(coffeeId: Int?): CoffeeInputViewModel
}

@HiltViewModel(assistedFactory = CoffeeInputViewModelFactory::class)
class CoffeeInputViewModel @AssistedInject constructor(
    @Assisted private val coffeeId: Int? = null,
    @ApplicationContext private val context: Context,
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
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
            // TODO: This does not have to be Flow
            viewModelScope.launch {
                myCoffeeDatabaseRepository.getCoffeeStream(coffeeId).filterNotNull().collect { coffee ->
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

    fun onAction(action: CoffeeInputAction) {
        when (action) {
            is CoffeeInputAction.OnImageUriChanged -> updateImageUri(action.uri)
            CoffeeInputAction.OnDeleteImageClicked -> deleteImage()
            is CoffeeInputAction.OnNameValueChanged -> updateName(action.name)
            CoffeeInputAction.OnNameInputFinished -> nameInputFinished()
            is CoffeeInputAction.OnBrandValueChanged -> updateBrand(action.brand)
            CoffeeInputAction.OnBrandInputFinished -> brandInputFinished()
            is CoffeeInputAction.OnAmountValueChanged -> updateAmount(action.amount)
            is CoffeeInputAction.OnScaScoreValueChanged -> updateScaScore(action.scaScore)
            is CoffeeInputAction.OnProcessChanged -> updateProcess(action.process)
            is CoffeeInputAction.OnRoastChanged -> updateRoast(action.roast)
            CoffeeInputAction.OnSaveClicked -> saveCoffee()
            else -> {

            }
        }
    }

    private fun updateImageUri(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    private fun deleteImage() {
        _uiState.update {
            it.copy(
                imageUri = null,
                deleteImage = true
            )
        }
    }

    private fun updateName(name: String) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(name = name)) }
    }

    private fun nameInputFinished() {
        _uiState.update { it.copy(isNameWrong = it.coffeeUiState.isNameWrong()) }
    }

    private fun updateBrand(brand: String) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(brand = brand)) }
    }

    private fun brandInputFinished() {
        _uiState.update { it.copy(isBrandWrong = it.coffeeUiState.isBrandWrong()) }
    }

    private fun updateAmount(amount: String) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(amount = amount)) }
    }

    private fun updateScaScore(scaScore: String) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(scaScore = scaScore)) }
    }

    private fun updateProcess(process: Process?) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(process = process)) }
    }

    private fun updateRoast(roast: Roast?) {
        _uiState.update { it.copy(coffeeUiState = it.coffeeUiState.copy(roast = roast)) }
    }

    private fun saveCoffee() {
        if (_uiState.value.isSaving) return

        val isNameWrong = _uiState.value.coffeeUiState.isNameWrong()
        val isBrandWrong = _uiState.value.coffeeUiState.isBrandWrong()

        _uiState.update { it.copy(isNameWrong = isNameWrong, isBrandWrong = isBrandWrong) }

        if (isNameWrong || isBrandWrong) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            withContext(Dispatchers.IO) {
                deleteCurrentPhotos()
                saveImageInMultipleSizes()

                if (_uiState.value.isEdit) {
                    myCoffeeDatabaseRepository.updateCoffee(coffeeEntity = _uiState.value.coffeeUiState.toCoffee())
                } else {
                    myCoffeeDatabaseRepository.insertCoffee(coffeeEntity = _uiState.value.coffeeUiState.toCoffee())
                }
            }

            _uiState.update { it.copy(isSaved = true) }
        }
    }

    private fun deleteCurrentPhotos() {
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

    private fun saveImageInMultipleSizes() {
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