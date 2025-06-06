package com.example.analysisgame.domain.gamestates

import android.content.Context
import android.media.MediaPlayer

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusicResId: Int? = null

    fun startMusic(context: Context, resId: Int) {
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
}