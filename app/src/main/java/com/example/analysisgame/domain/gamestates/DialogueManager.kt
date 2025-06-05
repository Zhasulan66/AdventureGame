package com.example.analysisgame.domain.gamestates

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

data class DialogueLine(
    val text: String,
    val options: List<DialogueOption>? = null
)

data class DialogueOption(
    val text: String,
    val onSelect: () -> Unit
)

class DialogueManager {
    private var dialogueLines: List<DialogueLine> = emptyList()
    private var currentDialogueIndex = 0
    var isDialogueActive = false
        private set
    var currentLine: String = ""
        private set
    var currentOptions: List<DialogueOption>? = null
        private set

    private val optionRects = mutableMapOf<Int, RectF>()

    /*private val textPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 50f
        isAntiAlias = true
    }*/
    private val textPaint = TextPaint().apply {
        color = android.graphics.Color.WHITE
        textSize = 48f
        isAntiAlias = true
    }

    private val boxPaint = Paint().apply {
        color = android.graphics.Color.argb(200, 0, 0, 0)
    }

    private val optionPaint = Paint().apply {
        color = android.graphics.Color.DKGRAY
    }

    fun startDialogue(lines: List<DialogueLine>) {
        dialogueLines = lines
        currentDialogueIndex = 0
        isDialogueActive = true
        nextLine()
    }

    fun nextLine() {
        if (currentDialogueIndex < dialogueLines.size) {
            val line = dialogueLines[currentDialogueIndex]
            currentLine = line.text
            currentOptions = line.options
            currentDialogueIndex++

            // If there are options, wait for player to choose
            if (currentOptions != null) {
                return
            }
        } else {
            endDialogue()
        }
    }

    fun selectOption(index: Int) {
        currentOptions?.getOrNull(index)?.onSelect?.invoke()
        currentOptions = null
        optionRects.clear()
        nextLine()
    }

    fun endDialogue() {
        isDialogueActive = false
        currentLine = ""
        currentOptions = null
        optionRects.clear()
    }

    private val padding = 30f
    private val boxWidth = 980f  // Width of dialogue box
    private val boxLeft = 50f
    private val boxRight = boxLeft + boxWidth

    fun draw(canvas: Canvas) {
        if (!isDialogueActive) return

        // === Layout text using StaticLayout ===
        val staticLayout = StaticLayout.Builder
            .obtain(currentLine, 0, currentLine.length, textPaint, boxWidth.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()

        val textHeight = staticLayout.height
        val boxTop = 200f //800
        val boxBottom = boxTop + textHeight + 2 * padding

        // Draw dialogue box
        val textBox = RectF(boxLeft, boxTop, boxRight, boxBottom)
        canvas.drawRoundRect(textBox, 20f, 20f, boxPaint)

        // Draw wrapped text
        canvas.save()
        canvas.translate(boxLeft + padding, boxTop + padding)
        staticLayout.draw(canvas)
        canvas.restore()

        // Draw options below dialogue box
        currentOptions?.let {
            optionRects.clear()
            it.forEachIndexed { index, option ->
                val top = boxBottom + 30f + index * 110f
                val rect = RectF(boxLeft + 20f, top, boxRight - 20f, top + 90f)
                canvas.drawRoundRect(rect, 15f, 15f, optionPaint)
                canvas.drawText(option.text, rect.left + 30f, rect.centerY() + 20f, textPaint)
                optionRects[index] = rect
            }
        }
    }
    /*fun draw(canvas: Canvas) {
        if (!isDialogueActive) return

        // Draw dialogue box
        val box = RectF(50f, 200f, 1030f, 400f)
        canvas.drawRoundRect(box, 20f, 20f, boxPaint)
        canvas.drawText(currentLine, box.left + 30f, box.top + 60f, textPaint)

        // Draw options if present
        currentOptions?.let {
            optionRects.clear()
            it.forEachIndexed { index, option ->
                val top = 450f + index * 110f
                val rect = RectF(100f, top, 1000f, top + 90f)
                canvas.drawRoundRect(rect, 15f, 15f, optionPaint)
                canvas.drawText(option.text, rect.left + 40f, rect.centerY() + 20f, textPaint)
                optionRects[index] = rect
            }
        }
    }*/

    fun handleTouch(x: Float, y: Float) {
        if (!isDialogueActive) return

        if (currentOptions != null) {
            optionRects.forEach { (index, rect) ->
                if (rect.contains(x, y)) {
                    selectOption(index)
                    return
                }
            }
        } else {
            nextLine()
        }
    }
}