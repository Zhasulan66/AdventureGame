package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.analysisgame.R
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import kotlin.math.cos
import kotlin.math.sin

class BossSpell(
    context: Context,
    startX: Float,
    startY: Float,
    angleDeg: Double
) : Circle(
    context,
    Color.RED,
    startX,
    startY,
    25f
) {
    private val fireballBitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.dark_fireball // Replace with your actual image name
    )

    companion object {
        private const val SPEED_PIXELS_PER_SECOND = 500.0
        private val MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS
    }

    init {
        val angleRad = Math.toRadians(angleDeg)
        velocityX = (cos(angleRad) * MAX_SPEED).toFloat()
        velocityY = (sin(angleRad) * MAX_SPEED).toFloat()
    }

    override fun update() {
        positionX += velocityX
        positionY += velocityY
    }

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        val displayX = gameDisplay.gameToDisplayCoordinatesX(positionX) - radius
        val displayY = gameDisplay.gameToDisplayCoordinatesY(positionY) - radius

        val dstRect = Rect(
            displayX.toInt(),
            displayY.toInt(),
            (displayX + radius * 2).toInt(),
            (displayY + radius * 2).toInt()
        )

        val paint = Paint().apply { color = Color.RED }
        //canvas.drawCircle(displayX, displayY, radius, paint)
        canvas.drawBitmap(fireballBitmap, null, dstRect, null)
    }
}