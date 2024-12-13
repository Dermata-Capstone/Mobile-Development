package com.dicoding.dermata.ui.ProfileActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dermata.R
import com.dicoding.dermata.api.ApiClient
import com.dicoding.dermata.api.ApiService
import com.dicoding.dermata.response.ProfileResponse
import com.dicoding.dermata.ui.CameraActivity.AnalysisActivity
import com.dicoding.dermata.ui.ComingSoonPage.ComingSoonActivity
import com.dicoding.dermata.ui.LoginPage.LoginActivity
import com.dicoding.dermata.ui.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation()

        val btnLogout: Button = findViewById(R.id.btn_logout)

        // Logout action
        btnLogout.setOnClickListener {
            // Tambahkan logika logout Anda di sini (seperti menghapus session/token, dll)
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

            // Pindah ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish() // Tutup LogoutActivity
        }
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
                R.id.menu_profile -> {
                    if (this !is ProfileActivity) {
                        Log.d(
                            "MainActivity", "Redirecting to LogoutActivity"
                        )
                        startActivity(Intent(this, ProfileActivity::class.java))
                        overridePendingTransition(0, 0)
                    }
                    true
                }
                R.id.menu_aichat -> {
                    Log.d("MainActivity", "Redirecting to ComingSoonActivity")  // Log saat membuka ComingSoonActivity
                    startActivity(Intent(this, ComingSoonActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.menu_home -> {
                    Log.d("MainActivity", "Redirecting to MainActivity")  // Log saat membuka MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.menu_profile
    }
}
