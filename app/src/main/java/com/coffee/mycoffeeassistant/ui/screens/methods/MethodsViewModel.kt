package com.coffee.mycoffeeassistant.ui.screens.methods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.FirebaseRepository
import com.coffee.mycoffeeassistant.ui.model.screens.MethodsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MethodsViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(MethodsUiState())
    val uiState: StateFlow<MethodsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            firebaseRepository.getMethods { methodList ->
                val methodUiStateListSorted = methodList
                    .map { it.toMethodCardUiState() }
                    .sortedBy { it.title }
                _uiState.update { it.copy(methodsList = methodUiStateListSorted) }
            }
        }
    }

}