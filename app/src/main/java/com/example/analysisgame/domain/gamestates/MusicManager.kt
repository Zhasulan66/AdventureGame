package com.example.analysisgame.domain.gamestates

import android.content.Context
import android.media.MediaPlayer

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isPaused = false
    private var isPrepared = false

    fun startMusic(context: Context, resId: Int) {
        if (!SettingsManager.isMusicOn) return
        stopMusic()
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        isPaused = false
        isPrepared = true
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPrepared = false
    }

    fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPaused = true
        }
    }

    fun resumeMusic() {
        if (SettingsManager.isMusicOn && isPaused && mediaPlayer != null && isPrepared) {
            mediaPlayer?.start()
            isPaused = false
        }
    }
}
/*
object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusicResId: Int? = null

    fun startMusic(context: Context, resId: Int) {
        if (!SettingsManager.isMusicOn) return
        // Avoid restarting the same music
        if (mediaPlayer != null && currentMusicResId == resId && mediaPlayer!!.isPlaying) return

        stopMusic()

        mediaPlayer = MediaPlayer.create(context, resId)?.apply {
            isLooping = true
            setVolume(0.5f, 0.5f)
            start()
        }

        currentMusicResId = resId
    }

    fun pauseMusic() {
        try {
            mediaPlayer?.takeIf { it.isPlaying }?.pause()
        } catch (_: Exception) {}
    }

    fun resumeMusic() {
        try {
            mediaPlayer?.start()
        } catch (_: Exception) {}
    }

    fun stopMusic() {
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        currentMusicResId = null
    }
}*/
