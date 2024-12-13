package com.dicoding.dermata.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dermata.R
import com.dicoding.dermata.api.ApiClient
import com.dicoding.dermata.ui.LoginPage.LoginActivity
import com.dicoding.dermata.ui.ComingSoonPage.ComingSoonActivity
import com.dicoding.dermata.ui.CameraActivity.AnalysisActivity
import com.dicoding.dermata.ui.ProfileActivity.ProfileActivity
import com.dicoding.dermata.utils.ArticleAdapter
import com.dicoding.dermata.utils.SharedPrefManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)

        Log.d("MainActivity", "onCreate started")  // Log saat onCreate dipanggil

        val sharedPrefManager = SharedPrefManager(this)
        val isLoggedIn = sharedPrefManager.isLoggedIn()

        Log.d("MainActivity", "Is user logged in? $isLoggedIn")  // Log status login

        if (!isLoggedIn) {
            Log.d("MainActivity", "User not logged in, redirecting to LoginActivity")  // Log jika belum login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        recyclerView = findViewById(R.id.rv_artikel)
        recyclerView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(emptyList()) { link ->
            Log.d("MainActivity", "Article clicked, opening link: $link")  // Log saat artikel diklik
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
        recyclerView.adapter = articleAdapter // Attach the adapter early

        Log.d("MainActivity", "Fetching articles...")  // Log sebelum fetchArticles dipanggil
        fetchArticles()

        setupInsets()
        setupBottomNavigation()
    }

    private fun fetchArticles() {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("MainActivity", "Starting API request for articles")
                val response = ApiClient.getSecondApiService().getArticles().execute() // Panggilan sinkron
                if (response.isSuccessful) {
                    val articleResponse = response.body() // Mendapatkan ArticleResponse
                    val websites = articleResponse?.websites ?: emptyList() // Akses field 'websites'
                    Log.d("MainActivity", "Articles fetched successfully: ${websites.size} websites")
                    withContext(Dispatchers.Main) {
                        val websites = articleResponse?.websites?.filterNotNull() ?: emptyList() // Filter out null values
                        articleAdapter = ArticleAdapter(websites) { link ->
                            Log.d("MainActivity", "Website clicked, opening link: $link")
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            startActivity(intent)
                        }
                        recyclerView.adapter = articleAdapter
                        progressBar.visibility = View.GONE
                    }
                } else {
                    Log.e("MainActivity", "Error fetching articles, response code: ${response.code()}")
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        showError("Failed to load articles. Please try again.")
                    }
                }
            } catch (e: TimeoutException) {
                Log.e("MainActivity", "Timeout occurred while fetching articles")
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    showError("Request timed out. Please check your internet connection and try again.")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Exception occurred while fetching articles: ${e.message}")
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    showError("An error occurred while fetching articles. Please try again.")
                }
            }
        }
    }

    private fun showError(message: String) {
        // Show an error message to the user, for example, using a Toast or a Snackbar
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupInsets() {
        Log.d("MainActivity", "Setting up window insets")  // Log saat setupInsets dipanggil
        ViewCompat.setOnApplyWindowInsetsListener(findViewById<View>(R.id.main)) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom)
            insets
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
                    Log.d("MainActivity", "Redirecting to ProfileActivity")  // Log saat membuka ComingSoonActivity
                    startActivity(Intent(this,ProfileActivity::class.java))
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
        bottomNavigationView.selectedItemId = R.id.menu_home
        Log.d("MainActivity", "Bottom navigation set to 'main'")  // Log saat bottom navigation diset
    }
}
