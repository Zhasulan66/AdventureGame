package com.example.analysisgame.domain.entities.npcs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import androidx.annotation.DrawableRes
import com.example.analysisgame.domain.entities.GameObject
import com.example.analysisgame.domain.entities.Player
import com.example.analysisgame.domain.gamestates.DialogueLine
import com.example.analysisgame.domain.gamestates.DialogueOption
import com.example.analysisgame.domain.model.AnswerRequest
import com.example.analysisgame.presentation.game.GameDisplay
import com.example.analysisgame.presentation.viewmodel.MainViewModel

class NPC_archer(
    context: Context,
    @DrawableRes imageResId: Int,
    positionX: Float,
    positionY: Float,
    private val player: Player,
    val viewModel: MainViewModel,
    val userName: String
) : GameObject(positionX, positionY) {

    var hasTalked = false
    private val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, imageResId)

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        val screenX = gameDisplay.gameToDisplayCoordinatesX(positionX)
        val screenY = gameDisplay.gameToDisplayCoordinatesY(positionY)

        // Draw image centered at positionX/positionY
        val halfWidth = bitmap.width / 2f
        val halfHeight = bitmap.height / 2f

        val destRect = Rect(
            (screenX - halfWidth).toInt(),
            (screenY - halfHeight).toInt(),
            (screenX - halfHeight).toInt() + (bitmap.width * 1.5).toInt(),
            (screenY - halfHeight).toInt() + (bitmap.height * 1.5).toInt()
        )
        canvas.drawBitmap(bitmap, null, destRect, null)
    }

    override fun update() {
        //later when I talk

    }

    fun isPlayerNearby(player: Player): Boolean {
        val distance = getDistanceBetweenObjects(this, player)
        return distance < 100
    }

    var talkCount = 0 // Tracks how many times player has interacted

    fun getDialogueLines(): List<DialogueLine> {
        return when (talkCount) {
            0 -> listOf(
                DialogueLine("Hello traveler! Welcome to our village."),
                DialogueLine("Please, make yourself at home.")
            )
            1 -> listOf(
                DialogueLine("You enter a place like one where something bad happened earlier.", listOf(

                    DialogueOption("It’s fine") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 13, 0))
                    },
                    DialogueOption("A little eerie") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 13, 1))
                    },
                    DialogueOption("Feel nervous") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 13, 2))
                    },
                    DialogueOption("Avoid going in") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 13, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            2 -> listOf(
                DialogueLine("You must complete a boring but necessary task.", listOf(

                    DialogueOption("No problem") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 15, 0))
                    },
                    DialogueOption("It’s hard to focus") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 15, 1))
                    },
                    DialogueOption("I get distracted easily") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 15, 2))
                    },
                    DialogueOption("I avoid it completely") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 15, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            else -> listOf(
                DialogueLine("I have nothing more to say... for now.")
            )
        }
    }
}