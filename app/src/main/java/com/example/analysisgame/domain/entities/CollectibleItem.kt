package com.example.analysisgame.domain.entities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import com.example.analysisgame.presentation.game.GameDisplay

class CollectibleItem(
    val type: ItemType,
    val bitmap: Bitmap,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
) {
    var isCollected = false

    fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        val scaleFactor = 3f
        val scaledWidth = width * scaleFactor
        val scaledHeight = height * scaleFactor

        val screenX = gameDisplay.gameToDisplayCoordinatesX(x)
        val screenY = gameDisplay.gameToDisplayCoordinatesY(y)
        if (!isCollected) {
            canvas.drawBitmap(bitmap, null, RectF(screenX, screenY, screenX + scaledWidth, screenY + scaledHeight), null)
        }
    }

    fun checkCollisionWithPlayer(player: Player): Boolean {
        // Rectangle boundaries
        val rectLeft = x
        val rectTop = y
        val rectRight = x + width
        val rectBottom = y + height

        // Closest point on the rectangle to the circle center
        val closestX = player.positionX.coerceIn(rectLeft, rectRight)
        val closestY = player.positionY.coerceIn(rectTop, rectBottom)

        // Distance from circle center to closest point
        val distanceX = player.positionX - closestX
        val distanceY = player.positionY - closestY

        val distanceSquared = distanceX * distanceX + distanceY * distanceY

        return distanceSquared < player.radius * player.radius
    }
}

enum class ItemType {
    KEY,
    BOOK,
    HEALTH_POTION
}