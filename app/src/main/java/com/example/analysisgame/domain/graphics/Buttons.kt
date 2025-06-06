package com.example.analysisgame.domain.graphics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

fun drawPauseButton(canvas: Canvas){
    val paint = Paint().apply {
        color = Color.MAGENTA
    }
    canvas.drawRect(2000f, 100f, 2200f, 200f, paint)
}