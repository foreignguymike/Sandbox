package com.distraction.sandbox.tilemap

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.distraction.sandbox.Context
import com.distraction.sandbox.getAtlas

class Tile(val context: Context, val value: Int, var active: Boolean = false) {
    fun toggleActive() {
        active = !active
    }

    fun getImage() = if (active) context.assets.getAtlas().findRegion("tilea") else context.assets.getAtlas().findRegion("tile")
}

class TileMap(private val context: Context, val levelData: TileMapDataModel) {

    companion object {
        const val TILE_WIDTH = 32f
        const val TILE_IWIDTH = 16f
        const val TILE_IHEIGHT = 8f
    }

    val p = Vector3()
    var grid = Array(levelData.grid.size) {
        Tile(context, levelData.grid[it])
    }

    fun render(sb: SpriteBatch) {
        for (row in 0 until levelData.numRows) {
            for (col in 0 until levelData.numCols) {
                val tile = getTile(row, col)
                if (tile.value == 1) {
                    toIsometric(col * TileMap.TILE_WIDTH, row * TileMap.TILE_WIDTH, p)
                    sb.draw(tile.getImage(), p.x - TILE_WIDTH / 2, p.y - TILE_IHEIGHT * 2)
                }
            }
        }
    }

    fun toPosition(row: Int, col: Int, p: Vector3) {
        p.x = col * TILE_WIDTH
        p.y = row * TILE_WIDTH
    }

    fun toIsometric(x: Float, y: Float, p: Vector3) {
        val xo = x / TileMap.TILE_WIDTH
        val yo = y / TileMap.TILE_WIDTH
        p.x = (xo - yo) * TileMap.TILE_IWIDTH
        p.y = (-xo - yo) * TileMap.TILE_IHEIGHT
    }

    fun isValidTile(row: Int, col: Int): Boolean {
        if (row < 0 || row >= levelData.numRows || col < 0 || col >= levelData.numCols) return false
        return levelData.grid[row * levelData.numCols + col] == 1
    }

    fun getTile(row: Int, col: Int) = grid[row * levelData.numCols + col]

    fun isFinished(): Boolean {
        for (row in 0 until levelData.numRows) {
            for (col in 0 until levelData.numCols) {
                val tile = getTile(row, col)
                if (tile.value == 1 && !tile.active) {
                    return false
                }
            }
        }
        return true
    }

}