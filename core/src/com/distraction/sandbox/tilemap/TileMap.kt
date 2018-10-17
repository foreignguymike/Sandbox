package com.distraction.sandbox.tilemap

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Point(var x: Float, var y: Float)

class TileMap(val levelData: TileMapDataModel) {

    companion object {
        const val TILE_WIDTH = 32f
        const val TILE_HEIGHT = 8f
    }

    val tex = Texture("tile.png")


    fun render(sb: SpriteBatch) {

        for (row in 0 until levelData.numRows) {
            for (col in 0 until levelData.numCols) {
                if (levelData.grid[row * levelData.numCols + col] == 1) {
                    var p = toPerspective(col * TileMap.TILE_WIDTH, row * TileMap.TILE_WIDTH)
                    sb.draw(tex, p.x, p.y)
                }
            }
        }

    }

    fun toPerspective(x: Float, y: Float) = Point(
                (x / TileMap.TILE_WIDTH) * TileMap.TILE_WIDTH / 2 - (y / TileMap.TILE_WIDTH) * TileMap.TILE_WIDTH / 2,
                -(x / TileMap.TILE_WIDTH) * TileMap.TILE_HEIGHT - (y / TileMap.TILE_WIDTH) * TileMap.TILE_HEIGHT)

}