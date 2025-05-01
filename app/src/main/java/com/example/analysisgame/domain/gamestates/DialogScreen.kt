package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.analysisgame.presentation.game.Game

class DialogScreen(game: Game) : BaseState(game), GameStateInterface {
    val alpha1 = Color.argb(120, 0, 0, 0)
    val paint_blackFill = Paint().apply {
        style = Paint.Style.FILL
        color = alpha1
    }
    val paint_stroke = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.WHITE
    }
    val paint_textB = Paint().apply {
        textSize = 50f
        color = Color.WHITE
    }
    val paint_text = Paint().apply {
        textSize = 40f
        color = Color.WHITE
    }
    val cornerRadius = 20f


    val big_rect = RectF(50f, 100f, 1050f, 400f)

    /*val rectOp1 = RectF(50f, 1400f, 450f, 1600f)
    val rectOp2 = RectF(650f, 1400f, 1050f, 1600f)
    val rectOp3 = RectF(50f, 1650f, 450f, 1850f)
    val rectOp4 = RectF(650f, 1650f, 1050f, 1850f)*/


    val rectOp1 = RectF(50f, 400f, 450f, 600f)
    val rectOp2 = RectF(650f, 400f, 1050f, 600f)
    val rectOp3 = RectF(50f, 650f, 450f, 850f)
    val rectOp4 = RectF(650f, 650f, 1050f, 850f)

    var question_num: Int = 0
    val questionList = ArrayList<String>()
    val answerList: Array<Array<String>> = Array(8) { Array(4) { "" } }

    var question = ""//questionList[question_num]
    var ans1 = answerList[question_num][0]
    var ans2 = answerList[question_num][1]
    var ans3 = answerList[question_num][2]
    var ans4 = answerList[question_num][3]


    init {
        initializeQuestions()
    }


    override fun update(delta: Double) {
        question = questionList[question_num]
        ans1 = answerList[question_num][0]
        ans2 = answerList[question_num][1]
        ans3 = answerList[question_num][2]
        ans4 = answerList[question_num][3]
    }

    override fun render(c: Canvas) {
        c.drawRoundRect(big_rect, cornerRadius, cornerRadius, paint_stroke)
        c.drawRoundRect(big_rect, cornerRadius, cornerRadius, paint_blackFill)

        drawOptions(c)

        val questionX = 100f
        var questionY = 160f
        for (line in question.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(line, questionX, questionY, paint_textB)
            questionY += 70
        }

        val ans1X = 100f
        var ans1Y = 500f
        val ans2X = 700f
        var ans2Y = 500f
        val ans3X = 100f
        var ans3Y = 750f
        val ans4X = 700f
        var ans4Y = 750f

        //for (line in ans1.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(ans1, ans1X, ans1Y, paint_text)
            ans1Y += 50
        //}
        //for (line in ans2.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(ans2, ans2X, ans2Y, paint_text)
            ans2Y += 50
        //}
        //for (line in ans3.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(ans3, ans3X, ans3Y, paint_text)
            ans3Y += 50
        //}
        //for (line in ans4.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(ans4, ans4X, ans4Y, paint_text)
            ans4Y += 50
        //}
    }

    override fun touchEvents(event: MotionEvent) {
        when (getGame().currentLevel) {
            1 -> sendAnswer(event, Game.GameState.PLAYING)
        }
    }

    fun drawOptions(c: Canvas) {
        c.drawRoundRect(rectOp1, cornerRadius, cornerRadius, paint_stroke);
        c.drawRoundRect(rectOp1, cornerRadius, cornerRadius, paint_blackFill);

        c.drawRoundRect(rectOp2, cornerRadius, cornerRadius, paint_stroke);
        c.drawRoundRect(rectOp2, cornerRadius, cornerRadius, paint_blackFill);

        c.drawRoundRect(rectOp3, cornerRadius, cornerRadius, paint_stroke);
        c.drawRoundRect(rectOp3, cornerRadius, cornerRadius, paint_blackFill);

        c.drawRoundRect(rectOp4, cornerRadius, cornerRadius, paint_stroke);
        c.drawRoundRect(rectOp4, cornerRadius, cornerRadius, paint_blackFill);
    }

    private fun initializeQuestions() {
        questionList.add("What is your favorite color?")//0
        questionList.add("Which season do you like the most?")//1
        questionList.add("What's your preferred type of movie genre?")//2
        questionList.add("What's your favorite animal?")//3
        questionList.add("What's your favorite dessert?")//4
        questionList.add("Which is your favorite sport?")//5
        questionList.add("What's your preferred mode\n of transportation?")//6
        questionList.add("Which is your favorite hobby?")//7

        answerList[0][0] = "Red"
        answerList[0][1] = "Blue"
        answerList[0][2] = "Green"
        answerList[0][3] = "Yellow"

        answerList[1][0] = "Spring"
        answerList[1][1] = "Summer"
        answerList[1][2] = "Fall"
        answerList[1][3] = "Winter"

        answerList[2][0] = "Comedy"
        answerList[2][1] = "Action"
        answerList[2][2] = "Drama"
        answerList[2][3] = "Horror"

        answerList[3][0] = "Dog"
        answerList[3][1] = "Cat"
        answerList[3][2] = "Dolphin"
        answerList[3][3] = "Elephant"

        answerList[4][0] = "Cake"
        answerList[4][1] = "Pie"
        answerList[4][2] = "Cookies"
        answerList[4][3] = "Fruit Salad"

        answerList[5][0] = "Soccer"
        answerList[5][1] = "Basketball"
        answerList[5][2] = "Tennis"
        answerList[5][3] = "Swimming"

        answerList[6][0] = "Car"
        answerList[6][1] = "Bicycle"
        answerList[6][2] = "Walking"
        answerList[6][3] = "Public Transit"

        answerList[7][0] = "Reading"
        answerList[7][1] = "Painting"
        answerList[7][2] = "Playing a \nmusical instrument"
        answerList[7][3] = "Gardening"
    }

    private fun sendAnswer(event: MotionEvent, gameState: Game.GameState) {
        if (event.action == MotionEvent.ACTION_MOVE) {
            if (rectOp1.contains(event.x, event.y)) {
                println(question + " answer is: " + answerList[question_num][0]);
                getGame().currentGameState = (gameState);
            }
            if (rectOp2.contains(event.x, event.y)) {
                println(question + "answer is: " + answerList[question_num][1]);
                getGame().currentGameState = (gameState);
            }
            if (rectOp3.contains(event.x, event.y)) {
                println(question + "answer is: " + answerList[question_num][2]);
                getGame().currentGameState = (gameState);
            }
            if (rectOp4.contains(event.x, event.y)) {
                println(question + "answer is: " + answerList[question_num][3]);
                getGame().currentGameState = (gameState);
            }
        }
    }
}