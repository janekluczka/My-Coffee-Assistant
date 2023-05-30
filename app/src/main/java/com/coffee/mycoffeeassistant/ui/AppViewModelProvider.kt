package com.coffee.mycoffeeassistant.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coffee.mycoffeeassistant.MyCoffeeAssistantApplication
import com.coffee.mycoffeeassistant.ui.screens.addcoffee.AddCoffeeViewModel
import com.coffee.mycoffeeassistant.ui.screens.brewassistant.BrewAssistantViewModel
import com.coffee.mycoffeeassistant.ui.screens.coffeedetails.CoffeeDetailsViewModel
import com.coffee.mycoffeeassistant.ui.screens.cupboard.CupboardViewModel
import com.coffee.mycoffeeassistant.ui.screens.home.HomeViewModel
import com.coffee.mycoffeeassistant.ui.screens.methodrecipes.MethodRecipesViewModel
import com.coffee.mycoffeeassistant.ui.screens.recipedetails.RecipeDetailsViewModel
import com.coffee.mycoffeeassistant.ui.screens.methods.MethodsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(myCoffeeAssistantApplication().container.coffeeRepository)
        }
        initializer {
            BrewAssistantViewModel(myCoffeeAssistantApplication().container.coffeeRepository)
        }
        initializer {
            CupboardViewModel(myCoffeeAssistantApplication().container.coffeeRepository)
        }
        initializer {
            AddCoffeeViewModel(myCoffeeAssistantApplication().container.coffeeRepository)
        }
        initializer {
            CoffeeDetailsViewModel(myCoffeeAssistantApplication().container.coffeeRepository)
        }
        initializer {
            MethodsViewModel(myCoffeeAssistantApplication().container.firebaseRepository)
        }
        initializer {
            MethodRecipesViewModel(myCoffeeAssistantApplication().container.firebaseRepository)
        }
        initializer {
            RecipeDetailsViewModel(myCoffeeAssistantApplication().container.firebaseRepository)
        }
    }
}

fun CreationExtras.myCoffeeAssistantApplication(): MyCoffeeAssistantApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyCoffeeAssistantApplication