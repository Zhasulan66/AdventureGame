package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.analysisgame.presentation.game.GameLoop

class Performance(
    val context: Context,
    val gameLoop: GameLoop,
    val player: Player
) {

    fun draw(canvas: Canvas){
        //drawUPS(canvas)
        drawFPS(canvas)
        drawCoordinates(canvas)
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
        canvas.drawText("FPS: $averageFPS", 100f, 100f, paint)
    }

    private fun drawCoordinates(canvas: Canvas){
        val coordinates = "x: ${player.positionX}\n y: ${player.positionY}"
        val paint = Paint().apply { color = Color.BLUE
            textSize = 50f}
        canvas.drawText(coordinates, 100f, 150f, paint)
    }
}