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
                DialogueLine("You held your ground well against those skeletons. Few have that kind of courage. You've earned my respect", listOf(

                    DialogueOption("Thank you. I did what had to be done.") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 14, 0))
                    },
                    DialogueOption("Oh... uh, thanks. Just got lucky, maybe") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 14, 1))
                    },
                    DialogueOption("Sure… if you say so") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 14, 2))
                    },
                    DialogueOption("You don’t need to flatter me.") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 14, 3))
                    }
                ))
            )
            1 -> listOf(
                DialogueLine("Every choice we make in battle or beyond… it echoes. Some warriors charge ahead without pause. Others hesitate, weighing every outcome before lifting a blade.", listOf(

                    DialogueOption("I go with my gut and move on.") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 16, 0))
                    },
                    DialogueOption("I think things through now and then.") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 16, 1))
                    },
                    DialogueOption("I try to plan every move") {
                        viewModel.createAnswer(AnswerRequest(userName, 4, 16, 2))
                    },
                    DialogueOption("I can’t stop second-guessing everything.") {
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