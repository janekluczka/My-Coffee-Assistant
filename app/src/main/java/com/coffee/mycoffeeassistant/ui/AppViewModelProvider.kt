package com.coffee.mycoffeeassistant.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coffee.mycoffeeassistant.MyCoffeeAssistantApplication
import com.coffee.mycoffeeassistant.ui.screens.addcoffee.AddCoffeeViewModel
import com.coffee.mycoffeeassistant.ui.screens.recipedetails.RecipeDetailsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AddCoffeeViewModel(myCoffeeAssistantApplication().container.coffeeRepository)
        }
        initializer {
            RecipeDetailsViewModel()
        }
    }
}

fun CreationExtras.myCoffeeAssistantApplication(): MyCoffeeAssistantApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyCoffeeAssistantApplication