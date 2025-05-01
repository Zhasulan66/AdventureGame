package com.example.analysisgame.domain.entities

import android.graphics.Canvas
import com.example.analysisgame.presentation.game.GameDisplay
import kotlin.math.pow
import kotlin.math.sqrt

abstract class GameObject(
    positionX: Float,
    positionY: Float
) {
    var positionX = 0f
    var positionY = 0f
    var velocityX = 0f
    var velocityY = 0f
    var directionX = 1f
    var directionY = 0f

     init {
        this.positionX = positionX
        this.positionY = positionY
    }

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