package com.luczka.mycoffee.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.luczka.mycoffee.data.remote.dto.MethodDto
import com.luczka.mycoffee.data.remote.dto.RecipeDto
import com.luczka.mycoffee.domain.mappers.toModel
import com.luczka.mycoffee.domain.models.MethodModel
import com.luczka.mycoffee.domain.models.RecipeModel
import com.luczka.mycoffee.domain.repository.FirebaseRepository

class FirebaseRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore
) : FirebaseRepository {

    companion object {
        private const val TAG = "OnlineFirebaseRepository"
        private const val COLLECTION_METHODS = "Methods"
        private const val COLLECTION_RECIPES = "Recipes"
        private const val FIELD_METHOD = "methodId"
        private const val FIELD_YOUTUBE_ID = "youtubeId"
    }

    override fun getMethods(
        onSuccess: (List<MethodModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseFirestore.collection(COLLECTION_METHODS)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents
                    .mapNotNull { documentSnapshot ->
                        documentSnapshot.toObject<MethodDto>()?.copy(id = documentSnapshot.id)
                    }
                    .map { it.toModel() }
                    .let(onSuccess)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d(TAG, it) }
                onError("Error: " + exception.message.toString())
            }
    }

    override fun getRecipes(
        methodId: String,
        onSuccess: (List<RecipeModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseFirestore.collection(COLLECTION_RECIPES)
            .whereEqualTo(FIELD_METHOD, methodId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents
                    .mapNotNull { documentSnapshot -> documentSnapshot.toObject<RecipeDto>() }
                    .map { recipeDto -> recipeDto.toModel() }
                    .let(onSuccess)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d(TAG, it) }
                onError("Error: " + exception.message.toString())
            }
    }

    override fun getRecipe(
        youtubeId: String,
        onSuccess: (RecipeModel) -> Unit
    ) {
        firebaseFirestore.collection(COLLECTION_RECIPES)
            .whereEqualTo(FIELD_YOUTUBE_ID, youtubeId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.documents
                    .firstOrNull()
                    ?.toObject<RecipeDto>()
                    ?.toModel()
                    ?.let(onSuccess)
            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.d(TAG, it) }
            }
    }


}