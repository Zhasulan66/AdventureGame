package com.example.analysisgame.presentation.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.entities.Character
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.entities.enemies.Skeleton
import com.example.analysisgame.domain.environments.MapManager
import com.example.analysisgame.domain.inputs.TouchEvents
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

    private var gameLoop: GameLoop
    private var touchEvents = TouchEvents(this)

    private var movePlayer = false
    private var lastTouchDiff = PointF(0f, 0f)
    private val mapManager = MapManager()

    private val player = Player()
    private val skeletons = ArrayList<Skeleton>()


    init {
        holder.addCallback(this)
        redPaint.color = Color.RED
        gameLoop = GameLoop(this)

        for (i in 0..20) skeletons.add(Skeleton(PointF(100f, 100f)))
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

            drawPlayer(canvas)
            for (skeleton in skeletons) drawCharacter(
                canvas,
                skeleton
            )

            holder.unlockCanvasAndPost(canvas) // Unlock to display changes
        }
    }

    private fun drawPlayer(c: Canvas) {
        c.drawBitmap(
            player.getGameCharType().getSprite(player.getFaceDir(), player.getAniIndex()),
            player.getHitBox().left,
            player.getHitBox().top,
            null
        )
    }

    private fun drawCharacter(canvas: Canvas, c: Character) {
        canvas.drawBitmap(
            c.getGameCharType().getSprite(c.getDirSelector(), c.getAniIndex()),
            c.getHitBox().left + cameraX,
            c.getHitBox().top + cameraY,
            null
        )
    }

    fun update(delta: Double) {
        updatePlayerMove(delta)
        player.update(delta, movePlayer)
        for (skeleton in skeletons) skeleton.update(delta)
        mapManager.setCameraValues(cameraX, cameraY)

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
            if (lastTouchDiff.x > 0) {
                player.setFaceDir(Constants.FaceDir.RIGHT)
            } else player.setFaceDir(Constants.FaceDir.LEFT)
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