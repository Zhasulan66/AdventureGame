package com.example.analysisgame.data.local

import android.content.Context
import android.os.Build
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object UserPreferences {
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val IS_SOUND_ON_KEY = booleanPreferencesKey("is_sound_on")
    private val IS_MUSIC_ON_KEY = booleanPreferencesKey("is_music_on")

    suspend fun saveUsername(context: Context, username: String) {
        context.dataStore.edit { prefs ->
            prefs[USERNAME_KEY] = username
        }
    }

    suspend fun setSoundOn(context: Context, isOn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_SOUND_ON_KEY] = isOn
        }
    }

    suspend fun setMusicOn(context: Context, isOn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_MUSIC_ON_KEY] = isOn
        }
    }

    val getUsername: (Context) -> Flow<String?> = { context ->
        context.dataStore.data.map { prefs -> prefs[USERNAME_KEY] }
    }

    val isSoundOn: (Context) -> Flow<Boolean> = { context ->
        context.dataStore.data.map { prefs -> prefs[IS_SOUND_ON_KEY] ?: true }
    }

    val isMusicOn: (Context) -> Flow<Boolean> = { context ->
        context.dataStore.data.map { prefs -> prefs[IS_MUSIC_ON_KEY] ?: true }
    }
}

fun generateUniqueUsername(): String {
    val manufacturer = Build.MANUFACTURER.capitalize()
    val model = Build.MODEL.replace(" ", "") // Remove spaces
    val timestamp = System.currentTimeMillis().toString().takeLast(6) // e.g., "987654"
    return "User${manufacturer}${model}$timestamp"
}