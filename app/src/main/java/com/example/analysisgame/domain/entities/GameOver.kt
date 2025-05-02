package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.analysisgame.R

class GameOver(private val context: Context) {

    fun draw(canvas: Canvas){
        val text = "Game Over"

        val x = 800f
        val y = 200f
        val paint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.gameOver)
            textSize = 150f
        }

        canvas.drawText(text, x, y, paint)
    }
}