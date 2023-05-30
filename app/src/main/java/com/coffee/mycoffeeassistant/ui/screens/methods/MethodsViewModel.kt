package com.coffee.mycoffeeassistant.ui.screens.methods

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.FirebaseRepository
import com.coffee.mycoffeeassistant.ui.model.MethodUiState
import kotlinx.coroutines.launch

class MethodsViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    var methodUiStateList by mutableStateOf(emptyList<MethodUiState>())
        private set

    fun getMethodUiStateList() {
        viewModelScope.launch {
            firebaseRepository.getMethods { methodList ->
                methodUiStateList = methodList.sortedBy { it.methodId }
            }
        }
    }

}