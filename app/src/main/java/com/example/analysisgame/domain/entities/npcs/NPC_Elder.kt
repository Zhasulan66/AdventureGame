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
                DialogueLine("Before we enter, you must focus. I will prepare a cast to spawn healing potions so wait it 20 seconds", listOf(

                    DialogueOption("Stand calmly, watching the preparation") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 18, 0))
                    },
                    DialogueOption("Shift a bit but try to stay focused") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 18, 1))
                    },
                    DialogueOption("Get restless and glance around") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 18, 2))
                    },
                    DialogueOption("Give up and walk off before it’s done") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 18, 3))
                    }
                ))
            )
            1 -> listOf(
                DialogueLine("This is it. The final path lies ahead. We’ve lost friends… seen horrors no one should. But if we don’t finish this, it was all for nothing", listOf(

                    DialogueOption("Let’s finish this—for them.") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 17, 0))
                    },
                    DialogueOption("I’m ready… I think") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 17, 1))
                    },
                    DialogueOption("I’m tired. But I’ll keep going") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 17, 2))
                    },
                    DialogueOption("What’s the point anymore?") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 17, 3))
                    }
                )),
            )
            2 -> listOf(
                DialogueLine("It’s over... and yet, this place—these echoes—remind me of when we lost the village. The silence feels the same.", listOf(

                    DialogueOption("It’s just another part of the story") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 19, 0))
                    },
                    DialogueOption("Yeah... it brings back some sadness.") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 19, 1))
                    },
                    DialogueOption("It hurts all over again") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 19, 2))
                    },
                    DialogueOption("I don’t want to think about it anymore.") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 19, 3))
                    }
                )),
            )
            3 -> listOf(
                DialogueLine("The darkness is gone. The world breathes again. Not many reach this point… but you did. Tell me—what does this moment mean to you?", listOf(

                    DialogueOption("That was incredible. Worth every step.") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 20, 0))
                    },
                    DialogueOption("Pretty cool, I guess.") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 20, 1))
                    },
                    DialogueOption("I don’t really feel anything") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 20, 2))
                    },
                    DialogueOption("Whatever. Doesn’t matter to me") {
                        viewModel.createAnswer(AnswerRequest(userName, 5, 20, 3))
                    }
                )),
                DialogueLine("Every journey leaves its mark—whether you feel it now or not.")
            )
            else -> listOf(
                DialogueLine("I have nothing more to say... for now.")
            )
        }
    }
}