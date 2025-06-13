package com.example.analysisgame.domain.entities.enemies

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.analysisgame.R
import com.example.analysisgame.domain.entities.Circle
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop


class Skeleton(
    context: Context,
    private val player: Player,
    val x: Float,
    val y: Float,
    val isInvencible: Boolean = false,
    val directionChar: Char = 'x', //'y'
    val distance: Float = 0f
) :
    Circle(
        context,
        ContextCompat.getColor(context, R.color.enemy),
        x,
        y,
        30f
    ) {

    private val bitmapOptions = BitmapFactory.Options().apply { inScaled = false }
    private val bitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.spritesheet_skeleton,
        bitmapOptions
    )

    private val animator = Animator(bitmap)

    companion object {
        private val SPEED_PIXELS_PER_SECOND: Double = Player.SPEED_PIXELS_PER_SECOND * 0.6
        private val MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS
        private val SPAWNS_PER_MINUTE = 60.0
        private val SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/60
        private val UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND
        private var updatesUnitNextSpawn = UPDATES_PER_SPAWN

        fun readyToSpawn(): Boolean {
            if(updatesUnitNextSpawn <= 0){
                updatesUnitNextSpawn += UPDATES_PER_SPAWN
                return true
            } else {
                updatesUnitNextSpawn--
                return false
            }
        }
    }



    override fun update() {
        if (isInvencible){
            backAndForthMovement()
        }else {
            // =========================================================================================
            //   Update velocity of the enemy so that the velocity is in the direction of the player
            // =========================================================================================
            // Calculate vector from enemy to player (in x and y)
            val distanceToPlayerX: Float = player.positionX - positionX
            val distanceToPlayerY: Float = player.positionY - positionY

            // Calculate (absolute) distance between enemy (this) and player
            val distanceToPlayer = getDistanceBetweenObjects(
                this,
                player
            )

            // Calculate direction from enemy to player
            directionX = distanceToPlayerX / distanceToPlayer
            directionY = distanceToPlayerY / distanceToPlayer

            // Set velocity in the direction to the player
            if (distanceToPlayer > 0) { // Avoid division by zero
                velocityX = (directionX * MAX_SPEED).toFloat()
                velocityY = (directionY * MAX_SPEED).toFloat()
            } else {
                velocityX = 0f
                velocityY = 0f
            }

            // =========================================================================================
            //   Update position of the enemy
            // =========================================================================================
            positionX += velocityX
            positionY += velocityY

            animator.updateAnimation(this)
        }
    }

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {

        animator.draw(
            canvas = canvas,
            gameDisplay = gameDisplay,
            this
        )
    }

    fun isPlayerNearby(player: Player): Boolean {
        val distance = getDistanceBetweenObjects(this, player)
        return distance < 500
    }

    private var patrolStartX = x
    private var patrolStartY = y
    private var patrolDirection = 1  // 1 or -1
    private var isPatrollingX = true

    fun backAndForthMovement() {
        directionX = patrolDirection.toFloat()
        velocityX = 1f
        isPatrollingX = directionChar == 'x'

        val maxOffset = distance
        val speed = (MAX_SPEED * patrolDirection).toFloat()

        if (isPatrollingX) {
            positionX += speed
            val offset = positionX - patrolStartX
            if (kotlin.math.abs(offset) >= maxOffset) {
                patrolDirection *= -1 // reverse direction
            }
        } else {
            positionY += speed
            val offset = positionY - patrolStartY
            if (kotlin.math.abs(offset) >= maxOffset) {
                patrolDirection *= -1 // reverse direction
            }
        }

        animator.updateAnimation(this)
    }


}