package com.coffee.mycoffeeassistant.ui.screens.addcoffee

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.AddCoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.markWrongFields
import com.coffee.mycoffeeassistant.ui.model.toCoffee
import com.coffee.mycoffeeassistant.ui.model.isValid
import com.coffee.mycoffeeassistant.util.BitmapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class AddCoffeeViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCoffeeUiState())

    val uiState: StateFlow<AddCoffeeUiState> = _uiState.asStateFlow()

    var coffeeUiState by mutableStateOf(CoffeeUiState())
        private set

    private fun updateUiState(newAddCoffeeUiState: AddCoffeeUiState) {
        _uiState.update { newAddCoffeeUiState.copy() }
    }

    fun updateCoffeeUiState(newCoffeeUiState: CoffeeUiState) {
        coffeeUiState = newCoffeeUiState.copy()
    }

    fun saveCoffee(onValid: () -> Unit) {
        viewModelScope.launch {
            updateUiState(coffeeUiState.markWrongFields())
            if (coffeeUiState.isValid()) {
                coffeeRepository.insertCoffee(coffeeUiState.toCoffee())
                onValid()
            }
        }
    }

    fun addImage(contentResolver: ContentResolver, uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val inputStream = contentResolver.openInputStream(uri)
                val outputStream = ByteArrayOutputStream()

                val buffer = ByteArray(4096)
                var bytesRead: Int

                while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                inputStream.close()
                outputStream.close()

                var image = outputStream.toByteArray()
                var bitmap = BitmapUtil.decodeByteArrayWithOrientation(image)

                bitmap = if (bitmap.width >= bitmap.height) {
                    val xOffset = bitmap.width / 2 - bitmap.height / 2
                    Bitmap.createBitmap(bitmap, xOffset, 0, bitmap.height, bitmap.height)
                } else {
                    val yOffset = bitmap.height / 2 - bitmap.width / 2
                    Bitmap.createBitmap(bitmap, 0, yOffset, bitmap.width, bitmap.width)
                }

                bitmap = Bitmap.createScaledBitmap(bitmap, 720, 720, true)

                image = ByteArrayOutputStream().apply {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, this)
                }.toByteArray()

                Log.d("AddCoffeeViewModel", "bitmap.width = ${bitmap.width}")
                Log.d("AddCoffeeViewModel", "bitmap.height = ${bitmap.height}")
                Log.d("AddCoffeeViewModel", "image.size = ${image.size} bytes")

                updateCoffeeUiState(
                    coffeeUiState.copy(
                        imageUri = uri,
                        image = image
                    )
                )
            }
        }
    }
}