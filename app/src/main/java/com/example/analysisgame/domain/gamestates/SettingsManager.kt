package com.example.analysisgame.domain.gamestates

import android.content.Context
import com.example.analysisgame.data.local.UserPreferences
import kotlinx.coroutines.flow.first

object SettingsManager {
    var isMusicOn: Boolean = true
    var isSoundOn: Boolean = true

    suspend fun loadSettings(context: Context) {
        val sound = UserPreferences.isSoundOn(context).first()
        val music = UserPreferences.isMusicOn(context).first()
        isSoundOn = sound
        isMusicOn = music
    }

    suspend fun saveSettings(context: Context) {
        UserPreferences.setSoundOn(context, isSoundOn)
        UserPreferences.setMusicOn(context, isMusicOn)
    }
}