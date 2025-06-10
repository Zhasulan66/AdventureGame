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

class NPC_elf(
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
                DialogueLine("An NPC gives you vague instructions. How do you feel?", listOf(

                    DialogueOption("Fine — I’ll figure it out") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 9, 0))
                    },
                    DialogueOption("Slightly confused") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 9, 1))
                    },
                    DialogueOption("Nervous I’ll mess up") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 9, 2))
                    },
                    DialogueOption("I don’t even try") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 9, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            2 -> listOf(
                DialogueLine("You find a big reward. How do you feel?", listOf(

                    DialogueOption("Happy and proud") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 10, 0))
                    },
                    DialogueOption("It’s cool, I guess") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 10, 1))
                    },
                    DialogueOption("Doesn’t matter much") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 10, 2))
                    },
                    DialogueOption("I didn’t deserve it") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 10, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            3 -> listOf(
                DialogueLine("You hear a loud explosion nearby. Your reaction?", listOf(

                    DialogueOption("Let’s check it out!") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 11, 0))
                    },
                    DialogueOption("Careful approach") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 11, 1))
                    },
                    DialogueOption("Panic a bit") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 11, 2))
                    },
                    DialogueOption("Freeze or hide") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 11, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            4 -> listOf(
                DialogueLine("You’re distracted mid-quest. What caused it?", listOf(

                    DialogueOption("Nothing — I’m focused") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 12, 0))
                    },
                    DialogueOption("A shiny thing") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 12, 1))
                    },
                    DialogueOption("A wandering thought") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 12, 2))
                    },
                    DialogueOption("I don’t know — I’m always distracted") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 12, 3))
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