package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(
                    this@MainActivity,
                    "Вы успешно нажали на ${searchButton.getText()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        searchButton.setOnClickListener(searchClickListener)

        val libraryButton = findViewById<Button>(R.id.library_button)
        libraryButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "Вы успешно нажали на ${libraryButton.getText()}",
                Toast.LENGTH_SHORT
            ).show()
        }
        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "Вы успешно нажали на ${settingsButton.getText()}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}