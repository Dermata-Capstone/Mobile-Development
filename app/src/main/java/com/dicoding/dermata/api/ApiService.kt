package com.dicoding.dermata.api


import com.dicoding.dermata.response.ArticleResponse
import com.dicoding.dermata.response.FileUploadResponse
import com.dicoding.dermata.response.LoginResponse
import com.dicoding.dermata.response.ProfileResponse
import com.dicoding.dermata.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    fun login(@Body body: HashMap<String, String>): Call<LoginResponse>

    @GET("profile")
    fun getProfile(@Header("Authorization") token: String): Call<ProfileResponse>

    @POST("api/SignUp")
    fun register(@Body body: HashMap<String, String>): Call<RegisterResponse>

    @POST("update")
    @Multipart
    fun updateProfile(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody,
        @Part fotoProfil: MultipartBody.Part?
    ): Call<Void>

    @GET("home/Pita")
    fun getArticles(): Call<ArticleResponse>

    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): FileUploadResponse

}