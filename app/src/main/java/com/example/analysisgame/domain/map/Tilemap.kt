package com.example.analysisgame.domain.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import com.example.analysisgame.domain.graphics.SpriteSheet
import com.example.analysisgame.presentation.game.GameDisplay


class Tilemap(
    private val spriteSheet: SpriteSheet,
    private val lvl: Int
) {
    val TILE_WIDTH_PIXELS: Int = 64 //64
    val TILE_HEIGHT_PIXELS: Int = 64 //64
    val NUMBER_OF_ROW_TILES: Int =
        when(lvl){
            1 -> 20
            2 -> 24
            3 -> 20
            4 -> 20
            else -> {20}
        }
    val NUMBER_OF_COLUMN_TILES: Int = when(lvl){
        1 -> 43
        2 -> 54
        3 -> 54
        4 -> 54
        else -> {54}
    }

    var mapWidth: Int = TILE_WIDTH_PIXELS * NUMBER_OF_COLUMN_TILES //40 48
    var mapHeight: Int = TILE_HEIGHT_PIXELS * NUMBER_OF_ROW_TILES //40 34

    val mapLayout = MapLayout(lvl)
    var tilemap: Array<Array<Tile>> = arrayOf()
        private set
    private var mapBitmap: Bitmap

    private val layout = mapLayout.getLayout()

    init {
        tilemap = Array(NUMBER_OF_ROW_TILES) { row ->
            Array(NUMBER_OF_COLUMN_TILES) { col ->
                TileFactory.createTile(
                    layout[row][col],
                    spriteSheet,
                    getRectByIndex(row, col))
            }
        }

        val config = Bitmap.Config.ARGB_8888
        mapBitmap = Bitmap.createBitmap(
            NUMBER_OF_COLUMN_TILES * TILE_WIDTH_PIXELS,
            NUMBER_OF_ROW_TILES * TILE_HEIGHT_PIXELS,
            config
        )

        val mapCanvas: Canvas = Canvas(mapBitmap)

        for (row in tilemap) {
            for (tile in row) {
                tile.draw(mapCanvas)
            }
        }
    }

    private fun getRectByIndex(idxRow: Int, idxCol: Int): Rect {
        return Rect(
            idxCol * TILE_WIDTH_PIXELS,
            idxRow * TILE_HEIGHT_PIXELS,
            (idxCol + 1) * TILE_WIDTH_PIXELS,
            (idxRow + 1) * TILE_HEIGHT_PIXELS
        )
    }

    fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        canvas.drawBitmap(
            mapBitmap,
            gameDisplay.getGameRect(),
            gameDisplay.DISPLAY_RECT,
            null
        )
        for (row in tilemap) {
            for (tile in row) {
                tile.draw(Canvas(mapBitmap))
            }
        }

    }

    // Returns the left coordinate of the map
    fun getLeft(): Int {
        return 0
    }

    // Returns the right coordinate of the map
    fun getRight(): Int {
        return mapWidth
    }

    // Returns the top coordinate of the map
    fun getTop(): Int {
        return 0
    }

    // Returns the bottom coordinate of the map
    fun getBottom(): Int {
        return mapHeight
    }

    fun isTileWalkable(tileX: Int, tileY: Int): Boolean {
        return tilemap[tileX][tileY].isWalkable()
    }

    fun getTileRect(tileX: Int, tileY: Int): RectF {
        // Calculate the top-left corner coordinates of the tile
        val left = (tileX * 64).toFloat() //+ 64f
        val top = (tileY * 64).toFloat() //+ 64f

        // Calculate the bottom-right corner coordinates of the tile
        val right = left + 64
        val bottom = top + 64

        // Create and return a RectF object that represents the rectangle of the tile
        return RectF(left, top, right, bottom)
    }
}