package com.example.analysisgame.domain.entities

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.pow
import kotlin.math.sqrt

class Joystick(
    private val outerCircleCenterPositionX: Float,
    private val outerCircleCenterPositionY: Float,
    private val outerCircleRadius: Float,
    private val innerCircleRadius: Float
) {
    private var innerCircleCenterPositionX: Float
    private var innerCircleCenterPositionY: Float


    private val innerCirclePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
    }
    private val outerCirclePaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL_AND_STROKE
    }
    var isPressed: Boolean = false
    private var joystickCenterToTouchDistance = 0f
    var actuatorX: Float = 0f
        private set
    var actuatorY: Float = 0f
        private set

    init {
        // Outer and inner circle make up the joystick
        innerCircleCenterPositionX = outerCircleCenterPositionX
        innerCircleCenterPositionY = outerCircleCenterPositionY

    }

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
            (outerCircleCenterPositionX + actuatorX * outerCircleRadius)
        innerCircleCenterPositionY =
            (outerCircleCenterPositionY + actuatorY * outerCircleRadius)
    }

    fun setActuator(touchPositionX: Float, touchPositionY: Float) {
        val deltaX = touchPositionX - outerCircleCenterPositionX
        val deltaY = touchPositionY - outerCircleCenterPositionY
        val deltaDistance = sqrt(deltaX.pow(2) + deltaY.pow(2))

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
            (outerCircleCenterPositionX - touchPositionX).pow(2) +
                    (outerCircleCenterPositionY - touchPositionY).pow(2)
        )
        return joystickCenterToTouchDistance < outerCircleRadius
    }

    fun resetActuator() {
        actuatorX = 0f
        actuatorY = 0f
    }
}