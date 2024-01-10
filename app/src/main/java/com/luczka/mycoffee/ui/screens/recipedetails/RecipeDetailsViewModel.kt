package com.luczka.mycoffee.ui.screens.recipedetails

import androidx.lifecycle.ViewModel
import com.luczka.mycoffee.data.repositories.FirebaseRepository

class RecipeDetailsViewModel(
    val recipeId: String,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

}