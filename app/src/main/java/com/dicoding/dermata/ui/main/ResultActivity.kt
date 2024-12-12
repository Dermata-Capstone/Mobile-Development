package com.dicoding.dermata.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dermata.R
import com.dicoding.dermata.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val prediction = intent.getStringExtra(EXTRA_PREDICTION)
        val confidence = intent.getDoubleExtra(EXTRA_CONFIDENCE, 0.0)

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
    }

    private fun getRecommendationStringId(prediction: String?): Int {
        return when (prediction) {
            "Normal Skin" -> R.string.recommendation_normal_skin
            "Oily Skin" -> R.string.recommendation_oily_skin
            "Dry Skin" -> R.string.recommendation_dry_skin
            "Acne-Prone Skin" -> R.string.recommendation_acne_skin
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
    }
}
