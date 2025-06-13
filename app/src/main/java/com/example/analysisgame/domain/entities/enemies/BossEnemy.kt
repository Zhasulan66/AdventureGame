package com.example.analysisgame.domain.entities.enemies

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import androidx.core.content.ContextCompat
import com.example.analysisgame.R
import com.example.analysisgame.common.Constants.BossState
import com.example.analysisgame.domain.entities.BossHealthBar
import com.example.analysisgame.domain.entities.BossSpell
import com.example.analysisgame.domain.entities.Circle
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.gamestates.SoundEffectsManager
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop

class BossEnemy(
    val context: Context,
    var x: Float,
    var y: Float,
    val player: Player,
    val skeletonList: ArrayList<Skeleton>
) :
    Circle(
        context,
        ContextCompat.getColor(context, R.color.enemy),
        x,
        y,
        100f
    ) {
    var MAX_HEALTH_POINTS = 20
    var isDead = false

    private val frameWidth = 72
    private val frameHeight = 90
    private var aniFrame = 0
    private var currentRow = BossState.IDLE
    private val scale = 4
    private val bitmapOptions = BitmapFactory.Options().apply { inScaled = false }

    private var maxFramesInRow = 8
    private var aniTick = 0
    private val aniSpeed = 5

    private var healthPoints = MAX_HEALTH_POINTS
    private val healthBar = BossHealthBar(context, this, ContextCompat.getColor(context, R.color.healthBarBoss))

    private var deathStartTime: Long = 0L
    private val DEATH_DURATION = 1000L
    var shouldBeRemoved = false

    private var stateStartTime = System.currentTimeMillis()
    private val idleDuration = 2000L
    private val moveDuration = 2000L
    private val attackDuration = 500L

    private val spellList = ArrayList<BossSpell>()
    private var alternatePattern = true // true = +, false = x
    private var timeSinceLastShot = 0L // milliseconds
    private val shotCooldown = 200L   // wait 200 ms (0.2 second) between shots
    private val summonCoolDown = 500L
    private var summonTurn = false

    override fun update() {
        if (isDead) {
            // Wait for death animation duration
            if (System.currentTimeMillis() - deathStartTime > DEATH_DURATION) {
                shouldBeRemoved = true
            }
            updateAnimation()
            return
        }

        // Update spells
        val iterator = spellList.iterator()
        while (iterator.hasNext()) {
            val spell = iterator.next()
            spell.update()

            // Remove if out of bounds (optional logic)
            if (spell.positionX < 0 || spell.positionX > 4000f ||
                spell.positionY < 0 || spell.positionY > 4000f
            ) {
                iterator.remove()
            }

            if (isColliding(spell, player)) {
                iterator.remove()
                player.setHealthPoints((player.getHealthPoints() - 1))
                SoundEffectsManager.playDamage()
                continue
            }
        }

        val currentTime = System.currentTimeMillis()
        val elapsed = currentTime - stateStartTime

        when (currentRow) {
            BossState.IDLE -> {
                if (elapsed >= idleDuration) {
                    currentRow = BossState.MOVE
                    stateStartTime = currentTime
                    currentRow = BossState.MOVE // if using sprite rows
                }
            }

            BossState.MOVE -> {
                // Move toward the player
                moveToPlayer()

                if (elapsed >= moveDuration) {
                    currentRow = BossState.ATTACK
                    stateStartTime = currentTime
                    currentRow = BossState.ATTACK // if using sprite rows
                }
            }

            BossState.ATTACK -> {
                if(summonTurn){
                    if (currentTime - timeSinceLastShot >= summonCoolDown) {
                        summonSkeletons()
                        timeSinceLastShot = currentTime
                    }

                    summonTurn = false
                }else {
                    if (currentTime - timeSinceLastShot >= shotCooldown) {
                        shootFireball()
                        timeSinceLastShot = currentTime
                    }
                    summonTurn = true
                }


                // Optionally: shoot or animate attack
                if (elapsed >= attackDuration) {
                    currentRow = BossState.IDLE
                    stateStartTime = currentTime
                    currentRow = BossState.IDLE
                }
            }

            BossState.DEATH -> {
                // Already handled in setHealthPoints()
            }
        }

        updateAnimation()
    }

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {

        val bitmap: Bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.necromancer,
            bitmapOptions
        )

        val srcRect = Rect(
            aniFrame * frameWidth,
            currentRow * frameHeight,
            (aniFrame + 1) * frameWidth,
            (currentRow + 1) * frameHeight
        )


        val destX = gameDisplay.gameToDisplayCoordinatesX(positionX).toInt() - (frameWidth * scale / 2)
        val destY = gameDisplay.gameToDisplayCoordinatesY(positionY).toInt() - (frameHeight * scale / 2)

        val destRect = Rect(
            destX,
            destY,
            destX + frameWidth * scale,
            destY + frameHeight * scale
        )

        canvas.drawBitmap(bitmap, srcRect, destRect, null)
        healthBar.draw(canvas, gameDisplay)

        for (spell in spellList)
            spell.draw(canvas, gameDisplay)
    }

    fun updateAnimation() {

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


    private fun shootFireball() {
        val angles = if (alternatePattern) {
            listOf(0.0, 90.0, 180.0, 270.0) // "+" pattern (up, right, down, left)
        } else {
            listOf(45.0, 135.0, 225.0, 315.0) // "×" pattern (diagonals)
        }

        for (angle in angles) {
            val spell = BossSpell(context, positionX, positionY, angle)
            spellList.add(spell)
        }

        alternatePattern = !alternatePattern // Toggle pattern
    }

    fun getHealthPoints(): Int {
        return healthPoints
    }

    fun setHealthPoints(healthPoint: Int){
        if(healthPoint >= 0)
            healthPoints = healthPoint

        if (healthPoint <= 0 && !isDead) {
            isDead = true
            currentRow = BossState.DEATH
            aniFrame = 0
            aniTick = 0
            deathStartTime = System.currentTimeMillis()
        }
    }

    private val SPEED_PIXELS_PER_SECOND: Double = Player.SPEED_PIXELS_PER_SECOND * 0.6
    private val MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS

    private fun moveToPlayer() {
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
    }

    fun summonSkeletons(){
        skeletonList.add(Skeleton(context, player, positionX + 50f, positionY))
        skeletonList.add(Skeleton(context, player, positionX - 50f, positionY))
    }

    fun isPlayerNearby(player: Player): Boolean {
        val distance = getDistanceBetweenObjects(this, player)
        return distance < 1000
    }
}

