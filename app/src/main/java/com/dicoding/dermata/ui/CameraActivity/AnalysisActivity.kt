package com.dicoding.dermata.ui.CameraActivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.dicoding.dermata.R
import com.dicoding.dermata.api.ApiClient
import com.dicoding.dermata.databinding.ActivityAnalysisBinding
import com.dicoding.dermata.ui.ComingSoonPage.ComingSoonActivity
import com.dicoding.dermata.ui.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class AnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalysisBinding
    private var currentImageUri: Uri? = null
    private var permissionsDeniedOnce = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { entry ->
                if (!entry.value) {
                    if (!permissionsDeniedOnce) {
                        permissionsDeniedOnce = true
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()

        // Meminta izin jika belum diberikan
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(arrayOf(REQUIRED_PERMISSION, Manifest.permission.READ_EXTERNAL_STORAGE))
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        // Disable scan button initially until an image is selected
        binding.uploadButton.isEnabled = currentImageUri != null
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
            binding.uploadButton.isEnabled = true // Enable scan button after selecting image
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        val imageUri = getImageUri(this) // Buat URI baru
        currentImageUri = imageUri      // Tetapkan ke properti
        launcherIntentCamera.launch(imageUri) // Gunakan salinan lokal
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            showImage()
            binding.uploadButton.isEnabled = true // Enable scan button after taking picture
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
            binding.uploadButton.isEnabled = true // Enable scan button after selecting image
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            showLoading(true)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("file", imageFile.name, requestImageFile)

            lifecycleScope.launch {
                try {
                    val apiService = ApiClient.getFirstApiService()
                    val response = apiService.uploadImage(multipartBody)
                    Log.d("APIResponse", "Full response: $response")

                    // Kirim hasil ke ResultActivity
                    val intent = Intent(this@AnalysisActivity, ResultActivity::class.java).apply {
                        putExtra(ResultActivity.EXTRA_PREDICTION, response.prediction)
                        putExtra(ResultActivity.EXTRA_CONFIDENCE, response.confidence)
                        putExtra(ResultActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
                    }
                    startActivity(intent)

                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    showToast("HTTP Error: $errorBody")
                } catch (e: Exception) {
                    showToast("Unexpected error: ${e.message}")
                } finally {
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun startScan() {
        // Ensure image is selected before scanning
        if (currentImageUri != null) {
            // Proceed with the scan using the selected image URI
            Log.d("AnalysisActivity", "Starting scan with image: $currentImageUri")
            // Proceed with scanning logic (e.g., start a new activity)
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("imageUri", currentImageUri.toString())
            startActivity(intent)
        } else {
            showToast("Please select an image first to scan.")
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private fun setupBottomNavigation() {
        Log.d("MainActivity", "Setting up bottom navigation")
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            Log.d("MainActivity", "Menu item selected: ${menuItem.itemId}")
            when (menuItem.itemId) {
                R.id.menu_camera -> {
                    if (this !is AnalysisActivity) {
                        Log.d("MainActivity", "Camera button clicked, redirecting to AnalysisActivity")
                        startActivity(Intent(this, AnalysisActivity::class.java))
                        overridePendingTransition(0, 0)
                    }
                    true
                }
                R.id.menu_profile, R.id.menu_aichat -> {
                    Log.d("MainActivity", "Redirecting to ComingSoonActivity")
                    startActivity(Intent(this, ComingSoonActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.menu_home -> {
                    Log.d("MainActivity", "Redirecting to MainActivity")
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.menu_camera
    }
}
