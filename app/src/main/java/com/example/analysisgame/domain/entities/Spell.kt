package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.analysisgame.R
import com.example.analysisgame.presentation.game.GameLoop


class Spell(context: Context, val spellcaster: Player) : Circle(
    context,
    Color.RED,
    spellcaster.positionX,
    spellcaster.positionY,
    25f
) {
    val SPEED_PIXELS_PER_SECOND: Float = 800f
    var MAX_SPEED = SPEED_PIXELS_PER_SECOND

    init {
        MAX_SPEED = (SPEED_PIXELS_PER_SECOND * GameLoop.delta).toFloat()
        velocityX = (spellcaster.directionX * MAX_SPEED).toFloat()
        velocityY = (spellcaster.directionY * MAX_SPEED).toFloat()
    }

    override fun update() {
        positionX += velocityX
        positionY += velocityY
    }

}