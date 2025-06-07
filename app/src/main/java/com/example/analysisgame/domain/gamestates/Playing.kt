package com.example.analysisgame.domain.gamestates

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
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
import com.example.analysisgame.domain.graphics.drawPauseButton
import com.example.analysisgame.domain.map.drawTiledLayer
import com.example.analysisgame.domain.map.loadTiledMap
import com.example.analysisgame.domain.map.parseLayers
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import com.example.analysisgame.presentation.viewmodel.MainViewModel
import kotlin.random.Random

class Playing(
    val game: Game,
    val context: Context,
    val gameLoop: GameLoop,
    userName: String,
    viewModel: MainViewModel
) : BaseState(game), GameStateInterface {


    //private var gameLoop = GameLoop(this, surfaceHolder = holder)
    private val bitmapOptions = BitmapFactory.Options().apply { inScaled = false }

    private val joystick = Joystick(275f, 700f, 140f, 80f)

    private val tileset: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.adv_game_tiles,
        bitmapOptions
    )
    val first_level_json = loadTiledMap(context, "first_level.json")
    val first_level_layer = parseLayers(first_level_json)

    private val bitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.spritesheet_rogue,
        bitmapOptions
    )
    private val animator = Animator(bitmap)//spriteArray  spriteSheet
    private val player =
        Player(context, joystick, 300f, 3500f, 32f, animator, first_level_layer[2])//1000f, 500f

    private val skeletonList = ArrayList<Skeleton>()
    private val spellList = ArrayList<Spell>()
    private val npc = NPC_Elder(
        context = context,
        imageResId = R.drawable.npc_img,
        positionX = 2200f,
        positionY = 2200f,
        player,
        viewModel,
        userName
    )
    val dialogueManager = DialogueManager()

    private var numberOfSpellToCast = 0
    private var joystickPointerId = 0

    private val gameOver = GameOver(context)
    private var gameOverStartTime = 0L
    private var isGameOverSoundPlayed = false
    private val performance = Performance(context, gameLoop)
    private val gameDisplay = GameDisplay(GAME_WIDTH, GAME_HEIGHT, player)

    override fun render(canvas: Canvas) {
        //super.draw(canvas)
        canvas.drawColor(Color.parseColor("#3e92d1")) //grass green "#337903"

        drawTiledLayer(
            canvas,
            tileset,
            first_level_layer[0],
            tilesetColumns = 10,
            gameDisplay = gameDisplay
        )

        drawTiledLayer(
            canvas,
            tileset,
            first_level_layer[1],
            tilesetColumns = 10,
            gameDisplay = gameDisplay
        )

        // Draw game objects
        player.draw(canvas, gameDisplay)

        for (skeleton in skeletonList)
            skeleton.draw(canvas, gameDisplay)

        for (spell in spellList)
            spell.draw(canvas, gameDisplay)

        npc.draw(canvas, gameDisplay)

        // Draw game panels
        joystick.draw(canvas)
        performance.draw(canvas)

        // Draw Game over if the player is dead
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(canvas)
        }

        /*dialogueManager.getCurrentLine()?.let { line ->
            val paint = Paint().apply {
                color = Color.WHITE
                textSize = 50f
            }
            val bgPaint = Paint().apply { color = Color.BLACK }

            val rect = RectF(100f, canvas.height - 300f, canvas.width - 100f, canvas.height - 100f)
            canvas.drawRoundRect(rect, 20f, 20f, bgPaint)
            canvas.drawText(line, rect.left + 30f, rect.top + 100f, paint)
        }*/
        dialogueManager.draw(canvas)

        drawPauseButton(canvas)
    }

    override fun update() {
        // Stop updating the game if the player is dead
        if (player.getHealthPoints() <= 0) {
            // Play Game Over sound only once
            if (!isGameOverSoundPlayed) {
                isGameOverSoundPlayed = true
                MusicManager.stopMusic()
                SoundEffectsManager.playGameOver()
                gameOverStartTime = System.currentTimeMillis()
            }

            // Wait 2 second, then go to Game Over state
            if (System.currentTimeMillis() - gameOverStartTime > 2000) {
                //game.currentGameState = Game.GameState.MENU // or GAME_OVER
                SoundEffectsManager.release()
            }

            return // Stop the rest of the update
        }

        joystick.update()
        player.update()

        if (npc.isPlayerNearby(player)
            && !dialogueManager.isDialogueActive
            && !npc.hasTalked
        ) {
            dialogueManager.startDialogue(npc.getDialogueLines())
            npc.talkCount++
            npc.hasTalked = true
        }

        // Reset the flag when player walks away
        if (!npc.isPlayerNearby(player)) {
            npc.hasTalked = false
        }
        if(Skeleton.readyToSpawn())
            skeletonList.add(Skeleton(context, player))

        for (skeleton in skeletonList)
            skeleton.update()

        // Update states of all spells
        while (numberOfSpellToCast > 0) {
            spellList.add(Spell(context, player))
            println("spell amount ${spellList.size}")
            numberOfSpellToCast--
        }

        val spellIterator = spellList.iterator()
        while (spellIterator.hasNext()) {
            val spell = spellIterator.next()
            spell.update()

            // Remove spell if it goes outside game boundaries
            if (spell.positionX < 0 || spell.positionX > 4000f ||
                spell.positionY < 0 || spell.positionY > 4000f
            ) {
                spellIterator.remove()
            }
        }

        val iteratorSkeleton = skeletonList.iterator()
        while (iteratorSkeleton.hasNext()) {
            val skeleton = iteratorSkeleton.next()
            if (Circle.isColliding(skeleton, player)) {
                iteratorSkeleton.remove()
                player.setHealthPoints((player.getHealthPoints() - 1))
                SoundEffectsManager.playDamage()
                continue
            }

            val iteratorSpell = spellList.iterator()
            while (iteratorSpell.hasNext()) {
                val spell = iteratorSpell.next()
                if (Circle.isColliding(spell, skeleton)) {
                    iteratorSpell.remove()
                    iteratorSkeleton.remove()
                    break
                }
            }
        }

        // Update gameDisplay so that it's center is set to the new center of the player's
        // game coordinates
        gameDisplay.update()
    }

    override fun touchEvents(event: MotionEvent) {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> {
                if(event.x > 2000 && event.y < 200){
                    game.currentGameState = Game.GameState.PAUSE
                    MusicManager.pauseMusic()
                }

                dialogueManager.handleTouch(event.x, event.y)

                if (joystick.isPressed) {
                    numberOfSpellToCast++
                    SoundEffectsManager.playFireball()
                } else if (joystick.isPressed(event.x, event.y)) {
                    joystickPointerId = event.getPointerId(event.actionIndex)
                    joystick.isPressed = true
                } else {
                    numberOfSpellToCast++
                    SoundEffectsManager.playFireball()
                }
                //return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (joystick.isPressed) {
                    joystick.setActuator(event.x, event.y)
                }
                //return true
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                if (joystickPointerId == event.getPointerId(event.actionIndex)) {
                    joystick.isPressed = false
                    joystick.resetActuator()
                }
                //return true
            }

            else -> {}
        }

        //return super.onTouchEvent(event)

    }

    fun pause() {
        gameLoop.stopLoop()
    }
}
