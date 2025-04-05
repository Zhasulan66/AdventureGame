package com.example.analysisgame.domain.environments

import android.graphics.Canvas
import com.example.analysisgame.common.Constants

class MapManager {
    private lateinit var currentMap: GameMap
    private var cameraX = 0f
    private var cameraY = 0f

    init {
        initTestMap()
    }

    fun setCameraValues(cameraX: Float, cameraY: Float){
        this.cameraX = cameraX
        this.cameraY = cameraY
    }

    fun canMoveHere(x: Float, y: Float): Boolean {
        if(x < 0 || y < 0){
            return false
        }

        if(x >= getMaxWidthCurrentMap() || y >= getMaxHeightCurrentMap()){
            return false
        }

        return true
    }

    fun getMaxWidthCurrentMap(): Int {
        return currentMap.getArrayWidth() * Constants.Sprite.SIZE
    }

    fun getMaxHeightCurrentMap(): Int {
        return currentMap.getArrayHeight() * Constants.Sprite.SIZE
    }

    fun draw(c: Canvas){
            for (j in 0 until currentMap.getArrayHeight()) {
                for (i in 0 until currentMap.getArrayWidth()){
                    c.drawBitmap(
                        Floor.OUTSIDE.getSprite(currentMap.getSpriteID(i,j)),
                        (i * Constants.Sprite.SIZE).toFloat() + cameraX, // + cameraX
                        (j * Constants.Sprite.SIZE).toFloat() + cameraY,
                        null)
                }
            }
    }

    private fun initTestMap() {
        val spriteIds = arrayOf(
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 327, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1),
        )

        currentMap = GameMap(spriteIds)
    }
}