package com.example.resepapp.data.model

/**
 * Bentuk mentah JSON dari TheMealDB: { "meals": [ {...}, ... ] } atau { "meals": null }.
 * Tiap meal berupa Map karena API memakai strIngredient1..20 & strMeasure1..20.
 */
data class MealResponse(
    val meals: List<Map<String, String?>>?
)
