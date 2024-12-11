package com.dicoding.dermata.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.dermata.R
import com.dicoding.dermata.ui.LoginPage.LoginActivity
import com.dicoding.dermata.utils.SharedPrefManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Cek apakah pengguna sudah login
        val sharedPrefManager = SharedPrefManager(this)
        val isLoggedIn = sharedPrefManager.isLoggedIn()

        if (!isLoggedIn) {
            // Jika belum login, arahkan ke halaman login
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Tutup MainActivity
            return
        }

        // Lanjutkan dengan konfigurasi MainActivity biasa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
