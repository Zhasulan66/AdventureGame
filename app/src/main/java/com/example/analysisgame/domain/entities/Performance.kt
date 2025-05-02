package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.analysisgame.presentation.game.GameLoop

class Performance(
    val context: Context,
    val gameLoop: GameLoop
) {

    fun draw(canvas: Canvas){
        drawUPS(canvas)
        drawFPS(canvas)
    }

    private fun drawUPS(canvas: Canvas){
        val averageUPS = gameLoop.getAverageUPS().toString()
        val paint = Paint().apply { color = Color.BLUE
            textSize = 50f}
        canvas.drawText("UPS: $averageUPS", 100f, 100f, paint)
    }

    private fun drawFPS(canvas: Canvas){
        val averageFPS = gameLoop.getAverageFPS().toString()
        val paint = Paint().apply { color = Color.BLUE
            textSize = 50f}
        canvas.drawText("FPS: $averageFPS", 100f, 200f, paint)
    }
}