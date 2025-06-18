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

class NPC_farmer(
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
                DialogueLine("Hello traveler! "),
                DialogueLine("Listen, traveler... There's an old magic book across the lake. They say it holds power to protect our land. Could you fetch it for us?", listOf(

                    DialogueOption("Let’s do this!") {
                        viewModel.createAnswer(AnswerRequest(userName, 1, 2, 0))
                        println("Let’s do this!")
                    },
                    DialogueOption("Meh, I guess") {
                        viewModel.createAnswer(AnswerRequest(userName, 1, 2, 1))
                        println("Meh, I guess")
                    },
                    DialogueOption("I’ll do it later") {
                        viewModel.createAnswer(AnswerRequest(userName, 1, 2, 2))
                        println("I’ll do it later")
                    },
                    DialogueOption("Not in the mood at all") {
                        viewModel.createAnswer(AnswerRequest(userName, 1, 2, 3))
                        println("Not in the mood at all")
                    }
                )),
                DialogueLine("Farewell.")
            )
            1 -> listOf(
                DialogueLine("Alright, this might take a moment. The book you’re looking for—it's not just any book.\n" +
                        " Long ago, the mage Elvar hid it beyond the lake to keep it safe from dark forces.\n" +
                        " To find it, you’ll need to know the story, the signs, the path through the forest..", listOf(
                    DialogueOption("Listen closely to every word"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 3, 0))
                    },
                    DialogueOption("Pay attention to the important bits"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 3, 1))
                    },
                    DialogueOption("Zone out halfway through"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 3, 2))
                    },
                    DialogueOption("Nod and skip the whole thing"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 3, 3))
                    },
                )),
                DialogueLine("Farewell.")
            )
            else -> listOf(
                DialogueLine("I have nothing more to say... for now.")
            )
        }
    }
}