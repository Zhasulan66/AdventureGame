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

class NPC_Elder(
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
                DialogueLine("A scene reminds you of a previous tragic moment in the game.", listOf(

                    DialogueOption("Just part of the story") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 17, 0))
                    },
                    DialogueOption("Feel a little sad") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 17, 1))
                    },
                    DialogueOption("Get emotional again") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 17, 2))
                    },
                    DialogueOption("Try to avoid it completely") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 17, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            2 -> listOf(
                DialogueLine("You’re asked to stay still and observe something for 30 seconds.", listOf(

                    DialogueOption("I observe peacefully") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 18, 0))
                    },
                    DialogueOption("I fidget a little") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 18, 1))
                    },
                    DialogueOption("I get up and explore") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 18, 2))
                    },
                    DialogueOption("I don’t even try") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 18, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            3 -> listOf(
                DialogueLine("The final mission is emotionally heavy. How do you feel?", listOf(

                    DialogueOption("Empowered") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 19, 0))
                    },
                    DialogueOption("Mixed emotions") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 19, 1))
                    },
                    DialogueOption("Emotionally drained") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 19, 2))
                    },
                    DialogueOption("Hopeless") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 19, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            4 -> listOf(
                DialogueLine("You complete the game. What’s your reaction?", listOf(

                    DialogueOption("That was awesome") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 20, 0))
                    },
                    DialogueOption("Cool, I guess") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 20, 1))
                    },
                    DialogueOption("Didn’t feel much") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 20, 2))
                    },
                    DialogueOption("I don’t care") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 20, 3))
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