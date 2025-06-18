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
                DialogueLine("Are you going after necromancer boss?."),
                DialogueLine("I once met a man who lost everything when the necromancer’s army passed through—family, home, even his dog. He told me the whole story while we waited out the rain", listOf(

                    DialogueOption("I listen and move on") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 6, 0))
                    },
                    DialogueOption("Feel bad for them") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 6, 1))
                    },
                    DialogueOption("It’s upsetting") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 6, 2))
                    },
                    DialogueOption("I feel like I’m right there, living it") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 6, 3))
                    }
                )),
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
                ))
            )
            //action
            2 -> listOf(
                DialogueLine("You’ll find the dungeon key at the end of the hidden path. Once you have it, wait by the gate—someone will come to open it. That’s the only way to reach the necromancer’s lair.", listOf(

                    DialogueOption("Stand guard and wait calmly") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 7, 0))
                    },
                    DialogueOption("Pace around the gate") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 7, 1))
                    },
                    DialogueOption("Get impatient and mutter to yourself") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 7, 2))
                    },
                    DialogueOption("Give up and wander off") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 7, 3))
                    }
                ))
            )
            3 -> listOf(
                DialogueLine("Hmm... you’re staring at the clouds again. Don’t tell me you’ve forgotten what you were supposed to be doing.\"\n" +
                        "\n" +
                        "How often does that happen to you?", listOf(

                    DialogueOption("Rarely — I stay focused") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 8, 0))
                    },
                    DialogueOption("Sometimes — it slips my mind") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 8, 1))
                    },
                    DialogueOption("Often — I get distracted") {
                        viewModel.createAnswer(AnswerRequest(userName, 2, 8, 2))
                    },
                    DialogueOption("Constantly — I forget everything") {
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