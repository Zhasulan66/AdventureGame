package com.example.analysisgame.domain.gamestates

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.R
import com.example.analysisgame.domain.entities.Circle
import com.example.analysisgame.domain.entities.CollectibleItem
import com.example.analysisgame.domain.entities.GameOver
import com.example.analysisgame.domain.entities.ItemType
import com.example.analysisgame.domain.entities.Joystick
import com.example.analysisgame.domain.entities.npcs.NPC_Elder
import com.example.analysisgame.domain.entities.Performance
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.entities.Spell
import com.example.analysisgame.domain.entities.enemies.Skeleton
import com.example.analysisgame.domain.entities.npcs.NPC_Knight
import com.example.analysisgame.domain.entities.npcs.NPC_farmer
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.domain.graphics.drawPauseButton
import com.example.analysisgame.domain.map.drawTiledLayer
import com.example.analysisgame.domain.map.loadTiledMap
import com.example.analysisgame.domain.map.parseLayers
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import com.example.analysisgame.presentation.viewmodel.MainViewModel

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

    private val npc_farmer = NPC_farmer(
        context = context, imageResId = R.drawable.npc_farmer,
        positionX = 3300f, positionY = 3300f,
        player, viewModel, userName
    )
    private val npc_knight = NPC_Knight(
        context = context, imageResId = R.drawable.npc_knight,
        positionX = 2950f, positionY = 450f,
        player, viewModel, userName
    )
    val dialogueManager = DialogueManager()

    val items = mutableListOf<CollectibleItem>()
    private val book_bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.book)

    private var numberOfSpellToCast = 0
    private var joystickPointerId = 0

    private val gameOver = GameOver(context)
    private var gameOverStartTime = 0L
    private var isGameOverSoundPlayed = false
    private val performance = Performance(context, gameLoop)
    private val gameDisplay = GameDisplay(GAME_WIDTH, GAME_HEIGHT, player)

    init {
        items.add(CollectibleItem(ItemType.BOOK, book_bitmap, 2100f, 2100f, 15f, 12f))
    }

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

        npc_farmer.draw(canvas, gameDisplay)
        npc_knight.draw(canvas, gameDisplay)
        for (item in items) {
            item.draw(canvas, gameDisplay)
        }

        // Draw game panels
        joystick.draw(canvas)
        performance.draw(canvas)

        // Draw Game over if the player is dead
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(canvas)
        }

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

        if (npc_farmer.isPlayerNearby(player)
            && !dialogueManager.isDialogueActive
            && !npc_farmer.hasTalked
        ) {
            dialogueManager.startDialogue(npc_farmer.getDialogueLines())
            npc_farmer.talkCount++
            npc_farmer.hasTalked = true
        }

        // Reset the flag when player walks away
        if (!npc_farmer.isPlayerNearby(player)) {
            npc_farmer.hasTalked = false
        }

        if (npc_knight.isPlayerNearby(player)
            && !dialogueManager.isDialogueActive
            && !npc_knight.hasTalked
        ) {
            dialogueManager.startDialogue(npc_knight.getDialogueLines())
            npc_knight.talkCount++
            npc_knight.hasTalked = true
        }

        // Reset the flag when player walks away
        if (!npc_knight.isPlayerNearby(player)) {
            npc_knight.hasTalked = false
        }
        /*if(Skeleton.readyToSpawn())
            skeletonList.add(Skeleton(context, player))*/

        for (skeleton in skeletonList)
            skeleton.update()

        // Update states of all spells
        while (numberOfSpellToCast > 0) {
            spellList.add(Spell(context, player))
            println("spell amount ${spellList.size}")
            numberOfSpellToCast--
        }

        for (item in items) {
            if (item.checkCollisionWithPlayer(player)) {
                when (item.type) {
                    ItemType.BOOK -> { /* collect book */ }
                    ItemType.KEY -> { /* unlock door */ }
                    ItemType.HEALTH_POTION -> {

                    }
                }
                items.remove(item) // or mark as collected
                break // avoid ConcurrentModificationException
            }
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
