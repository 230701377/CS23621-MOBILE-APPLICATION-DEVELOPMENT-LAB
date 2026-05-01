package com.example.recipegenerator

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("generate-recipe")
    fun generateRecipe(
        @Body body: IngredientsRequest
    ): Call<RecipeResponse>

}