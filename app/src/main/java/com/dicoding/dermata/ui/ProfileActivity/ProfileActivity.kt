package com.dicoding.dermata.ui.ProfileActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dermata.R
import com.dicoding.dermata.api.ApiClient
import com.dicoding.dermata.api.ApiService
import com.dicoding.dermata.response.ProfileResponse
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
    private lateinit var apiService: ApiService
    private lateinit var ivProfile: ImageView
    private lateinit var editUsername: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var btnSave: Button
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val token = intent.getStringExtra("token") ?: return
        apiService = ApiClient.getSecondApiService()

        ivProfile = findViewById(R.id.ivProfile)
        editUsername = findViewById(R.id.edit_username)
        editEmail = findViewById(R.id.edit_email)
        btnSave = findViewById(R.id.btnSave)

        // Load profile data
        apiService.getProfile("Bearer $token").enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Set existing user data
                        editUsername.setText(it.username)
                        editEmail.setText(it.email)
                        // Load profile picture
                        Picasso.get().load(it.fotoProfil).into(ivProfile)
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Gagal memuat profil!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Change profile picture
        ivProfile.setOnClickListener {
            pickImageFromGallery()
        }

        // Save profile changes
        btnSave.setOnClickListener {
            val newUsername = editUsername.text.toString()

            if (newUsername.isEmpty()) {
                Toast.makeText(this, "Username tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usernameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), newUsername)
            val photoPart = selectedImageUri?.let { uri ->
                val file = getFileFromUri(uri)
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("fotoProfil", file.name, requestFile)
            }

            apiService.updateProfile("Bearer $token", usernameRequestBody, photoPart).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProfileActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ProfileActivity, "Gagal memperbarui profil!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Pick image from gallery
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    // Handle image selection result
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                ivProfile.setImageURI(uri)
            }
        }
    }

    // Convert selected URI to file for upload
    private fun getFileFromUri(uri: Uri): File {
        // Check if URI is a content URI (common with images from gallery)
        if (uri.scheme == "content") {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val file = File(filesDir, "profile_image_${System.currentTimeMillis()}.jpg")
                inputStream.copyTo(file.outputStream())
                return file
            }
        }
        throw IllegalArgumentException("Unsupported URI scheme")
    }
}
