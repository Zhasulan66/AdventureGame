package com.example.analysisgame.data.local

import android.content.Context
import android.os.Build
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {
    val USERNAME_KEY = stringPreferencesKey("username")

    suspend fun saveUsername(context: Context, username: String) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
        }
    }

    val getUsername: (Context) -> Flow<String?> = { context ->
        context.dataStore.data
            .map { prefs -> prefs[USERNAME_KEY] }
    }
}

fun generateUniqueUsername(): String {
    val manufacturer = Build.MANUFACTURER.capitalize()
    val model = Build.MODEL.replace(" ", "") // Remove spaces
    val timestamp = System.currentTimeMillis().toString().takeLast(6) // e.g., "987654"
    return "User${manufacturer}${model}$timestamp"
}