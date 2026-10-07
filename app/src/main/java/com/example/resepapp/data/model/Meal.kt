package com.example.resepapp.data.model

/** Satu bahan beserta takarannya. */
data class Ingredient(
    val name: String,
    val measure: String
)

/** Model domain yang dipakai oleh UI (sudah dibersihkan dari bentuk mentah API). */
data class Meal(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnail: String,
    val tags: List<String>,
    val youtubeUrl: String?,
    val ingredients: List<Ingredient>
)
