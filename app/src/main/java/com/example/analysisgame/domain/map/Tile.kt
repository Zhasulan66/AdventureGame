package com.example.analysisgame.domain.map

import android.graphics.Canvas
import android.graphics.Rect
import com.example.analysisgame.domain.graphics.Sprite

class Tile(
    val mapLocation: Rect,
    val data: TileData,
    val sprite: Sprite
) {
    enum class TileType {
        WATER_TILE,
        GRASS_TILE
    }

    fun draw(canvas: Canvas) {
        sprite.draw(canvas, mapLocation.left, mapLocation.top)
    }

    fun isWalkable(): Boolean = data.walkable

    fun getBoundingBox(): Rect = Rect(mapLocation.left, mapLocation.top, mapLocation.right, mapLocation.bottom)
}
/*
class Tile(
    private val mapLocation: Rect,
    private var walkable: Boolean
) {

    enum class TileType {
        WATER_TILE,
        GRASS_TILE,
        WATER2_TILE
    }

    companion object {
        fun getTile(idxTileType: Int, spriteSheet: SpriteSheet, mapLocationRect: Rect, lvl: Int): Tile{
            return when(TileType.entries[idxTileType-10]){
                TileType.WATER_TILE -> WaterTile(spriteSheet, mapLocationRect, false) //10
                TileType.GRASS_TILE -> GrassTile(spriteSheet, mapLocationRect, true) //11
                TileType.WATER2_TILE -> Water2Tile(spriteSheet, mapLocationRect, false) //12
            }
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
}*/
