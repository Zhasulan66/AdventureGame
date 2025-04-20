package com.example.analysisgame.domain.entities

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.pow
import kotlin.math.sqrt

class Joystick(
    centerPositionX: Float,
    centerPositionY: Float,
    private var outerCircleRadius: Float,
    private var innerCircleRadius: Float
) {

    private var outerCircleCenterPositionX = centerPositionX
    private var outerCircleCenterPositionY = centerPositionY
    private var innerCircleCenterPositionX = centerPositionX
    private var innerCircleCenterPositionY = centerPositionY

    private var innerCirclePaint = Paint()
        .apply { color = Color.BLUE
            style = Paint.Style.FILL_AND_STROKE }
    private var outerCirclePaint = Paint().apply { color = Color.GRAY
        style = Paint.Style.FILL_AND_STROKE }

    private var isPressed = false
    private var joystickCenterToTouchDistance = 0.0
    private var actuatorX = 0.0
    private var actuatorY = 0.0


    fun draw(canvas: Canvas) {
        // Draw outer circle
        canvas.drawCircle(
            outerCircleCenterPositionX,
            outerCircleCenterPositionY,
            outerCircleRadius,
            outerCirclePaint
        )

        // Draw inner circle
        canvas.drawCircle(
            innerCircleCenterPositionX,
            innerCircleCenterPositionY,
            innerCircleRadius,
            innerCirclePaint
        )
    }

    fun update() {
        updateInnerCirclePosition()
    }

    private fun updateInnerCirclePosition() {
        innerCircleCenterPositionX =
            (outerCircleCenterPositionX + actuatorX * outerCircleRadius).toFloat()
        innerCircleCenterPositionY =
            (outerCircleCenterPositionY + actuatorY * outerCircleRadius).toFloat()
    }

    fun setActuator(touchPositionX: Double, touchPositionY: Double) {
        val deltaX = touchPositionX - outerCircleCenterPositionX
        val deltaY = touchPositionY - outerCircleCenterPositionY
        val deltaDistance = sqrt(deltaX.pow(2.0) + deltaY.pow(2.0))

        if (deltaDistance < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius
            actuatorY = deltaY / outerCircleRadius
        } else {
            actuatorX = deltaX / deltaDistance
            actuatorY = deltaY / deltaDistance
        }
    }

    fun isPressed(touchPositionX: Float, touchPositionY: Float): Boolean {
        joystickCenterToTouchDistance = sqrt(

            (outerCircleCenterPositionX - touchPositionX).toDouble().pow(2)
                    + (outerCircleCenterPositionY - touchPositionY).toDouble().pow(2
        ))
        return joystickCenterToTouchDistance < outerCircleRadius
    }

    fun getIsPressed(): Boolean {
        return isPressed
    }

    fun setIsPressed(isPressed: Boolean) {
        this.isPressed = isPressed
    }

    fun getActuatorX(): Double {
        return actuatorX
    }

    fun getActuatorY(): Double {
        return actuatorY
    }

    fun resetActuator() {
        actuatorX = 0.0
        actuatorY = 0.0
    }
}