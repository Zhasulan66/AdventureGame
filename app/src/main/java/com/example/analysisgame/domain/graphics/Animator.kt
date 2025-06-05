package com.example.analysisgame.domain.graphics

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.entities.Circle
import com.example.analysisgame.presentation.game.GameDisplay


class Animator(private val spriteSheet: Bitmap) {

    private val frameWidth = 32
    private val frameHeight = 32
    private val scale = 3 // Scale 2x → 64x64

    private var aniTick = 0
    private val aniSpeed = 5
    private var aniFrame = 0

    private var currentRow = Constants.FaceDir.IDLE_LEFT
    private var maxFramesInRow = 4

    fun updateAnimation(creature: Circle) {
        val moving = creature.velocityX != 0f || creature.velocityY != 0f

        // Decide which animation row to use
        currentRow = when {
            moving && creature.directionX > 0 -> Constants.FaceDir.RIGHT
            moving && creature.directionX < 0 -> Constants.FaceDir.LEFT
            !moving && creature.directionX >= 0 -> Constants.FaceDir.IDLE_RIGHT
            else -> Constants.FaceDir.IDLE_LEFT
        }

        maxFramesInRow = if (currentRow <= Constants.FaceDir.LEFT) 6 else 4

        // Update frame index
        aniTick++
        if (aniTick >= aniSpeed) {
            aniTick = 0
            aniFrame++
            if (aniFrame >= maxFramesInRow) {
                aniFrame = 0
            }
        }
    }

    fun draw(canvas: Canvas, gameDisplay: GameDisplay, creature: Circle) {
        val srcRect = Rect(
            aniFrame * frameWidth,
            currentRow * frameHeight,
            (aniFrame + 1) * frameWidth,
            (currentRow + 1) * frameHeight
        )

        val destX = gameDisplay.gameToDisplayCoordinatesX(creature.positionX).toInt() - (frameWidth * scale / 2)
        val destY = gameDisplay.gameToDisplayCoordinatesY(creature.positionY).toInt() - (frameHeight * scale / 2)

        val destRect = Rect(
            destX,
            destY,
            destX + frameWidth * scale,
            destY + frameHeight * scale
        )

        canvas.drawBitmap(spriteSheet, srcRect, destRect, null)
    }

    fun resetAnimation() {
        aniTick = 0
        aniFrame = 0
    }
}