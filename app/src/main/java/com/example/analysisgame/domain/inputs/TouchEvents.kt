package com.example.analysisgame.domain.inputs

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import com.example.analysisgame.presentation.game.GamePanel
import kotlin.math.abs
import kotlin.math.hypot

class TouchEvents(
    private val gamePanel: GamePanel
) {
    private val xCenter = 250f
    private val yCenter = 800f
    private val radius = 150f
    private val circlePaint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private val yellowPaint: Paint = Paint().apply {
        color = Color.YELLOW
        strokeWidth = 5f
    }

    private var xTouch: Float = 0.0f
    private var yTouch: Float = 0.0f
    private var touchDown: Boolean = false

    fun draw(c: Canvas){
        c.drawCircle(xCenter, yCenter, radius, circlePaint)

        if(touchDown){
            c.drawLine(xCenter, yCenter, xTouch, yTouch, yellowPaint)
            c.drawLine(xCenter, yCenter, xTouch, yCenter, yellowPaint)
            c.drawLine(xTouch, yTouch, xTouch, yCenter, yellowPaint)
        }

    }

    fun touchEvent(event: MotionEvent): Boolean {

            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x
                    val y = event.y

                    val a = abs(x - xCenter)
                    val b = abs(y - yCenter)
                    val c = hypot(a, b)

                    if(c <= radius){
                        touchDown = true
                        xTouch = x
                        yTouch = y
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if(touchDown){
                        xTouch = event.x
                        yTouch = event.y

                        val xDiff = xTouch - xCenter
                        val yDiff = yTouch - yCenter

                        gamePanel.setPlayerMoveTrue(PointF(xDiff, yDiff))
                    }
                }
                MotionEvent.ACTION_UP -> {
                    touchDown = false
                    gamePanel.setPlayerMoveFalse()
                }
            }


        return true
    }
}