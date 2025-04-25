package com.example.analysisgame.presentation.game

import android.graphics.Rect
import com.example.analysisgame.domain.entities.GameObject

class GameDisplay(
    private val widthPixels: Int,
    private val heightPixels: Int,
    private val centerObject: GameObject  //center object is player
) {
    val DISPLAY_RECT: Rect = Rect(0, 0, widthPixels, heightPixels)
    private var gameToDisplayCoordinateOffsetX = 0f
    private var gameToDisplayCoordinateOffsetY = 0f
    private var gameCenterX = 0f
    private var gameCenterY = 0f
    private var displayCenterX = widthPixels / 2f
    private var displayCenterY = heightPixels / 2f


    fun update() {
        gameCenterX = centerObject.positionX
        gameCenterY = centerObject.positionY

        gameToDisplayCoordinateOffsetX = displayCenterX - gameCenterX
        gameToDisplayCoordinateOffsetY = displayCenterY - gameCenterY
    }

    fun gameToDisplayCoordinatesX(x: Float): Float {
        return x + gameToDisplayCoordinateOffsetX
    }

    fun gameToDisplayCoordinatesY(y: Float): Float {
        return y + gameToDisplayCoordinateOffsetY
    }

    fun getGameRect(): Rect {
        return Rect(
            (gameCenterX - widthPixels / 2).toInt(),
            (gameCenterY - heightPixels / 2).toInt(),
            (gameCenterX + widthPixels / 2).toInt(),
            (gameCenterY + heightPixels / 2).toInt()
        )
    }
}