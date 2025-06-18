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
import com.example.analysisgame.domain.entities.npcs.NPC_archer
import com.example.analysisgame.domain.entities.npcs.NPC_paladin
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.domain.graphics.drawPauseButton
import com.example.analysisgame.domain.map.drawTiledLayer
import com.example.analysisgame.domain.map.loadTiledMap
import com.example.analysisgame.domain.map.parseLayers
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import com.example.analysisgame.presentation.viewmodel.MainViewModel


class Playing4(
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
        R.drawable.adv_game_tiles4,
        bitmapOptions
    )
    val forth_level_json = loadTiledMap(context, "forth_level.json")
    val forth_level_layer = parseLayers(forth_level_json)

    private val bitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.spritesheet_rogue,
        bitmapOptions
    )
    private val animator = Animator(bitmap)//spriteArray  spriteSheet
    private val player =
        Player(context, joystick, 200f, 600f, 32f, animator, forth_level_layer[2])//1000f, 500f

    private val skeletonList = ArrayList<Skeleton>()
    private val spellList = ArrayList<Spell>()
    private val npc_archer = NPC_archer(
        context = context, imageResId = R.drawable.npc_archer,
        positionX = 550f, positionY = 1150f,
        player, viewModel, userName
    )
    private val npc_paladin = NPC_paladin(
        context = context, imageResId = R.drawable.npc_paladin,
        positionX = 1800f, positionY = 1200f,
        player, viewModel, userName
    )
    val dialogueManager = DialogueManager()
    val items = mutableListOf<CollectibleItem>()
    private val potion_bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.health_potion)

    private var lastSpellCastTime = System.currentTimeMillis()
    private val spellCooldown = 500L // milliseconds (2 spells/second)

    private var numberOfSpellToCast = 0
    private var joystickPointerId = 0

    private val gameWinScreen = WinScreenView(context, game)
    var enemyDefeatedCount = 0
    var enemyToSpawn = 30

    private val gameOver = GameOverView(context, game)
    private var gameOverStartTime = 0L
    private var isGameOverSoundPlayed = false
    private val performance = Performance(context, gameLoop, player)
    private val gameDisplay = GameDisplay(GAME_WIDTH, GAME_HEIGHT, player)

    init {
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 1284f, 3169f, 16f, 21f))
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 2477f, 3765f, 16f, 21f))
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 2559f, 1751f, 16f, 21f))
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 3512f, 484f, 16f, 21f))
    }

    override fun render(canvas: Canvas) {
        //super.draw(canvas)
        canvas.drawColor(Color.parseColor("#dfe3ed"))

        drawTiledLayer(
            canvas,
            tileset,
            forth_level_layer[0],
            tilesetColumns = 62,
            gameDisplay = gameDisplay
        )

        drawTiledLayer(
            canvas,
            tileset,
            forth_level_layer[1],
            tilesetColumns = 62,
            gameDisplay = gameDisplay
        )

        // Draw game objects
        player.draw(canvas, gameDisplay)

        for (skeleton in skeletonList)
            skeleton.draw(canvas, gameDisplay)

        for (spell in spellList)
            spell.draw(canvas, gameDisplay)

        npc_archer.draw(canvas, gameDisplay)
        npc_paladin.draw(canvas, gameDisplay)

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
        if (enemyDefeatedCount >= 20 && npc_paladin.talkCount >= 2 && !npc_paladin.hasTalked) {
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

        if (npc_archer.isPlayerNearby(player)
            && !dialogueManager.isDialogueActive
            && !npc_archer.hasTalked
        ) {
            dialogueManager.startDialogue(npc_archer.getDialogueLines())
            npc_archer.talkCount++
            npc_archer.hasTalked = true
        }
        // Reset the flag when player walks away
        if (!npc_archer.isPlayerNearby(player)) {
            npc_archer.hasTalked = false
        }

        if (npc_paladin.isPlayerNearby(player)
            && !dialogueManager.isDialogueActive
            && !npc_paladin.hasTalked
        ) {
            dialogueManager.startDialogue(npc_paladin.getDialogueLines())
            npc_paladin.talkCount++
            npc_paladin.hasTalked = true
        }

        // Reset the flag when player walks away
        if (!npc_paladin.isPlayerNearby(player)) {
            npc_paladin.hasTalked = false
        }

        if(Skeleton.readyToSpawn() && enemyToSpawn > 0 && npc_archer.talkCount > 1) {
            skeletonList.add(
                Skeleton(
                    context, player,
                    (300..2500).random().toFloat(), (300..2500).random().toFloat()
                )
            )
            enemyToSpawn--
        }

        for (skeleton in skeletonList)
            skeleton.update()

        for (item in items) {
            if (item.checkCollisionWithPlayer(player)) {
                when (item.type) {
                    ItemType.BOOK -> {/* collect book */ }
                    ItemType.KEY -> {  }
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
                    enemyDefeatedCount++
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

                if (enemyDefeatedCount >= 20 && npc_paladin.talkCount >= 2 && !npc_paladin.hasTalked) {
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


