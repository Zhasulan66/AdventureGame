package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.PointF
import android.view.MotionEvent
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.entities.Character
import com.example.analysisgame.domain.entities.Joystick
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.entities.enemies.Skeleton
import com.example.analysisgame.domain.environments.MapManager
import com.example.analysisgame.presentation.game.Game
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin


class Playing(game: Game) : BaseState(game), GameStateInterface {

    private var cameraX = 0f
    private var cameraY = 0f
    private var movePlayer = false
    private val mapManager = MapManager()

    private val player = Player()
    private val skeletons = ArrayList<Skeleton>()
    //private val arrowList = ArrayList<Arrow>()

    //For UI
    private val joystick = Joystick(250f, 800f, 150f, 80f)

    init {
        for (i in 0..20)
            skeletons.add(Skeleton(PointF(100f, 100f)))
    }

    override fun update(delta: Double) {
        joystick.update()
        updatePlayerMove(delta)
        player.update(delta, movePlayer)
        for (skeleton in skeletons) skeleton.update(delta)
        mapManager.setCameraValues(cameraX, cameraY)
    }

    override fun render(c: Canvas) {
        mapManager.draw(c)

        drawPlayer(c)
        for(skeleton in skeletons)
            drawCharacter(
                c,
                skeleton
            )

        joystick.draw(c)

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
        val ratio = abs(joystick.getActuatorY()) / abs(joystick.getActuatorX())
        val angle = atan(ratio)

        var xSpeed = cos(angle)
        var ySpeed = sin(angle)

        if (xSpeed > ySpeed) {
            if (joystick.getActuatorX() > 0) {
                player.setFaceDir(Constants.FaceDir.RIGHT)
            } else player.setFaceDir(Constants.FaceDir.LEFT)
        } //else face dir for up and down

        if (joystick.getActuatorX() < 0) {
            xSpeed *= -1
        }
        if (joystick.getActuatorY() < 0) {
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

        val deltaX = (xSpeed * baseSpeed * -1).toFloat()
        val deltaY = (ySpeed * baseSpeed * -1).toFloat()

        if(mapManager.canMoveHere(
                player.getHitBox().left + cameraX * -1 + deltaX * -1 + pWidth,
                player.getHitBox().top + cameraY * -1 + deltaY * -1 + pHeight)){
            cameraX += deltaX
            cameraY += deltaY
        }

    }

    private fun setPlayerMoveFalse() {
        movePlayer = false
        player.resetAnimation()
    }

    override fun touchEvents(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (joystick.isPressed(event.x, event.y)) {
                    joystick.setIsPressed(true)
                }
                else getGame().currentGameState = Game.GameState.MENU
            }

            MotionEvent.ACTION_MOVE -> {

                if (joystick.getIsPressed()) {
                    joystick.setActuator(event.x.toDouble(), event.y.toDouble())
                    movePlayer = true
                }
            }

            MotionEvent.ACTION_UP -> {
                joystick.setIsPressed(false)
                joystick.resetActuator()
                setPlayerMoveFalse()
            }
        }
    }
}