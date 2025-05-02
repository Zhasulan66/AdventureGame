package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.example.analysisgame.presentation.game.Game


class Menu(val game: Game, val currentLevel: Int) : BaseState(game), GameStateInterface {
    private val paint: Paint = Paint()

    init {
        paint.textSize = 60F
        paint.setColor(Color.WHITE)
    }

    override fun update() {
    }

    override fun render(c: Canvas) {
        c.drawText("MENU!", 800f, 200f, paint)
    }

    override fun touchEvents(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            when(currentLevel){
                1 -> game.currentGameState = Game.GameState.PLAYING
                2 -> game.currentGameState = Game.GameState.PLAYING2
                3 -> game.currentGameState = Game.GameState.PLAYING3
                4 -> game.currentGameState = Game.GameState.PLAYING4
                5 -> game.currentGameState = Game.GameState.PLAYING5
            }

        }
    }
}