package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Color
import com.example.analysisgame.presentation.game.GameLoop


class Spell(context: Context, spellCaster: Player) : Circle(
    context,
    Color.RED,
    spellCaster.positionX,
    spellCaster.positionY,
    25f
) {
    companion object {
        val SPEED_PIXELS_PER_SECOND = 800.0
        val MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS
    }

    init {
        velocityX = (spellCaster.directionX * MAX_SPEED).toFloat()
        velocityY = (spellCaster.directionY * MAX_SPEED).toFloat()
    }

    override fun update() {
        positionX += velocityX
        positionY += velocityY
    }

}