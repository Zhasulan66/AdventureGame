package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import com.example.analysisgame.presentation.game.Game

class DialogScreen(val game: Game) : BaseState(game), GameStateInterface {
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

    private var question_num: Int = 0
    val questionList = ArrayList<String>()
    val answerList: Array<Array<String>> = Array(9) { Array(4) { "" } }

    var question = ""//questionList[question_num]
    var ans1 = answerList[question_num][0]
    var ans2 = answerList[question_num][1]
    var ans3 = answerList[question_num][2]
    var ans4 = answerList[question_num][3]


    init {
        initializeQuestions()
    }


    override fun update() {
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
        for (line in ans3.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(line, ans3X, ans3Y, paint_text)
            ans3Y += 50
        }
        //for (line in ans4.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            c.drawText(ans4, ans4X, ans4Y, paint_text)
            ans4Y += 50
        //}
    }

    override fun touchEvents(event: MotionEvent) {
        when (game.currentLevel) {
            1 -> sendAnswer(event, Game.GameState.PLAYING)
            2 -> sendAnswer(event, Game.GameState.PLAYING2)
            3 -> sendAnswer(event, Game.GameState.PLAYING3)
            4 -> sendAnswer(event, Game.GameState.PLAYING4)
            else -> sendAnswer(event, Game.GameState.PLAYING5)
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
        questionList.add("You’re about to enter a mysterious area. \n What’s your first thought?")//0
        questionList.add("You’re given a simple quest. \n How do you respond?")//1
        questionList.add("You talk to an NPC with lots of info.\n What do you do?")//2
        questionList.add("A bush rustles suddenly.\n What’s your reaction?")//3
        questionList.add("After losing a battle, what do you feel?")//4
        questionList.add("An NPC starts talking about a past disaster.\n How do you feel?")//5
        questionList.add("You’re told to follow a path and wait.\n How do you handle it?")//6
        questionList.add("You forgot what your current task was.\n How often does that happen?")//7
        questionList.add("An NPC gives you vague instructions.\n How do you feel?")//8

        answerList[0][0] = "Exciting! Let’s go"
        answerList[0][1] = "I’ll go but carefully"
        answerList[0][2] = "What if something \n bad happens?"
        answerList[0][3] = "I’d rather avoid it"

        answerList[1][0] = "Let’s do this!"
        answerList[1][1] = "Meh, I guess"
        answerList[1][2] = "I’ll do it later"
        answerList[1][3] = "Not in the mood at all"

        answerList[2][0] = "Read it all carefully"
        answerList[2][1] = "Skim the parts"
        answerList[2][2] = "Forget most of it"
        answerList[2][3] = "Skip through everything"

        answerList[3][0] = "check it out"
        answerList[3][1] = "approach slowly"
        answerList[3][2] = "pause or back off"
        answerList[3][3] = "run away"

        answerList[4][0] = "Try again — no big deal"
        answerList[4][1] = "That was tough"
        answerList[4][2] = "I’m a failure"
        answerList[4][3] = "I want to quit"

        answerList[5][0] = "I listen and move on"
        answerList[5][1] = "Feel bad for them"
        answerList[5][2] = "It’s upsetting"
        answerList[5][3] = "I feel like it’s happening to me"

        answerList[6][0] = "Wait calmly"
        answerList[6][1] = "Walk in circles"
        answerList[6][2] = "Get agitated"
        answerList[6][3] = "Leave the area"

        answerList[7][0] = "Rarely"
        answerList[7][1] = "Sometimes"
        answerList[7][2] = "Often"
        answerList[7][3] = "Constantly"

        answerList[8][0] = "I’ll figure it out"
        answerList[8][1] = "Slightly confused"
        answerList[8][2] = "Nervous I’ll mess up"
        answerList[8][3] = "I don’t even try"
    }

    fun setQuestion_num(question_num: Int) {
        this.question_num = question_num
    }


    private fun sendAnswer(event: MotionEvent, gameState: Game.GameState) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (rectOp1.contains(event.x, event.y)) {
                println(question + " answer is: " + answerList[question_num][0]);
                game.currentGameState = (gameState);
            }
            if (rectOp2.contains(event.x, event.y)) {
                println(question + "answer is: " + answerList[question_num][1]);
                game.currentGameState = (gameState);
            }
            if (rectOp3.contains(event.x, event.y)) {
                println(question + "answer is: " + answerList[question_num][2]);
                game.currentGameState = (gameState);
            }
            if (rectOp4.contains(event.x, event.y)) {
                println(question + "answer is: " + answerList[question_num][3]);
                game.currentGameState = (gameState);
            }
        }
    }
}