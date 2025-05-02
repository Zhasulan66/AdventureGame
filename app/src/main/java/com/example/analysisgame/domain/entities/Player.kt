package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import com.example.analysisgame.common.Utils
import com.example.analysisgame.common.Utils.Companion.circleIntersectsRect
import com.example.analysisgame.domain.gamestates.Playing
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.domain.map.Tilemap
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop


class Player(
    context: Context,
    gameLoop: GameLoop,
    val joystick: Joystick,
    positionX: Float,
    positionY: Float,
    radius: Float,
    val animator: Animator,
    val tilemap: Tilemap
) : Circle(context, Color.RED, positionX, positionY, radius)
{
    companion object {
        val SPEED_PIXELS_PER_SECOND: Double = 400.0
        val MAX_SPEED: Double = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS
        const val MAX_HEALTH_POINTS: Int = 5
    }
    private var healthPoints = MAX_HEALTH_POINTS
    private val healthBar = HealthBar(context, this)

    var previousPositionX = positionX
    var previousPositionY = positionY

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay){
        animator.draw(canvas, gameDisplay, this)

        healthBar.draw(canvas, gameDisplay)
    }

    override fun update() {
        //MAX_SPEED = (SPEED_PIXELS_PER_SECOND * GameLoop.getAverageUPS()).toFloat()
        // Update velocity based on actuator of joystick
        velocityX = (joystick.getActuatorX() * MAX_SPEED).toFloat()
        velocityY = (joystick.getActuatorY() * MAX_SPEED).toFloat()

        // Update position
        val newPositionX = positionX + velocityX
        val newPositionY = positionY + velocityY

        // Check if the new position is within the bounds of the map
        if (newPositionX - radius - 32f >= tilemap.getLeft()
            && newPositionX + radius + 64f <= tilemap.getRight()
            && newPositionY - radius - 32f >= tilemap.getTop()
            && newPositionY + radius + 64f <= tilemap.getBottom()
            && canWalkTo(newPositionX, newPositionY)
        ) {
            // Update previous position
            previousPositionX = positionX
            previousPositionY = positionY

            positionX = newPositionX
            positionY = newPositionY


            // Update direction
            if (velocityX != 0f || velocityY != 0f) {
                // Normalize velocity to get direction (unit vector of velocity)
                val distance: Float = Utils.getDistanceBetweenPoints(0f, 0f, velocityX, velocityY)
                directionX = (velocityX / distance)
                directionY = (velocityY / distance)
            }


            animator.updateAnimation(this)
        } else {
            // Player is trying to move outside of the map, so don't update position or direction
            //velocityX = 0f;
            //velocityY = 0f;
            positionX = previousPositionX
            positionY = previousPositionY
            animator.updateAnimation(this)

            println("unwalkableeeeee!")
        }

        //if (this.velocityX == 0f && this.velocityY == 0f) animator.resetAnimation()
    }

    fun canWalkTo(nextPositionX: Float, nextPositionY: Float): Boolean {
        // Calculate the indices of the tile that the player is trying to move onto
        val tileX = (nextPositionX / 64).toInt()
        val tileY = (nextPositionY / 64).toInt()

        // Get the rectangle of the tile
        val tileRect: RectF = tilemap.getTileRect(tileX, tileY)

        // Check if the player's circle intersects with the tile's rectangle
        if (circleIntersectsRect(nextPositionX, nextPositionY, radius, tileRect)) {
            return tilemap.isTileWalkable(tileY, tileX)
        }

        return false
    }

    fun checkObject(playing: Playing, gameDisplay: GameDisplay?, player: Boolean): Int {
        val index = 999

       /* for (i in 0 until playing.obj.length) {
            if (playing.obj.get(i) != null) {
                if (isColliding(this, playing.obj.get(i).rect)) {
                    playing.obj.get(i) = null
                }
            }
        }*/

        return index
    }

    fun getHealthPoints(): Int {
        return healthPoints
    }

    fun setHealthPoints(healthPoint: Int){
        if(healthPoint >= 0)
            healthPoints = healthPoint
    }

}