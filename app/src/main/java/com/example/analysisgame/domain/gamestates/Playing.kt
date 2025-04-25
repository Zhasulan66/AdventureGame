package com.example.analysisgame.domain.gamestates

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.R
import com.example.analysisgame.domain.entities.Joystick
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.domain.graphics.SpriteSheet
import com.example.analysisgame.domain.map.Tilemap
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.game.GameDisplay


class Playing(game: Game, context: Context) : BaseState(game), GameStateInterface {

    val tilemap = Tilemap(SpriteSheet(context, R.drawable.tileset), 1)

    private val animator = Animator(SpriteSheet(context, R.drawable.spritesheet_rogue))//spritesheet_rogue

   // private val skeletons = ArrayList<Skeleton>()
    //private val arrowList = ArrayList<Arrow>()

    //For UI
    private val joystick = Joystick(250f, 800f, 150f, 80f)
    private val player = Player(context, joystick, 1050f, 1170f, 32f, animator, tilemap); // 1080 x 2340

    val gameDisplay = GameDisplay(GAME_WIDTH, GAME_HEIGHT, player)

    /*init {
        for (i in 0..20)
            skeletons.add(Skeleton(PointF(100f, 100f)))
    }*/

    override fun update(delta: Double) {
        if(player.healthPoints <= 0) return

        joystick.update()
        player.update()

        if(player.positionX < 0 || player.positionY < 0){
            player.positionX = player.previousPositionX
            player.positionY = player.previousPositionY
        }

        //for (skeleton in skeletons) skeleton.update(delta, player)

        gameDisplay.update()
    }

    override fun render(c: Canvas) {

        tilemap.draw(c, gameDisplay)

        player.draw(c, gameDisplay)

       /* for(skeleton in skeletons)
            drawCharacter(
                c,
                skeleton
            )*/

        joystick.draw(c) //check if there no dialog later

    }

    override fun touchEvents(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (joystick.isPressed(event.x, event.y)) {
                    joystick.setIsPressed(true)
                }
                //else getGame().currentGameState = Game.GameState.MENU
            }

            MotionEvent.ACTION_MOVE -> {

                if (joystick.getIsPressed()) {
                    joystick.setActuator(event.x.toDouble(), event.y.toDouble())
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                joystick.setIsPressed(false)
                joystick.resetActuator()
            }
        }
    }
}