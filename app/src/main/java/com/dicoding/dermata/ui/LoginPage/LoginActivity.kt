package com.dicoding.dermata.ui.LoginPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dermata.R
import com.dicoding.dermata.api.ApiClient
import com.dicoding.dermata.api.ApiService
import com.dicoding.dermata.response.LoginResponse
import com.dicoding.dermata.ui.MainActivity
import com.dicoding.dermata.ui.RegisterPage.RegisterActivity
import com.dicoding.dermata.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var sharedPrefManager: SharedPrefManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi SharedPrefManager dan ApiService
        sharedPrefManager = SharedPrefManager(this)
        apiService = ApiClient.apiService

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val email = findViewById<EditText>(R.id.edit_email).text.toString()
            val password = findViewById<EditText>(R.id.edit_password).text.toString()

            val body = hashMapOf("email" to email, "password" to password)
            apiService.login(body).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token

                        // Simpan status login
                        sharedPrefManager.setLoggedIn(true)

                        // Arahkan ke ProfileActivity dengan membawa token
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                            putExtra("token", token)
                        })
                        finish() // Tutup LoginActivity
                    } else {
                        Toast.makeText(this@LoginActivity, "Login gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Login gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        // Menangani klik pada teks Register
        findViewById<TextView>(R.id.txt_register).setOnClickListener {
            // Arahkan ke RegisterActivity
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
}
