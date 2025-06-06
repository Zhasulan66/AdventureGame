package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import androidx.navigation.NavController
import com.example.analysisgame.R
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.navigation.Screen

class PauseScreen(val game: Game, val navController: NavController) : BaseState(game), GameStateInterface {
    val alpha1 = Color.argb(120, 0, 0, 0)
    val paint_blackFill = Paint().apply {
        style = Paint.Style.FILL
        color = alpha1
    }
    val paint_stroke = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.WHITE
    }
    val paint_textB = Paint().apply {
        textSize = 50f
        color = Color.WHITE
    }
    val paint_text = Paint().apply {
        textSize = 40f
        color = Color.WHITE
    }
    val cornerRadius = 20f

    val question = "PAUSE MENU"
    val big_rect = RectF(200f, 100f, 2000f, 300f)
    val rectOp1 = RectF(2000f, 100f, 2200f, 200f)

    //music and sound effect toggle
    val music_btn = RectF(200f, 400f, 1000f, 500f)
    val sound_btn = RectF(1200f, 400f, 2000f, 500f)

    val quit_btn = RectF(200f, 600f, 2000f, 700f)
    val quit_text = "QUIT GAME"


    override fun update() {

    }

    override fun render(c: Canvas) {
        val music_text = if (SettingsManager.isMusicOn) "MUSIC ON" else "MUSIC OFF"
        val sound_text = if (SettingsManager.isSoundOn) "SOUND ON" else "SOUND OFF"
        c.drawRoundRect(big_rect, cornerRadius, cornerRadius, paint_stroke)
        c.drawRoundRect(big_rect, cornerRadius, cornerRadius, paint_blackFill)

        val questionX = 900f
        var questionY = 160f
        for (line in question.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(line, questionX, questionY, paint_textB)
            questionY += 70
        }

        c.drawRect(rectOp1, paint_blackFill)

        //music and sound effect toggle
        c.drawRoundRect(music_btn, cornerRadius, cornerRadius, paint_stroke)
        c.drawRoundRect(music_btn, cornerRadius, cornerRadius, paint_blackFill)
        c.drawText(music_text, 400f, 460f, paint_textB)

        c.drawRoundRect(sound_btn, cornerRadius, cornerRadius, paint_stroke)
        c.drawRoundRect(sound_btn, cornerRadius, cornerRadius, paint_blackFill)
        c.drawText(sound_text, 1400f, 460f, paint_textB)

        //quit btn
        c.drawRoundRect(quit_btn, cornerRadius, cornerRadius, paint_stroke)
        c.drawRoundRect(quit_btn, cornerRadius, cornerRadius, paint_blackFill)
        c.drawText(quit_text, 900f, 660f, paint_textB)
    }

    override fun touchEvents(event: MotionEvent) {
        when (game.currentLevel) {
            1 -> sendAnswer(event, Game.GameState.PLAYING)
            2 -> sendAnswer(event, Game.GameState.PLAYING2)
            3 -> sendAnswer(event, Game.GameState.PLAYING3)
            4 -> sendAnswer(event, Game.GameState.PLAYING4)
            else -> sendAnswer(event, Game.GameState.PLAYING5)
        }
    }

    private fun sendAnswer(event: MotionEvent, gameState: Game.GameState) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (rectOp1.contains(event.x, event.y)) {
                game.currentGameState = (gameState)
                if (SettingsManager.isMusicOn) {
                    MusicManager.resumeMusic()
                }
            }

            if (quit_btn.contains(event.x, event.y)) {
                MusicManager.stopMusic()
                SoundEffectsManager.release()
                //game.currentGameState = Game.GameState.MENU
                navController.navigate(Screen.HomeScreen.route){
                    popUpTo(0)
                    launchSingleTop = true
                }
            }

            if(music_btn.contains(event.x, event.y)){
                SettingsManager.isMusicOn = !SettingsManager.isMusicOn
                if (SettingsManager.isMusicOn) {
                    when(game.currentLevel){
                        1 -> MusicManager.startMusic(game.context, R.raw.disco_dj)
                        2 -> MusicManager.startMusic(game.context, R.raw.disco_dj)
                        3 -> MusicManager.startMusic(game.context, R.raw.penacony_dark)
                        4 -> MusicManager.startMusic(game.context, R.raw.disco_dj)
                        5 -> MusicManager.startMusic(game.context, R.raw.disco_dj)
                    }
                } else {
                    MusicManager.stopMusic()
                }
            }
            if(sound_btn.contains(event.x, event.y)){
                SettingsManager.isSoundOn = !SettingsManager.isSoundOn
            }
        }
    }
}