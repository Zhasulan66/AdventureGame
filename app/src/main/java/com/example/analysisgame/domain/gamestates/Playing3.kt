package com.example.analysisgame.domain.gamestates

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.R
import com.example.analysisgame.domain.entities.Circle
import com.example.analysisgame.domain.entities.GameOver
import com.example.analysisgame.domain.entities.Joystick
import com.example.analysisgame.domain.entities.NPC_Elder
import com.example.analysisgame.domain.entities.Performance
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.entities.Spell
import com.example.analysisgame.domain.entities.enemies.Skeleton
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.domain.graphics.SpriteSheet
import com.example.analysisgame.domain.map.Tilemap
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import kotlin.random.Random


class Playing3(val game: Game, val context: Context, gameLoop: GameLoop) : BaseState(game), GameStateInterface {

    val tilemap = Tilemap(SpriteSheet(context, R.drawable.adv_game_tiles), 3)

    private var joystickPointerId = 0

    private val animator = Animator(SpriteSheet(context, R.drawable.spritesheet_rogue))//spritesheet_rogue

    private val skeletons = ArrayList<Skeleton>()
    private val spellList = ArrayList<Spell>()

    private var skeletonAmount = 0

    private val npcElder = NPC_Elder(context, 500f, 500f)
    var dialogNum = 0

    //For UI
    private val joystick = Joystick(250f, 800f, 150f, 80f)
    private val player = Player(context, gameLoop, joystick, 1050f, 1170f, 32f, animator, tilemap) // 1080 x 2340
    //val skeleton = Skeleton(context, player, 0f, 0f)
    val gameDisplay = GameDisplay(GAME_WIDTH, GAME_HEIGHT, player)

    /*init {
        Skeleton.init(GameLoop.delta.toFloat())
    }*/
    val gameOver = GameOver(context)
    val performance = Performance(context, gameLoop)

    override fun update() {
        // Stop updating the game if the player is dead
        if (player.getHealthPoints() <= 0) {
            return
        }

        if(player.getHealthPoints() == 1 && dialogNum == 0){
            game.currentGameState = Game.GameState.DIALOG
            dialogNum = 1
        }

        joystick.update()
        player.update()

        if(Skeleton.readyToSpawn() && skeletonAmount > 0){
            skeletons.add(Skeleton(context, player, Random.nextFloat() * 1000, Random.nextFloat() * 500))
            skeletonAmount--
        }

        for (skeleton in skeletons){
            skeleton.update()
            skeleton.updateAnimation()
        }

        for (spell in spellList){
            spell.update()
        }

        val iteratorSkeleton = skeletons.iterator()
        while (iteratorSkeleton.hasNext()){
            val skeleton = iteratorSkeleton.next()
            if(Circle.isColliding(skeleton, player)){
                iteratorSkeleton.remove()
                player.setHealthPoints(player.getHealthPoints()-1)
                continue
            }

            val iteratorSpell = spellList.iterator()
            while (iteratorSpell.hasNext()) {
                val spell: Circle = iteratorSpell.next()
                // Remove enemy if it collides with a spell
                if (Circle.isColliding(spell, skeleton)) {
                    iteratorSpell.remove()
                    iteratorSkeleton.remove()
                    break
                }
            }
        }


        gameDisplay.update()

        //skeleton.update()
        //skeleton.updateAnimation()

    }

    override fun render(c: Canvas) {

        tilemap.draw(c, gameDisplay)

        player.draw(c, gameDisplay)

        npcElder.draw(c, gameDisplay, SpriteSheet(context, R.drawable.spritesheet_rogue))

        for(skeleton in skeletons)
            skeleton.draw(
                c,
                gameDisplay,
                SpriteSheet(context, R.drawable.spritesheet_skeleton)
            )

        for (spell in spellList) {
            spell.draw(c, gameDisplay)
        }
        if(game.currentGameState != Game.GameState.DIALOG){
            joystick.draw(c)
        }

        performance.draw(c)

        // Draw Game over if the player is dead
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(c)
        }


    }

    override fun touchEvents(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if(joystick.getIsPressed()){
                    // Joystick was pressed before this event -> cast spell
                    spellList.add(Spell(context, player))
                }else if (joystick.isPressed(event.x, event.y)) {
                    // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                    joystickPointerId = event.getPointerId(event.actionIndex);
                    joystick.setIsPressed(true)
                }
                else {
                    spellList.add(Spell(context, player))
                }
                //else getGame().currentGameState = Game.GameState.MENU
            }

            MotionEvent.ACTION_MOVE -> {

                if (joystick.getIsPressed()) {
                    joystick.setActuator(event.x.toDouble(), event.y.toDouble())
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if(joystickPointerId == event.getPointerId(event.actionIndex)){
                    joystick.setIsPressed(false)
                    joystick.resetActuator()
                }
            }
        }
    }
}