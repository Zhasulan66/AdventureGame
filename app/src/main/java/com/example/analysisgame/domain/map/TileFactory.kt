package com.example.analysisgame.domain.map

import android.graphics.Rect
import com.example.analysisgame.domain.graphics.SpriteSheet

object TileFactory {

    fun getTileData(tileId: Int): TileData {
        return when (tileId) {
            10 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 0) }
            11 -> TileData(Tile.TileType.GRASS_TILE, true)  { it.getTileByIndex(0, 1) }
            12 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 2) }
            13 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 3) }
            14 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 4) }
            15 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 5) }
            16 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 6) }
            17 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 7) }
            18 -> TileData(Tile.TileType.GRASS_TILE, true) { it.getTileByIndex(0, 8) }
            19 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 9) }
            20 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 10) }
            21 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 11) }
            22 -> TileData(Tile.TileType.GRASS_TILE, true) { it.getTileByIndex(0, 12) }
            23 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 13) }
            24 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 14) }
            25 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 15) }
            26 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 16) }
            27 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 17) }
            28 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 18) }
            29 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 19) }
            30 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 20) }
            31 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 21) }
            32 -> TileData(Tile.TileType.WATER_TILE, false) { it.getTileByIndex(0, 22) }

            33 -> TileData(Tile.TileType.GRASS_TILE, true) { it.getTileByIndex(1, 0) } //snow
            34 -> TileData(Tile.TileType.GRASS_TILE, true) { it.getTileByIndex(1, 1) } //desert
            35 -> TileData(Tile.TileType.GRASS_TILE, true) { it.getTileByIndex(1, 2) } //room
            36 -> TileData(Tile.TileType.GRASS_TILE, true) { it.getTileByIndex(1, 3) } //road1
            37 -> TileData(Tile.TileType.GRASS_TILE, true) { it.getTileByIndex(1, 4) } //road2
            else -> throw IllegalArgumentException("Unknown tile ID: $tileId")
        }
    }

    fun createTile(tileId: Int, spriteSheet: SpriteSheet, mapLocation: Rect): Tile {
        val data = getTileData(tileId)
        return Tile(mapLocation, data, data.getSprite(spriteSheet))
    }
}