package com.luczka.mycoffee.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.luczka.mycoffee.data.remote.model.CategoriesDto
import com.luczka.mycoffee.data.remote.model.CategoryDto
import com.luczka.mycoffee.data.remote.model.FirebaseServiceException
import com.luczka.mycoffee.data.remote.model.RecipeDto
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class FirebaseService(private val firebaseFirestore: FirebaseFirestore) {

    companion object {
        private const val COLLECTION_RECIPES = "Recipes"
        private const val COLLECTION_CONFIG = "config"

        private const val DOCUMENT_METHODS = "methods"

        private const val FIELD_METHOD = "methodId"
    }

    @Throws(FirebaseServiceException::class)
    suspend fun getCategories(): List<CategoryDto> {
        return safeFirestoreRequest {
            val configDocumentSnapshot = firebaseFirestore
                .collection(COLLECTION_CONFIG)
                .document(DOCUMENT_METHODS)
                .get()
                .await()

            if (configDocumentSnapshot.metadata.isFromCache) {
                throw FirebaseServiceException.CacheDataException()
            } else {
                configDocumentSnapshot.toObject<CategoriesDto>()?.list ?: emptyList()
            }
        }
    }

    @Throws(FirebaseServiceException::class)
    suspend fun getRecipesDto(methodId: String): List<RecipeDto> {
        return safeFirestoreRequest {
            val recipesQuerySnapshot = firebaseFirestore
                .collection(COLLECTION_RECIPES)
                .whereEqualTo(FIELD_METHOD, methodId)
                .get()
                .await()

            if (recipesQuerySnapshot.metadata.isFromCache) {
                throw FirebaseServiceException.CacheDataException()
            } else {
                recipesQuerySnapshot.mapNotNull { it.toObject<RecipeDto>() }
            }
        }
    }

    private suspend fun <T> safeFirestoreRequest(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: FirebaseFirestoreException) {
            throw FirebaseServiceException.ServerException(e.message)
        } catch (e: CancellationException) {
            throw FirebaseServiceException.CancelledException(e.message)
        } catch (e: Exception) {
            throw FirebaseServiceException.UnknownException(e.message)
        }
    }
}