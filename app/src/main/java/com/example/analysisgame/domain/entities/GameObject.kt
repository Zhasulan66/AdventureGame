package com.example.analysisgame.domain.entities

import android.graphics.Canvas
import com.example.analysisgame.presentation.game.GameDisplay
import kotlin.math.pow
import kotlin.math.sqrt

abstract class GameObject(
    var positionX: Float,
    var positionY: Float
) {

    var velocityX: Float = 0f
    var velocityY: Float = 0f
    var directionX: Float = 1f
    var directionY: Float = 0f

    abstract fun draw(canvas: Canvas, gameDisplay: GameDisplay)
    abstract fun update()

    companion object {
        fun getDistanceBetweenObjects(obj1: GameObject, obj2: GameObject): Float {
            return sqrt(
                (obj2.positionX - obj1.positionX).pow(2) +
                        (obj2.positionY - obj1.positionY).pow(2)
            )
        }
    }
}