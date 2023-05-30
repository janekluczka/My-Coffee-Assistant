package com.coffee.mycoffeeassistant.data

import android.util.Log
import com.coffee.mycoffeeassistant.ui.model.MethodUiState
import com.coffee.mycoffeeassistant.ui.model.RecipeDetailsUiState
import com.coffee.mycoffeeassistant.ui.model.RecipeUiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class OnlineFirebaseRepository : FirebaseRepository {

    companion object {
        private const val COLLECTION_METHODS = "Methods"
        private const val COLLECTION_RECIPES = "Recipes"
        private const val FIELD_METHOD = "methodId"
        private const val FIELD_YOUTUBE_ID = "youtubeId"
    }

    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance(Firebase.app)

    override fun getMethods(onSuccess: (MutableList<MethodUiState>) -> Unit) {
        firebaseFirestore.collection(COLLECTION_METHODS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val methods = mutableListOf<MethodUiState>()
                querySnapshot.documents.forEach { documentSnapshot ->
                    val method = documentSnapshot.toObject<MethodUiState>()
                    method?.let {
                        methods.add(it.copy(methodId = documentSnapshot.id))
                    }
                }
                onSuccess(methods)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d("OnlineFirebaseRepository", it) }
            }
    }

    override fun getRecipes(methodId: String, onSuccess: (MutableList<RecipeUiState>) -> Unit) {
        firebaseFirestore.collection(COLLECTION_RECIPES)
            .whereEqualTo(FIELD_METHOD, methodId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = mutableListOf<RecipeUiState>()
                querySnapshot.documents.forEach { documentSnapshot ->
                    val recipe = documentSnapshot.toObject<RecipeUiState>()
                    recipe?.let {
                        recipes.add(it)
                    }
                }
                onSuccess(recipes)
            }
            .addOnFailureListener {

            }
    }

    override fun getRecipe(youtubeId: String, onSuccess: (RecipeDetailsUiState) -> Unit) {
        firebaseFirestore.collection(COLLECTION_RECIPES)
            .whereEqualTo(FIELD_YOUTUBE_ID, youtubeId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents.firstOrNull()?.let { documentSnapshot ->
                    documentSnapshot.data
                    val recipe = documentSnapshot.toObject<RecipeDetailsUiState>()
                    recipe?.let {
                        onSuccess(it)
                    }
                }
            }
            .addOnFailureListener {

            }
    }


}