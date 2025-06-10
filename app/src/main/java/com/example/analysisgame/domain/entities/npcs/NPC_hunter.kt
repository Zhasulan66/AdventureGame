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

class NPC_hunter(
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
                DialogueLine("After losing a battle, what do you feel?", listOf(

                    DialogueOption("Try again — no big deal") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 5, 0))
                    },
                    DialogueOption("That was tough") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 5, 1))
                    },
                    DialogueOption("I’m a failure") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 5, 2))
                    },
                    DialogueOption("I want to quit") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 5, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            2 -> listOf(
                DialogueLine("An NPC starts talking about a past disaster. How do you feel?", listOf(

                    DialogueOption("I listen and move on") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 6, 0))
                    },
                    DialogueOption("Feel bad for them") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 6, 1))
                    },
                    DialogueOption("It’s upsetting") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 6, 2))
                    },
                    DialogueOption("I feel like it’s happening to me") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 6, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            3 -> listOf(
                DialogueLine("You’re told to follow a path and wait. How do you handle it?", listOf(

                    DialogueOption("Wait calmly") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 7, 0))
                    },
                    DialogueOption("Walk in circles") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 7, 1))
                    },
                    DialogueOption("Get agitated") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 7, 2))
                    },
                    DialogueOption("Leave the area") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 7, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            4 -> listOf(
                DialogueLine("You forgot what your current task was. How often does that happen?", listOf(

                    DialogueOption("Rarely") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 8, 0))
                    },
                    DialogueOption("Sometimes") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 8, 1))
                    },
                    DialogueOption("Often") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 8, 2))
                    },
                    DialogueOption("Constantly") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 8, 3))
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