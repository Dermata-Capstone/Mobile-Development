package com.dicoding.dermata.ui.CameraActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.dermata.R
import com.dicoding.dermata.databinding.ActivityResultBinding
import com.dicoding.dermata.ui.ComingSoonPage.ComingSoonActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()

        // Ambil data dari Intent
        val prediction = intent.getStringExtra(EXTRA_PREDICTION)
        val confidence = intent.getDoubleExtra(EXTRA_CONFIDENCE, 0.0)
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)

        // Tampilkan data ke UI
        val resultMessage = String.format(
            "%s with %.2f%% confidence.",
            prediction,
            confidence * 100
        )
        binding.resultTextView.text = resultMessage

        // Tampilkan rekomendasi berdasarkan prediksi
        val recommendation = getString(getRecommendationStringId(prediction))
        binding.recommendationTextView.text = recommendation

        // Tampilkan gambar
        val imageUri = imageUriString?.let { Uri.parse(it) }
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(binding.resultImageView) // Pastikan Anda memiliki ImageView di layout
        }
    }

    private fun getRecommendationStringId(prediction: String?): Int {
        return when (prediction) {
            "Normal Skin" -> R.string.recommendation_normal_skin
            "Oily Skin" -> R.string.recommendation_oily_skin
            "Dry Skin" -> R.string.recommendation_dry_skin
            "Acne" -> R.string.recommendation_acne_skin
            "Blackheads" -> R.string.recommendation_blackheads
            "Dark Circles" -> R.string.recommendation_dark_circles
            "Large Pores" -> R.string.recommendation_large_pores
            "Skin Redness" -> R.string.recommendation_skin_redness
            "Wrinkles" -> R.string.recommendation_wrinkles
            "Dark Spots" -> R.string.recommendation_dark_spots
            else -> R.string.recommendation_default
        }
    }

    companion object {
        const val EXTRA_PREDICTION = "extra_prediction"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
    private fun setupBottomNavigation() {
        Log.d("MainActivity", "Setting up bottom navigation")
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            Log.d("MainActivity", "Menu item selected: ${menuItem.itemId}")  // Log saat menu item dipilih
            when (menuItem.itemId) {
                R.id.menu_camera -> {

                    Log.d("MainActivity", "Camera button clicked, redirecting to AnalysisActivity")
                    startActivity(Intent(this, AnalysisActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.menu_profile, R.id.menu_aichat -> {
                    Log.d("MainActivity", "Redirecting to ComingSoonActivity")  // Log saat membuka ComingSoonActivity
                    startActivity(Intent(this, ComingSoonActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.menu_aichat -> {
                    Log.d("MainActivity", "Redirecting to ComingSoonActivity")  // Log saat membuka ComingSoonActivity
                    startActivity(Intent(this, ComingSoonActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.menu_camera
        Log.d("MainActivity", "Bottom navigation set to 'main'")  // Log saat bottom navigation diset
    }
}
