package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import com.example.analysisgame.domain.graphics.SpriteSheet
import com.example.analysisgame.presentation.game.GameDisplay

class NPC_Elder(
    context: Context,
    positionX: Float,
    positionY: Float,
) : Circle(context, Color.RED, positionX, positionY, 64f) {


    override fun update() {

    }

    fun draw(canvas: Canvas, gameDisplay: GameDisplay, spriteSheet: SpriteSheet){
        val npc_elder = spriteSheet.getSprite(0, 0)

        val x = gameDisplay.gameToDisplayCoordinatesX(positionX).toInt()
        val y = gameDisplay.gameToDisplayCoordinatesY(positionY).toInt()

        val rect: Rect = Rect(0, 0, npc_elder.getWidth(), npc_elder.getHeight())

        canvas.drawBitmap(
            npc_elder,
            rect,
            Rect(x, y, x + rect.width(), y + rect.height()),
            null
        )
    }
}