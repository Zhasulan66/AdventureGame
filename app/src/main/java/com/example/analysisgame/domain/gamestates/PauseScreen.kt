package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.analysisgame.presentation.game.Game

class PauseScreen(val game: Game) : BaseState(game), GameStateInterface {
    val alpha1 = Color.argb(120, 0, 0, 0)
    val paint_blackFill = Paint().apply {
        style = Paint.Style.FILL
        color = alpha1
    }
    val paint_stroke = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.WHITE
    }
    val paint_textB = Paint().apply {
        textSize = 50f
        color = Color.WHITE
    }
    val paint_text = Paint().apply {
        textSize = 40f
        color = Color.WHITE
    }
    val cornerRadius = 20f

    val big_rect = RectF(50f, 100f, 1050f, 400f)


    val rectOp1 = RectF(2000f, 100f, 2200f, 200f)
    val question = "Pause"

    override fun update() {

    }

    override fun render(c: Canvas) {
        c.drawRoundRect(big_rect, cornerRadius, cornerRadius, paint_stroke)
        c.drawRoundRect(big_rect, cornerRadius, cornerRadius, paint_blackFill)

        val questionX = 100f
        var questionY = 160f
        for (line in question.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(line, questionX, questionY, paint_textB)
            questionY += 70
        }

        c.drawRect(rectOp1, paint_blackFill)
    }

    override fun touchEvents(event: MotionEvent) {
        when (game.currentLevel) {
            1 -> sendAnswer(event, Game.GameState.PLAYING)
            2 -> sendAnswer(event, Game.GameState.PLAYING2)
            3 -> sendAnswer(event, Game.GameState.PLAYING3)
            4 -> sendAnswer(event, Game.GameState.PLAYING4)
            else -> sendAnswer(event, Game.GameState.PLAYING5)
        }
    }

    private fun sendAnswer(event: MotionEvent, gameState: Game.GameState) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (rectOp1.contains(event.x, event.y)) {
                game.currentGameState = (gameState)
                MusicManager.resumeMusic()
            }
        }
    }
}