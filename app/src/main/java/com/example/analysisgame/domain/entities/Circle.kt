package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.example.analysisgame.presentation.game.GameDisplay
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt


abstract class Circle(
    context: Context,
    val color: Int,
    positionX: Float,
    positionY: Float,
    var radius: Float
) : GameObject(positionX, positionY) {
    val paint = Paint().apply { color = color }


    fun isColliding(obj1: Circle, obj2: Circle): Boolean {
        val distance = getDistanceBetweenObjects(obj1, obj2)
        val distanceToCollision = obj1.radius + obj2.radius
        return if (distance < distanceToCollision) true
        else false
    }

    fun isColliding(circle: Circle, rect: Rect): Boolean {
        val closestX = clamp(circle.positionX, rect.left.toFloat(), rect.right.toFloat())
        val closestY = clamp(circle.positionY, rect.top.toFloat(), rect.bottom.toFloat())
        val distance = getDistanceBetweenPoints(
            circle.positionX,
            circle.positionY,
            closestX,
            closestY
        )

        return distance <= circle.radius
    }

    private fun clamp(value: Float, min: Float, max: Float): Float {
        return max(min, min(value, max))
    }

    private fun getDistanceBetweenPoints(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x1 - x2
        val dy = y1 - y2
        return sqrt(dx * dx + dy * dy)
    }

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        canvas.drawCircle(
            gameDisplay.gameToDisplayCoordinatesX(positionX),
            gameDisplay.gameToDisplayCoordinatesY(positionY),
            radius,
            paint
        )
    }

}