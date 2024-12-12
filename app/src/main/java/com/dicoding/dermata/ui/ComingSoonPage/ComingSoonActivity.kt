package com.dicoding.dermata.ui.ComingSoonPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.dicoding.dermata.R

class ComingSoonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coming_soon)

        // Find the button in layout
        val btnClose: Button = findViewById(R.id.btnClose)

        // Set onClickListener for close button
        btnClose.setOnClickListener {
            // Close the current activity or navigate elsewhere
            finish()  // Will finish the activity and go back to previous screen
            Toast.makeText(this, "Kembali ke HomePage", Toast.LENGTH_SHORT).show()
        }
    }
}
