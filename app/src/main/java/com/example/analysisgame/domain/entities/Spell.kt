package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import com.example.analysisgame.R
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop


class Spell(
    context: Context,
    spellCaster: Player
) : Circle(
    context, Color.GREEN, spellCaster.positionX, spellCaster.positionY, 25f
) {
    companion object {
        val SPEED_PIXELS_PER_SECOND = 800.0
        val MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS
    }

    init {
        velocityX = (spellCaster.directionX * MAX_SPEED).toFloat()
        velocityY = (spellCaster.directionY * MAX_SPEED).toFloat()
    }

    override fun update() {
        positionX += velocityX
        positionY += velocityY
    }

    private val fireballBitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.fireball // Replace with your actual image name
    )

    private val bitmapWidth = 50  // Adjust for your desired size
    private val bitmapHeight = 50

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        val displayX = gameDisplay.gameToDisplayCoordinatesX(positionX) - radius
        val displayY = gameDisplay.gameToDisplayCoordinatesY(positionY) - radius

        val dstRect = Rect(
            displayX.toInt(),
            displayY.toInt(),
            (displayX + radius * 2).toInt(),
            (displayY + radius * 2).toInt()
        )

        canvas.drawBitmap(fireballBitmap, null, dstRect, null)
    }

}