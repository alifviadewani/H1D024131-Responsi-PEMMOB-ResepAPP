package com.example.resepapp.data.model

/** Extension function: mengubah satu map JSON mentah menjadi [Meal]. Null jika id tidak ada. */
fun Map<String, String?>.toMeal(): Meal? {
    val id = this["idMeal"]?.takeIf { it.isNotBlank() } ?: return null

    // Gabungkan strIngredient1..20 dengan strMeasure1..20, buang yang kosong/null
    val ingredients = (1..20).mapNotNull { i ->
        val name = this["strIngredient$i"]?.trim()
        if (name.isNullOrEmpty()) null
        else Ingredient(name = name, measure = this["strMeasure$i"]?.trim().orEmpty())
    }

    return Meal(
        id = id,
        name = this["strMeal"].orEmpty(),
        category = this["strCategory"].orEmpty(),
        area = this["strArea"].orEmpty(),
        instructions = this["strInstructions"].orEmpty(),
        thumbnail = this["strMealThumb"].orEmpty(),
        tags = this["strTags"]
            ?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            .orEmpty(),
        youtubeUrl = this["strYoutube"]?.takeIf { it.isNotBlank() },
        ingredients = ingredients
    )
}

/** Extension function: response API -> daftar [Meal] (aman terhadap "meals": null). */
fun MealResponse.toMeals(): List<Meal> = meals.orEmpty().mapNotNull { it.toMeal() }
