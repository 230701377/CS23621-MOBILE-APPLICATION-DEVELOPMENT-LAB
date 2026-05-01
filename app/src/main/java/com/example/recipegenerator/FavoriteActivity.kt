package com.example.recipegenerator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val textView = findViewById<TextView>(R.id.favText)

        val sharedPref = getSharedPreferences("recipes", MODE_PRIVATE)
        val data = sharedPref.getString("data", "No favorites yet 😢")

        textView.text = data ?: "No favorites yet 😢"
    }
}