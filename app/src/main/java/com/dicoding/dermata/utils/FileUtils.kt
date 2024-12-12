package com.dicoding.dermata.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun getFileFromUri(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
        val filePath = context.cacheDir.path + "/" + System.currentTimeMillis()
        val file = File(filePath)
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }
}
