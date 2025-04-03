package com.example.analysisgame.domain.environments

import android.graphics.Canvas
import com.example.analysisgame.common.Constants

class GameMap(private var spriteIds: Array<IntArray>) {

    fun draw(c: Canvas) {
        for (i in spriteIds.indices) {
            for (j in spriteIds[i].indices){
                c.drawBitmap(
                    Floor.OUTSIDE.getSprite(spriteIds[i][j]),
                    (j * Constants.Sprite.SIZE).toFloat(),
                    (i * Constants.Sprite.SIZE).toFloat(),
                    null)
            }

        }
    }
}