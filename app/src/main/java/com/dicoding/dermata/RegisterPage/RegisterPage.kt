package com.dicoding.dermata.RegisterPage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dermata.R
import com.dicoding.dermata.api.ApiClient
import com.dicoding.dermata.api.ApiService
import com.dicoding.dermata.response.RegisterResponse
import com.dicoding.dermata.LoginPage.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        apiService = ApiClient.instance.create(ApiService::class.java)

        val usernameField = findViewById<TextInputEditText>(R.id.edit_username)
        val emailField = findViewById<TextInputEditText>(R.id.edit_email)
        val passwordField = findViewById<TextInputEditText>(R.id.edit_password)
        val confirmPasswordField = findViewById<TextInputEditText>(R.id.edit_confirm_password)
        val registerButton = findViewById<MaterialButton>(R.id.btn_register)

        registerButton.setOnClickListener {
            val username = usernameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            // Validasi input
            if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val body = hashMapOf(
                "username" to username,
                "email" to email,
                "password" to password
            )

            apiService.register(body).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registrasi gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Gagal terhubung: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
