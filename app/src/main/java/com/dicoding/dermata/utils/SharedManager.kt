package com.dicoding.dermata.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

    companion object {
        const val IS_LOGGED_IN = "isLoggedIn"
    }

    // Fungsi untuk menyimpan status login
    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // Fungsi untuk mengecek status login
    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(IS_LOGGED_IN, false) // Default false jika belum ada
    }
}
