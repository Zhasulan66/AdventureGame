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
        val y = player.positionY
        val distanceToPlayer = 50f
        var healthPointPercentage = player.getHealthPoints() / 5f

        //draw border
        val borderLeft = x - width / 2
        val borderRight = x + width / 2
        val borderBottom = y - distanceToPlayer
        val borderTop = borderBottom - height
        canvas.drawRect(
            gameDisplay.gameToDisplayCoordinatesX(borderLeft),
            gameDisplay.gameToDisplayCoordinatesY(borderTop),
            gameDisplay.gameToDisplayCoordinatesX(borderRight),
            gameDisplay.gameToDisplayCoordinatesY(borderBottom),
            borderPaint
        )

        //draw healthBar
        val healthWidth = width - 2 * margin
        val healthHeight = height - 2 * margin
        val healthLeft = borderLeft + margin
        val healthRight = healthLeft + healthWidth * healthPointPercentage
        val healthBottom = borderBottom - margin
        val healthTop = healthBottom - healthHeight
        canvas.drawRect(
            gameDisplay.gameToDisplayCoordinatesX(healthLeft),
            gameDisplay.gameToDisplayCoordinatesY(healthTop),
            gameDisplay.gameToDisplayCoordinatesX(healthRight),
            gameDisplay.gameToDisplayCoordinatesY(healthBottom),
            healthPaint
        )
    }
}