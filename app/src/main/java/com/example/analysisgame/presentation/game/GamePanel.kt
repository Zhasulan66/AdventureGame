package com.example.analysisgame.presentation.game

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.navigation.NavController
import com.example.analysisgame.R
import com.example.analysisgame.domain.gamestates.MusicManager
import com.example.analysisgame.domain.gamestates.SoundEffectsManager


class GamePanel(
    context: Context,
    val level: Int,
    navController: NavController
) : SurfaceView(context), SurfaceHolder.Callback {

    private val game = Game(holder, context, level, navController)

    init {
        holder.addCallback(this)
        SoundEffectsManager.init(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return game.touchEvent(event!!)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("Game.kt", "surfaceCreated()")
        when (level) {
            1 -> MusicManager.startMusic(context, R.raw.disco_dj)
            2 -> MusicManager.startMusic(context, R.raw.disco_dj)
            3 -> MusicManager.startMusic(context, R.raw.penacony_dark)
            4 -> MusicManager.startMusic(context, R.raw.disco_dj)
            5 -> MusicManager.startMusic(context, R.raw.disco_dj)
        }
        game.startGameLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("Game.kt", "surfaceChanged()")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("Game.kt", "surfaceDestroyed()")
        MusicManager.stopMusic()
        SoundEffectsManager.release()
        game.pauseGameLoop()
    }

}