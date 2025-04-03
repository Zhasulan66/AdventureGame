package com.example.analysisgame.domain.environments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.analysisgame.MainActivity
import com.example.analysisgame.R
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.repository.BitmapMethods

enum class Floor(
    resId: Int,
    tilesInWidth: Int,
    tilesInHeight: Int
) : BitmapMethods {
    OUTSIDE(R.drawable.tiles_floor, 25, 26);

    private var sprites: Array<Bitmap?> = Array<Bitmap?>(tilesInHeight * tilesInWidth){null}

    init {
        val spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().resources, resId, options)
        for (j in 0 until tilesInHeight){
            for(i in 0 until tilesInWidth){
                val index = j * tilesInWidth + i
                sprites[index] = getScaledBitmap(Bitmap.createBitmap(
                    spriteSheet,
                    Constants.Sprite.SMALLER_SIZE*i,
                    Constants.Sprite.SMALLER_SIZE * j,
                    Constants.Sprite.SMALLER_SIZE,
                    Constants.Sprite.SMALLER_SIZE
                ))
            }
        }

    }

    fun getSprite(id: Int): Bitmap {
        //return getDoubledBitmap(sprites[id]!!)
        return sprites[id]!!
    }

    /*override fun getScaledBitmap(bitmap: Bitmap) : Bitmap {
        return Bitmap.createScaledBitmap(bitmap,
            bitmap.width * Constants.Sprite.SCALE_MULTIPLIER * 2,
            bitmap.height * Constants.Sprite.SCALE_MULTIPLIER * 2, false)
    }*/
}