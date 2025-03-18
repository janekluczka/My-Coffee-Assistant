package com.luczka.mycoffee.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.luczka.mycoffee.data.remote.dto.CategoriesDto
import com.luczka.mycoffee.data.remote.dto.CategoryDto
import com.luczka.mycoffee.data.remote.dto.RecipeDto
import kotlinx.coroutines.tasks.await

class FirebaseService(private val firebaseFirestore: FirebaseFirestore) {

    companion object {
        private const val COLLECTION_RECIPES = "Recipes"
        private const val COLLECTION_CONFIG = "config"

        private const val DOCUMENT_METHODS = "methods"

        private const val FIELD_METHOD = "methodId"
    }

    suspend fun getCategories(): List<CategoryDto> {
        return try {
            val configDocumentSnapshot = firebaseFirestore
                .collection(COLLECTION_CONFIG)
                .document(DOCUMENT_METHODS)
                .get()
                .await()

            if (configDocumentSnapshot.metadata.isFromCache) {
                emptyList()
            } else {
                configDocumentSnapshot.toObject<CategoriesDto>()?.list ?: emptyList()
            }
        } catch (exception: Exception) {
            throw exception
        }
    }

    suspend fun getRecipesDto(methodId: String): List<RecipeDto> {
        return try {
            val recipesQuerySnapshot = firebaseFirestore
                .collection(COLLECTION_RECIPES)
                .whereEqualTo(FIELD_METHOD, methodId)
                .get()
                .await()

            if (recipesQuerySnapshot.metadata.isFromCache) {
                emptyList()
            } else {
                recipesQuerySnapshot.mapNotNull { it.toObject<RecipeDto>() }
            }
        } catch (exception: Exception) {
            throw exception
        }
    }
}