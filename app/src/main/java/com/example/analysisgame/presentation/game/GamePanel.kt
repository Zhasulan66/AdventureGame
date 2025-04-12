package com.example.analysisgame.presentation.game

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView


class GamePanel(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {

    private val game = Game(holder)

    init {
        holder.addCallback(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return game.touchEvent(event!!)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        game.startGameLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

}