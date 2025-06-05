package com.example.analysisgame.presentation.game

import com.example.analysisgame.domain.entities.GameObject

class GameDisplay(
    widthPixels: Int,
    heightPixels: Int,
    private val centerObject: GameObject
) {
    private val displayCenterX = widthPixels/2f
    private val displayCenterY = heightPixels/2f
    private var gameToDisplayCoordinatesOffsetX = 0f
    private var gameToDisplayCoordinatesOffsetY = 0f

    private var gameCenterX = 0f
    private var gameCenterY = 0f


    fun update(){
        gameCenterX = centerObject.positionX
        gameCenterY = centerObject.positionY

        gameToDisplayCoordinatesOffsetX = displayCenterX - gameCenterX
        gameToDisplayCoordinatesOffsetY = displayCenterY - gameCenterY
    }

    fun gameToDisplayCoordinatesX(x: Float): Float{
        return x + gameToDisplayCoordinatesOffsetX
    }

    fun gameToDisplayCoordinatesY(y: Float): Float{
        return y + gameToDisplayCoordinatesOffsetY
    }
}