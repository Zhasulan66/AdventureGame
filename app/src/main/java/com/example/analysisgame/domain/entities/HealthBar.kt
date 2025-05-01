package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.example.analysisgame.R
import com.example.analysisgame.presentation.game.GameDisplay



class HealthBar(
    context: Context,
    val player: Player
) {

    val borderPaint = Paint().apply { color = context.getColor(R.color.healthBarBorder) }
    val healthPaint = Paint().apply { color = context.getColor(R.color.healthBarHealth) }
    val width = 100
    val height = 20
    val margin = 2

    fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        val x = player.positionX
        val distanceToPlayer = 100f
        val healthPointPercentage = player.healthPoints.toFloat() / player.MAX_HEALTH_POINTS.toFloat()
        val borderTop: Float

        // Draw border
        val borderLeft = x - width / 2
        val borderRight = x + width / 2
        val borderBottom = player.positionY - distanceToPlayer
        borderTop = borderBottom - height
        canvas.drawRect(
            gameDisplay.gameToDisplayCoordinatesX(borderLeft),
            gameDisplay.gameToDisplayCoordinatesY(borderTop),
            gameDisplay.gameToDisplayCoordinatesX(borderRight),
            gameDisplay.gameToDisplayCoordinatesY(borderBottom),
            borderPaint
        )
        val healthTop: Float
        val healthRight: Float
        val healthWidth = (width - 2 * margin).toFloat()
        val healthHeight = (height - 2 * margin).toFloat()

        // Draw health
        val healthLeft = borderLeft + margin
        healthRight = healthLeft + healthWidth * healthPointPercentage
        val healthBottom = borderBottom - margin
        healthTop = healthBottom - healthHeight
        canvas.drawRect(
            gameDisplay.gameToDisplayCoordinatesX(healthLeft),
            gameDisplay.gameToDisplayCoordinatesY(healthTop),
            gameDisplay.gameToDisplayCoordinatesX(healthRight),
            gameDisplay.gameToDisplayCoordinatesY(healthBottom),
            healthPaint
        )
    }
}