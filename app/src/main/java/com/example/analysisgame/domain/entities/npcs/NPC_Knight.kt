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

class NPC_Knight(
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
                DialogueLine("Honor to you."),
                DialogueLine("While patrolling the woods, you hear a sudden rustle from the bushes nearby. Could be a beast... or worse. How do you respond?", listOf(
                    DialogueOption("Curious — stride toward it"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 4, 0))
                    },
                    DialogueOption("Cautious — draw your blade and approach"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 4, 1))
                    },
                    DialogueOption("Startled — step back and observe"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 4, 2))
                    },
                    DialogueOption("Panic — flee without looking back"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 4, 3))
                    },
                )),
                DialogueLine("Farewell.")
            )

            1 -> listOf(
                DialogueLine("You’re about to enter a mysterious area. What’s your first thought?", listOf(
                    DialogueOption("Exciting! Let’s go"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 1, 0))
                    },
                    DialogueOption("I’ll go but carefully"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 1, 1))
                    },
                    DialogueOption("What if something bad happens?"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 1, 2))
                    },
                    DialogueOption("I’d rather avoid it"){
                        viewModel.createAnswer(AnswerRequest(userName, 1, 1, 3))
                    },
                )),
                DialogueLine("Stand tall, friend.")
            )
            else -> listOf(
                DialogueLine("I have nothing more to say... for now.")
            )
        }
    }
}