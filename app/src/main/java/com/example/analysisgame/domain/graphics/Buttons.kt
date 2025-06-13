package com.example.analysisgame.domain.graphics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

fun drawPauseButton(canvas: Canvas) {
    val buttonRect = RectF(2000f, 100f, 2200f, 200f)
    val cornerRadius = 20f

    // Draw dark gray rounded rectangle
    val buttonPaint = Paint().apply {
        color = Color.DKGRAY
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    canvas.drawRoundRect(buttonRect, cornerRadius, cornerRadius, buttonPaint)

    // Draw white triangle (play icon)
    val trianglePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // Center of the button
    val centerX = (buttonRect.left + buttonRect.right) / 2
    val centerY = (buttonRect.top + buttonRect.bottom) / 2

    // Size of the triangle
    val triangleWidth = 30f
    val triangleHeight = 40f

    // Define triangle path
    val path = android.graphics.Path().apply {
        moveTo(centerX - triangleWidth / 2, centerY - triangleHeight / 2) // left point
        lineTo(centerX + triangleWidth / 2, centerY)                      // right middle point
        lineTo(centerX - triangleWidth / 2, centerY + triangleHeight / 2) // left bottom point
        close()
    }

    canvas.drawPath(path, trianglePaint)
}