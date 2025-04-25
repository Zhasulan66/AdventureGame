package com.example.analysisgame.domain.map

import android.graphics.Canvas
import android.graphics.Rect
import com.example.analysisgame.domain.graphics.SpriteSheet
import com.example.analysisgame.domain.map.tiles.GrassTile

abstract class Tile(
    private val mapLocation: Rect,
    private var walkable: Boolean
) {

    companion object {
        fun getTile(idxTileType: Int, spriteSheet: SpriteSheet, mapLocationRect: Rect, lvl: Int): Tile{
            return GrassTile(spriteSheet, mapLocationRect, true)
        }
    }

    abstract fun draw(canvas: Canvas)

    fun isWalkable(): Boolean {
        return walkable
    }

    fun setWalkable(walkable: Boolean){
        this.walkable = walkable
    }

    fun getBoundingBox(): Rect {
        return Rect(mapLocation.left, mapLocation.top, mapLocation.right, mapLocation.bottom)
    }
}