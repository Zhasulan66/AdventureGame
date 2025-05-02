package com.example.analysisgame.presentation.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.example.analysisgame.domain.gamestates.DialogScreen
import com.example.analysisgame.domain.gamestates.Menu
import com.example.analysisgame.domain.gamestates.Playing
import com.example.analysisgame.domain.gamestates.Playing2
import com.example.analysisgame.domain.gamestates.Playing3
import com.example.analysisgame.domain.gamestates.Playing4
import com.example.analysisgame.domain.gamestates.Playing5


class Game(
    private val holder: SurfaceHolder,
    private val context: Context,
    var currentLevel: Int
) {
    private val gameLoop = GameLoop(this, holder)
    var currentGameState: GameState = GameState.MENU

    private var menu: Menu = Menu(this, currentLevel)
    private var dialogScreen: DialogScreen = DialogScreen(this)
    private var playing: Playing = Playing(this, context, gameLoop)
    private var playing2: Playing2 = Playing2(this, context, gameLoop)
    private var playing3: Playing3 = Playing3(this, context, gameLoop)
    private var playing4: Playing4 = Playing4(this, context, gameLoop)
    private var playing5: Playing5 = Playing5(this, context, gameLoop)


    var questionNum = 0
    //var currentLevel = 1
    var dialogNum = 0


    fun update() {
        when (currentGameState) {
            GameState.MENU -> menu.update()
            GameState.PLAYING -> playing.update()
            GameState.PLAYING2 -> playing2.update()
            GameState.PLAYING3 -> playing3.update()
            GameState.PLAYING4 -> playing4.update()
            GameState.PLAYING5 -> playing5.update()
            GameState.DIALOG -> {
                initQuestionNum()
                dialogScreen.update()
            }
        }
    }

    fun render(c: Canvas) {
        //val c: Canvas = holder.lockCanvas()
        //c.drawColor(Color.BLUE)

        //Draw the game
        when (currentGameState) {
            GameState.MENU -> menu.render(c)
            GameState.PLAYING -> {
                c.drawColor(Color.BLUE)
                playing.render(c)
            }
            GameState.PLAYING2 -> {
                c.drawColor(Color.BLACK)
                playing2.render(c)
            }
            GameState.PLAYING3 -> {
                c.drawColor(Color.BLACK)
                playing3.render(c)
            }
            GameState.PLAYING4 -> {
                c.drawColor(Color.BLACK)
                playing4.render(c)
            }
            GameState.PLAYING5 -> {
                c.drawColor(Color.BLACK)
                playing5.render(c)
            }
            GameState.DIALOG -> {
                when(currentLevel){
                    1 -> playing.render(c)
                }
                dialogScreen.render(c)
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
            GameState.DIALOG -> dialogScreen.touchEvents(event)
        }

        return true
    }

    fun startGameLoop() {
        gameLoop.startLoop()
    }

    fun pauseGameLoop(){
        gameLoop.stopLoop()
    }

    enum class GameState {
        MENU, DIALOG, PLAYING, PLAYING2, PLAYING3, PLAYING4, PLAYING5
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