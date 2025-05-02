package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.example.analysisgame.presentation.game.Game


class Menu(val game: Game) : BaseState(game), GameStateInterface {
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
        if (event.action == MotionEvent.ACTION_DOWN) game.currentGameState = Game.GameState.PLAYING
    }
}