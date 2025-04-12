package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.entities.Character
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.entities.enemies.Skeleton
import com.example.analysisgame.domain.environments.MapManager
import com.example.analysisgame.presentation.game.Game
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin


class Playing(game: Game) : BaseState(game), GameStateInterface {

    private var cameraX = 0f
    private var cameraY = 0f
    private var movePlayer = false
    private var lastTouchDiff = PointF(0f, 0f)
    private val mapManager = MapManager()

    private val player = Player()
    private val skeletons = ArrayList<Skeleton>()

    //For UI
    private val xCenter = 250f
    private val yCenter = 800f
    private val radius = 150f
    private val circlePaint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }
    private var xTouch = 0f
    private var yTouch = 0f
    private var touchDown = false

    init {
        for (i in 0..20)
            skeletons.add(Skeleton(PointF(100f, 100f)))
    }

    override fun update(delta: Double) {
        updatePlayerMove(delta)
        player.update(delta, movePlayer)
        for (skeleton in skeletons) skeleton.update(delta)
        mapManager.setCameraValues(cameraX, cameraY)
    }

    override fun render(c: Canvas) {
        mapManager.draw(c)
        drawUI(c)

        drawPlayer(c)
        for(skeleton in skeletons)
            drawCharacter(
                c,
                skeleton
            )

    }

    private fun drawUI(c: Canvas){
        c.drawCircle(xCenter, yCenter, radius, circlePaint)
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
                player.getHitBox().left + cameraX * -1 + deltaX * -1 + pWidth,
                player.getHitBox().top + cameraY * -1 + deltaY * -1 + pHeight)){
            cameraX += deltaX
            cameraY += deltaY
        }

    }

    private fun setPlayerMoveTrue(lastTouchDiff: PointF) {
        movePlayer = true
        this.lastTouchDiff = lastTouchDiff
    }

    private fun setPlayerMoveFalse() {
        movePlayer = false
        player.resetAnimation()
    }

    override fun touchEvents(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y

                val a = abs((x - xCenter))
                val b = abs((y - yCenter))
                val c = hypot(a, b)

                if (c <= radius) {
                    touchDown = true
                    xTouch = x
                    yTouch = y
                } else getGame().currentGameState = Game.GameState.MENU
            }

            MotionEvent.ACTION_MOVE -> {
                if (touchDown) {
                    xTouch = event.x
                    yTouch = event.y

                    val xDiff = xTouch - xCenter
                    val yDiff = yTouch - yCenter

                    setPlayerMoveTrue(PointF(xDiff, yDiff))
                }
            }

            MotionEvent.ACTION_UP -> {
                touchDown = false
                setPlayerMoveFalse()
            }
        }
    }
}