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
                DialogueLine("Listen well, traveler. Three keys are hidden in these twisting corridors. Only with all three will the final gate yield. Beware—the skeleton sentries here laugh at magic. That’s all I can tell you.", listOf(

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
                ))
            )
            1 -> listOf(
                DialogueLine("Careful where you step. Just saw someone touch that pressure plate—needle popped out and bam, poison. They’re still coughing up green", listOf(

                    DialogueOption("Stay calm and deal with it") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 11, 0))
                    },
                    DialogueOption("Start checking your body frantically") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 11, 1))
                    },
                    DialogueOption("Panic and imagine the worst") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 11, 2))
                    },
                    DialogueOption("Freeze and hope it goes away") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 11, 3))
                    }
                )),
                DialogueLine("Farewell.")
            )
            2 -> listOf(
                DialogueLine("We were halfway to the crypt and suddenly you just... stopped walking. Staring off like you saw a ghost or gold. What pulled your attention?", listOf(

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
            )
            3 -> listOf(
                DialogueLine("You did it… All three keys, and the vault opens. The reward is yours—gold, relics, and power long lost.\"\n" +
                        "\n" +
                        "How do you feel standing before the treasure?", listOf(

                    DialogueOption("Happy and proud — I earned this") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 10, 0))
                    },
                    DialogueOption("It’s cool, I guess") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 10, 1))
                    },
                    DialogueOption("Feels kind of empty") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 10, 2))
                    },
                    DialogueOption("I don’t think I deserve it") {
                        viewModel.createAnswer(AnswerRequest(userName, 3, 10, 3))
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