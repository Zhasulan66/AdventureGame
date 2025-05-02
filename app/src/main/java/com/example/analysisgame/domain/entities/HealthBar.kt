package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.analysisgame.R
import com.example.analysisgame.presentation.game.GameDisplay



class HealthBar(
    context: Context,
    val player: Player
) {

    private val borderPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.healthBarBorder)
    }
    private val healthPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.healthBarHealth)
    }
    private val width = 100f
    private val height = 20f
    private val margin = 2f

    fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        val x = player.positionX
        val distanceToPlayer = 100f
        val healthPointPercentage = player.getHealthPoints().toFloat() / 5f //player max health point = 5
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