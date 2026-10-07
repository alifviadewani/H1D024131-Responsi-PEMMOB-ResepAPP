package com.example.resepapp.data.repository

import com.example.resepapp.data.model.Meal
import com.example.resepapp.data.model.toMeals
import com.example.resepapp.data.remote.MealApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Satu-satunya pintu data untuk ViewModel.
 * Semua pemanggilan API dibungkus Result agar ViewModel mudah menangani sukses / gagal.
 */
class MealRepository(private val api: MealApiService) {

    /** Cari resep berdasarkan nama. */
    suspend fun searchMeals(query: String): Result<List<Meal>> =
        safeCall { api.searchMeals(query).toMeals() }

    /** Daftar awal Home: gabungan resep berawalan beberapa huruf (dipanggil paralel). */
    suspend fun getDefaultMeals(): Result<List<Meal>> = safeCall {
        coroutineScope {
            listOf("a", "b", "c", "s")
                .map { letter -> async { api.getMealsByFirstLetter(letter).toMeals() } }
                .awaitAll()
                .flatten()
                .distinctBy { it.id }
        }
    }

    /** Detail satu resep berdasarkan id. */
    suspend fun getMealDetail(id: String): Result<Meal?> =
        safeCall { api.getMealDetail(id).toMeals().firstOrNull() }

    // runCatching biasa ikut menelan CancellationException; ini memastikan coroutine tetap bisa dibatalkan.
    private inline fun <T> safeCall(block: () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}
