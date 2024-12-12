plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.dicoding.dermata"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dicoding.dermata"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    val cameraxVersion = "1.3.0"
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.androidx.fragment.ktx)

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Retrofit for networking (used in login, register, reset password)
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.8.7")

    // Picasso for image loading (optional, e.g., profile pictures)
    implementation ("com.squareup.picasso:picasso:2.8")

    // Glide (for optional image handling, e.g., profile images)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    // OkHttp (for networking with Retrofit)
    implementation ("com.squareup.okhttp3:okhttp:3.12.13")

    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.material:material:1.9.0")


    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")

    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") //untuk lifecycleScope

    implementation("androidx.exifinterface:exifinterface:1.3.6")

}
