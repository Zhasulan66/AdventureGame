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
import com.example.analysisgame.domain.entities.enemies.BossEnemy
import com.example.analysisgame.domain.entities.enemies.Skeleton
import com.example.analysisgame.domain.entities.npcs.NPC_Elder
import com.example.analysisgame.domain.graphics.Animator
import com.example.analysisgame.domain.graphics.drawPauseButton
import com.example.analysisgame.domain.map.drawTiledLayer
import com.example.analysisgame.domain.map.loadTiledMap
import com.example.analysisgame.domain.map.parseLayers
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.game.GameLoop
import com.example.analysisgame.presentation.viewmodel.MainViewModel


class Playing5(
    val game: Game,
    val context: Context,
    val gameLoop: GameLoop,
    val userName: String,
    viewModel: MainViewModel
) : BaseState(game), GameStateInterface {

    private val bitmapOptions = BitmapFactory.Options().apply { inScaled = false }

    private val joystick = Joystick(275f, 700f, 140f, 80f)

    private val tileset: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.adv_game_tiles2,
        bitmapOptions
    )
    val fifth_level_json = loadTiledMap(context, "fifth_level.json")
    val fifth_level_layer = parseLayers(fifth_level_json)

    private val bitmap: Bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.spritesheet_rogue,
        bitmapOptions
    )
    private val animator = Animator(bitmap)//spriteArray  spriteSheet
    private val player =
        Player(context, joystick, 300f, 3600f, 32f, animator, fifth_level_layer[2])//1000f, 500f

    private val skeletonList = ArrayList<Skeleton>()
    private val bossEnemy = BossEnemy(context, 2600f, 1400f, player, skeletonList)
    private val spellList = ArrayList<Spell>()
    private val npc_mage = NPC_Elder(
        context = context, imageResId = R.drawable.npc_mage,
        positionX = 2600f, positionY = 1400f,
        player, viewModel, userName
    )
    val dialogueManager = DialogueManager()
    val items = mutableListOf<CollectibleItem>()
    private val potion_bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.health_potion)

    private var numberOfSpellToCast = 0
    private var joystickPointerId = 0

    private val gameWinScreen = WinScreenView(context, game)
    var enemyDown = 0

    private val gameOver = GameOverView(context, game)
    private val performance = Performance(context, gameLoop, player)
    private val gameDisplay = GameDisplay(GAME_WIDTH, GAME_HEIGHT, player)

    init {
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 3413f, 3191f, 16f, 21f))
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 3358f, 1070f, 16f, 21f))
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 2488f, 470f, 16f, 21f))
        items.add(CollectibleItem(ItemType.HEALTH_POTION, potion_bitmap, 407f, 544f, 16f, 21f))
    }

    override fun render(canvas: Canvas) {
        //super.draw(canvas)
        canvas.drawColor(Color.parseColor("#160f09"))

        drawTiledLayer(
            canvas,
            tileset,
            fifth_level_layer[0],
            tilesetColumns = 17,
            gameDisplay = gameDisplay
        )

        drawTiledLayer(
            canvas,
            tileset,
            fifth_level_layer[1],
            tilesetColumns = 17,
            gameDisplay = gameDisplay
        )

        // Draw game objects
        player.draw(canvas, gameDisplay)

        for (skeleton in skeletonList)
            skeleton.draw(canvas, gameDisplay)

        if (!bossEnemy.shouldBeRemoved) {
            bossEnemy.draw(canvas, gameDisplay)
        }

        for (spell in spellList)
            spell.draw(canvas, gameDisplay)

        if(bossEnemy.shouldBeRemoved){
            npc_mage.draw(canvas, gameDisplay)
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
        if (npc_mage.talkCount >= 4 && !npc_mage.hasTalked) {
            gameWinScreen.draw(canvas)
        }
    }

    override fun update() {

        // Stop updating the game if the player is dead
        if (player.getHealthPoints() <= 0) {
            return
        }

        joystick.update()
        player.update()
        if (!bossEnemy.shouldBeRemoved && bossEnemy.isPlayerNearby(player)) {
            bossEnemy.update()
        }

        if(bossEnemy.shouldBeRemoved) {
            if (npc_mage.isPlayerNearby(player)
                && !dialogueManager.isDialogueActive
                && !npc_mage.hasTalked
            ) {
                dialogueManager.startDialogue(npc_mage.getDialogueLines())
                npc_mage.talkCount++
                npc_mage.hasTalked = true
            }

            // Reset the flag when player walks away
            if (!npc_mage.isPlayerNearby(player)) {
                npc_mage.hasTalked = false
            }
        }
        //if(Skeleton.readyToSpawn())
        //    skeletonList.add(Skeleton(context, player))

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
            if (Circle.isColliding(spell, bossEnemy)) {
                spellIterator.remove()
                bossEnemy.setHealthPoints((bossEnemy.getHealthPoints() - 1))
                continue
            }
        }

        val iteratorSkeleton = skeletonList.iterator()
        while (iteratorSkeleton.hasNext()) {
            val skeleton = iteratorSkeleton.next()
            if (Circle.isColliding(skeleton, player)) {
                iteratorSkeleton.remove()
                player.setHealthPoints((player.getHealthPoints() - 1))
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

                if (npc_mage.talkCount >= 4 && !npc_mage.hasTalked) {
                    gameWinScreen.onTouchEvent(event)
                }

                dialogueManager.handleTouch(event.x, event.y)

                if (joystick.isPressed) {
                    numberOfSpellToCast++
                } else if (joystick.isPressed(event.x, event.y)) {
                    joystickPointerId = event.getPointerId(event.actionIndex)
                    joystick.isPressed = true
                } else {
                    numberOfSpellToCast++
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





