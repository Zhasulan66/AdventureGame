package com.example.analysisgame.domain.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.analysisgame.MainActivity
import com.example.analysisgame.R
import com.example.analysisgame.common.Constants
import com.example.analysisgame.domain.repository.BitmapMethods

enum class GameCharacters(private val resId: Int) : BitmapMethods {
    ROGUE(R.drawable.spritesheet_rogue),
    SKELETON(R.drawable.spritesheet_skeleton);


    private var spriteSheet: Bitmap =
        BitmapFactory.decodeResource(MainActivity.getGameContext().resources, resId, options)

    private var sprites: Array<Array<Bitmap?>> = Array(6) { Array(6) { null } }

    init {
        for (i in sprites.indices) { // Loop through rows
            for (j in sprites[i].indices) { // Loop through columns
                sprites[i][j] = getScaledBitmap(Bitmap.createBitmap(
                    spriteSheet,
                    Constants.Sprite.DEFAULT_SIZE * j, Constants.Sprite.DEFAULT_SIZE * i,
                    Constants.Sprite.DEFAULT_SIZE, Constants.Sprite.DEFAULT_SIZE))

            }
        }
    }

    fun getSpriteSheet(): Bitmap {
        return spriteSheet
    }

    fun getSprite(xPos: Int, yPos: Int): Bitmap {
        return sprites[xPos][yPos]!!
    }
}