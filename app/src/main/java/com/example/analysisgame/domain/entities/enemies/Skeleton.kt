package com.example.analysisgame.domain.entities.enemies

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.entities.Circle
import com.example.analysisgame.domain.entities.GameObject
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.graphics.SpriteSheet
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import kotlin.random.Random


class Skeleton(
    context: Context,
    val player: Player,
    positionX: Float,
    positionY: Float,
) : Circle(
    context, Color.BLUE,
    positionX = positionX,
    positionY = positionY,
    radius = 60f
) {

   val SPEED_PIXELS_PER_SECOND = 400f * 0.6f //* player.MAX_SPEED
    var MAX_SPEED = SPEED_PIXELS_PER_SECOND
     /*val SPAWNS_PER_MINUTE = 20f
    val SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60f
    val UPDATES_PER_SPAWN = GameLoop.delta / SPAWNS_PER_SECOND
    var updatesUntilNextSpawn = UPDATES_PER_SPAWN*/


    private var skeletonAniIndexY = 0
    private var skeletonFaceDir = Constants.FaceDir.RIGHT
    private var aniTick = 0
    private val aniSpeed = 5

    /*init {
        MAX_SPEED = (SPEED_PIXELS_PER_SECOND / GameLoop.delta).toFloat()
    }*/


    companion object {
        private const val SPAWNS_PER_MINUTE = 4f
        private const val SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60f
        private var UPDATES_PER_SPAWN = GameLoop.delta / SPAWNS_PER_SECOND
        private var updatesUntilNextSpawn = UPDATES_PER_SPAWN

       /* fun init(gameLoopDelta: Float) {
            UPDATES_PER_SPAWN = gameLoopDelta / SPAWNS_PER_SECOND
            updatesUntilNextSpawn = UPDATES_PER_SPAWN
        }*/

        fun readyToSpawn(): Boolean {
            if (updatesUntilNextSpawn <= 0) {
                updatesUntilNextSpawn += UPDATES_PER_SPAWN
                return true
            } else {
                updatesUntilNextSpawn--
                return false
            }
        }
    }


    fun draw(canvas: Canvas, gameDisplay: GameDisplay, spriteSheet: SpriteSheet) {
        updateEnemyDir()

        val enemy = spriteSheet.getSprite(skeletonFaceDir, skeletonAniIndexY)

        val x = gameDisplay.gameToDisplayCoordinatesX(positionX).toInt()
        val y = gameDisplay.gameToDisplayCoordinatesY(positionY).toInt()

        val rect: Rect = Rect(0, 0, enemy.width, enemy.height)

        canvas.drawBitmap(
            enemy,
            rect,
            Rect(x, y, x + rect.width(), y + rect.height()),
            null
        )

    }

    override fun update() {
        MAX_SPEED = (SPEED_PIXELS_PER_SECOND * GameLoop.delta ).toFloat()

        // =========================================================================================
        //   Update velocity of the enemy so that the velocity is in the direction of the player
        // =========================================================================================
        // Calculate vector from enemy to player (in x and y)
        val distanceToPlayerX = (player.positionX - positionX)
        val distanceToPlayerY = (player.positionY - positionY)


        // Calculate (absolute) distance between enemy (this) and player
        val distanceToPlayer = getDistanceBetweenObjects(this, player)


        // Calculate direction from enemy to player
        directionX = distanceToPlayerX / distanceToPlayer
        directionY = distanceToPlayerY / distanceToPlayer


        // Set velocity in the direction to the player
        if (distanceToPlayer > 0) { // Avoid division by zero
            velocityX = (directionX * MAX_SPEED).toFloat()
            velocityY = (directionY * MAX_SPEED).toFloat()
        } /*else {
            velocityX = 0f
            velocityY = 0f
        }*/


        // =========================================================================================
        //   Update position of the enemy
        // =========================================================================================
        positionX += velocityX
        positionY += velocityY
    }

    private fun updateEnemyDir() {
        if (velocityX != 0f || velocityY != 0f) {
            //if (velocityX > velocityY) {
            if (velocityX > 0) skeletonFaceDir = Constants.FaceDir.RIGHT
            else skeletonFaceDir = Constants.FaceDir.LEFT
            /*} else {
                if (velocityY > 0) skeletonFaceDir = Constants.FaceDir.DOWN
                else skeletonFaceDir = Constants.FaceDir.UP
            }*/
        }
    }

    fun updateAnimation() {
        if (velocityX == 0f && velocityY == 0f) return
        aniTick++
        if (aniTick >= aniSpeed) {
            aniTick = 0
            skeletonAniIndexY++
            if (skeletonAniIndexY >= 4) skeletonAniIndexY = 0
        }
    }
}