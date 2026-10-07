package com.example.resepapp.data.remote

import com.example.resepapp.data.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

/** Definisi endpoint TheMealDB. Base URL: https://www.themealdb.com/api/json/v1/1/ */
interface MealApiService {

    /** search.php?s={nama_makanan} */
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealResponse

    /** lookup.php?i={id_recipe} */
    @GET("lookup.php")
    suspend fun getMealDetail(@Query("i") id: String): MealResponse

    /** search.php?f={huruf} -> dipakai untuk daftar awal di Home */
    @GET("search.php")
    suspend fun getMealsByFirstLetter(@Query("f") letter: String): MealResponse
}
