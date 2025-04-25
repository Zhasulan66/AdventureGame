package com.example.analysisgame.presentation.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.example.analysisgame.domain.gamestates.Menu
import com.example.analysisgame.domain.gamestates.Playing


class Game(
    private val holder: SurfaceHolder,
    private val context: Context
) {
    private var menu: Menu = Menu(this)
    private var playing: Playing = Playing(this, context)
    private val gameLoop = GameLoop(this)
    var currentGameState: GameState = GameState.MENU


    fun update(delta: Double) {
        when (currentGameState) {
            GameState.MENU -> menu.update(delta)
            GameState.PLAYING -> playing.update(delta)
        }
    }

    fun render() {
        val c: Canvas = holder.lockCanvas()
        c.drawColor(Color.BLUE)

        //Draw the game
        when (currentGameState) {
            GameState.MENU -> menu.render(c)
            GameState.PLAYING -> playing.render(c)
        }

        holder.unlockCanvasAndPost(c)
    }

    fun touchEvent(event: MotionEvent): Boolean {
        when (currentGameState) {
            GameState.MENU -> menu.touchEvents(event)
            GameState.PLAYING -> playing.touchEvents(event)
        }

        return true
    }

    fun startGameLoop() {
        gameLoop.startGameLoop()
    }

    enum class GameState {
        MENU, PLAYING
    }
}