package com.example.analysisgame.domain.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.analysisgame.common.Constants


interface BitmapMethods {

    val options: BitmapFactory.Options
        get() = BitmapFactory.Options().apply {
            inScaled = false
        }

    fun getScaledBitmap(bitmap: Bitmap) : Bitmap {
        return Bitmap.createScaledBitmap(bitmap,
            bitmap.width * Constants.Sprite.SCALE_MULTIPLIER,
            bitmap.height * Constants.Sprite.SCALE_MULTIPLIER, false)
    }

    fun getDoubledBitmap(bitmap: Bitmap) : Bitmap {
        return Bitmap.createScaledBitmap(bitmap,
            bitmap.width * 2,
            bitmap.height * 2, false)
    }
}