package com.example.analysisgame.presentation.game

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GamePanel(context: Context, level: Int) : SurfaceView(context), SurfaceHolder.Callback {

    private val game = Game(holder, context, level)

    init {
        holder.addCallback(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return game.touchEvent(event!!)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("Game.kt", "surfaceCreated()")
        game.startGameLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("Game.kt", "surfaceChanged()")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("Game.kt", "surfaceDestroyed()")
        game.pauseGameLoop()
    }

}