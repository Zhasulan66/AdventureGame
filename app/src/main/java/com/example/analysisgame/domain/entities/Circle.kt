package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.example.analysisgame.presentation.game.GameDisplay


abstract class Circle(
    context: Context,
    myColor: Int,
    positionX: Float,
    positionY: Float,
    var radius: Float
) : GameObject(positionX, positionY) {
    // Set colors of circle
    var paint = Paint().apply { color = myColor }

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        canvas.drawCircle(
            gameDisplay.gameToDisplayCoordinatesX(positionX),
            gameDisplay.gameToDisplayCoordinatesY(positionY),
            radius,
            paint
        )
    }

    companion object {
        fun isColliding(obj1: Circle, obj2: Circle): Boolean {
            val distance = getDistanceBetweenObjects(obj1, obj2)
            val distanceToCollision = obj1.radius + obj2.radius
            return if (distance < distanceToCollision)
                true
            else
                false
        }
    }
}