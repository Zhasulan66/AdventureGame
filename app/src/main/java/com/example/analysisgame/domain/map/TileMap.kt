package com.example.analysisgame.domain.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import com.example.analysisgame.presentation.game.GameDisplay
import org.json.JSONObject

data class TiledLayer(val name: String, val width: Int, val height: Int, val data: List<Int>)

fun loadTiledMap(context: Context, fileName: String): JSONObject {
    val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    return JSONObject(jsonString)
}

fun parseLayers(json: JSONObject): List<TiledLayer> {
    val layersJson = json.getJSONArray("layers")
    val layers = mutableListOf<TiledLayer>()

    for (i in 0 until layersJson.length()) {
        val layer = layersJson.getJSONObject(i)
        if (layer.getString("type") == "tilelayer") {
            val name = layer.getString("name")
            val width = layer.getInt("width")
            val height = layer.getInt("height")
            val data = layer.getJSONArray("data")
            val tiles = mutableListOf<Int>()
            for (j in 0 until data.length()) {
                tiles.add(data.getInt(j) - 1) // Tiled uses 1-based tile IDs
            }
            layers.add(TiledLayer(name, width, height, tiles))
        }
    }
    return layers
}

fun drawTiledLayer(
    canvas: Canvas,
    tileset: Bitmap,
    layer: TiledLayer,
    tileSizeSrc: Int = 16,
    tileSizeDest: Int = 64,
    tilesetColumns: Int,
    gameDisplay: GameDisplay
) {
    for (row in 0 until layer.height) {
        for (col in 0 until layer.width) {
            val tileIndex = row * layer.width + col
            val tileId = layer.data[tileIndex]
            if (tileId < 0) continue // -1 means empty

            val srcX = (tileId % tilesetColumns) * tileSizeSrc
            val srcY = (tileId / tilesetColumns) * tileSizeSrc
            val srcRect = Rect(srcX, srcY, srcX + tileSizeSrc, srcY + tileSizeSrc)

            val destX = col * tileSizeDest.toFloat()
            val destY = row * tileSizeDest.toFloat()
            val screenX = gameDisplay.gameToDisplayCoordinatesX(destX)
            val screenY = gameDisplay.gameToDisplayCoordinatesY(destY)
            val destRect = RectF(screenX, screenY, screenX + tileSizeDest, screenY + tileSizeDest)

            canvas.drawBitmap(tileset, srcRect, destRect, null)
        }
    }
}

/*
class TileMap {
}*/
