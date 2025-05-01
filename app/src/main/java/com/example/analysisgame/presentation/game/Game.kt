package com.example.analysisgame.presentation.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.example.analysisgame.domain.gamestates.DialogScreen
import com.example.analysisgame.domain.gamestates.Menu
import com.example.analysisgame.domain.gamestates.Playing


class Game(
    private val holder: SurfaceHolder,
    private val context: Context
) {
    private var menu: Menu = Menu(this)
    private var playing: Playing = Playing(this, context)
    private var dialogScreen: DialogScreen = DialogScreen(this)
    private val gameLoop = GameLoop(this)
    var currentGameState: GameState = GameState.MENU

    var questionNum = 0
    var currentLevel = 1
    var dialogNum = 0


    fun update(delta: Double) {
        when (currentGameState) {
            GameState.MENU -> menu.update(delta)
            GameState.PLAYING -> playing.update(delta)
            GameState.DIALOG -> {
                initQuestionNum()
                dialogScreen.update(delta)
            }
        }
    }

    fun render() {
        val c: Canvas = holder.lockCanvas()
        //c.drawColor(Color.BLUE)

        //Draw the game
        when (currentGameState) {
            GameState.MENU -> menu.render(c)
            GameState.PLAYING -> {
                c.drawColor(Color.BLUE)
                playing.render(c)
            }
            GameState.DIALOG -> {
                when(currentLevel){
                    1 -> playing.render(c)
                }
                dialogScreen.render(c)
            }
        }

        holder.unlockCanvasAndPost(c)
    }

    fun touchEvent(event: MotionEvent): Boolean {
        when (currentGameState) {
            GameState.MENU -> menu.touchEvents(event)
            GameState.PLAYING -> playing.touchEvents(event)
            GameState.DIALOG -> dialogScreen.touchEvents(event)
        }

        return true
    }

    fun startGameLoop() {
        gameLoop.startGameLoop()
    }

    enum class GameState {
        MENU, PLAYING, DIALOG
    }

    private fun initQuestionNum(){
        when(currentLevel){
            1 -> {
                if(dialogNum == 0)
                    dialogScreen.question_num = 0
                if(dialogNum == 1)
                    dialogScreen.question_num = 1
            }
        }
    }
}