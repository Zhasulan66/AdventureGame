package com.example.analysisgame.presentation.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.entities.GameCharacters
import com.example.analysisgame.domain.environments.MapManager
import com.example.analysisgame.domain.inputs.TouchEvents
import java.util.Random
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin


class GamePanel(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {
    private val redPaint = Paint()
    private val playerX = (GAME_WIDTH / 2).toFloat() - Constants.Sprite.SIZE
    private val playerY = (GAME_HEIGHT / 2).toFloat() - Constants.Sprite.SIZE
    private var cameraX = 0f
    private var cameraY = 0f
    private var rand = Random()
    private var gameLoop: GameLoop
    private var touchEvents = TouchEvents(this)

    private var movePlayer = false
    private var lastTouchDiff = PointF(0f, 0f)

    private var skeletonDir: Int = Constants.FaceDir.DEATH_R
    private var lastDirChange = System.currentTimeMillis()
    private var playerAniIndexX = 0
    private var playerFaceDir = Constants.FaceDir.RIGHT
    private var skeletonFaceDir = 0
    private var aniTick = 0
    private val aniSpeed = 10

    private var skeletonPos =
        PointF(rand.nextInt(GAME_WIDTH).toFloat(), rand.nextInt(GAME_HEIGHT).toFloat())

    private val mapManager = MapManager()


    init {
        holder.addCallback(this)
        redPaint.color = Color.RED
        gameLoop = GameLoop(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        return touchEvents.touchEvent(event!!)

    }

    fun render() {
        val canvas = holder.lockCanvas()
        if (canvas != null) {
            canvas.drawColor(Color.BLUE) // Clear previous frame

            mapManager.draw(canvas)

            touchEvents.draw(canvas)

            canvas.drawBitmap(
                GameCharacters.ROGUE.getSprite(playerFaceDir, playerAniIndexX),
                playerX,
                playerY,
                null
            )

            canvas.drawBitmap(
                GameCharacters.SKELETON.getSprite(skeletonFaceDir, playerAniIndexX),
                skeletonPos.x + cameraX,
                skeletonPos.y + cameraY,
                null
            )


            holder.unlockCanvasAndPost(canvas) // Unlock to display changes
        }
    }

    fun update(delta: Double) {
        updatePlayerMove(delta)
        mapManager.setCameraValues(cameraX, cameraY)

        if (System.currentTimeMillis() - lastDirChange >= 5000) {
            skeletonDir = rand.nextInt(4)
            lastDirChange = System.currentTimeMillis()
        }

        when (skeletonDir) {
            Constants.FaceDir.DEATH_R -> {
                skeletonPos.y += (delta * 300).toFloat()
                if (skeletonPos.y >= GAME_HEIGHT - 200) skeletonDir = Constants.FaceDir.DEATH_L
            }

            Constants.FaceDir.DEATH_L -> {
                skeletonPos.y -= (delta * 300).toFloat()
                if (skeletonPos.y <= 0) skeletonDir = Constants.FaceDir.DEATH_R
            }

            Constants.FaceDir.RIGHT -> {
                skeletonFaceDir = 0
                skeletonPos.x += (delta * 300).toFloat()
                if (skeletonPos.x >= GAME_WIDTH - 100) skeletonDir = Constants.FaceDir.LEFT
            }

            Constants.FaceDir.LEFT -> {
                skeletonFaceDir = 1
                skeletonPos.x -= (delta * 300).toFloat()
                if (skeletonPos.x <= 0) skeletonDir = Constants.FaceDir.RIGHT
            }
        }

        updateAnimation()
    }

    private fun updatePlayerMove(delta: Double) {
        if (!movePlayer) {
            return
        }

        val baseSpeed = (delta * 300).toFloat()
        val ratio = abs(lastTouchDiff.y) / abs(lastTouchDiff.x)
        val angle = atan(ratio)

        var xSpeed = cos(angle)
        var ySpeed = sin(angle)

        if (xSpeed > ySpeed) {
            playerFaceDir = if (lastTouchDiff.x > 0) {
                Constants.FaceDir.RIGHT
            } else Constants.FaceDir.LEFT
        } //else face dir for up and down

        if (lastTouchDiff.x < 0) {
            xSpeed *= -1
        }
        if (lastTouchDiff.y < 0) {
            ySpeed *= -1
        }

        var pWidth = Constants.Sprite.SIZE * 2
        var pHeight = Constants.Sprite.SIZE * 2

        if(xSpeed <= 0){
            pWidth = 0
        }
        if(ySpeed <= 0){
            pHeight = 0
        }

        val deltaX = xSpeed * baseSpeed * -1
        val deltaY = ySpeed * baseSpeed * -1

        if(mapManager.canMoveHere(
                playerX + cameraX * -1 + deltaX * -1 + pWidth,
                playerY + cameraY * -1 + deltaY * -1 + pHeight)){
            cameraX += deltaX
            cameraY += deltaY
        }

    }

    private fun updateAnimation() {
        if (!movePlayer) {
            when (playerFaceDir) {
                Constants.FaceDir.RIGHT -> playerFaceDir = Constants.FaceDir.IDLE_RIGHT
                Constants.FaceDir.LEFT -> playerFaceDir = Constants.FaceDir.IDLE_LEFT
            }
            aniTick++
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                playerAniIndexX++
                if (playerAniIndexX >= 4)
                    playerAniIndexX = 0
            }
        } else {
            aniTick++
            if (aniTick >= aniSpeed) {
                aniTick = 0;
                playerAniIndexX++
                if (playerAniIndexX >= 6)
                    playerAniIndexX = 0
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        gameLoop.startGameLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    fun setPlayerMoveTrue(lastTouchDiff: PointF) {
        movePlayer = true
        this.lastTouchDiff = lastTouchDiff

    }

    fun setPlayerMoveFalse() {
        movePlayer = false
    }

}