package com.example.analysisgame.domain.entities.npcs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
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

        canvas.drawBitmap(
            bitmap,
            screenX - halfWidth,
            screenY - halfHeight,
            null
        )
    }

    override fun update() {
        //later when I talk
        if(isPlayerNearby(player)){
            positionY += 20
        }
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
                DialogueLine("You're back again! Anything new?")
            )
            2 -> listOf(
                DialogueLine("Still wandering, huh? Be careful out there.")
            )
            3 -> listOf(
                DialogueLine("Hello, traveler! Choose your path:Hello, traveler! Choose your path:Hello, traveler! Choose your path:" +
                        "Hello, traveler! Choose your path:", listOf(

                    DialogueOption("Adventure") {
                        viewModel.createAnswer(AnswerRequest(userName, 1, 1, 0))
                        println("Adventure chosen")
                    },
                    DialogueOption("Wisdom") {
                        viewModel.createAnswer(AnswerRequest(userName, 1, 1, 1))
                        println("Wisdom chosen")
                    },
                    DialogueOption("GoldGoldGoldGoldGoldGoldGoldGoldGoldGoldGoldGoldGoldGoldGold") {
                        viewModel.createAnswer(AnswerRequest(userName, 1, 1, 2))
                        println("Gold chosen")
                    },
                    DialogueOption("Nothing") {
                        viewModel.createAnswer(AnswerRequest(userName, 1, 1, 3))
                        println("Nothing chosen")
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