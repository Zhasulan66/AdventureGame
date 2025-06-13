package com.example.analysisgame.domain.gamestates

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.R
import com.example.analysisgame.domain.entities.Circle
import com.example.analysisgame.domain.entities.CollectibleItem
import com.example.analysisgame.domain.entities.GameOverView
import com.example.analysisgame.domain.entities.ItemType
import com.example.analysisgame.domain.entities.Joystick
import com.example.analysisgame.domain.entities.Performance
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.entities.Spell
import com.example.analysisgame.domain.entities.WinScreenView
import com.example.analysisgame.domain.entities.enemies.Skeleton
import com.example.analysisgame.domain.entities.npcs.NPC_hunter
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.domain.graphics.drawPauseButton
import com.example.analysisgame.domain.map.drawTiledLayer
import com.example.analysisgame.domain.map.loadTiledMap
import com.example.analysisgame.domain.map.parseLayers
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import com.example.analysisgame.presentation.viewmodel.MainViewModel


class Playing2(
    val game: Game,
    val context: Context,
    val gameLoop: GameLoop,
    userName: String,
    viewModel: MainViewModel
) : BaseState(game), GameStateInterface {

    private val bitmapOptions = BitmapFactory.Options().apply { inScaled = false }

    private val joystick = Joystick(275f, 700f, 140f, 80f)

    private val tileset: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.adv_game_tiles2,
        bitmapOptions
    )
    val second_level_json = loadTiledMap(context, "second_level.json")
    val second_level_layer = parseLayers(second_level_json)

    private val bitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.spritesheet_rogue,
        bitmapOptions
    )
    private val animator = Animator(bitmap)//spriteArray  spriteSheet
    private val player =
        Player(context, joystick, 100f, 800f, 32f, animator, second_level_layer[2])//1000f, 500f

    private val skeletonList = ArrayList<Skeleton>()
    private val spellList = ArrayList<Spell>()
    private val npc_hunter = NPC_hunter(
        context = context, imageResId = R.drawable.npc_hunter,
        positionX = 650f, positionY = 350f,
        player, viewModel, userName
    )
    val dialogueManager = DialogueManager()
    val items = mutableListOf<CollectibleItem>()
    private val key_bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.key)
    private val potion_bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.health_potion)

    private var lastSpellCastTime = System.currentTimeMillis()
    private val spellCooldown = 500L // milliseconds (2 spells/second)

    private var numberOfSpellToCast = 0
    private var joystickPointerId = 0

    private val gameWinScreen = WinScreenView(context, game)
    var isKeyTaken = false

    private val gameOver = GameOverView(context, game)
    private var gameOverStartTime = 0L
    private var isGameOverSoundPlayed = false
    private val performance = Performance(context, gameLoop, player)
    private val gameDisplay = GameDisplay(GAME_WIDTH, GAME_HEIGHT, player)

    init {
        items.add(CollectibleItem(ItemType.KEY, key_bitmap, 1715f, 2465f, 24f, 14f))
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 284f, 2115f, 16f, 21f))

        skeletonList.add(Skeleton(context, player, 2490f, 1265f))
        skeletonList.add(Skeleton(context, player, 2667f, 1408f))
        skeletonList.add(Skeleton(context, player, 3590f, 1623f))
        skeletonList.add(Skeleton(context, player, 1982f, 2576f))
        skeletonList.add(Skeleton(context, player, 2116f, 2330f))
        skeletonList.add(Skeleton(context, player, 2116f, 2330f))
        skeletonList.add(Skeleton(context, player, 3566f, 514f))
        skeletonList.add(Skeleton(context, player, 965f, 1597f))
        skeletonList.add(Skeleton(context, player, 2263f, 3665f))
        skeletonList.add(Skeleton(context, player, 2267f, 3400f))
    }

    override fun render(canvas: Canvas) {
        //super.draw(canvas)
        canvas.drawColor(Color.parseColor("#160f09"))

        drawTiledLayer(
            canvas,
            tileset,
            second_level_layer[0],
            tilesetColumns = 17,
            gameDisplay = gameDisplay
        )

        drawTiledLayer(
            canvas,
            tileset,
            second_level_layer[1],
            tilesetColumns = 17,
            gameDisplay = gameDisplay
        )

        // Draw game objects
        player.draw(canvas, gameDisplay)

        for (skeleton in skeletonList)
            skeleton.draw(canvas, gameDisplay)

        for (spell in spellList)
            spell.draw(canvas, gameDisplay)

        npc_hunter.draw(canvas, gameDisplay)

        for (item in items) {
            item.draw(canvas, gameDisplay)
        }

        // Draw game panels
        joystick.draw(canvas)
        performance.draw(canvas)

        dialogueManager.draw(canvas)
        drawPauseButton(canvas)

        // Draw Game over if the player is dead
        if (player.getHealthPoints() <= 0) {
            gameOver.draw(canvas)
        }

        // Draw Game Win if the player wins
        if (isKeyTaken && npc_hunter.talkCount >=2
            && (player.positionX > 3550f && player.positionX < 3650f)
            && (player.positionY > 3100f && player.positionY < 3150f)) {
            gameWinScreen.draw(canvas)
        }
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

        if (npc_hunter.isPlayerNearby(player)
            && !dialogueManager.isDialogueActive
            && !npc_hunter.hasTalked
        ) {
            dialogueManager.startDialogue(npc_hunter.getDialogueLines())
            npc_hunter.talkCount++
            npc_hunter.hasTalked = true
        }

        // Reset the flag when player walks away
        if (!npc_hunter.isPlayerNearby(player)) {
            npc_hunter.hasTalked = false
        }

        for (skeleton in skeletonList)
            if(skeleton.isPlayerNearby(player)){
                skeleton.update()
            }


        for (item in items) {
            if (item.checkCollisionWithPlayer(player)) {
                when (item.type) {
                    ItemType.BOOK -> {/* collect book */ }
                    ItemType.KEY -> { isKeyTaken = true }
                    ItemType.HEALTH_POTION -> {
                        if(player.getHealthPoints() < 5){
                            player.setHealthPoints(player.getHealthPoints()+1)
                        }
                    }
                }
                items.remove(item) // or mark as collected
                break // avoid ConcurrentModificationException
            }
        }

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

                if(player.getHealthPoints() >= 0){
                    gameOver.onTouchEvent(event)
                }

                if (isKeyTaken && npc_hunter.talkCount >=2
                    && (player.positionX > 3550f && player.positionX < 3650f)
                    && (player.positionY > 3100f && player.positionY < 3150f)) {
                    gameWinScreen.onTouchEvent(event)
                }

                dialogueManager.handleTouch(event.x, event.y)

                val currentTime = System.currentTimeMillis()

                if (joystick.isPressed) {
                    if (currentTime - lastSpellCastTime >= spellCooldown) {
                        numberOfSpellToCast++
                        lastSpellCastTime = currentTime
                        SoundEffectsManager.playFireball()
                    }
                } else if (joystick.isPressed(event.x, event.y)) {
                    joystickPointerId = event.getPointerId(event.actionIndex)
                    joystick.isPressed = true
                } else {
                    if (currentTime - lastSpellCastTime >= spellCooldown) {
                        numberOfSpellToCast++
                        lastSpellCastTime = currentTime
                        SoundEffectsManager.playFireball()
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (joystick.isPressed) {
                    joystick.setActuator(event.x, event.y)
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                if (joystickPointerId == event.getPointerId(event.actionIndex)) {
                    joystick.isPressed = false
                    joystick.resetActuator()
                }
            }

            else -> {}
        }

    }

    fun pause() {
        gameLoop.stopLoop()
    }
}


