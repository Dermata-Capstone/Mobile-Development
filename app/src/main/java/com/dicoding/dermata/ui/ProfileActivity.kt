package com.dicoding.dermata.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dermata.R
import com.dicoding.dermata.api.ApiClient
import com.dicoding.dermata.api.ApiService
import com.dicoding.dermata.response.ProfileResponse


class ProfileActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val token = intent.getStringExtra("token") ?: return
        apiService = ApiClient.instance.create(ApiService::class.java)

        apiService.getProfile("Bearer $token").enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        findViewById<TextView>(R.id.tvUsername).text = it.username
                        findViewById<TextView>(R.id.tvEmail).text = it.email
                        Picasso.get().load(it.fotoProfil).into(findViewById(R.id.ivProfile))
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Gagal memuat profil!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
