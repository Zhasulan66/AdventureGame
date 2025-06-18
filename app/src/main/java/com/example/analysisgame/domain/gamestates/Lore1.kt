package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.example.analysisgame.presentation.game.Game

class LoreScreen1(val game: Game) : BaseState(game), GameStateInterface {
    private val paint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    private val backgroundPaint = Paint().apply {
        color = Color.BLACK
    }

    private val loreText = "Long ago, our village knew only peace.\n" +
            "But that changed when the skies darkened and the dead began to rise.\n" +
            " The necromancer, once a forgotten name in dusty legends,\n" +
            " returned—bringing with him an army of skeletons. They burned fields,\n" +
            " shattered homes, and tore apart the land we loved.\n" +
            "Now, with our village in ruins and nowhere left to run,\n" +
            " we’ve made a choice: we fight back.\n" +
            "We may not be heroes, but we are all that’s left.\n" +
            "And we will end this curse at its source."
    private val displayTime = 10000L // 5 seconds
    private var startTime = System.currentTimeMillis()

    override fun update() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - startTime >= displayTime) {
            game.currentGameState = Game.GameState.PLAYING
        }
    }

    override fun render(c: Canvas) {
        c.drawRect(0f, 0f, c.width.toFloat(), c.height.toFloat(), backgroundPaint)
        val lines = loreText.split("\n")
        for ((i, line) in lines.withIndex()) {
            c.drawText(line, c.width / 2f, 200f + i * 100f, paint)
        }
    }

    override fun touchEvents(event: MotionEvent) {
        // Optional: skip lore on tap
        if (event.action == MotionEvent.ACTION_MOVE) {
            game.currentGameState = Game.GameState.PLAYING
        }
    }
}