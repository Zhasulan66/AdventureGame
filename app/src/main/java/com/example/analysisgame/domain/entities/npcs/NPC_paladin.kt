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

class NPC_paladin(
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
                DialogueLine("A character praises your actions. How do you feel?", listOf(

                    DialogueOption("Grateful and confident") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 14, 0))
                    },
                    DialogueOption("A bit awkward") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 14, 1))
                    },
                    DialogueOption("It feels fake") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 14, 2))
                    },
                    DialogueOption("I don’t believe them") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 14, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            2 -> listOf(
                DialogueLine("How often do you overthink the consequences of in-game choices?", listOf(

                    DialogueOption("Rarely") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 16, 0))
                    },
                    DialogueOption("Sometimes") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 16, 1))
                    },
                    DialogueOption("Often") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 16, 2))
                    },
                    DialogueOption("Constantly") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 16, 3))
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