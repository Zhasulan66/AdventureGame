package com.example.analysisgame.domain.graphics

import android.graphics.Canvas
import android.graphics.Rect

class Sprite(
    private var spriteSheet: SpriteSheet,
    private var rect: Rect
) {

    fun draw(canvas: Canvas, x: Int, y: Int) {
        val destRect: Rect = Rect(
            x,
            y,
            x + getWidth() * 4,
            y + getHeight() * 4
        )
        canvas.drawBitmap(
            spriteSheet.getBitmap(),
            rect,
            destRect,
            null
        )
        /*canvas.drawBitmap(
                spriteSheet.getBitmap(),
                rect,
                new Rect(x, y, x+getWidth(), y+getHeight()),
                null
        );*/
    }


    fun getRect(): Rect {
        return rect
    }

    fun getWidth(): Int {
        return rect.width()
    }

    fun getHeight(): Int {
        return rect.height()
    }
}