package com.example.analysisgame.presentation.game

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceHolder
import androidx.navigation.NavController
import com.example.analysisgame.domain.gamestates.Menu
import com.example.analysisgame.domain.gamestates.PauseScreen
import com.example.analysisgame.domain.gamestates.Playing
import com.example.analysisgame.domain.gamestates.Playing2
import com.example.analysisgame.domain.gamestates.Playing3
import com.example.analysisgame.domain.gamestates.Playing4
import com.example.analysisgame.domain.gamestates.Playing5
import com.example.analysisgame.presentation.navigation.Screen


class Game(
    private val holder: SurfaceHolder,
    val context: Context,
    var currentLevel: Int,
    val navController: NavController
) {
    private val gameLoop = GameLoop(this, holder)
    var currentGameState: GameState =
        when(currentLevel) {
            1 -> GameState.PLAYING
            2 -> GameState.PLAYING2
            3 -> GameState.PLAYING3
            4 -> GameState.PLAYING4
            5 -> GameState.PLAYING5
            else -> GameState.MENU
        }

    private var menu: Menu = Menu(this, currentLevel, navController)
    private var pauseScreen: PauseScreen = PauseScreen(this, navController)
    private var playing: Playing = Playing(this, context, gameLoop)
    private var playing2: Playing2 = Playing2(this, context, gameLoop)
    private var playing3: Playing3 = Playing3(this, context, gameLoop)
    private var playing4: Playing4 = Playing4(this, context, gameLoop)
    private var playing5: Playing5 = Playing5(this, context, gameLoop)

    fun update() {
        when (currentGameState) {
            GameState.MENU -> menu.update()
            GameState.PLAYING -> playing.update()
            GameState.PLAYING2 -> playing2.update()
            GameState.PLAYING3 -> playing3.update()
            GameState.PLAYING4 -> playing4.update()
            GameState.PLAYING5 -> playing5.update()
            GameState.PAUSE ->  pauseScreen.update()
        }
    }

    fun render(c: Canvas) {
        //val c: Canvas = holder.lockCanvas()

        //Draw the game
        when (currentGameState) {
            GameState.MENU -> menu.render(c)
            GameState.PLAYING -> {
                playing.render(c)
            }

            GameState.PLAYING2 -> {
                playing2.render(c)
            }

            GameState.PLAYING3 -> {
                playing3.render(c)
            }

            GameState.PLAYING4 -> {
                playing4.render(c)
            }

            GameState.PLAYING5 -> {
                playing5.render(c)
            }

            GameState.PAUSE -> {
                when (currentLevel) {
                    1 -> playing.render(c)
                    2 -> playing2.render(c)
                    3 -> playing3.render(c)
                    4 -> playing4.render(c)
                    5 -> playing5.render(c)
                }
                pauseScreen.render(c)
            }
        }

        //holder.unlockCanvasAndPost(c)
    }

    fun touchEvent(event: MotionEvent): Boolean {
        when (currentGameState) {
            GameState.MENU -> menu.touchEvents(event)
            GameState.PLAYING -> playing.touchEvents(event)
            GameState.PLAYING2 -> playing2.touchEvents(event)
            GameState.PLAYING3 -> playing3.touchEvents(event)
            GameState.PLAYING4 -> playing4.touchEvents(event)
            GameState.PLAYING5 -> playing5.touchEvents(event)
            GameState.PAUSE -> pauseScreen.touchEvents(event)
        }

        return true
    }

    fun startGameLoop() {
        gameLoop.startLoop()
    }

    fun pauseGameLoop() {
        gameLoop.stopLoop()
    }


    enum class GameState {
        MENU, PAUSE, PLAYING, PLAYING2, PLAYING3, PLAYING4, PLAYING5
    }
}