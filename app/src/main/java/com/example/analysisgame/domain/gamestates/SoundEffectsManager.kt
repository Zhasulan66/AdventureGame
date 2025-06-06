package com.example.analysisgame.domain.gamestates

import android.content.Context
import android.media.SoundPool
import com.example.analysisgame.R

object SoundEffectsManager {
    private var soundPool: SoundPool? = null
    private var fireballSoundId: Int = 0
    private var damageSoundId: Int = 0
    private var gameOverSoundId: Int = 0
    private var loaded = false

    fun init(context: Context) {
        if (soundPool != null) return // Already initialized

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .build()

        fireballSoundId = soundPool!!.load(context, R.raw.poink, 1)
        damageSoundId = soundPool!!.load(context, R.raw.damage, 1)
        gameOverSoundId = soundPool!!.load(context, R.raw.gameover, 1)

        loaded = true
    }

    fun playFireball() {
        if (loaded) {
            soundPool?.play(fireballSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun playDamage() {
        if (loaded) {
            soundPool?.play(damageSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun playGameOver() {
        if (loaded) {
            soundPool?.play(gameOverSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        loaded = false
    }
}