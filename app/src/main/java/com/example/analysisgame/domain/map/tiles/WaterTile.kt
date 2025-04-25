package com.example.analysisgame.domain.map.tiles

import android.graphics.Canvas
import android.graphics.Rect
import com.example.analysisgame.domain.graphics.SpriteSheet
import com.example.analysisgame.domain.map.Tile

class WaterTile(
    spriteSheet: SpriteSheet,
    val mapLocationRect: Rect,
    walkable: Boolean
) : Tile(mapLocationRect, walkable) {

    val sprite = spriteSheet.getWaterSprite()

    override fun draw(canvas: Canvas) {
        sprite.draw(canvas, mapLocationRect.left, mapLocationRect.top)
    }


}