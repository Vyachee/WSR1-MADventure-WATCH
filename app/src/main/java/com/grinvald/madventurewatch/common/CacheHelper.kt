package com.grinvald.madventurewatch.common

import android.content.Context
import android.content.Context.MODE_PRIVATE

class CacheHelper(context: Context) {

    val prefs = context.getSharedPreferences("auth_data", MODE_PRIVATE)


    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken() : String{
        return prefs.getString("token", null).toString()
    }

    fun removeToken() {
        prefs.edit().remove("token").apply()
    }


}