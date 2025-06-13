package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.analysisgame.MainActivity.Companion.GAME_HEIGHT
import com.example.analysisgame.MainActivity.Companion.GAME_WIDTH
import com.example.analysisgame.R
import com.example.analysisgame.presentation.game.Game
import com.example.analysisgame.presentation.navigation.Screen

class WinScreenView(context: Context, private val game: Game) : View(context) {

    private val backgroundPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.darkGreen)
        textSize = 150f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val buttonPaint = Paint().apply {
        color = Color.DKGRAY
        style = Paint.Style.FILL
    }

    private val buttonTextPaint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    private val buttonRadius = 40f
    private var exitRect = RectF()
    private var retryRect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Background rectangle behind text
        val bgRect = RectF(100f, 100f, GAME_WIDTH - 100f, 350f)
        canvas.drawRoundRect(bgRect, 50f, 50f, backgroundPaint)

        // Game Over text
        canvas.drawText("You Won!", GAME_WIDTH / 2f, 250f, textPaint)

        // Buttons
        val buttonWidth = 300f
        val buttonHeight = 120f
        val spacing = 80f
        val bottomY = GAME_HEIGHT - 200f

        // Exit button
        exitRect = RectF(
            GAME_WIDTH / 2f - buttonWidth - spacing / 2,
            bottomY,
            GAME_WIDTH / 2f - spacing / 2,
            bottomY + buttonHeight
        )

        // Retry button
        retryRect = RectF(
            GAME_WIDTH / 2f + spacing / 2,
            bottomY,
            GAME_WIDTH / 2f + buttonWidth + spacing / 2,
            bottomY + buttonHeight
        )

        canvas.drawRoundRect(exitRect, buttonRadius, buttonRadius, buttonPaint)
        canvas.drawRoundRect(retryRect, buttonRadius, buttonRadius, buttonPaint)

        // Button Text
        canvas.drawText("Exit", exitRect.centerX(), exitRect.centerY() + 20f, buttonTextPaint)
        canvas.drawText("Next", retryRect.centerX(), retryRect.centerY() + 20f, buttonTextPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (exitRect.contains(event.x, event.y)) {
                // Handle Exit click
                game.pauseGameLoop()
                game.navController.navigate(Screen.HomeScreen.route){
                    popUpTo(0) { inclusive = true } // clears entire backstack
                    launchSingleTop = true
                }
                // (Optional) trigger a callback or finish activity
            } else if (retryRect.contains(event.x, event.y)) {
                // Handle Retry click
                game.pauseGameLoop()
                game.navController.navigateUp()
                // (Optional) reset game or notify listener
            }
        }
        return true
    }
}