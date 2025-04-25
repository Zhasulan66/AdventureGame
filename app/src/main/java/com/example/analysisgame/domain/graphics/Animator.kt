package com.example.analysisgame.domain.graphics

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.presentation.game.GameDisplay


class Animator(
    private var spriteSheet: SpriteSheet
) {

    private var playerAniIndexY = 0
    private var playerFaceDir = Constants.FaceDir.RIGHT
    private var aniTick = 0
    private val aniSpeed = 5

    private val prevFaceDir = Constants.FaceDir.RIGHT


    fun draw(canvas: Canvas, gameDisplay: GameDisplay, player: Player) {
        updatePlayerDir(player)
        drawFrame(
            canvas,
            gameDisplay,
            player,
            spriteSheet.getSprite(playerFaceDir, playerAniIndexY)
        )
    }

    private fun drawFrame(canvas: Canvas, gameDisplay: GameDisplay, player: Player, sprite: Bitmap?) {
        val x = gameDisplay.gameToDisplayCoordinatesX(player.positionX)
            .toInt() - sprite!!.width / 2
        val y =
            gameDisplay.gameToDisplayCoordinatesY(player.positionY).toInt() - sprite.height / 2

        val rect: Rect = Rect(0, 0, sprite.width * 2, sprite.height * 2)

        canvas.drawBitmap(
            sprite,
            rect,
            Rect(x, y, x + rect.width(), y + rect.height()),
            null
        )
    }

    fun updateAnimation(player: Player) {
        if (player.velocityX == 0f && player.velocityY == 0f){
        //return
            aniTick++
            if (aniTick >= aniSpeed) {
                aniTick = 0
                playerAniIndexY++
                if (playerAniIndexY >= 4) playerAniIndexY = 0
            }
        }
        else {
            aniTick++
            if (aniTick >= aniSpeed) {
                aniTick = 0
                playerAniIndexY++
                if (playerAniIndexY >= 6) playerAniIndexY = 0
            }
        }

    }

    fun resetAnimation() {
        aniTick = 0
        playerAniIndexY = 0
    }

    fun updatePlayerDir(player: Player) {
        if (player.velocityX != 0f || player.velocityY != 0f) {
            //if (player.velocityX > player.velocityY) {
                if (player.directionX > 0) playerFaceDir = Constants.FaceDir.RIGHT
                else playerFaceDir = Constants.FaceDir.LEFT  //}
             /*else {
                if (player.getDirectionY() > 0) playerFaceDir = Constants.FaceDir.DOWN
                else playerFaceDir = Constants.FaceDir.UP
            }*/
        }
        else {
            if (player.directionX > 0) playerFaceDir = Constants.FaceDir.IDLE_RIGHT
            else playerFaceDir = Constants.FaceDir.IDLE_LEFT
        }
    }
}