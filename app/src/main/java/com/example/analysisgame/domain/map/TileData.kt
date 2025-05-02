package com.example.analysisgame.domain.map

import com.example.analysisgame.domain.graphics.Sprite
import com.example.analysisgame.domain.graphics.SpriteSheet

data class TileData(
    val type: Tile.TileType,
    val walkable: Boolean,
    val getSprite: (SpriteSheet) -> Sprite
)
