package com.dicoding.dermata.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("confidence")
    val confidence: Double,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("prediction")
    val prediction: String
)

