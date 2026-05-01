package com.example.recipegenerator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = findViewById<EditText>(R.id.input)
        val btn = findViewById<MaterialButton>(R.id.btn)
        val resultCard = findViewById<View>(R.id.resultCard)
        val titleText = findViewById<TextView>(R.id.titleText)
        val stepsText = findViewById<TextView>(R.id.stepsText)

        // ⭐ NEW BUTTONS
        val favBtn = findViewById<MaterialButton>(R.id.favPageBtn)
        val saveBtn = findViewById<MaterialButton>(R.id.saveBtn)

        // ⭐ OPEN FAVORITES PAGE
        favBtn.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }

        // 🔥 GENERATE RECIPE
        btn.setOnClickListener {

            val ingredientsText = input.text.toString().trim()

            if (ingredientsText.isEmpty()) {
                Toast.makeText(this, "Enter ingredients first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            resultCard.visibility = View.VISIBLE
            titleText.text = "Cooking... 🍳"
            stepsText.text = ""

            val ingredientsList = ingredientsText
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val request = IngredientsRequest(ingredientsList)

            RetrofitClient.api.generateRecipe(request)
                .enqueue(object : Callback<RecipeResponse> {

                    override fun onResponse(
                        call: Call<RecipeResponse>,
                        response: Response<RecipeResponse>
                    ) {
                        if (response.isSuccessful) {
                            val recipe = response.body()

                            if (recipe != null) {
                                titleText.text = recipe.title
                                stepsText.text =
                                    recipe.steps.joinToString("\n• ", prefix = "• ")
                            } else {
                                titleText.text = "No response 😢"
                            }
                        } else {
                            titleText.text = "Server error 😢"
                        }
                    }

                    override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                        titleText.text = "Error: ${t.message}"
                    }
                })
        }

        // ⭐ SAVE RECIPE (NEW FEATURE)
        saveBtn.setOnClickListener {

            val title = titleText.text.toString()
            val steps = stepsText.text.toString()

            if (title.isEmpty() || steps.isEmpty()) {
                Toast.makeText(this, "Generate recipe first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("recipes", MODE_PRIVATE)
            val oldData = sharedPref.getString("data", "") ?: ""

            val newData = oldData + "\n\n" + title + "\n" + steps

            sharedPref.edit().putString("data", newData).apply()

            Toast.makeText(this, "Saved ⭐", Toast.LENGTH_SHORT).show()
        }
    }
}