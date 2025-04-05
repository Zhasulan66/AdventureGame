package com.example.analysisgame.domain.environments

class GameMap(private var spriteIds: Array<IntArray>) {

    fun getSpriteID(xIndex: Int, yIndex: Int): Int {
        return spriteIds[yIndex][xIndex]
    }

    fun getArrayWidth(): Int {
        return spriteIds[0].size
    }

    fun getArrayHeight(): Int {
        return spriteIds.size
    }
}