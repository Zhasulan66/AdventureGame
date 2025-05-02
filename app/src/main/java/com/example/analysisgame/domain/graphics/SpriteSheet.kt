package com.example.analysisgame.domain.graphics

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect


class SpriteSheet(
    context: Context,
    resId: Int
) {
    val SPRITE_WIDTH_PIXELS: Int = 64
    val SPRITE_HEIGHT_PIXELS: Int = 64
    private var bitmap: Bitmap

    private val sprites = Array(6) { arrayOfNulls<Bitmap>(6) } //7 4

    init {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inScaled = false
        bitmap = BitmapFactory.decodeResource(context.resources, resId, bitmapOptions)

        for (j in sprites.indices)
            for (i in sprites[j].indices)
                sprites[j][i] = getScaledBitmap(Bitmap.createBitmap(bitmap, 32 * i, 32 * j, 32, 32))
    }

    fun getBitmap(): Bitmap {
        return bitmap
    }

    fun getSprite(yPos: Int, xPos: Int): Bitmap {
        return sprites[yPos][xPos]!!
    }

    fun getScaledBitmap(bitmap: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, bitmap.width * 6, bitmap.height * 6, false)
    }

    fun getPlayerSpriteArray(): Array<Sprite?> {
        val spriteArray = arrayOfNulls<Sprite>(3)
        //spriteArray[0] = Sprite(this, Rect(0 * 16, 0, 1 * 16, 1 * 16))
        spriteArray[0] = Sprite(this, Rect(0 * 16, 0, 1 * 16, 1 * 16))
        spriteArray[1] = Sprite(this, Rect(0 * 16, 1 * 16, 1 * 16, 2 * 16))
        spriteArray[2] = Sprite(this, Rect(0 * 16, 2 * 16, 1 * 16, 3 * 16))
        return spriteArray
    }

    private fun getSpriteByIndex(idxRow: Int, idxCol: Int): Sprite {
        return Sprite(
            this, Rect(
                idxCol * SPRITE_WIDTH_PIXELS,
                idxRow * SPRITE_HEIGHT_PIXELS,
                (idxCol + 1) * SPRITE_WIDTH_PIXELS,
                (idxRow + 1) * SPRITE_HEIGHT_PIXELS
            )
        )
    }

    fun getTileByIndex(idxRow: Int, idxCol: Int): Sprite {
        return Sprite(
            this, Rect(
                idxCol * 16,
                idxRow * 16,
                (idxCol + 1) * 16,
                (idxRow + 1) * 16
            )
        )
    }

    fun getWaterSprite(): Sprite {
        return getTileByIndex(0, 0)
    }


}