package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.analysisgame.R
import com.example.analysisgame.common.Utils
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.domain.map.TiledLayer
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import kotlin.math.floor


class Player(
    context: Context,
    private val joystick: Joystick,
    positionX: Float,
    positionY: Float,
    radius: Float,
    private val animator: Animator,
    private val collisionLayer: TiledLayer
) : Circle(
    context,
    ContextCompat.getColor(context, R.color.player),
    positionX,
    positionY,
    radius
) {
    companion object {
        val SPEED_PIXELS_PER_SECOND: Double = 400.0
        val MAX_SPEED: Double = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS
        const val MAX_HEALTH_POINTS: Int = 5
    }

    private var healthPoints = MAX_HEALTH_POINTS
    private val healthBar = HealthBar(context, this, ContextCompat.getColor(context, R.color.healthBarHealth))


    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        animator.draw(canvas, gameDisplay, this)
        healthBar.draw(canvas, gameDisplay)
    }

    override fun update() {

        velocityX = (joystick.actuatorX*MAX_SPEED).toFloat()
        velocityY = (joystick.actuatorY*MAX_SPEED).toFloat()

        val nextX = positionX + velocityX
        val nextY = positionY + velocityY

        if (canMoveTo(nextX, positionY)) {
            positionX = nextX
        }

        if (canMoveTo(positionX, nextY)) {
            positionY = nextY
        }

        if(velocityX != 0f || velocityY != 0f){
            // Normalize velocity to get direction (unit vector of velocity)
            val distance = Utils.getDistanceBetweenPoints(0f, 0f, velocityX, velocityY)
            directionX = velocityX/distance
            directionY = velocityY/distance
        }

        // Update animator state
        animator.updateAnimation(this)

    }

    fun setPosition(positionX: Float, positionY: Float) {
        this.positionX = positionX
        this.positionY = positionY
    }

    fun getHealthPoints(): Int {
        return healthPoints
    }

    fun setHealthPoints(healthPoint: Int){
        if(healthPoint >= 0)
            healthPoints = healthPoint
    }

    private fun canMoveTo(x: Float, y: Float): Boolean {
        val tileSize = 64
        val r = radius

        val pointsToCheck = listOf(
            Pair(x - r, y - r), // Top-left
            Pair(x + r, y - r), // Top-right
            Pair(x - r, y + r), // Bottom-left
            Pair(x + r, y + r)  // Bottom-right
        )

        for ((px, py) in pointsToCheck) {
            val tileX = floor(px / tileSize).toInt()
            val tileY = floor(py / tileSize).toInt()

            if (tileX < 0 || tileX >= collisionLayer.width || tileY < 0 || tileY >= collisionLayer.height) {
                return false // Out of bounds
            }

            val index = tileY * collisionLayer.width + tileX
            val value = collisionLayer.data[index] + 1 // -1 shifted to 0

            if (value != 0) {
                return false // Collision
            }
        }

        return true // All corners are walkable
    }

    /*private fun canMoveTo(x: Float, y: Float): Boolean {
        val tileSize = 64

        val tileX = floor(x / tileSize).toInt()
        val tileY = floor(y / tileSize).toInt()

        if (tileX < 0 || tileX >= collisionLayer.width || tileY < 0 || tileY >= collisionLayer.height) {
            return false // Out of bounds
        }

        val index = tileY * collisionLayer.width + tileX
        val value = collisionLayer.data[index] + 1 // because -1 shifted

        return value == 0 // walkable
    }*/
}