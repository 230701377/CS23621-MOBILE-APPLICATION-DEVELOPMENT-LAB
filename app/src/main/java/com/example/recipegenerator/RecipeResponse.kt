package com.example.recipegenerator

data class RecipeResponse(
    val title: String,
    val ingredients: List<String>,
    val steps: List<String>
)