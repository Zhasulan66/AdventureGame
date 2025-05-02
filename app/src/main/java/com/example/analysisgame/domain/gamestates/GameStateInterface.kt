package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.view.MotionEvent

interface GameStateInterface {
    fun update()
    fun render(c: Canvas)
    fun touchEvents(event: MotionEvent)
}